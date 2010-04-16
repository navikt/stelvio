package no.stelvio.common.systemavailability;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.logging.Level;

import no.stelvio.common.interceptor.GenericInterceptor;
import no.stelvio.common.interceptor.InterceptorChain;

import org.eclipse.hyades.logging.core.SerializationException;

import com.ibm.websphere.bo.BOFactory;
import com.ibm.websphere.bo.BOXMLDocument;
import com.ibm.websphere.bo.BOXMLSerializer;
import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.ServiceUnavailableException;
import com.ibm.websphere.sca.scdl.OperationType;
import com.ibm.websphere.sca.sdo.DataFactory;
import com.ibm.ws.bo.bomodel.impl.DynamicBusinessObjectImpl;
import com.ibm.ws.sca.internal.multipart.impl.ManagedMultipartImpl;
import com.ibm.wsspi.sca.multipart.impl.MultipartImpl;
import commonj.sdo.DataObject;
import commonj.sdo.Type;

/**
 * Interceptor that can simulate a system. It also has the ability to record data that can be used in simulation.
 * 
 * @author person73874c7d71f8
 * @author test@example.com
 */
public class SystemStubbingInterceptor extends GenericInterceptor {
	private final String systemName;
	private final String STRINGWRAPPER_NAMESPACE = "http://stelvio-commons-lib/no/stelvio/common/systemavailability";
	private final String STRINGWRAPPER_NAME = "StelvioStringWrapper";

	public SystemStubbingInterceptor(String systemName) {
		this.systemName = systemName;
	}

	@Override
	protected Object doInterceptInternal(OperationType operationType, Object input, InterceptorChain interceptorChain) {
		OperationAvailabilityRecord availRec = null;
		try {
			availRec = new SystemAvailabilityStorage().calculateOperationAvailability(systemName, operationType.getName());
		} catch (Throwable t) {
			String logMessage = "AvailabilityCheck: got exception "
					+ t.getMessage()
					+ ". Make sure that the stelvio-commons-lib is available, and that the version of the StelvioSystemAvailabilityFramework config files matches the stelvio-lib version. Now disabling system availability check...";
			logger.logp(Level.WARNING, className, "getMyService", logMessage);
			setEnabled(false);
		}
		if (availRec != null) {
			if (availRec.unAvailable) {
				throw new ServiceUnavailableException("The system " + systemName + " is currently not available. Reason: "
						+ availRec.unavailableReason + ".");
				// Expected timeframe for downtime:
				// "+availRec.unavailableFrom.toString()+" to
				// "+availRec.unavailableTo.toString());
			}
			if (availRec.stubbed) {
				DataObject ret = findMatchingTestData(operationType, (DataObject) input);
				return ret;
			} else if (availRec.recordStubData) {
				DataObject preRecorded = null;
				try {
					preRecorded = findMatchingTestData(operationType, (DataObject) input);
				} catch (ServiceBusinessException sbe) {
					// This is OK, as it is a prerecorded SBE
				} catch (ServiceRuntimeException sre) {
					// This is also OK as it is a prerecorded SRE
				} catch (RuntimeException re) {
					// No prerecorded stub found
					long timestamp = System.currentTimeMillis();
					String requestId = Long.toString(timestamp);

					Type sdoTypeReq = ((DataObject) input).getType();
					String requestObjectName = sdoTypeReq.getName();
					recordStubData(operationType, requestId, input, requestObjectName, "Request");
					Object ret;
					try {
						ret = interceptorChain.doIntercept(operationType, input);
					} catch (ServiceBusinessException sbe) {
						recordStubDataException(operationType, requestId, input, sbe);
						throw sbe;
					} catch (ServiceRuntimeException sre) {
						recordStubDataRuntimeException(operationType, requestId, input, sre);
						throw sre;
					}
					Type sdoTypeRes = ((DataObject) ret).getType();
					String responseObjectName = sdoTypeRes.getName();

					recordStubData(operationType, requestId, ret, responseObjectName, "Response");
					return ret;
				}
				String logMessage = "Found prerecorded matching stub data for " + systemName + "." + operationType.getName()
						+ ". Ignoring.";
				logger.logp(Level.FINE, className, "invoke", logMessage);
				return interceptorChain.doIntercept(operationType, input);
			}
		}
		return interceptorChain.doIntercept(operationType, input);
	}

	private void recordStubDataException(OperationType operationType, String requestId, Object input,
			ServiceBusinessException sbe) {
		try {
			storeObjectOrPrimitive(operationType, sbe, "exception", requestId, "Exception");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SerializationException e) {
			e.printStackTrace();
		}
	}

	private void recordStubDataRuntimeException(OperationType operationType, String requestId, Object input,
			ServiceRuntimeException sre) {
		try {
			storeObjectOrPrimitive(operationType, sre, "exception", requestId, "RuntimeException");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SerializationException e) {
			e.printStackTrace();
		}
	}

	private void recordStubData(OperationType operationType, String requestId, Object input, String requestObjectName,
			String fileSuffix) {
		try {
			storeObjectOrPrimitive(operationType, input, requestObjectName, requestId, fileSuffix);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void storeObjectOrPrimitive(OperationType operationType, Object object, String requestObjectName, String requestId,
			String fileSuffix) throws IOException, SerializationException {
		File dir = getDirectory(operationType);
		File file = new File(dir, "Stub_" + requestId + "_" + fileSuffix + ".xml");
		FileOutputStream fos = new FileOutputStream(file);
		if (object instanceof MultipartImpl) {
			if (((ManagedMultipartImpl) object).get(0) instanceof DataObject) {
				DataObject requestDataObject = (DataObject) ((ManagedMultipartImpl) object).get(0);
				BOXMLSerializer xmlSerializerService = (BOXMLSerializer) new ServiceManager()
						.locateService("com/ibm/websphere/bo/BOXMLSerializer");
				xmlSerializerService.writeDataObject(requestDataObject, "http://no.stelvio.stubdata/", requestObjectName, fos);
			} else {
				BOXMLSerializer xmlSerializerService = (BOXMLSerializer) new ServiceManager()
						.locateService("com/ibm/websphere/bo/BOXMLSerializer");
				xmlSerializerService.writeDataObject((ManagedMultipartImpl) object, "http://no.stelvio.stubdata/",
						requestObjectName, fos);
			}
		} else {
			if (object instanceof DynamicBusinessObjectImpl) {
				if (((DynamicBusinessObjectImpl) object).get(0) instanceof DataObject) {
					DataObject requestDataObject = (DataObject) ((DynamicBusinessObjectImpl) object).get(0);
					BOXMLSerializer xmlSerializerService = (BOXMLSerializer) new ServiceManager()
							.locateService("com/ibm/websphere/bo/BOXMLSerializer");
					xmlSerializerService.writeDataObject(requestDataObject, "http://no.stelvio.stubdata/", requestObjectName,
							fos);
				} else {
					BOXMLSerializer xmlSerializerService = (BOXMLSerializer) new ServiceManager()
							.locateService("com/ibm/websphere/bo/BOXMLSerializer");
					xmlSerializerService.writeDataObject((DynamicBusinessObjectImpl) object, "http://no.stelvio.stubdata/",
							requestObjectName, fos);
				}

			} else if (object instanceof String) {

				DataObject stringWrapper = DataFactory.INSTANCE.create(STRINGWRAPPER_NAMESPACE, STRINGWRAPPER_NAME);
				stringWrapper.setString("value", (String) object);
				BOXMLSerializer xmlSerializerService = (BOXMLSerializer) new ServiceManager()
						.locateService("com/ibm/websphere/bo/BOXMLSerializer");
				xmlSerializerService.writeDataObject(stringWrapper, "http://no.stelvio.stubdata/", requestObjectName, fos);

			} else {
				if (object instanceof ServiceBusinessException) {
					DataObject requestDataObject = (DataObject) ((ServiceBusinessException) object).getData();
					BOXMLSerializer xmlSerializerService = (BOXMLSerializer) new ServiceManager()
							.locateService("com/ibm/websphere/bo/BOXMLSerializer");
					xmlSerializerService.writeDataObject(requestDataObject, "http://no.stelvio.stubdata/", requestObjectName,
							fos);

				} else if (object instanceof ServiceRuntimeException) {
					PrintWriter pw = new PrintWriter(fos);
					pw.println(((ServiceRuntimeException) object).getMessage());
					pw.close();
				}

			}
		}
		String logMessage = "Recorded stubdata " + file;
		logger.logp(Level.FINE, className, "storeObjectOrPrimitive", logMessage);
		fos.close();
	}

	private File getDirectory(OperationType operationType) {
		String dirName = new SystemAvailabilityStorage().getSystemAvailabilityDirectory() + "/" + this.systemName + "/"
				+ operationType.getName();
		File dir = new File(dirName);
		if (!dir.exists())
			dir.mkdirs();
		return dir;
	}

	/**
	 * Finds matching test data based on operation type and input object
	 * 
	 * @param operationType
	 * @param input
	 * @return the DataObject representation of the matched test data
	 * @throws ServiceBusinessException
	 *             if matching test data is an exception (intentionally recorded to return exception)
	 */
	private DataObject findMatchingTestData(OperationType operationType, DataObject input) throws ServiceBusinessException {
		File dir = getDirectory(operationType);
		File[] requestFiles = dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith("_Request.xml");
			}
		});
		Arrays.sort(requestFiles);
		File matchingRequestFile = null;
		for (File requestFile : requestFiles) {
			DataObject storedInput = readDataObject(requestFile);
			if (input.get(0) instanceof DataObject) {
				if (MatcherUtils.match(storedInput, (DataObject) input.get(0))) {
					matchingRequestFile = requestFile;
					break;
				}
			} else // Primitive interface
			if (MatcherUtils.match(storedInput, input)) {
				matchingRequestFile = requestFile;
				break;
			}
		}
		if (matchingRequestFile == null) {
			throw new RuntimeException("No matching stub found for system " + systemName + ", operation "
					+ operationType.getName() + " in path " + dir);
		}
		DataObject response = null;
		String logMessage = "Found matching test data " + matchingRequestFile;
		logger.logp(Level.FINE, className, "findMatchingTestData", logMessage);
		String tmStamp = matchingRequestFile.getName().substring(0, matchingRequestFile.getName().length() - 12); // Deduct
		// "_Request.xml"
		File responseFile = new File(dir, tmStamp + "_Response.xml");
		if (!responseFile.exists()) {
			File exceptionFile = new File(dir, tmStamp + "_Exception.xml");
			if (exceptionFile.exists()) {
				DataObject responseObject = readDataObject(exceptionFile);
				throw new ServiceBusinessException(responseObject);
			}
			File runtimeExceptionFile = new File(dir, tmStamp + "_RuntimeException.xml");
			if (runtimeExceptionFile.exists()) {
				throw new ServiceRuntimeException(
						"A ServiceRuntimeException was recorded, but unfortunately I'm not able yet to provide the original message. It can maybe be found in the recorded data, but I'm pretty dumb.");
			}
		}
		if (responseFile.exists()) {
			response = createResponseObject(responseFile, operationType.getOutputType());
		}
		return response;
	}

	/**
	 * Reads the xml file and creates the dataObject of the requested Type. If the response object is a StelvioStringWrapper,
	 * the String vaule is unwrapped and put into the response.
	 * 
	 * @param responseFile -
	 *            the file to create the response object from
	 * @param outputType -
	 *            the type of response object to be created
	 * @return
	 */
	private DataObject createResponseObject(File responseFile, Type outputType) {
		DataObject responseObject = readDataObject(responseFile);
		DataObject response;
		if (responseObject.getType().getName().equals(STRINGWRAPPER_NAME)) {
			BOFactory dataFactory = (BOFactory) ServiceManager.INSTANCE.locateService("com/ibm/websphere/bo/BOFactory");
			response = dataFactory.createByType(outputType);
			response.set(0, responseObject.getString("value"));
		} else if ((!(responseObject instanceof ManagedMultipartImpl)) || responseObject == null) {
			BOFactory dataFactory = (BOFactory) ServiceManager.INSTANCE.locateService("com/ibm/websphere/bo/BOFactory");
			response = dataFactory.createByType(outputType);
			response.set(0, responseObject);
		} else { // Assume MultipartImpl, because use of primitives
			// in interface
			response = (ManagedMultipartImpl) responseObject;
		}

		return response;
	}

	/**
	 * Deserializes the xml file and extracts the dataobject from it.
	 * 
	 * @param file -
	 *            the file to be read
	 * @return the extracted DataObject
	 */
	private DataObject readDataObject(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);

			BOXMLSerializer xmlSerializerService = (BOXMLSerializer) new ServiceManager()
					.locateService("com/ibm/websphere/bo/BOXMLSerializer");
			BOXMLDocument criteriaDoc = xmlSerializerService.readXMLDocument(fis);

			return criteriaDoc.getDataObject();

		} catch (IOException e) {
			e.printStackTrace();
			return null;

		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					// Ignore
				}
			}
		}
	}
}
