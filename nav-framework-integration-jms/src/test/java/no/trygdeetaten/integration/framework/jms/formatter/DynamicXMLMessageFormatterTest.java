package no.trygdeetaten.integration.framework.jms.formatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Message;

import junit.framework.TestCase;

import no.trygdeetaten.common.framework.service.ServiceRequest;
import no.trygdeetaten.integration.framework.jms.DummyQueueSession;

/**
 * Test class for Dynamic XML message formatting.
 * 
 * @author persone5d69f3729a8, Accenture
 * @version $Id: DynamicXMLMessageFormatterTest.java 2803 2006-03-01 11:39:42Z
 *          skb2930 $
 */
public class DynamicXMLMessageFormatterTest extends TestCase {

	/**
	 * Constructor for DynamicXMLMessageFormatterTest.
	 * 
	 * @param arg0
	 */
	public DynamicXMLMessageFormatterTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test init method call.
	 */
	public void testInit() {
	}

	/**
	 * Test formatting a message.
	 */
	public void testFormatMessage() {
		try {

			// TEST CASE #1: Normal operation

			// creates a formatter and set basic properties
			DynamicXMLMessageFormatter formatter = new DynamicXMLMessageFormatter();
			setPropertiesInFormatter(formatter);

			// creates a request and dummy session
			ServiceRequest request = new ServiceRequest();
			DummyQueueSession session = new DummyQueueSession();

			// call init method
			formatter.init();

			// set mal key
			request.setData("mal", "keyToFile");

			// set bean to be serialized
			OppdragTestBean bean = createTestBean();
			request.setData("bean", "keyToBean");
			request.setData("keyToBean", bean);

			Message msg = formatter.formatMessage(session, request);
			assertNotNull("Message serialized is null; Not OK!", msg);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexptected exception thrown:" + e.getMessage());
		}
	}

	private OppdragTestBean createTestBean() {
		OppdragTestBean bean = new OppdragTestBean();

		List elements = new ArrayList();
		for (int i = 0; i < 4; i++) {
			OppdragTestBeanElement beanElement = new OppdragTestBeanElement();
			beanElement.setVal("JALLA");
			beanElement.setDato(new Date());
			elements.add(beanElement);
		}
		bean.setElements(elements);
		bean.setD(100);
		bean.setIntTest(9999);
		bean.setTestField("TEST");

		return bean;
	}

	/**
	 * Sets basic properties in formatter.
	 * 
	 * @param formatter
	 *            the formatter to be set
	 */
	private void setPropertiesInFormatter(DynamicXMLMessageFormatter formatter) {
		formatter.setXmlFileMappings(createXmlFileMappingExample());
		formatter.setKeyField("mal");
		formatter.setKeyBean("bean");
	}

	/**
	 * Creates a xml file mapping example for mapping.
	 * 
	 * @return map with file used for mapping
	 */
	private Map createXmlFileMappingExample() {
		Map map = new HashMap();
		map.put("keyToFile", "testFile5.xml");
		return map;
	}
}
