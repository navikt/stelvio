package no.stelvio.integration.jms.handler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.commons.lang.StringUtils;

import no.stelvio.integration.jms.MessageHandler;
import no.stelvio.common.FrameworkError;
import no.stelvio.common.Constants;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceResponse;

/**
 * Handles javax.jms.TextMesssages that contains XML as the message body.
 * 
 * @author person356941106810, Accenture
 * @version $Id: XMLMessageHandler.java 2794 2006-02-28 19:32:20Z skb2930 $
 */
public class XMLMessageHandler implements MessageHandler {

	protected static final String RESPONSE_FIELDS = "responseFields";
	protected static final String RESPONSE_CLASS_NAME = "responseClassNames";
	protected static final String STATUS_RESPONSE_FIELD = "statusResponseField";
	protected static final String ERROR_STATUS = "errorStatus";
	protected static final String ERROR_RESPONSE_FIELD = "errorResponseField";
	protected static final String EXCEPTION_ON_ERROR = "exceptionOnError";
	protected static final String EXCEPTION_ON_ERROR_STATUS = "exceptionOnErrorStatus";
	protected static final String DATE_PATTERN = "datePattern";

	// holds date format pattern
	protected String datePattern = null;

	// holds the field mapping for the static response fields
	protected Map staticResponseFields = null;

	// holds the mappings for the header fields
	protected Map headerFields = null;

	// holds the mapping for the response fields.
	protected Map responseFields = null;

	// holds the internal representation of iterative response fields
	protected Map iterativeResponseFields = null;

	// holds the class names of the top level response classes
	protected Map responseClassNames = null;

	// holds the Classes
	protected Map responseClasses = null;

	// Defect #299 - SAXParser is not Thread safe, using SAXParserFactory instead
	protected SAXParserFactory factory = null;

	//	holds the xpath to the error message
	protected String errorMessageField = null;

	// holds the field name where the error message should be put
	protected String errorResponseField = null;

	// holds the xpath to where the status can be found
	protected String statusMessageField = null;

	// holds the field name where the status should be put
	protected String statusResponseField = null;

	/// holds the xpath to where additional info can be found
	protected String statusMessageInfo = null;

	// indicates wether or not to throw a ServiceFailed exception if there is an error
	protected boolean exceptionOnError = false;

	// indicates wether or not to throw a ServiceFailed exception if there is an error status
	protected boolean exceptionOnErrorStatus = false;

	// the preferred List implementation to use
	protected Class prefListType = ArrayList.class;

	// the preferred Map implemenetation to use
	protected Class prefMapType = HashMap.class;

	// a list of all statuses which are considered an error
	protected List errorStatus = null;

	/**
	 * Handles the TextMessage messgage.
	 * 
	 * @param msg the message to handle.
	 * @throws ServiceFailedException handling of message failed du to application error.
	 * @return the result of the handling.
	 */
	public ServiceResponse handleMessage(Message msg) throws ServiceFailedException {

		if (!(msg instanceof TextMessage)) {
			throw new SystemException(FrameworkError.JMS_INVALID_MESSAGE_TYPE, msg);
		}

		ServiceResponse response = new ServiceResponse();
		setStaticFields(response);
		setHeaderFields(response, msg);

		try {
			// must create a separate handler which can hold state
			SAXXMLHandler handler =
				new SAXXMLHandler(
					responseFields,
				    iterativeResponseFields,
				    response,
				    this,
				    statusMessageField,
				    errorMessageField);

			TextMessage txtMsg = (TextMessage) msg;
			InputSource source = new InputSource(new StringReader(txtMsg.getText()));

			// Defect #299 - SAXParser not thread safe, can't be declared as instance variable
			factory.newSAXParser().parse(source, handler);
			handleStatusAndError(response, handler);
		} catch (ParserConfigurationException e) {
			throw new SystemException(FrameworkError.JMS_XML_HANDLER_PARSER_ERROR, e);
		} catch (SAXException e) {
			throw new SystemException(FrameworkError.JMS_XML_HANDLER_PARSER_ERROR, e);
		} catch (IOException e) {
			throw new SystemException(FrameworkError.JMS_XML_HANDLER_PARSER_ERROR, e);
		} catch (JMSException e) {
			throw new SystemException(FrameworkError.JMS_MESSAGE_READ_ERROR, e);
		}

		return response;
	}

	/**
	 * Handles any status and error messages received from the backend system.
	 * @param response the response to populate
	 * @param handler the SAXHandler which parsed the return message
	 * @throws ServiceFailedException a status which indicated and error or and error was found.
	 */
	private void handleStatusAndError(ServiceResponse response, SAXXMLHandler handler) throws ServiceFailedException {

		if (StringUtils.isNotBlank(handler.getStatusValue())) {
			// check if this status is actually an error
			if (errorStatus.contains(handler.getStatusValue()) && exceptionOnErrorStatus) {
				// check if there also is an error message to bring along
				String exceptionString = "Status: " + handler.getStatusValue();

				if (handler.getErrorValue() != null) {
					exceptionString += ", Error Message: " + handler.getErrorValue();
				}

				throw new ServiceFailedException(FrameworkError.JMS_ENTERPRISE_SYSTEM_ERROR, exceptionString);
			}

			// check if this status should be set on the response
			if (StringUtils.isNotBlank(statusResponseField)) {
				setField(response, statusResponseField, null, 0, handler.getStatusValue(), null);
			}
		}

		// the status is not set or there is an error message
		if (StringUtils.isNotBlank(handler.getErrorValue())) {
			if (exceptionOnError) {
				// check if there also is an error message to bring along
				String exceptionString = "";

				if (handler.getStatusValue() != null) {
					exceptionString = "Status: " + handler.getStatusValue() + ", ";
				}

				exceptionString += "Error Message: " + handler.getErrorValue();
				throw new ServiceFailedException(FrameworkError.JMS_ENTERPRISE_SYSTEM_ERROR, exceptionString);
			}

			if (StringUtils.isNotBlank(errorResponseField)) {
				setField(response, errorResponseField, null, 0, handler.getErrorValue(), null);
			}
		}
	}

	/**
	 * Performs additional initialization and validation of the configuration of this MessageHandler.
	 */
	public void init() {
		// validate that the date pattern is set
		if (null == datePattern) {
			throw new SystemException(FrameworkError.JMS_HANDLER_CONFIG_ERROR, DATE_PATTERN);
		}

		// validate that no static response fields have a null value
		List l = new ArrayList();
		if (staticResponseFields != null) {
			Iterator iter = staticResponseFields.keySet().iterator();
			Object key = null;
			Object value = null;
			while (iter.hasNext()) {
				key = iter.next();
				value = staticResponseFields.get(key);
				if (value == null) {
					throw new SystemException(FrameworkError.JMS_HANDLER_CONFIG_ERROR, key);
				}
				l.add(key);
			}
		}

		// validate that no header response fields have a null value
		if (headerFields != null) {
			Iterator iter = headerFields.keySet().iterator();
			Object key = null;
			Object value = null;
			while (iter.hasNext()) {
				key = iter.next();
				value = headerFields.get(key);
				if (value == null) {
					throw new SystemException(FrameworkError.JMS_HANDLER_CONFIG_ERROR, key);
				}
				l.add(value);
			}
		}

		// validate that no response fields have a null value
		if (responseFields == null || responseFields.isEmpty()) {
			throw new SystemException(FrameworkError.JMS_HANDLER_CONFIG_ERROR, RESPONSE_FIELDS);
		}

		if (responseClassNames == null || responseClassNames.isEmpty()) {
			throw new SystemException(FrameworkError.JMS_HANDLER_CONFIG_ERROR, RESPONSE_CLASS_NAME);
		}

		Iterator iter = responseFields.keySet().iterator();
		Object key = null;
		String value = null;
		String className = null;
		int index = -1;
		responseClasses = new HashMap();
		Class clazz = null;

		while (iter.hasNext()) {
			key = iter.next();
			value = (String) responseFields.get(key);
			if (value == null) {
				throw new SystemException(FrameworkError.JMS_HANDLER_CONFIG_ERROR, key);
			}
			l.add(value);

		}

		// if we have a status message field, we must either:
		// 		have set exception on status field
		//		have a status response field

		if (statusMessageField != null) {
			if (!exceptionOnErrorStatus && statusResponseField == null) {
				throw new SystemException(
					FrameworkError.JMS_HANDLER_CONFIG_ERROR,
				    new Object[] { STATUS_RESPONSE_FIELD, EXCEPTION_ON_ERROR_STATUS });
			}
		}

		// if we have exception on status, ww must also have an error list
		if (exceptionOnErrorStatus && (errorStatus == null || errorStatus.isEmpty())) {
			throw new SystemException(FrameworkError.JMS_HANDLER_CONFIG_ERROR, ERROR_STATUS);
		}

		// ex on status and ex on error cannot both be set
		if (exceptionOnError && exceptionOnErrorStatus) {
			throw new SystemException(
				FrameworkError.JMS_HANDLER_CONFIG_ERROR,
			    new Object[] { EXCEPTION_ON_ERROR, EXCEPTION_ON_ERROR_STATUS });
		}

		// if we have error we must either have ex on status or exception on error or error response field
		if (errorMessageField != null && !exceptionOnError && !exceptionOnErrorStatus && errorResponseField == null) {
			throw new SystemException(
				FrameworkError.JMS_HANDLER_CONFIG_ERROR,
			    new Object[] { EXCEPTION_ON_ERROR, EXCEPTION_ON_ERROR_STATUS, ERROR_RESPONSE_FIELD });
		}

		// if we have an error response field, we cannot have ex on error or ex on status
		if (errorResponseField != null && (exceptionOnError || exceptionOnErrorStatus)) {
			throw new SystemException(
				FrameworkError.JMS_HANDLER_CONFIG_ERROR,
			    new Object[] { EXCEPTION_ON_ERROR, EXCEPTION_ON_ERROR_STATUS, ERROR_RESPONSE_FIELD });
		}

		if (errorResponseField != null) {
			l.add(errorResponseField);
		}

		if (statusResponseField != null) {
			l.add(statusResponseField);
		}

		int numExtraKeys = l.size();
		try {
			for (int i = 0; i < numExtraKeys; i++) {
				value = (String) l.get(i);
				index = value.indexOf('.');
				if (index != -1) {
					value = value.substring(0, index);
				}

				if (!responseClasses.containsKey(value)) {
					className = (String) responseClassNames.get(value);
					if (className == null) {
						throw new SystemException(FrameworkError.JMS_HANDLER_CONFIG_ERROR, value);
					}
					if (!"String".equalsIgnoreCase(className)) {
						clazz = Class.forName(className);
						responseClasses.put(value, clazz);
					}
				}

			}
			// handle all remaining keys
			Iterator responseClassIter = responseClassNames.keySet().iterator();
			while (responseClassIter.hasNext()) {
				value = (String) responseClassIter.next();
				if (!responseClasses.containsKey(value)) {
					className = (String) responseClassNames.get(value);
					if (!"String".equalsIgnoreCase(className)) {
						clazz = Class.forName(className);
						responseClasses.put(value, clazz);
					}
				}
			}

		} catch (ClassNotFoundException e) {
			throw new SystemException(FrameworkError.JMS_HANDLER_CONFIG_ERROR, e);
		}
		// prepare and validate the iterative elements
		prepareIterativeElements();

		try {
			// Defect #299 - SAXParser not thread safe, but SAXParserFactory is
			factory = SAXParserFactory.newInstance();
		} catch (FactoryConfigurationError e) {
			throw new SystemException(FrameworkError.JMS_XML_HANDLER_PARSER_ERROR, e);
		}
	}

	/**
	 * Creates a non-lenient date formatter from the pattern set in the config. Fixes defect #3044 by creating a new instance
	 * of the date formatter for every use making it thread safe. 
	 *
	 * @return a date formatter.
	 */
	private DateFormat createDateFormat() {
		SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat.getDateInstance();
		dateFormat.setLenient(false);
		dateFormat.applyLocalizedPattern(datePattern);

		return dateFormat;
	}

	/**
	 * Sets the configured header fields from the message on the response
	 * 
	 * @param response the response to populate
	 * @param msg the message to read the values from
	 */
	private void setHeaderFields(ServiceResponse response, Message msg) {
		try {
			if (null != headerFields) {
				for (Iterator iterator = headerFields.keySet().iterator(); iterator.hasNext();) {
					String headerField = (String) iterator.next();
					String respField = (String) headerFields.get(headerField);
					String value = msg.getStringProperty(headerField);
					setField(response, respField, null, 0, value, null);
				}
			}

			setField(response, Constants.JMSSERVICE_CORRELATION_ID, null, 0, msg.getJMSCorrelationID(), null);
		} catch (JMSException e) {
			throw new SystemException(FrameworkError.JMS_HEADER_READ_ERROR, e);
		}
	}

	/**
	 * Sets the fields on the response which never change.
	 * @param response the response to populate
	 */
	private void setStaticFields(ServiceResponse response) {
		if (staticResponseFields == null || staticResponseFields.isEmpty()) {
			return;
		}

		String responseField = null;
		String value = null;
		Iterator iter = staticResponseFields.keySet().iterator();

		while (iter.hasNext()) {
			responseField = (String) iter.next();
			value = (String) staticResponseFields.get(responseField);
			setField(response, responseField, null, 0, value, null);
		}
	}

	/**
	 * Sets a field on the response object with the given value. If parent is null,
	 * the value is set on the ServiceResponse.
	 * 
	 * @param response the response to populate
	 * @param fieldName the name of the field to set.
	 * @param iteratorValues the iterator values of fields
	 * @param currentIterator the index of current value in the iterator
	 * @param value the value to set.
	 * @param parent the parent object to set the field on
	 */
	protected void setField(
		ServiceResponse response,
	    String fieldName,
	    LinkedList iteratorValues,
	    int currentIterator,
	    String value,
	    Object parent) {
		String currFieldName = fieldName;
		int dotIndex = fieldName.indexOf('.');
		if (dotIndex != -1) {
			currFieldName = fieldName.substring(0, dotIndex);
		}

		// determine wether this field uses [] and what its name is
		int bracketIndex = currFieldName.indexOf('[');
		String iteratorValue = null;
		if (bracketIndex != -1) {
			// find the value for the specified key
			iteratorValue = (String) iteratorValues.get(currentIterator);
			currentIterator++;
			// update the current field name so that is does not include brackets
			currFieldName = currFieldName.substring(0, bracketIndex);
		}

		if (parent == null) {
			// process a top level field in the response
			Object instance = setRootField(response, fieldName, value, currFieldName, dotIndex, bracketIndex, iteratorValue);
			if (instance != null) {
				setField(response, fieldName.substring(dotIndex + 1), iteratorValues, currentIterator, value, instance);
			}
		} else {
			// we are not at top level
			PropertyDescriptor prop = null;
			try {
				prop = new PropertyDescriptor(currFieldName, parent.getClass());
			} catch (IntrospectionException e) {
				throw new SystemException(FrameworkError.JMS_PROPERTY_READ_ERROR, e);
			}

			if (dotIndex == -1) {
				// this is the outermost leaf in the object graph
				if (bracketIndex == -1) {
					// set the value on the parent
					Object typedValue = convertType(value, prop.getPropertyType());
					invokeMethod(prop.getWriteMethod(), parent, new Object[] { typedValue });
				} else {
					setCollection(fieldName, parent, dotIndex, iteratorValue, prop, value);
				}
			} else {
				// this is a compound field
				Object instance = null;
				if (bracketIndex == -1) {
					instance = setSimpleBranch(parent, prop);
				} else {
					instance = setCollection(fieldName, parent, dotIndex, iteratorValue, prop, instance);
				}
				// recurse
				setField(response, fieldName.substring(dotIndex + 1), iteratorValues, currentIterator, value, instance);
			}
		}
	}

	/**
	 * Sets a value on a branch. The brancg is assumed to be a simple object and not a List, Map or array
	 * @param parent the object to set the value on
	 * @param prop the property on the object
	 * @return the branch instance
	 */
	private Object setSimpleBranch(Object parent, PropertyDescriptor prop) {
		// call the getter on the parent to check it object exists
		Object instance = invokeMethod(prop.getReadMethod(), parent, null);

		// if not, create, set
		if (instance == null) {
			Class propType = prop.getPropertyType();
			instance = createInstance(propType);
			invokeMethod(prop.getWriteMethod(), parent, new Object[] { instance });
		}
		return instance;
	}

	/**
	 * Sets a branch or a leaf that is a Map, List or array.
	 * @param fieldName the name if the field to set
	 * @param parent the the parent object
	 * @param dotIndex the index in the string of the period ('.')
	 * @param iteratorValue the value of the current iterator
	 * @param prop the property descriptor
	 * @param value the value to set
	 * @return the object instance created
	 */
	private Object setCollection(
		String fieldName,
	    Object parent,
	    int dotIndex,
	    String iteratorValue,
	    PropertyDescriptor prop,
	    Object value) {

		// find out what type the array elements are
		Class collectionType = prop.getPropertyType();
		Class componentType = null;
		// get the old array if there is one
		Object result = invokeMethod(prop.getReadMethod(), parent, null);
		Object instance = null;
		if (collectionType.isArray()) {
			instance =
				setArrayField(
					parent,
				    dotIndex,
				    Integer.parseInt(iteratorValue),
				    prop,
				    value,
				    collectionType.getComponentType(),
				    result);
		} else {
			String completeFieldName = fieldName.substring(0, dotIndex);
			// since this is a List or Map we need to be told the data type
			componentType = (Class) responseClasses.get(completeFieldName);
			if (List.class.isAssignableFrom(collectionType)) {
				instance =
					setListField(
						parent,
					    dotIndex,
					    Integer.parseInt(iteratorValue),
					    prop,
					    value,
					    collectionType,
					    componentType,
					    result);
			} else if (Map.class.isAssignableFrom(collectionType)) {
				instance = setMapField(parent, dotIndex, iteratorValue, prop, value, collectionType, componentType, result);
			} else {
				throw new SystemException(
					FrameworkError.JMS_OBJECT_CREATE_ERROR,
				    "Unsupported collection type:" + collectionType);
			}

		}

		return instance;
	}

	/**
	 * Sets a field that is a map type
	 * @param parent the parent object
	 * @param dotIndex the index of the period
	 * @param iteratorValue the iterator value
	 * @param prop the property descriptor
	 * @param value the value to set
	 * @param collectionType the type of collection to use
	 * @param componentType the type of the component in the Map
	 * @param oldObject the old object if there is one 
	 * @return the new instance
	 */
	private Object setMapField(
		Object parent,
	    int dotIndex,
	    String iteratorValue,
	    PropertyDescriptor prop,
	    Object value,
	    Class collectionType,
	    Class componentType,
	    Object oldObject) {
		Object newCollection = null;
		Object instance = null;
		// this is a Map type implementation
		if (oldObject != null) {
			Map map = (Map) oldObject;
			newCollection = map;
			instance = map.get(iteratorValue);
			if (instance == null) {
				if (dotIndex == -1) {
					instance = convertType((String) value, componentType);
				} else {
					instance = createInstance(componentType);
				}
				map.put(iteratorValue, instance);
			}
		} else {
			Map map = null;
			if (collectionType.isInterface()) {
				map = (Map) createInstance(prefMapType);
			} else {
				map = (Map) createInstance(collectionType);
			}
			if (dotIndex == -1) {
				instance = convertType((String) value, componentType);
			} else {
				instance = createInstance(componentType);
			}
			map.put(iteratorValue, instance);
			newCollection = map;
		}
		invokeMethod(prop.getWriteMethod(), parent, new Object[] { newCollection });
		return instance;
	}

	/**
	 * Sets a field that uses a List implementation
	 * @param parent the parent object
	 * @param dotIndex the index of the period
	 * @param iteratorValue the value of the iterator
	 * @param prop the property descriptor
	 * @param value the value
	 * @param collectionType the type of collection
	 * @param componentType the type of components in the List
	 * @param oldObject the old object if there is one
	 * @return the object instance created
	 */
	private Object setListField(
		Object parent,
	    int dotIndex,
	    int iteratorValue,
	    PropertyDescriptor prop,
	    Object value,
	    Class collectionType,
	    Class componentType,
	    Object oldObject) {

		Object newCollection = null;
		Object instance = null;
		// handle List implementations
		if (oldObject != null) {
			// this is a List implementation
			List coll = (List) oldObject;
			int oldLength = coll.size();
			// if the arrayIndex is out of bounds, create a new element
			if (iteratorValue >= oldLength) {
				if (dotIndex == -1) {
					instance = convertType((String) value, componentType);
				} else {
					instance = createInstance(componentType);
				}
				coll.add(instance);
			} else {
				instance = coll.get(iteratorValue);
			}
			newCollection = oldObject;
		} else {
			// check if this is an interface
			List coll = null;
			if (collectionType.isInterface()) {
				coll = (List) createInstance(prefListType);
			} else {
				coll = (List) createInstance(collectionType);
			}
			if (dotIndex == -1) {
				instance = convertType((String) value, componentType);
			} else {
				instance = createInstance(componentType);
			}
			coll.add(instance);
			newCollection = coll;
		}
		invokeMethod(prop.getWriteMethod(), parent, new Object[] { newCollection });
		return instance;
	}

	/**
	 * 
	 * @param parent the parent object
	 * @param dotIndex the index of the period
	 * @param iteratorValue the value of the iterator
	 * @param prop the property descriptor
	 * @param value the value
	 * @param componentType the type of components to be added to the array
	 * @param oldObject the old object if there is one
	 * @return the created instance
	 */
	private Object setArrayField(
		Object parent,
	    int dotIndex,
	    int iteratorValue,
	    PropertyDescriptor prop,
	    Object value,
	    Class componentType,
	    Object oldObject) {
		Object newCollection;
		Object instance;
		// the collection is an array
		int oldLength = 0;
		int setArrayIndex = 0;
		if (oldObject != null) {
			oldLength = Array.getLength(oldObject);
			setArrayIndex = iteratorValue;
			// if the arrayIndex is out of bounds, create a new element
			if (iteratorValue >= oldLength) {
				newCollection = Array.newInstance(componentType, oldLength + 1);
				System.arraycopy(oldObject, 0, newCollection, 0, oldLength);
				if (dotIndex == -1) {
					// this is the outermost leaf, therefor we can proceed with converting the value
					instance = convertType((String) value, componentType);
				} else {
					instance = createInstance(componentType);
				}
			} else {
				instance = Array.get(oldObject, iteratorValue);
				newCollection = oldObject;
			}
		} else {
			instance = createInstance(componentType);
			newCollection = Array.newInstance(componentType, 1);
		}
		Array.set(newCollection, setArrayIndex, instance);
		invokeMethod(prop.getWriteMethod(), parent, new Object[] { newCollection });
		return instance;
	}

	/**
	 * Sets a field which will go directly onto the response object.
	 * 
	 * @param response the response to set the object on
	 * @param fieldName the fieldName
	 * @param value the value of the field
	 * @param currFieldName the current field name (if the field name is compound)
	 * @param dotIndex the index of the first period (.)
	 * @param bracketIndex the index of the first bracket ([
	 * @param iteratorValue the value of the current iterator
	 * @return the created instance
	 */
	private Object setRootField(
		ServiceResponse response,
	    String fieldName,
	    String value,
	    String currFieldName,
	    int dotIndex,
	    int bracketIndex,
	    String iteratorValue) {
		// this the first name and thus has no parent
		Class clazz = (Class) responseClasses.get(currFieldName);
		// the clazz is null, but the bracket index is not, so we need to get the clazz
		if (clazz == null && bracketIndex != -1) {
			if (dotIndex == -1) {
				clazz = (Class) responseClasses.get(fieldName);
			} else {
				clazz = (Class) responseClasses.get(fieldName.substring(0, dotIndex));
			}
		}
		Map m = null;
		Object instance = null;
		if (bracketIndex != -1) {
			// we have an iterative element and need to initialize the container
			m = (Map) response.getData(currFieldName);
			if (m == null) {
				m = new HashMap();
				response.setData(currFieldName, m);
			}
		}
		if (clazz == null) {
			// this is a string and we can set it directly
			if (bracketIndex == -1) {
				response.setData(currFieldName, value);
			} else {
				// if the id already exists, it will be overridden
				m.put(iteratorValue, value);
			}
		} else {
			instance = null;
			if (bracketIndex == -1) {
				instance = response.getData(currFieldName);
				if (instance == null) {
					instance = createInstance(clazz);
					response.setData(currFieldName, instance);
				}
			} else {
				instance = m.get(iteratorValue);
				if (instance == null) {
					instance = createInstance(clazz);
					m.put(iteratorValue, instance);
				}
			}
		}
		return instance;
	}

	/**
		 * Creates a new instance of the given class.
		 * 
		 * @param clazz the class to create an instance from
		 * @return the instance.
		 */
	private Object createInstance(Class clazz) {
		Object instance = null;
		try {
			instance = clazz.newInstance();
		} catch (InstantiationException e) {
			throw new SystemException(FrameworkError.JMS_OBJECT_CREATE_ERROR, e);
		} catch (IllegalAccessException e) {
			throw new SystemException(FrameworkError.JMS_OBJECT_CREATE_ERROR, e);
		}
		return instance;
	}

	/**
	 * Invokes a method on the object.
	 * @param method the Method to invoke
	 * @param target the object to invoke the method on
	 * @param args the arguments to the invocation.
	 * 
	 * @return the return value of the method or null of method has no return.
	 */
	private Object invokeMethod(Method method, Object target, Object[] args) {
		Object result = null;
		try {
			result = method.invoke(target, args);
		} catch (IllegalArgumentException e) {
			throw new SystemException(FrameworkError.JMS_XML_HANDLER_METHOD_CALL_ERROR, e);
		} catch (IllegalAccessException e) {
			throw new SystemException(FrameworkError.JMS_XML_HANDLER_METHOD_CALL_ERROR, e);
		} catch (InvocationTargetException e) {
			throw new SystemException(FrameworkError.JMS_XML_HANDLER_METHOD_CALL_ERROR, e);
		}
		return result;
	}

	/**
		 * Converts a string into the correct type.
		 * @param value the value to convert.
		 * @param clazz the class to convert it into
		 * @return the converted value
		 */
	private Object convertType(String value, Class clazz) {
		String className = clazz.getName();
		Object typedValue = null;

		try {
			// do this test first in order to save time
			if (null == value) {
				typedValue = value;
			} else if ("java.lang.String".equals(className)) {
				typedValue = value;
			} else if ("java.util.Date".equals(className)) {
				typedValue = createDateFormat().parse(value);
			} else if ("java.lang.Double".equals(className) || "double".equals(className)) {
				typedValue = Double.valueOf(value);
			} else if ("java.lang.Float".equals(className) || "float".equals(className)) {
				typedValue = Float.valueOf(value);
			} else if ("java.lang.Integer".equals(className) || "int".equals(className)) {
				typedValue = Integer.valueOf(value);
			} else if ("java.lang.Character".equals(className) || "char".equals(className)) {
				typedValue = new Character(value.charAt(0));
			} else if ("java.lang.Byte".equals(className) || "byte".equals(className)) {
				typedValue = Byte.valueOf(value);
			} else if ("java.lang.Boolean".equals(className) || "boolean".equals(className)) {
				typedValue = Boolean.valueOf(value);
			} else if ("java.lang.Long".equals(className) || "long".equals(className)) {
				typedValue = Long.valueOf(value);
			} else if ("java.lang.Short".equals(className) || "short".equals(className)) {
				typedValue = Short.valueOf(value);
			} else {
				// return as string if no other type
				typedValue = value;
			}
		} catch (Exception e) {
			throw new SystemException(FrameworkError.JMS_XML_CONVERT_ERRROR, new Object[] {value, clazz});
		}
		// return the value (string if no other type has been specified
		return typedValue;
	}

	/**
	 * Loops through the response fields and processes any iterative elements.
	 * These must be put into a separate map.
	 */
	private void prepareIterativeElements() {
		iterativeResponseFields = new HashMap();
		Iterator keys = responseFields.keySet().iterator();
		String key = null;
		int bracketIndex = -1;
		int endBracketIndex = -1;
		String elemName = null;
		String iteratorName = null;
		while (keys.hasNext()) {
			key = (String) keys.next();
			bracketIndex = key.indexOf('[');
			while (bracketIndex != -1) {
				// we have found an iterative element
				endBracketIndex = key.indexOf(']', bracketIndex);
				elemName = key.substring(0, bracketIndex);
				iteratorName = key.substring(bracketIndex + 1, endBracketIndex);
				// Maybe later, we need to check if the key exists and if it does, 
				// check that the iteratorName is the same
				iterativeResponseFields.put(elemName, iteratorName);
				bracketIndex = key.indexOf('[', endBracketIndex);
			}
		}
	}

	/**
	 * Sets the mapping for the static response fields .
	 * @param map the mapping
	 */
	public void setStaticResponseFields(Map map) {
		staticResponseFields = map;
	}

	/**
	 * Sets the mapping for the header fields
	 * @param map the mapping
	 */
	public void setHeaderFields(Map map) {
		headerFields = map;
	}

	/**
	 * Sets the mapping for the response fields
	 * @param map the mapping
	 */
	public void setResponseFields(Map map) {
		responseFields = map;
	}

	/**
	 * Sets the class names for the top level properties.
	 * @param map the class names
	 */
	public void setResponseClassNames(Map map) {
		responseClassNames = map;
	}

	/**
	 * Sets the xpath in the message which points to the error message
	 * @param string the xpath
	 */
	public void setErrorMessageField(String string) {
		errorMessageField = string;
	}

	/**
	 * The field name on the response where the error message can be set
	 * @param string the field name
	 */
	public void setErrorResponseField(String string) {
		errorResponseField = string;
	}

	/**
	 * The xpath pointing to the status message/code
	 * @param string the xpath
	 */
	public void setStatusMessageField(String string) {
		statusMessageField = string;
	}

	/**
	 * The name of the field in the response where the status should be set
	 * @param string the name of the field
	 */
	public void setStatusResponseField(String string) {
		statusResponseField = string;
	}

	/**
	 * Sets a list of all statuses which are errors
	 * @param list the list of error statuses
	 */
	public void setErrorStatus(List list) {
		errorStatus = list;
	}

	/**
	 * Sets wether or not to throw an exception of there is an error message 
	 * @param b the flag
	 */
	public void setExceptionOnError(boolean b) {
		exceptionOnError = b;
	}

	/**
	 * Sets wether or not to throw and exception if there is an error status
	 * @param b the flag
	 */
	public void setExceptionOnErrorStatus(boolean b) {
		exceptionOnErrorStatus = b;
	}

	/**
	 * Gets addition message info
	 * 
	 * @return message info
	 */
	public String getStatusMessageInfo() {
		return statusMessageInfo;
	}

	/**
	 * Ses additional message info
	 * 
	 * @param string message info
	 */
	public void setStatusMessageInfo(String string) {
		statusMessageInfo = string;
	}

	/**
	 * Setter for date format
	 * 
	 * @param string formatet på date
	 */
	public void setDatePattern(String string) {
		datePattern = string;
	}

}
