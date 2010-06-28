package no.stelvio.common.systemavailability;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import no.stelvio.common.interceptor.GenericInterceptor;
import no.stelvio.common.interceptor.InterceptorChain;

import com.ibm.websphere.bo.BOEquality;
import com.ibm.websphere.bo.BOFactory;
import com.ibm.websphere.bo.BOType;
import com.ibm.websphere.bo.BOXMLSerializer;
import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.ServiceUnavailableException;
import com.ibm.websphere.sca.scdl.OperationType;
import commonj.sdo.DataObject;
import commonj.sdo.Property;
import commonj.sdo.Type;

/**
 * Interceptor that can simulate a system. It also has the ability to record data that can be used in simulation.
 * 
 * @author person73874c7d71f8
 * @author test@example.com
 */
public class SystemStubbingInterceptor extends GenericInterceptor {
	private final String systemName;

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
				return findStubData(operationType, input);
			} else if (availRec.recordStubData) {
				try {
					findStubData(operationType, input);
				} catch (IllegalStateException e) {
					return recordStubData(operationType, input, interceptorChain);
				}
				String logMessage = "Found prerecorded matching stub data for " + systemName + "." + operationType.getName()
						+ ". Ignoring.";
				logger.logp(Level.FINE, className, "invoke", logMessage);
			}
		}
		return interceptorChain.doIntercept(operationType, input);
	}

	private Object recordStubData(OperationType operationType, Object input, InterceptorChain interceptorChain) {
		RuntimeException exception = null;
		Object output = null;
		try {
			output = interceptorChain.doIntercept(operationType, input);
		} catch (RuntimeException e) {
			exception = e;
		}
		recordStubData(operationType, input, output, exception);
		if (exception != null) {
			throw exception;
		} else {
			return output;
		}
	}

	private boolean isOneWayOperation(Type outputType) {
		return outputType == null;
	}

	private Object findStubData(OperationType operationType, Object input) {
		File directory = getDirectory(operationType);

		File[] requestFiles = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("_Request.xml");
			}
		});
		Arrays.sort(requestFiles);

		DataObject inputDataObject = getDataObject(operationType.getInputType(), input);
		BOEquality boEquality = getBOEquality();
		for (File requestFile : requestFiles) {
			DataObject requestDataObject = readStubData(requestFile);
			if (isDefaultRequest(requestDataObject) || boEquality.isEqual(inputDataObject, requestDataObject)) {
				if (isOneWayOperation(operationType.getOutputType())) {
					return null;
				}

				String requestFilename = requestFile.getName();
				String responseFilename = requestFilename.substring(0, requestFilename.indexOf("_Request.xml"))
						+ "_Response.xml";
				File responseFile = new File(directory, responseFilename);
				if (responseFile.exists()) {
					DataObject responseDataObject = readStubData(responseFile);
					if (getBOType().isDataTypeWrapper(responseDataObject)) {
						return responseDataObject.get("value");
					} else {
						return responseDataObject;
					}
				}
				String exceptionFilename = requestFilename.substring(0, requestFilename.indexOf("_Request.xml"))
						+ "_Exception.xml";
				File exceptionFile = new File(directory, exceptionFilename);
				if (exceptionFile.exists()) {
					DataObject faultDataObject = readStubData(exceptionFile);
					throw new ServiceBusinessException(faultDataObject);
				}
				String runtimeExceptionFilename = requestFilename.substring(0, requestFilename.indexOf("_Request.xml"))
						+ "_RuntimeException.ser";
				File runtimeExceptionFile = new File(directory, runtimeExceptionFilename);
				if (runtimeExceptionFile.exists()) {
					throw readRuntimeException(runtimeExceptionFile);
				}
			}
		}
		throw new IllegalStateException("No matching stub found for system " + systemName + ", operation "
				+ operationType.getName() + " in path " + directory);
	}

	private boolean isDefaultRequest(DataObject requestDataObject) {
		for (Property property : (List<Property>) requestDataObject.getType().getProperties()) {
			if (requestDataObject.isSet(property)) {
				return false;
			}
		}
		return true;
	}

	private RuntimeException readRuntimeException(File file) {
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream(new FileInputStream(file));
			return (RuntimeException) objectInputStream.readObject();
		} catch (IOException e) {
			logger.logp(Level.WARNING, getClass().getName(), "readRuntimeException", "Error reading runtime exception", e);
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			logger.logp(Level.WARNING, getClass().getName(), "readRuntimeException", "Error reading runtime exception", e);
			throw new RuntimeException(e);
		} finally {
			closeQuietly(objectInputStream);
		}
	}

	private DataObject readStubData(File file) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			return getBOXMLSerializer().readXMLDocument(inputStream).getDataObject();
		} catch (IOException e) {
			logger.logp(Level.WARNING, getClass().getName(), "readStubData", "Error reading stub data", e);
			throw new RuntimeException(e);
		} finally {
			closeQuietly(inputStream);
		}
	}

	private void recordStubData(OperationType operationType, Object input, Object output, RuntimeException exception) {
		String requestId = Long.toString(System.currentTimeMillis());
		File directory = getDirectory(operationType);

		DataObject inputDataObject = getDataObject(operationType.getInputType(), input);
		persistStubData(inputDataObject, new File(directory, getFilename(requestId, "Request.xml")));

		if (exception != null) {
			if (exception instanceof ServiceBusinessException) {
				DataObject faultDataObject = (DataObject) ((ServiceBusinessException) exception).getData();
				persistStubData(faultDataObject, new File(directory, getFilename(requestId, "Exception.xml")));
			} else {
				persistStubData(exception, new File(directory, getFilename(requestId, "RuntimeException.ser")));
			}
		} else {
			Type outputType = operationType.getOutputType();
			if (!isOneWayOperation(outputType)) {
				// Two-way operation
				DataObject outputDataObject = getDataObject(outputType, output);
				persistStubData(outputDataObject, new File(directory, getFilename(requestId, "Response.xml")));
			}
		}
	}

	private void persistStubData(DataObject dataObject, File file) {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			getBOXMLSerializer().writeDataObject(dataObject, "http://no.stelvio.stubdata/", dataObject.getType().getName(),
					outputStream);
		} catch (IOException e) {
			logger.logp(Level.WARNING, getClass().getName(), "persistStubData", "Error persisting stub data", e);
		} finally {
			closeQuietly(outputStream);
		}
	}

	private void persistStubData(RuntimeException exception, File file) {
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
			objectOutputStream.writeObject(exception);
		} catch (IOException e) {
			logger.logp(Level.WARNING, getClass().getName(), "persistStubData", "Error persisting stub data", e);
		} finally {
			closeQuietly(objectOutputStream);
		}
	}

	private void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {
			logger.logp(Level.WARNING, getClass().getName(), "closeQuietly", "Error closing IO object", e);
		}
	}

	private String getFilename(String requestId, String suffix) {
		return "Stub_" + requestId + "_" + suffix;
	}

	private File getDirectory(OperationType operationType) {
		String directoryName = new SystemAvailabilityStorage().getSystemAvailabilityDirectory() + "/" + systemName + "/"
				+ operationType.getName();
		File directory = new File(directoryName);
		directory.mkdirs();
		return directory;
	}

	private DataObject getDataObject(Type type, Object obj) {
		if (obj instanceof DataObject) {
			return (DataObject) obj;
		} else {
			return getBOFactory().createDataTypeWrapper(type, obj);
		}
	}

	private BOFactory getBOFactory() {
		return (BOFactory) ServiceManager.INSTANCE.locateService("com/ibm/websphere/bo/BOFactory");
	}

	private BOXMLSerializer getBOXMLSerializer() {
		return (BOXMLSerializer) ServiceManager.INSTANCE.locateService("com/ibm/websphere/bo/BOXMLSerializer");
	}

	private BOEquality getBOEquality() {
		return (BOEquality) ServiceManager.INSTANCE.locateService("com/ibm/websphere/bo/BOEquality");
	}

	private BOType getBOType() {
		return (BOType) ServiceManager.INSTANCE.locateService("com/ibm/websphere/bo/BOType");
	}
}
