package no.nav.integration.framework.jms.formatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

import no.nav.integration.framework.jms.MessageFormatter;
import no.nav.common.framework.FrameworkError;
import no.nav.common.framework.core.TransferObject;
import no.nav.common.framework.error.SystemException;
import no.nav.common.framework.service.ServiceFailedException;
import no.nav.common.framework.service.ServiceRequest;

/**
 * Dynamic formats data contained in a ServiceRequest into a
 * javax.jms.TextMessage with XML in the message body.
 * 
 * @author persone5d69f3729a8, Accenture
 * @version $Id: DynamicXMLMessageFormatter.java 2803 2006-03-01 11:39:42Z
 *          skb2930 $
 */
public class DynamicXMLMessageFormatter implements MessageFormatter {

	// hold the keyField which will determine what input value should determine
	// what mapping to use.
	private String keyField = null;

	// holds key to bean that is to be serialized
	private String keyBean = null;

	// holds the xml mapping
	private Map xmlFileMappings = null;

	// Holds the actual xml templates (internal use only)
	private Map xmlMappings = null;

	// the log
	private Log log = LogFactory.getLog(DynamicXMLMessageFormatter.class.getName());

	/**
	 * Performs additional initialization and validation of the configuration of
	 * this MessageFormatter.
	 */
	public void init() {
		if (xmlFileMappings == null || xmlFileMappings.size() == 0) {
			throw new SystemException(FrameworkError.JMS_XML_FORMATTER_CONFIG_ERROR, "xmlFileMappings");
		}

		if (keyField == null || keyField.length() == 0) {
			throw new SystemException(FrameworkError.JMS_XML_FORMATTER_CONFIG_ERROR, "keyField");
		}

		if (keyBean == null || keyBean.length() == 0) {
			throw new SystemException(FrameworkError.JMS_XML_FORMATTER_CONFIG_ERROR, "keyBean");
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

	}

	/**
	 * Format a message according to internal and backend system rules.
	 * 
	 * @param session
	 *            the session to use for creating messages.
	 * @param input
	 *            the input to format.
	 * @throws ServiceFailedException
	 *             the requested template does not exist
	 * @return the finshed JMS message.
	 */
	public Message formatMessage(Session session, ServiceRequest input) throws ServiceFailedException {

		// determine which mapping to use
		String mappingName = (String) getField(input, keyField);
		if (mappingName == null || mappingName.length() == 0) {
			throw new ServiceFailedException(FrameworkError.JMS_MISSING_INPUT_ERROR, keyField);
		}
		String mapping = (String) xmlMappings.get(mappingName);
		if (mapping == null) {
			throw new ServiceFailedException(FrameworkError.JMS_XML_MISSING_TEMPLATE_ERROR, mappingName);
		}

		// determines which bean to serialize
		String beanName = (String) getField(input, keyBean);
		if (beanName == null || beanName.length() == 0) {
			throw new ServiceFailedException(FrameworkError.JMS_MISSING_INPUT_ERROR, keyBean);
		}
		Object domainObject = getField(input, beanName);
		if (domainObject == null) {
			throw new ServiceFailedException(FrameworkError.JMS_MISSING_INPUT_ERROR, beanName);
		}

		TextMessage msg = null;
		try {
			msg = session.createTextMessage(serializeBean(mapping, domainObject));
		} catch (JMSException e) {
			throw new SystemException(FrameworkError.JMS_MESSAGE_CREATE_ERROR, e);
		}
		return msg;
	}

	/**
	 * Method that serialize a bean.
	 * 
	 * @param object
	 *            the bean to be serialized.
	 * @return serialized bean(xml).
	 */
	private String serializeBean(String mapping, Object object) {
		String serializedBean = null;
		Writer writer;

		try {
			InputSource inputSource = new InputSource(new StringReader(mapping));
			writer = new StringWriter();
			Mapping map = new Mapping();
			map.loadMapping(inputSource);
			Marshaller marshaller = new Marshaller(writer);
			marshaller.setMapping(map);
			marshaller.marshal(object);
			serializedBean = writer.toString();
		} catch (IOException ioe) {
			throw new SystemException(FrameworkError.JMS_MESSAGE_CREATE_ERROR, ioe);
		} catch (MarshalException me) {
			throw new SystemException(FrameworkError.JMS_MESSAGE_CREATE_ERROR, me);
		} catch (ValidationException ve) {
			throw new SystemException(FrameworkError.JMS_MESSAGE_CREATE_ERROR, ve);
		} catch (MappingException me) {
			throw new SystemException(FrameworkError.JMS_MESSAGE_CREATE_ERROR, me);
		}
		return serializedBean;
	}

	/**
	 * Gets the field specified if a Transfer Object, or the object if not a
	 * transfer object.
	 * 
	 * @param parent
	 *            the parent object.
	 * @param fieldName
	 *            the field name.
	 * @return the value.
	 */
	private Object getField(Object parent, String fieldName) {
		Object result = null;
		// make sure to check if parent is null
		if (parent == null) {
			return result;
		}

		if (parent instanceof TransferObject) {
			TransferObject o = (TransferObject) parent;
			result = o.getData(fieldName);
		} else {
			result = parent;
		}
		return result;
	}

	/**
	 * Reads the filename specified and returns the contents of the file as a
	 * string.
	 * 
	 * @param fileName
	 *            the file to read.
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
	 * Sets the XML file mappings for this dynamic XMLFormatter. One mapping
	 * represents one mapping between av POJO and XML. This can be used to map
	 * method calls against templates.
	 * 
	 * @param map
	 *            the XML file mappings.
	 */
	public void setXmlFileMappings(Map map) {
		xmlFileMappings = map;
	}

	/**
	 * Sets which field from the request that will determine which of the
	 * configured templates should be used for formatting.
	 * 
	 * @param string
	 *            the key fields.
	 */
	public void setKeyField(String string) {
		keyField = string;
	}

	/**
	 * Sets the field from the request that will determine which bean should be
	 * serialized.
	 * 
	 * @param string
	 *            the key to the bean.
	 */
	public void setKeyBean(String string) {
		keyBean = string;
	}

}
