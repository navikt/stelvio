package no.stelvio.integration.jms.formatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import no.stelvio.integration.jms.DummyQueueSession;
import no.stelvio.integration.jms.DummyTextMessage;
import no.stelvio.integration.jms.formatter.XMLMessageFormatter;
import no.stelvio.common.FrameworkError;
import no.stelvio.common.core.TransferObject;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceRequest;


/**
 * 
 * @author person356941106810, Accenture
 */
public class XMLMessageFormatterTest extends TestCase {

	/**
	 * Constructor for XMLMessageFormatterTest.
	 * @param arg0
	 */
	public XMLMessageFormatterTest(String arg0) {
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

	public void testFormatMessage() {
		try {

			XMLMessageFormatter formatter = new XMLMessageFormatter();
			formatter.setKeyField("testKeyField");
			Map m = new HashMap();
			m.put("Test1", "testFile1.xml");
			m.put("Test2", "testFile2.xml");
			formatter.setXmlFileMappings(m);
			formatter.init();

			// test: the keyfield does does not exist in the input
			boolean ex = false;
			ServiceRequest request = new ServiceRequest();
			try {
				formatter.formatMessage(null, request);
			} catch (ServiceFailedException e) {
				ex = true;
				assertEquals("Test 1.", FrameworkError.JMS_MISSING_INPUT_ERROR.getCode(), e.getErrorCode());
			}
			assertTrue("Test 2.", ex);

			// test: the desired template does not exist	
			request.setData("testKeyField", "nonExistent");
			try {
				formatter.formatMessage(null, request);
			} catch (ServiceFailedException e) {
				ex = true;
				assertEquals("Test 3.", FrameworkError.JMS_XML_MISSING_TEMPLATE_ERROR.getCode(), e.getErrorCode());
			}
			assertTrue("Test 4.", ex);
			request.setData("testKeyField", "Test1");
			ex = false;

			// test: creation of the textmessage fails
			DummyQueueSession session = new DummyQueueSession();
			session.setFailOnMessageCreate(true);
			try {
				formatter.formatMessage(session, request);
			} catch (SystemException e1) {
				ex = true;
				assertEquals("Test 5.", FrameworkError.JMS_MESSAGE_CREATE_ERROR.getCode(), e1.getErrorCode());
			}
			assertTrue("Test 6.", ex);
			session.setFailOnMessageCreate(false);

			// test: none of the fields exist in the input		
			DummyTextMessage msg = (DummyTextMessage) formatter.formatMessage(session, request);
			String file1 = readFile("testFile1.xml");
			assertEquals("Test 7.", MessageFormat.format(file1, new String[] { "", "" }), msg.getText());

			// test: dot notation fieldnames are correct
			request.setData("field1", "This is field 1");
			TransferObject o = new ServiceRequest();
			o.setData("subField2", "This is field 2");
			request.setData("field2", o);
			String[] fieldNames = new String[] { "field1", "field2.subField2" };
			formatter.setFieldNames(fieldNames);
			msg = (DummyTextMessage) formatter.formatMessage(session, request);
			assertEquals(
				"Test 8.",
				MessageFormat.format(file1, new String[] { "This is field 1", "This is field 2" }),
				msg.getText());

			// test: correlation id is set correctly
			String corrId = msg.getJMSCorrelationID();
			try {
				Long.parseLong(corrId);
			} catch (RuntimeException e2) {
				fail("Test 9. This exception should not have happened");
			}
			request.setData("correlationId", "TEST_CORR_ID");
			formatter.setJmsCorrelationIdField("correlationId");
			formatter.init();
			msg = (DummyTextMessage) formatter.formatMessage(session, request);
			assertEquals("Test 10.", "TEST_CORR_ID", msg.getJMSCorrelationID());

			// test: all static headers are set correcly
			Map staticHeaders = new HashMap();
			staticHeaders.put("Static1", "StaticVal1");
			staticHeaders.put("Static2", "StaticVal2");
			formatter.setStaticHeaderFields(staticHeaders);
			msg = (DummyTextMessage) formatter.formatMessage(session, request);
			assertEquals("Test 11.", "StaticVal1", msg.getStringProperty("Static1"));
			assertEquals("Test 12.", "StaticVal2", msg.getStringProperty("Static2"));

			// test: all dynamic headers are set correcly
			Map dynamicHeaders = new HashMap();
			dynamicHeaders.put("Dynamic1", "dynHeaderVal1");
			dynamicHeaders.put("Dynamic2", "dynHeaderVal2");
			formatter.setDynamicHeaderFields(dynamicHeaders);
			request.setData("dynHeaderVal1", "TestDynHeaderVal1");
			request.setData("dynHeaderVal2", "TestDynHeaderVal2");
			msg = (DummyTextMessage) formatter.formatMessage(session, request);
			assertEquals("Test 13.", "TestDynHeaderVal1", msg.getStringProperty("Dynamic1"));
			assertEquals("Test 14.", "TestDynHeaderVal2", msg.getStringProperty("Dynamic2"));

			// test: the result message has correct content
			formatter.setFieldNames(new String[] { "field1", "field2", "field3" });
			request.setData("field1", "data1");
			request.setData("field2", "data2");
			request.setData("field3", "data3");
			request.setData("testKeyField", "Test2");
			String file2 = readFile("testFile2.xml");
			msg = (DummyTextMessage) formatter.formatMessage(session, request);
			assertEquals("Test 15.", MessageFormat.format(file2, new String[] { "data1", "data2", "data3" }), msg.getText());

			// test: use reflection to get field value
			formatter.setFieldNames(new String[] { "field1", "field2", "field3.testField" });
			TestBean bean = new TestBean();
			bean.setTestField("TestValue");
			request.setData("field3",bean);
			msg = (DummyTextMessage) formatter.formatMessage(session, request);
			assertEquals("Test 16.", MessageFormat.format(file2, new String[] { "data1", "data2", "TestValue" }), msg.getText());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexptected exception thrown:" + e.getMessage());
		}
	}

	public void testInit() {
		try {
			XMLMessageFormatter formatter = new XMLMessageFormatter();

			// test: xml file mappings must be present
			boolean ex = false;
			try {
				formatter.init();
			} catch (SystemException s) {
				ex = true;
				assertEquals(
					"Test 1: Unexpected error code.",
					FrameworkError.JMS_XML_FORMATTER_CONFIG_ERROR.getCode(),
					s.getErrorCode());
			}
			assertTrue("Test 2: Expected exception was not thrown.", ex);
			ex = false;
			Map m = new HashMap();
			m.put("Test1", "testFile1.xml");
			m.put("Test2", "testFile2.xml");

			// test: keyfield is missing
			formatter.setXmlFileMappings(m);
			try {
				formatter.init();
			} catch (SystemException s) {
				ex = true;
				assertEquals(
					"Test 2: Unexpected error code.",
					FrameworkError.JMS_XML_FORMATTER_CONFIG_ERROR.getCode(),
					s.getErrorCode());
			}
			assertTrue("Test 3: Expected exception was not thrown.", ex);
			ex = false;
			formatter.setKeyField("keyField");

			// test: one of the static header fields are null
			Map sh = new HashMap();
			sh.put("static1", "static1");
			sh.put("static2", null);
			formatter.setStaticHeaderFields(sh);
			try {
				formatter.init();
			} catch (SystemException s) {
				ex = true;
				assertEquals(
					"Test 4: Unexpected error code.",
					FrameworkError.JMS_XML_FORMATTER_CONFIG_ERROR.getCode(),
					s.getErrorCode());
			}
			assertTrue("Test 5: Expected exception was not thrown.", ex);
			sh.remove("static2");
			formatter.setStaticHeaderFields(sh);
			ex = false;

			// test: one of the dynamic header fields are null
			Map dh = new HashMap();
			dh.put("dynamic1", "dynamic1");
			dh.put("dynamic2", null);
			formatter.setDynamicHeaderFields(dh);
			try {
				formatter.init();
			} catch (SystemException s) {
				ex = true;
				assertEquals(
					"Test 6: Unexpected error code.",
					FrameworkError.JMS_XML_FORMATTER_CONFIG_ERROR.getCode(),
					s.getErrorCode());
			}
			assertTrue("Test 7: Expected exception was not thrown.", ex);
			dh.remove("dynamic2");
			formatter.setDynamicHeaderFields(dh);
			ex = false;

			// test: one of the files in file mapping does not exist
			m.put("Test3", "testFile1000.xml");
			formatter.setXmlFileMappings(m);
			try {
				formatter.init();
			} catch (SystemException s) {
				ex = true;
				assertEquals(
					"Test 8: Unexpected error code.",
					FrameworkError.JMS_XML_FORMATTER_READ_ERROR.getCode(),
					s.getErrorCode());
			}
			assertTrue("Test 9: Expected exception was not thrown.", ex);
			ex = false;
			m.remove("Test3");
			formatter.setXmlFileMappings(m);

			// test: all clear
			formatter.init();

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexptected error:" + e.getMessage());
		}
	}

	private String readFile(String fileName) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer buf = new StringBuffer();
		String line = null;
		while ((line = reader.readLine()) != null) {
			buf.append(line);
		}
		reader.close();
		return buf.toString();
	}
}
