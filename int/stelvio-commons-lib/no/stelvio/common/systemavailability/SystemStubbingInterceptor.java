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
import com.ibm.ws.bo.bomodel.util.BOPropertyImpl;
import com.ibm.wsspi.sca.scdl.OperationType;
import commonj.sdo.DataObject;
import commonj.sdo.Property;
import commonj.sdo.Type;

/**
 * Interceptor that can simulate a system. It also has the ability to record data that can be used in simulation.
 * 
 * @author person73874c7d71f8
 * @author test@example.com
 * @author test@example.com
 */
public class SystemStubbingInterceptor extends GenericInterceptor {
	private final String systemName;
	private boolean findStubDataForRecording = false;

	public SystemStubbingInterceptor(String systemName) {
		this.systemName = systemName;
	}

	@Override
	protected Object doInterceptInternal(com.ibm.websphere.sca.scdl.OperationType operationType, Object input,
			InterceptorChain interceptorChain) {
		OperationAvailabilityRecord availRec = null;
		findStubDataForRecording = false;
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
			OperationType castedOperationType = (OperationType) operationType;
			if (availRec.stubbed) {
				return findStubData(castedOperationType, input);
			} else if (availRec.recordStubData) {
				try {
					findStubDataForRecording = true;
					// Trying to find matching stub data first, to avoid creating "duplicates"
					findStubData(castedOperationType, input);

					String logMessage = "Found prerecorded matching stub data for " + systemName + "."
							+ operationType.getName() + ". Ignoring.";
					logger.logp(Level.FINE, className, "invoke", logMessage);
				} catch (IllegalStateException e) {
					// No matching stub found, record stub data
					return recordStubData(castedOperationType, input, interceptorChain);
				}
			}
		}
		return interceptorChain.doIntercept(operationType, input);
	}
	
	/**
	 * Returnerer respons/exception-objekt hvis innkommende operasjon er recorded.
	 * Kaster IllegalStateException hvis operasjonen ikke er recorded (ingen matchende request).
	 * 
	 * @param operationType
	 * @param input
	 * @return
	 */
	private Object findStubData(OperationType operationType, Object input) {
		validateSupportedInput(operationType, input);
		
		File directory = getDirectory(operationType);

		File[] requestFiles = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("_Request.xml");
			}
		});
		Arrays.sort(requestFiles);

		DataObject inputDataObject = getDataObject(operationType.getInputType(), input);
		
		// Workaround start
		if (operationType.isWrapperType(inputDataObject.getType())) {
			inputDataObject = (DataObject)inputDataObject.get(0);
		}
		DataObject requestContext = removeRequestContext(inputDataObject);
		// Workaround slutt
		
		BOEquality boEquality = getBOEquality();
		for (File requestFile : requestFiles) {
			DataObject requestDataObject = readStubData(requestFile);
			String requestFilename = requestFile.getName();
			
			if (isDefaultRequest(requestDataObject) || boEquality.isEqual(inputDataObject, requestDataObject)) {
				setRequestContext(inputDataObject, requestContext);

				// If findStubData was called in recording state (in order to prevent duplicates),
				if (findStubDataForRecording) {
					// then return an empty object (we are not interesting in the response from stubbing file). 
					return new Object();
				}
				
				if (isOneWayOperation(operationType)) {
					checkForException(directory, requestFilename);
					return null;
				}

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
				checkForException(directory, requestFilename);
			}
		}
		
		throw new IllegalStateException("No matching stub found for system " + systemName + ", operation "
				+ operationType.getName() + " in path " + directory);
	}

	/**
	 * Hjelpemetode for å se etter om innkommende request har en matchende exception-response.
	 * 
	 * @param directory
	 * @param requestFilename
	 */
	private void checkForException(File directory, String requestFilename) {
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
	
	/**
	 * Leser exception fra fil og kaster denne som RuntimeException.
	 * 
	 * @param file
	 * @return
	 */
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
	
	/**
	 * Metode for recording av data (skriving av request/response/exception til fil).
	 * 
	 * @param operationType
	 * @param input
	 * @param interceptorChain
	 * @return
	 */
	private Object recordStubData(OperationType operationType, Object input, InterceptorChain interceptorChain) {
		RuntimeException exception = null;
		Object output = null;
		validateSupportedInput(operationType, input);
		String requestId = Long.toString(System.currentTimeMillis());
		recordStubDataRequest(requestId, operationType, input);
		try {
			output = interceptorChain.doIntercept(operationType, input);
		} catch (RuntimeException e) {
			exception = e;
		}
		validateSupportedOutput(requestId, operationType, output, exception);
		recordStubDataResponse(requestId, operationType, output, exception);
		if (exception != null) {
			throw exception;
		} else {
			return output;
		}
	}

	/**
	 * Input kan ikke være null med mindre operasjonstype er "WrappedStyle" (kompleks dataobjekt, og ikke int/boolean osv).
	 * Hvis operasjonstype er "WrappedStyle", må inputDataObject ikke kaste NullPointerException (som betyr null i input).
	 * Kaster UnsupportedOperationException ved disse tilfellene.
	 * 
	 * @param operationType
	 * @param input
	 */
	private void validateSupportedInput(OperationType operationType, Object input) {
		if (!operationType.isWrappedStyle()) {
			if (input == null) {
				throw new UnsupportedOperationException(
					"Document literal non-wrapped operations with null as input are currently not supported by the stubbing framework.");
			}
		} else {
			DataObject inputDataObject = getDataObject(operationType.getInputType(), input);
			try {
				inputDataObject = (DataObject)inputDataObject.get(0);
			} catch (NullPointerException npe) {
				throw new UnsupportedOperationException(
					"Document literal non-wrapped operations with null as input are currently not supported by the stubbing framework.");
			}
		}
	}
	
	/**
	 * Output kan ikke være null med mindre operasjonstype er "WrappedStyle" (kompleks dataobjekt, og ikke int/boolean osv).
	 * 
	 * @param requestId
	 * @param operationType
	 * @param output
	 * @param exception
	 */
	private void validateSupportedOutput(String requestId, OperationType operationType, Object output, Exception exception) {
		if (!operationType.isWrappedStyle()) {
			Type outputType = operationType.getOutputType();
			if (!isOneWayOperation(operationType)) {
				if (!outputType.isDataType() && operationType.isWrapperType(outputType)) {
					String fail = removeRequestStubbingFile(requestId, operationType);
					throw new UnsupportedOperationException(
							"Document literal non-wrapped two-way operations without response (void methods) are currently not supported by the stubbing framework. " + fail);
				}
				if (output == null && exception == null) {
					String fail = removeRequestStubbingFile(requestId, operationType);
					throw new UnsupportedOperationException(
							"Document literal non-wrapped operations with null as output are currently not supported by the stubbing framework. " + fail);
				}
			}
		}
	}
	
	/**
	 * Recording fungerer slik at først skriver vi requesten til fil.
	 * Så utføres kallet, og hvis det viser seg at responsen er null og operasjonstype ikke er "WrappedStyle", så er ikke operasjonen støttet av stubbingrammeverket.
	 * I disse tilfellene skal request-filen som ble laget, slettes igjen.
	 * 
	 * @param requestId
	 * @param operationType
	 * @return
	 */
	private String removeRequestStubbingFile(String requestId, OperationType operationType) {
		File request = new File(getDirectory(operationType), getFilename(requestId, "Request.xml"));
		if (request != null && request.exists()) {
			if (request.delete() == true) {
				return "";
			}
		}
		return "\nError! The request stubbing file \"" + getFilename(requestId, "Request.xml") + "\" could not be deleted!";
	}

	/**
	 * Tar imot input, behandler denne, og skriver den til fil.
	 * 
	 * @param requestId
	 * @param operationType
	 * @param input
	 */
	private void recordStubDataRequest(String requestId, OperationType operationType, Object input) {
		File directory = getDirectory(operationType);

		DataObject inputDataObject = getDataObject(operationType.getInputType(), input);
		persistStubData(inputDataObject, new File(directory, getFilename(requestId, "Request.xml")), operationType);
	}
	
	/**
	 * Tar imot output og exception (én av disse er null ved toveisoperasjon), og skriver til fil.
	 * 
	 * @param requestId
	 * @param operationType
	 * @param output
	 * @param exception
	 */
	private void recordStubDataResponse(String requestId, OperationType operationType, Object output, RuntimeException exception) {
		File directory = getDirectory(operationType);
		
		if (exception != null) {
			if (exception instanceof ServiceBusinessException) {
				DataObject faultDataObject = (DataObject) ((ServiceBusinessException) exception).getData();
				persistStubData(faultDataObject, new File(directory, getFilename(requestId, "Exception.xml")), operationType);
			} else {
				persistStubData(exception, new File(directory, getFilename(requestId, "RuntimeException.ser")));
			}
		} else if (!isOneWayOperation(operationType)) {
			// Two-way operation
			Type outputType = operationType.getOutputType();
			DataObject outputDataObject = getDataObject(outputType, output);
			persistStubData(outputDataObject, new File(directory, getFilename(requestId, "Response.xml")), operationType);
		}
	}

	/**
	 * Hjelpemetode for å lese en fil og returnere denne som et DataObject.
	 * 
	 * @param file
	 * @return
	 */
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
	
	/**
	 * Hjelpemetode for å skrive et DataObject til fil.
	 * 
	 * @param dataObject
	 * @param file
	 * @param operationType
	 */
	private void persistStubData(DataObject dataObject, File file, OperationType operationType) {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			Type type = dataObject.getType();
			
			// Workaround start
			if (operationType.isWrapperType(type)) {
				DataObject child = (DataObject)dataObject.get(0);
				DataObject requestContext = removeRequestContext(child);
				getBOXMLSerializer().writeDataObject(child, type.getURI(), type.getName(), outputStream);
				//System.out.println("WrapperType");
				setRequestContext(child, requestContext);
			} else {
				getBOXMLSerializer().writeDataObject(dataObject, type.getURI(), type.getName(), outputStream);
				//System.out.println("Not WrapperType");
			}
			// Workaround slutt
		} catch (IOException e) {
			logger.logp(Level.WARNING, getClass().getName(), "persistStubData", "Error persisting stub data", e);
		} finally {
			closeQuietly(outputStream);
		}
	}

	/**
	 * Hjelpemetode for å skrive en RuntimeException til fil.
	 * 
	 * @param dataObject
	 * @param file
	 * @param operationType
	 */
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
	
	/*
	// Metode for å lage et DOM-dokument
	private Document createDocument(DataObject dataObject) {
		Type type = dataObject.getType();
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element element = createElement(doc, dataObject, type.getName());
			String uri = type.getURI();
			element.setAttribute("uri", uri);
			doc.appendChild(element);
			doc.normalize();
			return doc;
		} catch (ParserConfigurationException e) {
			logger.logp(Level.WARNING, getClass().getName(), "createDocument", "Error creating stub request document", e);
			return null;
		}
	}
	
	// Metode for å lage DOM-element av et DataObject.
	private Element createElement(Document doc, DataObject dataObject, String name) {
		Element element = doc.createElement(name);
		Type type = dataObject.getType();
		List properties = type.getProperties();
		for (int i = 0; i < properties.size(); i++) {
			Property property = (Property) properties.get(i);
			String propName = property.getName();
			Object object = dataObject.get(propName);
			addChildren(doc, object, element, propName);
		}
		
		return element;
	}
	// Legger til alle barna til et element
	private void addChildren(Document doc, Object obj, Element parent, String name) {
		if (obj instanceof DataObject) {
			parent.appendChild(createElement(doc, (DataObject) obj, name));
		} else if (obj instanceof List) {
			List list = (List) obj;
			for (int i = 0; i < list.size(); i++) {
				Object listObj = list.get(i);
				addChildren(doc, listObj, parent, name);
			}
		} else if (obj != null) {
			Element element = doc.createElement(name);
			Text text = doc.createTextNode(obj.toString());
			element.appendChild(text);
			parent.appendChild(element);
		}
	}
	*/
	

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
	
	private boolean isOneWayOperation(OperationType operationType) {
		Type outputType = operationType.getOutputType();
		if (!operationType.isWrappedStyle()) {
			return outputType == null;
		}
		return (outputType.getProperties().size() == 0);
	}
	
	private boolean isDefaultRequest(DataObject requestDataObject) {
		for (Property property : (List<Property>) requestDataObject.getType().getProperties()) {
			if (requestDataObject.isSet(property)) {
				return false;
			}
		}
		return true;
	}

	private DataObject removeRequestContext(DataObject dataObject) {
		DataObject requestContext = null;
		List properties = dataObject.getInstanceProperties();
		for (int i = 0; i < properties.size(); i++) {
			BOPropertyImpl prop = (BOPropertyImpl) properties.get(i);
			if (prop != null && prop.getType() != null && "requestContextDto".equalsIgnoreCase(prop.getType().getName())) {
				requestContext = dataObject.getDataObject(prop.getName());
				dataObject.setDataObject(prop.getName(), null);
			}
		}
		return requestContext;
	}
	
	private boolean setRequestContext(DataObject dataObject, DataObject requestContext) {
		List properties = dataObject.getInstanceProperties();
		for (int i = 0; i < properties.size(); i++) {
			BOPropertyImpl prop = (BOPropertyImpl) properties.get(i);
			if (prop != null && prop.getType() != null && "requestContextDto".equalsIgnoreCase(prop.getType().getName())) {
				dataObject.setDataObject(prop.getName(), requestContext);
				return true;
			}
		}
		return false;
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
