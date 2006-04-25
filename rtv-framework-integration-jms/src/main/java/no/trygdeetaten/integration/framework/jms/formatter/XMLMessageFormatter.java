package no.trygdeetaten.integration.framework.jms.formatter;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.core.TransferObject;
import no.trygdeetaten.common.framework.error.SystemException;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceRequest;

import no.trygdeetaten.integration.framework.jms.MessageFormatter;

/**
 * Formats data contained in a ServiceRequest into a javax.jms.TextMessage with
 * XML in the message body. The format of the XML is specified using Hibernate.
 * 
 * @author person356941106810, Accenture
 * @version $Id: XMLMessageFormatter.java 2737 2006-01-12 02:24:04Z skb2930 $
 */
public class XMLMessageFormatter implements MessageFormatter {

	// Holds the procedure names and the xml template it maps to
	private Map xmlFileMappings = null;

	// Holds the actual xml templates (internal use only)
	private Map xmlMappings = null;

	// holds the name and values of the dynamic header fields
	private Map dynamicHeaderFields = null;

	// holds the static header fields
	private Map staticHeaderFields = null;

	// holds the name of the field which should be used as a correclation-id
	private String jmsCorrelationIdField = null;

	// hold the keyField which will determine what input value should determine what template to use.
	private String keyField = null;

	// holds the fieldsnames to map into the templates
	private String[] fieldNames = null;

	// the log
	private Log log = LogFactory.getLog(XMLMessageFormatter.class.getName());

	private boolean generateCorrelationId = false;

	/**
	 * Format a message according to internal and backend system rules.
	 * 
	 * @param session the session to use for creating messages.
	 * @param input the input to format.
	 * @throws ServiceFailedException the requested template does not exist
	 * @return the finshed JMS message.
	 */
	public Message formatMessage(Session session, ServiceRequest input) throws ServiceFailedException {
		// determine which template to use
		String templateName = (String) getField(input, keyField);
		if (templateName == null || templateName.length() == 0) {
			throw new ServiceFailedException(FrameworkError.JMS_MISSING_INPUT_ERROR, keyField);
		}

		// get the template xml string
		String template = (String) xmlMappings.get(templateName);
		if (template == null) {
			throw new ServiceFailedException(FrameworkError.JMS_XML_MISSING_TEMPLATE_ERROR, templateName);
		}
		Object[] args = null;
		MessageFormat format = new MessageFormat(template);
		int numPositions = format.getFormatsByArgumentIndex().length;
		int numFields = 0;
		if (fieldNames != null) {
			numFields = fieldNames.length;
		}
		int arrSize = numFields > numPositions ? numFields : numPositions;

		args = new Object[arrSize];
		Arrays.fill(args, "");

		// retrieve all fields from the request
		Object value = null;
		for (int i = 0; i < numFields; i++) {
			value = getField(input, fieldNames[i]);
			if (value != null) {
				args[i] = value;
			}
		}
		
		// makes a check to see if not formatter is set and object is NULL
		// if this is the case the formatter will crash if we dont remove
		// the formatter
		Format[] formats = format.getFormatsByArgumentIndex();
		for (int i=0; i < formats.length; i++) {
			Format tmp = formats[i];
			if (tmp instanceof SimpleDateFormat) {
				// the object is not set and SimpleDateFormat
				if ("".equals(args[i])) {
					formats[i] = null;
				}
			}
		}
		format.setFormatsByArgumentIndex(formats);
		String content = format.format(args, new StringBuffer(), null).toString();
		TextMessage msg = null;
		try {
			msg = session.createTextMessage(content);
		} catch (JMSException e) {
			throw new SystemException(FrameworkError.JMS_MESSAGE_CREATE_ERROR, e);
		}
		formatHeader(msg, input);

		return msg;
	}

	/**
	 * Performs additional initialization and validation of the configuration of this
	 * MessageFormatter. This methods reads all the xml file mappings. These files must exist in
	 * the classpath.
	 */
	public void init() {
		if (xmlFileMappings == null || xmlFileMappings.size() == 0) {
			throw new SystemException(FrameworkError.JMS_XML_FORMATTER_CONFIG_ERROR, "xmlFileMappings");
		}

		if (keyField == null || keyField.length() == 0) {
			throw new SystemException(FrameworkError.JMS_XML_FORMATTER_CONFIG_ERROR, "keyField");
		}
		// validate that no staticHeaderFields are null
		if (staticHeaderFields != null && staticHeaderFields.size() != 0) {
			Iterator iter = staticHeaderFields.keySet().iterator();

			while (iter.hasNext()) {
				if (staticHeaderFields.get(iter.next()) == null) {
					throw new SystemException(FrameworkError.JMS_XML_FORMATTER_CONFIG_ERROR, iter.next());
				}
			}
		}

		// validate that no dynamicHeaderFields are null
		if (dynamicHeaderFields != null && dynamicHeaderFields.size() != 0) {
			Iterator iter = dynamicHeaderFields.keySet().iterator();

			while (iter.hasNext()) {
				if (dynamicHeaderFields.get(iter.next()) == null) {
					throw new SystemException(FrameworkError.JMS_XML_FORMATTER_CONFIG_ERROR, iter.next());
				}
			}
		}

		// loop through all the filenames and read the templates
		Iterator keyIter = xmlFileMappings.keySet().iterator();
		Object key = null;
		String fileName = null;
		String contents = null;
		xmlMappings = new HashMap();

		while (keyIter.hasNext()) {
			key = keyIter.next();
			fileName = (String) xmlFileMappings.get(key);
			contents = readFile(fileName);
			xmlMappings.put(key, contents);
		}

		generateCorrelationId = StringUtils.isBlank(jmsCorrelationIdField);
	}

	/**
	 * Reads the filename specified and returns the contents of the file as a string.
	 * @param fileName the file to read.
	 * @return the contents of the file.
	 */
	private String readFile(String fileName) {
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		if (stream == null) {
			throw new SystemException(FrameworkError.JMS_XML_FORMATTER_READ_ERROR, fileName);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		StringBuffer buf = new StringBuffer();
		try {
			String line = reader.readLine();
			while (line != null) {
				buf.append(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			throw new SystemException(FrameworkError.JMS_XML_FORMATTER_READ_ERROR, e, fileName);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					log.debug("Unable to close stream reading from file:" + fileName);
				}
			}
		}

		return buf.length() == 0 ? null : buf.toString();
	}

	/**
	 * Calls a getter method on the parent object with the name specified in fieldName.
	 * @param parent the parent object.
	 * @param fieldName the field name.
	 * @return the value.
	 */
	private Object getField(Object parent, String fieldName) {
		int indexOfDot = fieldName.indexOf('.');
		String newFieldName = fieldName;
		if (indexOfDot != -1) {
			newFieldName = fieldName.substring(0, indexOfDot);
		}

		Object result = null;
		// make sure to check if parent is null
		if (parent == null) {
			return result;
		}
		
		if (parent instanceof TransferObject) {
			TransferObject o = (TransferObject) parent;
			result = o.getData(newFieldName);
		} else {
			Class parentClass = parent.getClass();
			try {
				PropertyDescriptor descriptor = new PropertyDescriptor(newFieldName, parentClass);
				Method meth = descriptor.getReadMethod();
				result = meth.invoke(parent, null);
			} catch (SecurityException e) {
				throw new SystemException(FrameworkError.JMS_PROPERTY_READ_ERROR, e);
			} catch (IllegalArgumentException e) {
				throw new SystemException(FrameworkError.JMS_PROPERTY_READ_ERROR, e);
			} catch (IllegalAccessException e) {
				throw new SystemException(FrameworkError.JMS_PROPERTY_READ_ERROR, e);
			} catch (InvocationTargetException e) {
				throw new SystemException(FrameworkError.JMS_PROPERTY_READ_ERROR, e);
			} catch (IntrospectionException e) {
				throw new SystemException(FrameworkError.JMS_PROPERTY_READ_ERROR, e);
			}
		}

		// make sure we call the method recursively in order to traverse a tree
		if (indexOfDot != -1) {
			result = getField(result, fieldName.substring(indexOfDot + 1));
		}
		return result;
	}

	/**
	 * Sets the header fields on the JMS message. This sets both the static and dynamic 
	 * header fields.
	 * 
	 * @param msg the message to set the header on.
	 * @param input the object to read the dynamic fields from.
	 */
	private void formatHeader(Message msg, Object input) {

		String correlationId = null;
		if (generateCorrelationId) {
			correlationId = Long.toString(System.currentTimeMillis());
		} else {
			correlationId = (String) getField(input, jmsCorrelationIdField);
		}
		try {
			msg.setJMSCorrelationID(correlationId);
		} catch (JMSException e) {
			throw new SystemException(FrameworkError.JMS_HEADER_SET_ERROR, e, jmsCorrelationIdField);
		}

		// set the static fields
		if (staticHeaderFields != null && staticHeaderFields.size() > 0) {
			Iterator iter = staticHeaderFields.keySet().iterator();
			String key = null;
			String value = null;
			try {
				while (iter.hasNext()) {
					key = (String) iter.next();
					value = (String) staticHeaderFields.get(key);
					msg.setStringProperty(key, value);
				}
			} catch (JMSException e) {
				throw new SystemException(FrameworkError.JMS_HEADER_SET_ERROR, e, key);
			}
		}
		// set the dynamic fields
		if (dynamicHeaderFields != null && dynamicHeaderFields.size() != 0) {
			Iterator iter = dynamicHeaderFields.keySet().iterator();
			String key = null;
			String value = null;
			String realValue = null;
			try {
				while (iter.hasNext()) {
					key = (String) iter.next();
					value = (String) dynamicHeaderFields.get(key);
					realValue = (String) getField(input, value);
					if (realValue != null) {
						msg.setStringProperty(key, realValue);
					}
				}
			} catch (JMSException e) {
				throw new SystemException(FrameworkError.JMS_HEADER_SET_ERROR, e, key);
			}
		}
	}

	/**
	 * Sets the XML file mappings for this XMLFormatter. One mapping
	 * represents one XML template. This can be used to map method calls against
	 * templates.
	 * 
	 * @param map the XML file mappings.
	 */
	public void setXmlFileMappings(Map map) {
		xmlFileMappings = map;
	}

	/**
	 * Sets the map over the header fields which should be mapped to values
	 * in the request.
	 * 
	 * @param map the dynamic header field mapping
	 */
	public void setDynamicHeaderFields(Map map) {
		dynamicHeaderFields = map;
	}

	/**
	 * Sets the map of the configured header fields. These fields
	 * do not change between each request.
	 * 
	 * @param map the configured header fields.
	 */
	public void setStaticHeaderFields(Map map) {
		staticHeaderFields = map;
	}

	/**
	 * Sets the name of the field in the request which should be used as 
	 * a JMSCorrelationId. If this is not set, an ID will be generated.
	 *
	 * @param jmsCorrelationIdField the field name to use as correlation ID
	 */
	public void setJmsCorrelationIdField(String jmsCorrelationIdField) {
		this.jmsCorrelationIdField = jmsCorrelationIdField;
	}

	/**
	 * Sets which field from the request that will determine which of the configured templates
	 * should be used for formatting.
	 * 
	 * @param keyField the key fields.
	 */
	public void setKeyField(String keyField) {
		this.keyField = keyField;
	}

	/**
	 * Sets the names of the fields which will be used to extract values from 
	 * the request.
	 * 
	 * @param fieldNames the request field names.
	 */
	public void setFieldNames(String[] fieldNames) {
		if (null == fieldNames) {
			this.fieldNames = null;
		} else {
			this.fieldNames = new String[fieldNames.length];
			System.arraycopy(fieldNames, 0, this.fieldNames, 0, this.fieldNames.length);
		}
	}
}
