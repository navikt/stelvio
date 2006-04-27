package no.nav.integration.framework.jms.handler.stubs;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.jms.Message;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.InputStreamResource;

import no.nav.integration.framework.jms.handler.XMLMessageHandler;
import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.error.SystemException;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceResponse;

/**
 * Stub for properties that does not yet exist in TPS.
 * 
 * @author person356941106810, Accenture
 * @version $Id: TPSXMLMessageHandlerStub.java 2372 2005-06-23 14:53:27Z skb2930 $
 */
public class TPSXMLMessageHandlerStub extends XMLMessageHandler {

	protected static final String PERSON_DATA = "personData";

	XmlBeanFactory historikkFactory = null;
	String stubFileName = null;
	String stubPostSted = "Oslo";
	String stubSprakKode = "NO";

	/**
	 * Handles the response message by calling the super implementations and then 
	 * adding the stubbed properties
	 * @see no.nav.integration.framework.jms.MessageHandler#handleMessage(javax.jms.Message)
	 */
	public ServiceResponse handleMessage(Message msg) throws ServiceFailedException {
		ServiceResponse resp = super.handleMessage(msg);
		String fnr = (String) getValueFromRootObject(resp, PERSON_DATA, "fnr");

		if (fnr != null) {
			setValueOnRootObject(resp, PERSON_DATA, "sprakKode", stubSprakKode);
			setValueOnRootObject(resp, PERSON_DATA, "postSted", stubPostSted);

			try {
				List historikk = (List) historikkFactory.getBean("fnr" + fnr);

				if (historikk != null) {
					setValueOnRootObject(resp, PERSON_DATA, "fnrHistorikk", historikk);
				}

			} catch (NoSuchBeanDefinitionException e) {
				System.err.println("**** TPSXMLMessageHandlerStub fnrHistorikk ERROR:  " + e.getMessage());
			}

			try {
				List historikk = (List) historikkFactory.getBean("navn" + fnr);
				if (historikk != null) {
					setValueOnRootObject(resp, PERSON_DATA, "navneHistorikk", historikk);
				}

			} catch (NoSuchBeanDefinitionException e) {
				System.err.println("**** TPSXMLMessageHandlerStub navneHistorikk ERROR:  " + e.getMessage());
			}

			try {
				List historikk = (List) historikkFactory.getBean("adrln" + fnr);
				if (historikk != null) {
					setValueOnRootObject(resp, PERSON_DATA, "adresselinjeHistorikk", historikk);
				}

			} catch (NoSuchBeanDefinitionException e) {
				System.err.println("**** TPSXMLMessageHandlerStub adresselinjeHistorikk ERROR:  " + e.getMessage());
			}

		}
		return resp;
	}

	/**
	 * Initializes the stub
	 * @see no.nav.integration.framework.jms.MessageHandler#init()
	 */
	public void init() {
		super.init();
		if (stubFileName != null) {
			InputStream stream =
				new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(stubFileName));
			historikkFactory = new XmlBeanFactory(new InputStreamResource(stream));
		}
	}

	/**
	 * Returns the field value from the specified object.
	 * 
	 * @param response the service response.
	 * @param objectName name of the object.
	 * @param fieldName name of the field.
	 * @return field contents.
	 */
	private Object getValueFromRootObject(ServiceResponse response, String objectName, String fieldName) {
		Object rootObject = response.getData(objectName);

		if (rootObject == null) {
			return null;
		}
		Object value = null;
		PropertyDescriptor desc;
		try {
			desc = new PropertyDescriptor(fieldName, rootObject.getClass());
			Method meth = desc.getReadMethod();
			value = meth.invoke(rootObject, null);
		} catch (IntrospectionException e) {
			throw new SystemException(FrameworkError.JMS_PROPERTY_READ_ERROR, e);
		} catch (IllegalArgumentException e) {
			throw new SystemException(FrameworkError.JMS_PROPERTY_READ_ERROR, e);
		} catch (IllegalAccessException e) {
			throw new SystemException(FrameworkError.JMS_PROPERTY_READ_ERROR, e);
		} catch (InvocationTargetException e) {
			throw new SystemException(FrameworkError.JMS_PROPERTY_READ_ERROR, e);
		}
		return value;
	}

	/**
	 * Sets the field value on the specified object.
	 * 
	 * @param response the service response.
	 * @param objectName name of the object.
	 * @param fieldName name of the field.
	 * @param value field contents.
	 */
	private void setValueOnRootObject(ServiceResponse response, String objectName, String fieldName, Object value) {
		Object rootObject = response.getData(objectName);

		if (rootObject == null) {
			return;
		}
		PropertyDescriptor desc;
		try {
			desc = new PropertyDescriptor(fieldName, rootObject.getClass());
			Method meth = desc.getWriteMethod();
			meth.invoke(rootObject, new Object[] { value });
		} catch (IntrospectionException e) {
			throw new SystemException(FrameworkError.JMS_PROPERTY_READ_ERROR, e);
		} catch (IllegalArgumentException e) {
			throw new SystemException(FrameworkError.JMS_PROPERTY_READ_ERROR, e);
		} catch (IllegalAccessException e) {
			throw new SystemException(FrameworkError.JMS_PROPERTY_READ_ERROR, e);
		} catch (InvocationTargetException e) {
			throw new SystemException(FrameworkError.JMS_PROPERTY_READ_ERROR, e);
		}
	}

	/**
	 * Gets the name of the stub file.
	 * 
	 * @return the stub filename. 
	 */
	public String getStubFileName() {
		return stubFileName;
	}

	/**
	 * Sets the name of the stub file.
	 * 
	 * @param string the stub filename.
	 */
	public void setStubFileName(String string) {
		stubFileName = string;
	}

	/**
	 * Gets the Spring Framework's bean factory for the stub file.
	 * @return the bean factory.
	 */
	public XmlBeanFactory getHistorikkFactory() {
		return historikkFactory;
	}
}
