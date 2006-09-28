package no.stelvio.integration.jms.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;

import junit.framework.TestCase;

import no.stelvio.integration.jms.DummyTextMessage;
import no.stelvio.integration.jms.handler.XMLMessageHandler;
import no.stelvio.common.FrameworkError;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceResponse;


/**
 * 
 * @author person356941106810, Accenture
 */
public class XMLMessageHandlerTest extends TestCase {

	public void testHandleMessage() {
		try {
			XMLMessageHandler handler = new XMLMessageHandler();
			handler.setDatePattern("yyyy-MM-dd");
			Map staticFields = new HashMap();
			Map headerFields = new HashMap();
			Map classNames = new HashMap();
			Map responseFields = new HashMap();

			staticFields.put("testFlatField1", "Value 1");
			classNames.put("testFlatField1", "String");

			staticFields.put("testNestedField2.stringValue", "Value 2");
			classNames.put("testNestedField2", "no.stelvio.integration.jms.handler.TestBean");

			headerFields.put("headerField1", "testHeaderField1");
			classNames.put("testHeaderField1", "String");
			headerFields.put("headerField2", "testHeaderField2.stringValue");
			classNames.put("testHeaderField2", "no.stelvio.integration.jms.handler.TestBean");

			responseFields.put("/root/characterValue", "field1.nestedBean.characterValue");
			responseFields.put("/root/cValue", "field1.nestedBean.cValue");
			responseFields.put("/root/doubleValue", "field2.nestedBean.doubleValue");
			responseFields.put("/root/dValue", "field2.nestedBean.dValue");
			responseFields.put("/root/shortValue", "field2.nestedBean.nestedBean.shortValue");
			responseFields.put("/root/sValue", "field2.nestedBean.nestedBean.sValue");
			responseFields.put("/root/integerValue", "field3.integerValue");
			responseFields.put("/root/iValue", "field3.iValue");
			responseFields.put("/root/longValue", "field3.longValue");
			responseFields.put("/root/lValue", "field3.lValue");
			responseFields.put("/root/booleanValue", "field3.booleanValue");
			responseFields.put("/root/bValue", "field3.bValue");
			responseFields.put("/root/floatValue", "field3.floatValue");
			responseFields.put("/root/fValue", "field3.fValue");
			responseFields.put("/root/byteValue", "field3.byteValue");
			responseFields.put("/root/byValue", "field3.byValue");
			responseFields.put("/root/attributes/@att1", "field4.stringValue");
			responseFields.put("/root/attributes/@att2", "field4.nestedBean.stringValue");
			classNames.put("field1", "no.stelvio.integration.jms.handler.TestBean");
			classNames.put("field2", "no.stelvio.integration.jms.handler.TestBean");
			classNames.put("field3", "no.stelvio.integration.jms.handler.TestBean");
			classNames.put("field4", "no.stelvio.integration.jms.handler.TestBean");

			DummyTextMessage msg = new DummyTextMessage();
			String xml = readFile("testFile3.xml");
			msg.setText(xml);
			msg.setStringProperty("headerField1", "header1");
			msg.setStringProperty("headerField2", "header2");

			handler.setHeaderFields(headerFields);
			handler.setResponseClassNames(classNames);
			handler.setStaticResponseFields(staticFields);
			handler.setResponseFields(responseFields);
			handler.init();
			ServiceResponse response = handler.handleMessage(msg);

			// TEST: Static values
			assertEquals("Test 1:", "Value 1", response.getData("testFlatField1"));
			TestBean bean = (TestBean) response.getData("testNestedField2");
			assertEquals("Test 2:", "Value 2", bean.getStringValue());

			// TEST: header values
			assertEquals("Test 3:", "header1", response.getData("testHeaderField1"));
			bean = (TestBean) response.getData("testHeaderField2");
			assertEquals("Test 4:", "header2", bean.getStringValue());

			// TEST: structure building and conversion routines
			bean = (TestBean) response.getData("field1");
			bean = bean.getNestedBean();
			assertEquals("Test 5:", new Character('C'), bean.getCharacterValue());
			assertEquals("Test 6:", 'c', bean.getCValue());

			bean = (TestBean) response.getData("field2");
			bean = bean.getNestedBean();
			assertEquals("Test 7:", new Double(1.0), bean.getDoubleValue());
			assertEquals("Test 8:", 2.0d, bean.getDValue(), 0.0d);
			bean = bean.getNestedBean();
			assertEquals("Test 9: ", new Short((short) 3), bean.getShortValue());
			assertEquals("Test 10: ", (short) 4, bean.getSValue());

			bean = (TestBean) response.getData("field3");
			assertEquals("Test 11:", new Integer(6), bean.getIntegerValue());
			assertEquals("Test 12:", 7, bean.getIValue());
			assertEquals("Test 13:", new Long(8), bean.getLongValue());
			assertEquals("Test 14:", 9l, bean.getLValue());
			assertEquals("Test 15:", Boolean.TRUE, bean.getBooleanValue());
			assertFalse("Test 16:", bean.isBValue());
			assertEquals("Test 17:", new Float(0.1), bean.getFloatValue());
			assertEquals("Test 18:", 0.2f, bean.getFValue(), 0.0f);
			assertEquals("Test 19:", new Byte((byte) 56), bean.getByteValue());
			assertEquals("Test 20:", (byte) 57, bean.getByValue());

			bean = (TestBean) response.getData("field4");
			assertEquals("Test 21:", "Attrib 1", bean.getStringValue());
			bean = bean.getNestedBean();
			assertEquals("Test 22:", "Attrib 2", bean.getStringValue());

			// TEST repeating elements with attribute id
			responseFields = new HashMap();
			responseFields.put("/root/node[id]/characterValue", "field1[id].characterValue");
			responseFields.put("/root/node[id]/stringValue", "field1[id].stringValue");
			responseFields.put("/root/node[id]/attributes/@att1", "field1[id].dValue");
			responseFields.put("/root/node[id]/attributes/@att2", "field1[id].doubleValue");
			handler.setResponseFields(responseFields);
			classNames = new HashMap();
			classNames.put("field1[id]", "no.stelvio.integration.jms.handler.TestBean");
			handler.setResponseClassNames(classNames);
			handler.setHeaderFields(null);
			handler.setStaticResponseFields(null);
			handler.init();

			msg = new DummyTextMessage();
			xml = readFile("testFile4.xml");
			msg.setText(xml);

			response = handler.handleMessage(msg);
			Map m = (Map) response.getData("field1");
			assertNotNull("Test 23: Expected Map not found", m);
			assertNotNull("Test 24: expected object not found", m.get("1001"));
			assertNotNull("Test 25: expected object not found", m.get("1003"));
			assertNotNull("Test 25: expected object not found", m.get("1004"));

			// TEST repeating elements with counter id
			responseFields = new HashMap();
			responseFields.put("/root/node[c]/characterValue", "field1[c].characterValue");
			responseFields.put("/root/node[c]/stringValue", "field1[c].stringValue");
			responseFields.put("/root/node[c]/attributes/@att1", "field1[c].dValue");
			responseFields.put("/root/node[c]/attributes/@att2", "field1[c].doubleValue");
			classNames = new HashMap();
			classNames.put("field1[c]", "no.stelvio.integration.jms.handler.TestBean");
			handler.setResponseClassNames(classNames);
			handler.setResponseFields(responseFields);
			handler.init();

			msg = new DummyTextMessage();
			xml = readFile("testFile4.xml");
			msg.setText(xml);

			response = handler.handleMessage(msg);
			m = (Map) response.getData("field1");
			assertNotNull("Test 23: Expected Map not found", m);
			assertNotNull("Test 24: expected object not found", m.get("0"));
			assertNotNull("Test 25: expected object not found", m.get("1"));
			assertNotNull("Test 25: expected object not found", m.get("2"));

			// TEST repeating with more than one level
			responseFields = new HashMap();
			responseFields.put("/root/deepNode[id]/deepNode[i]/stringValue", "field1[id].nestedArrayBean[i].stringValue");
			responseFields.put("/root/deepNode[id]/deepNode[i]/characterValue", "field1[id].nestedArrayBean[i].characterValue");
			classNames = new HashMap();
			classNames.put("field1[id]", "no.stelvio.integration.jms.handler.TestBean");
			handler.setResponseClassNames(classNames);
			handler.setResponseFields(responseFields);
			handler.init();

			msg = new DummyTextMessage();
			xml = readFile("testFile4.xml");
			msg.setText(xml);

			response = handler.handleMessage(msg);
			m = (Map) response.getData("field1");
			assertNotNull("Test 26: Expected Map not found", m);
			assertNotNull("Test 27: expected object not found", m.get("x"));
			TestBean tbx = (TestBean) m.get("x");
			assertNotNull("Test 28: expected object not found", tbx.getNestedArrayBean());
			assertEquals("Test 29: expected length not met", 2, tbx.getNestedArrayBean().length);
			assertEquals("Test 29.a ", "String X.Y", tbx.getNestedArrayBean()[0].getStringValue());
			assertEquals("Test 29.b ", new Character('C'), tbx.getNestedArrayBean()[0].getCharacterValue());
			assertEquals("Test 29.c ", "String X.Z", tbx.getNestedArrayBean()[1].getStringValue());
			assertEquals("Test 29.d ", new Character('D'), tbx.getNestedArrayBean()[1].getCharacterValue());

			// TEST List implemenations with defined list types
			responseFields = new HashMap();
			responseFields.put("/root/deepNode[id]/deepNode[i]/stringValue", "field1[id].typedListBean[i].stringValue");
			responseFields.put("/root/deepNode[id]/deepNode[i]/characterValue", "field1[id].typedListBean[i].characterValue");
			classNames.put("typedListBean[i]", "no.stelvio.integration.jms.handler.TestBean");
			handler.setResponseClassNames(classNames);
			handler.setResponseFields(responseFields);
			handler.init();

			msg = new DummyTextMessage();
			xml = readFile("testFile4.xml");
			msg.setText(xml);

			response = handler.handleMessage(msg);
			m = (Map) response.getData("field1");
			assertNotNull("Test 30: Expected Map not found", m);
			assertNotNull("Test 31: expected object not found", m.get("x"));
			tbx = (TestBean) m.get("x");
			assertNotNull("Test 32: expected object not found", tbx.getTypedListBean());
			assertEquals("Test 33: expected length not met", 2, tbx.getTypedListBean().size());
			assertEquals("Test 33.a ", "String X.Y", ((TestBean) tbx.getTypedListBean().get(0)).getStringValue());
			assertEquals("Test 33.b ", new Character('C'), ((TestBean) tbx.getTypedListBean().get(0)).getCharacterValue());
			assertEquals("Test 33.c ", "String X.Z", ((TestBean) tbx.getTypedListBean().get(1)).getStringValue());
			assertEquals("Test 33.d ", new Character('D'), ((TestBean) tbx.getTypedListBean().get(1)).getCharacterValue());

			// TEST List implemenations with interface list types
			responseFields = new HashMap();
			responseFields.put("/root/deepNode[id]/deepNode[i]/stringValue", "field1[id].interfaceListBean[i].stringValue");
			responseFields.put(
				"/root/deepNode[id]/deepNode[i]/characterValue",
				"field1[id].interfaceListBean[i].characterValue");
			classNames.put("interfaceListBean[i]", "no.stelvio.integration.jms.handler.TestBean");
			handler.setResponseClassNames(classNames);
			handler.setResponseFields(responseFields);
			handler.init();

			msg = new DummyTextMessage();
			xml = readFile("testFile4.xml");
			msg.setText(xml);

			response = handler.handleMessage(msg);
			m = (Map) response.getData("field1");
			assertNotNull("Test 34: Expected Map not found", m);
			assertNotNull("Test 35: expected object not found", m.get("x"));
			tbx = (TestBean) m.get("x");
			assertNotNull("Test 36: expected object not found", tbx.getInterfaceListBean());
			assertEquals("Test 37: expected length not met", 2, tbx.getInterfaceListBean().size());
			assertEquals("Test 37.a ", "String X.Y", ((TestBean) tbx.getInterfaceListBean().get(0)).getStringValue());
			assertEquals("Test 37.b ", new Character('C'), ((TestBean) tbx.getInterfaceListBean().get(0)).getCharacterValue());
			assertEquals("Test 37.c ", "String X.Z", ((TestBean) tbx.getInterfaceListBean().get(1)).getStringValue());
			assertEquals("Test 37.d ", new Character('D'), ((TestBean) tbx.getInterfaceListBean().get(1)).getCharacterValue());

			// TEST Map implemenations with defined list types
			responseFields = new HashMap();
			responseFields.put("/root/deepNode[id]/deepNode[i]/stringValue", "field1[id].typedMapBean[i].stringValue");
			responseFields.put("/root/deepNode[id]/deepNode[i]/characterValue", "field1[id].typedMapBean[i].characterValue");
			classNames.put("typedMapBean[i]", "no.stelvio.integration.jms.handler.TestBean");
			handler.setResponseClassNames(classNames);
			handler.setResponseFields(responseFields);
			handler.init();

			msg = new DummyTextMessage();
			xml = readFile("testFile4.xml");
			msg.setText(xml);

			response = handler.handleMessage(msg);
			m = (Map) response.getData("field1");
			assertNotNull("Test 38: Expected Map not found", m);
			assertNotNull("Test 39: expected object not found", m.get("x"));
			tbx = (TestBean) m.get("x");
			assertNotNull("Test 40: expected object not found", tbx.getTypedMapBean());
			assertEquals("Test 41: expected length not met", 2, tbx.getTypedMapBean().size());
			assertEquals("Test 41.a ", "String X.Y", ((TestBean) tbx.getTypedMapBean().get("0")).getStringValue());
			assertEquals("Test 41.b ", new Character('C'), ((TestBean) tbx.getTypedMapBean().get("0")).getCharacterValue());
			assertEquals("Test 41.c ", "String X.Z", ((TestBean) tbx.getTypedMapBean().get("1")).getStringValue());
			assertEquals("Test 41.d ", new Character('D'), ((TestBean) tbx.getTypedMapBean().get("1")).getCharacterValue());

			// TEST Map implemenations with interface list types
			responseFields = new HashMap();
			responseFields.put("/root/deepNode[id]/deepNode[i]/stringValue", "field1[id].interfaceMapBean[i].stringValue");
			responseFields.put(
				"/root/deepNode[id]/deepNode[i]/characterValue",
				"field1[id].interfaceMapBean[i].characterValue");
			classNames.put("interfaceMapBean[i]", "no.stelvio.integration.jms.handler.TestBean");
			handler.setResponseClassNames(classNames);
			handler.setResponseFields(responseFields);
			handler.init();

			msg = new DummyTextMessage();
			xml = readFile("testFile4.xml");
			msg.setText(xml);

			response = handler.handleMessage(msg);
			m = (Map) response.getData("field1");
			assertNotNull("Test 42: Expected Map not found", m);
			assertNotNull("Test 43: expected object not found", m.get("x"));
			tbx = (TestBean) m.get("x");
			assertNotNull("Test 44: expected object not found", tbx.getInterfaceMapBean());
			assertEquals("Test 45: expected length not met", 2, tbx.getInterfaceMapBean().size());
			assertEquals("Test 45.a ", "String X.Y", ((TestBean) tbx.getInterfaceMapBean().get("0")).getStringValue());
			assertEquals("Test 45.b ", new Character('C'), ((TestBean) tbx.getInterfaceMapBean().get("0")).getCharacterValue());
			assertEquals("Test 45.c ", "String X.Z", ((TestBean) tbx.getInterfaceMapBean().get("1")).getStringValue());
			assertEquals("Test 45.d ", new Character('D'), ((TestBean) tbx.getInterfaceMapBean().get("1")).getCharacterValue());
			// Maybe later, we add test for leaves that are iterators
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexptected exception thrown:" + e.getMessage());
		}
	}

	public void testInit() {
		try {

			// TEST: one of the static response fields are null
			Map statics = new HashMap();
			statics.put("testField1", "TEST Value1");
			statics.put("testField2", null);
			XMLMessageHandler handler = new XMLMessageHandler();
			handler.setDatePattern("yyyy-MM-dd");
			handler.setStaticResponseFields(statics);

			boolean ex = false;
			try {
				handler.init();
			} catch (SystemException e) {
				ex = true;
				assertEquals(
					"Test 1: Incorrect error code",
					FrameworkError.JMS_HANDLER_CONFIG_ERROR.getCode(),
					e.getErrorCode());
			}
			assertTrue("Test 2:", ex);
			statics.remove("testField2");
			ex = false;

			// TEST: one of the header fields are null
			Map header = new HashMap();
			header.put("testHeaderField1", "HEADER 1");
			header.put("testHeaderField2", null);
			handler.setHeaderFields(header);
			try {
				handler.init();
			} catch (SystemException e) {
				ex = true;
				assertEquals(
					"Test 3: Incorrect error code",
					FrameworkError.JMS_HANDLER_CONFIG_ERROR.getCode(),
					e.getErrorCode());
			}
			assertTrue("Test 4:", ex);
			header.remove("testHeaderField2");
			ex = false;

			// TEST: response fields is missing
			try {
				handler.init();
			} catch (SystemException e) {
				ex = true;
				assertEquals(
					"Test 4: Incorrect error code",
					FrameworkError.JMS_HANDLER_CONFIG_ERROR.getCode(),
					e.getErrorCode());
			}
			assertTrue("Test 5:", ex);
			ex = false;

			// TEST: the response class names is empty
			Map responseFields = new HashMap();
			responseFields.put("/data/test1", "testRField1");
			handler.setResponseFields(responseFields);
			try {
				handler.init();
			} catch (SystemException e) {
				ex = true;
				assertEquals(
					"Test 6: Incorrect error code",
					FrameworkError.JMS_HANDLER_CONFIG_ERROR.getCode(),
					e.getErrorCode());
			}
			assertTrue("Test 7:", ex);
			ex = false;

			// TEST: one of the response fields are null
			Map classNames = new HashMap();
			classNames.put("testField1", "String");
			classNames.put("testHeaderField1", "String");
			classNames.put("testRField1", "String");
			handler.setResponseClassNames(classNames);
			responseFields.put("/data/test2", null);
			try {
				handler.init();
			} catch (SystemException e) {
				ex = true;
				assertEquals(
					"Test 8: Incorrect error code",
					FrameworkError.JMS_HANDLER_CONFIG_ERROR.getCode(),
					e.getErrorCode());
			}
			assertTrue("Test 9:", ex);
			ex = false;

			// TEST: one of the class names are null
			responseFields.put("/data/test2", "X");
			try {
				handler.init();
			} catch (SystemException e) {
				ex = true;
				assertEquals(
					"Test 10: Incorrect error code",
					FrameworkError.JMS_HANDLER_CONFIG_ERROR.getCode(),
					e.getErrorCode());
			}
			assertTrue("Test 11:", ex);
			ex = false;

			// TEST: one of the classes does not exist
			classNames.put("X", "test.Test");
			try {
				handler.init();
			} catch (SystemException e) {
				ex = true;
				assertEquals(
					"Test 12: Incorrect error code",
					FrameworkError.JMS_HANDLER_CONFIG_ERROR.getCode(),
					e.getErrorCode());
			}
			assertTrue("Test 13:", ex);
			ex = false;

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexptected exception:" + e.getMessage());
		}
	}

	public void testErrorHandling() {
		try {
			boolean ex = false;
			XMLMessageHandler handler = new XMLMessageHandler();
			handler.setDatePattern("yyyy-MM-dd");
			Map responseFields = new HashMap();
			responseFields.put("/root/stringValue", "field1");
			Map classNames = new HashMap();
			classNames.put("field1", "String");
			classNames.put("errorMsg", "String");
			classNames.put("statusMsg", "String");
			handler.setResponseFields(responseFields);
			handler.setResponseClassNames(classNames);

			// TEST: status message is set and neither exception or response field is set
			handler.setStatusMessageField("/root/longValue");
			try {
				handler.init();
			} catch (SystemException e) {
				ex = true;
				assertEquals(
					"Test 12: Incorrect error code",
					FrameworkError.JMS_HANDLER_CONFIG_ERROR.getCode(),
					e.getErrorCode());
			}
			assertTrue("Test 13:", ex);
			ex = false;

			// TEST: exception on error status is set but not the list
			handler.setExceptionOnErrorStatus(true);
			try {
				handler.init();
			} catch (SystemException e) {
				ex = true;
				assertEquals(
					"Test 14: Incorrect error code",
					FrameworkError.JMS_HANDLER_CONFIG_ERROR.getCode(),
					e.getErrorCode());
			}
			assertTrue("Test 15:", ex);
			ex = false;
			List l = new ArrayList();
			l.add("8");
			handler.setErrorStatus(l);

			// TEST: bot exception flags are set
			handler.setExceptionOnError(true);
			try {
				handler.init();
			} catch (SystemException e) {
				ex = true;
				assertEquals(
					"Test 16: Incorrect error code",
					FrameworkError.JMS_HANDLER_CONFIG_ERROR.getCode(),
					e.getErrorCode());
			}
			assertTrue("Test 17:", ex);
			ex = false;

			// TEST: error is set but not any of it's associated fields
			handler.setExceptionOnError(false);
			handler.setExceptionOnErrorStatus(false);
			handler.setErrorMessageField("/data/iValue");
			handler.setStatusResponseField("CX");
			try {
				handler.init();
			} catch (SystemException e) {
				ex = true;
				assertEquals(
					"Test 16: Incorrect error code",
					FrameworkError.JMS_HANDLER_CONFIG_ERROR.getCode(),
					e.getErrorCode());
			}
			assertTrue("Test 17:", ex);
			ex = false;

			// TEST: actual operation
			handler.setStatusMessageField("/root/longValue");
			handler.setStatusResponseField(null);
			handler.setExceptionOnErrorStatus(true);
			handler.setExceptionOnError(false);
			handler.setErrorMessageField("/root/stringValue");
			handler.init();
			String str = readFile("testFile3.xml");
			DummyTextMessage msg = new DummyTextMessage();
			msg.setText(str);
			try {
				handler.handleMessage(msg);
			} catch (ServiceFailedException e) {
				ex = true;
			}
			assertTrue("Test 18", ex);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected excetpion thrown");
		}

	}
	public void testInputBufferSize_2048() throws IOException, JMSException, ServiceFailedException {
		XMLMessageHandler handler = new XMLMessageHandler();
		handler.setDatePattern("yyyy-MM-dd");
		Map classNames = new HashMap();
		Map responseFields = new HashMap();
		
		responseFields.put("/root/value", "valueStringValue");
		classNames.put("valueStringValue", "String");		
		responseFields.put("/root/balue", "balueStringValue");
		classNames.put("balueStringValue", "String");
				
		handler.setResponseClassNames(classNames);	
		handler.setResponseFields(responseFields);
		handler.init();
		
		DummyTextMessage msg = new DummyTextMessage();
		String xml = readFile("testFile6.xml");
		msg.setText(xml);

		ServiceResponse response = handler.handleMessage(msg);
		
		
		// Default input-buffer-size for the SAX parser seems to be 2048 B.
		// Need to check data (document) greater than 2048 B
		String s = (String) response.getData("valueStringValue");
		assertTrue(s.length() > 2048);
		assertTrue(s.length() == 2870);
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
