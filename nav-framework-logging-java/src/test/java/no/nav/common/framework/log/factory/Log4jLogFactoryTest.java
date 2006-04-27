package no.nav.common.framework.log.factory;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import junit.framework.TestCase;

import no.nav.common.framework.log.factory.Log4jLogFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Log4JLogger;

/**
 * Log4jFactory unit test.
 * 
 * @author person356941106810, Accenture
 * @version $Id: Log4jLogFactoryTest.java 2195 2005-04-06 09:43:32Z psa2920 $
 */
public class Log4jLogFactoryTest extends TestCase {

	/**
	 * Constructor for Log4jLogFactoryTest.
	 * @param arg0
	 */
	public Log4jLogFactoryTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		//System.setProperty("org.apache.commons.logging.LogFactory", "no.nav.common.framework.log.factory.Log4jLogFactory");
		System.setProperty("log4j.configuration", "test-log4j.properties");
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test for void release()
	 */
	public void testRelease() {
		System.out.println("############## testRelease ################");
		// test: check that two logger instances are not equal after a release
		LogFactory fac = LogFactory.getFactory();
		Log log1 = fac.getInstance(this.getClass());
		fac.release();
		Log log2 = fac.getInstance(this.getClass());
		assertNotSame("Test 1: The logger instance was unexpectedly the same.", log1, log2);
	}

	/*
	 * Test for Object getAttribute(String)
	 */
	public void testGetAttributeString() {
		System.out.println("############## testGetAttributeString ################");
		// test: check that we get the refresh interval. This will not be set since we are still using defaults
		LogFactory fac = LogFactory.getFactory();
		Object value = fac.getAttribute(Log4jLogFactory.DEFAULT_INIT_OVERRIDE_KEY);
		assertEquals("Test 1: Attribute value was unexpected", null, value);

		// test: check that the refresh interval is set correctly
		String oldAtt = (String) fac.getAttribute(Log4jLogFactory.REFRESH_INTERVAL);
		fac.setAttribute(Log4jLogFactory.REFRESH_INTERVAL, "10000");
		value = fac.getAttribute(Log4jLogFactory.REFRESH_INTERVAL);
		assertEquals("Test 2: Unexpected refresh interval", "10000", value);
		fac.setAttribute(Log4jLogFactory.REFRESH_INTERVAL, oldAtt);
	}

	/*
	 * Test for String[] getAttributeNames()
	 */
	public void testGetAttributeNames() {
		System.out.println("############## testGetAttributeNames ################");
		// test: the names list should only contain one item
		LogFactory fac = LogFactory.getFactory();
		String[] names = fac.getAttributeNames();
		assertEquals("Test 1: Unexpected number of attribute names", 2, names.length);
		assertEquals("Test 2: Unexptected attrbite name", Log4jLogFactory.REFRESH_INTERVAL, names[1]);
	}

	/*
	 * Test for Log getInstance(Class)
	 */
	public void testGetInstanceClass() {
		System.out.println("############## testGetInstanceClass ################");
		// test: test that we a Log instance and that it is the same instance each time
		LogFactory fac = LogFactory.getFactory();
		Log log1 = fac.getInstance(this.getClass());
		assertEquals("Test 1: Unexpected Logger type", Log4JLogger.class.getName(), log1.getClass().getName());
		Log log2 = fac.getInstance(this.getClass());
		assertSame("Test 2: Unexpected Log instance", log1, log2);

	}

	/*
	 * Test for Log getInstance(String)
	 */
	public void testGetInstanceString() {
		System.out.println("############## testGetInstanceString ################");
		// test: test that we a Log instance and that it is the same instance each time
		LogFactory fac = LogFactory.getFactory();
		Log log1 = fac.getInstance(this.getClass().getName());
		assertEquals("Test 1: Unexpected Logger type", Log4JLogger.class.getName(), log1.getClass().getName());
		Log log2 = fac.getInstance(this.getClass().getName());
		assertSame("Test 2: Unexpected Log instance", log1, log2);
	}

	/*
	 * Test for Log getInstance(String) and getInstance(Class)
	 */
	public void testGetInstance() {
		System.out.println("############## testGetInstance ################");
		// test: test that we a Log instance and that it is the same instance each time
		// test: we get the same instance even if we use two different getInstance methods
		LogFactory fac = LogFactory.getFactory();
		Log log1 = fac.getInstance(this.getClass());
		assertEquals("Test 1: Unexpected Logger type", Log4JLogger.class.getName(), log1.getClass().getName());
		Log log2 = fac.getInstance(this.getClass().getName());
		assertSame("Test 2: Unexpected Log instance", log1, log2);
	}
	/*
	 * Test for void removeAttribute(String)
	 */
	public void testRemoveAttributeString() {
		System.out.println("############## testRemoveAttributeString ################");
		// test: that an attribute is removed after a call to renoteAttribute
		LogFactory fac = LogFactory.getFactory();
		fac.setAttribute("TEST", "TESTVALUE");
		Object val = fac.getAttribute("TEST");
		assertEquals("Test 1: Unexpected attribute value", "TESTVALUE", val);
		fac.removeAttribute("TEST");
		val = fac.getAttribute("TEST");
		assertNull("Test 2: Unexpected attribute value", val);
	}

	/*
	 * Test for void setAttribute(String, Object)
	 */
	public void testSetAttributeStringObject() {
		System.out.println("############## testSetAttributeStringObject ################");
		// test: that an attribute is set
		LogFactory fac = LogFactory.getFactory();
		Object value = fac.getAttribute("TEST");
		assertNull("Test 1: Unexpected attribute value", value);
		fac.setAttribute("TEST", "TESTVALUE");
		value = fac.getAttribute("TEST");
		assertEquals("Test 2: Unexpected attribute value", "TESTVALUE", value);
		fac.removeAttribute("TEST");
	}

	public void testRefresh() {
		System.out.println("############## testRefresh ################");
		Properties prop = new Properties();
		URL url = Thread.currentThread().getContextClassLoader().getResource(System.getProperty("log4j.configuration"));
		try {
			// reset the file
			FileOutputStream out = new FileOutputStream(url.getFile(), false);
			prop.setProperty("log4j.logger.no.trygdeetaten", "ERROR, Console, TEST");
			prop.store(out, null);
			out.close();
			LogFactory fac = LogFactory.getFactory();
			
			// set to 5 second wait
			fac.setAttribute(Log4jLogFactory.REFRESH_INTERVAL, "5000");
			Thread.sleep(6000);
			Log log1 = fac.getInstance(this.getClass());
			assertFalse("Test 1: Debug is enabled, please reset prop. file", log1.isDebugEnabled());

			InputStream in = url.openStream();
			prop.load(in);
			in.close();

			// assert that we are in a valid state
			assertEquals(
				"Test 2: Unexpected LOGGER configuration",
				"ERROR, Console, TEST",
				prop.getProperty("log4j.logger.no.trygdeetaten"));
			// set the level to DEBUG
			prop.setProperty("log4j.logger.no.trygdeetaten", "DEBUG, Console, TEST");

			out = new FileOutputStream(url.getFile(), false);
			prop.store(out, null);
			out.close();
			// sleep for 6 seconds to make sure the changes take effect
			Thread.sleep(6000);
			assertTrue("Test 3: Debug logging is not enabled", log1.isDebugEnabled());

		} catch (Exception e) {
			e.printStackTrace(System.err);
			fail("Unexpected exception:" + e.getMessage());
		} 
	}
}
