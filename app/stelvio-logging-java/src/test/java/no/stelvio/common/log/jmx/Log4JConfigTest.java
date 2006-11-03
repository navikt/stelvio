package no.stelvio.common.log.jmx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import no.stelvio.common.log.jmx.Log4JConfig;

import junit.framework.TestCase;

/**
 * Log4jConfig unit test.
 * 
 * @author person356941106810, Accenture
 * @version $Id: Log4JConfigTest.java 2866 2006-04-25 11:20:22Z psa2920 $
 */
public class Log4JConfigTest extends TestCase {

	/**
	 * Constructor for Log4JConfigTest.
	 * 
	 * @param arg0
	 */
	public Log4JConfigTest(String arg0) {
		super(arg0);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testLog4JConfig() {
		try {
			new Log4JConfig("test-log4j.properties");
			ArrayList servers = MBeanServerFactory.findMBeanServer(null);
			MBeanServer server = (MBeanServer) servers.get(0);
			ObjectName name = new ObjectName(
					"Config:type=Log4j,filename=test-log4j.properties");
			assertTrue("Test 1: Log4j is not registered", server
					.isRegistered(name));
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			fail("Unexpected error:" + t.getMessage());
		}
	}

	public void testSetLogConfig() {
		try {

		} catch (Throwable t) {
			t.printStackTrace(System.err);
			fail("Unexpected error:" + t.getMessage());
		}
	}

	public void testLoad() {
		try {
			URL fileUrl = Thread.currentThread().getContextClassLoader()
					.getResource("test-log4j.properties");
			Log4JConfig l4j = new Log4JConfig(fileUrl.getFile());
			l4j.load();
			assertEquals("Test 1: files not the same",
					readFile("test-log4j.properties"), l4j.getLogConfig());
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			fail("Unexpected error:" + t.getMessage());
		}
	}

	public void testSave() {
		try {
			URL fileUrl = Thread.currentThread().getContextClassLoader()
					.getResource("test-log4j.properties");
			int index = fileUrl.getFile().indexOf("test-log4j.properties");
			String newFileName = fileUrl.getFile().substring(0, index)
					+ "dummyLog4j.properties";
			String fileContent = readFile("test-log4j.properties");
			Log4JConfig config = new Log4JConfig(newFileName);
			config.setLogConfig(fileContent);
			config.save();
			fileContent = readFile("dummyLog4j.properties");
			assertEquals("Test 1: file contents does not match", fileContent,
					config.getLogConfig());
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			fail("Unexpected error:" + t.getMessage());
		}
	}

	private String readFile(String fileName) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer buf = new StringBuffer();
		String line = null;
		while ((line = reader.readLine()) != null) {
			buf.append(line).append("\n");
		}
		reader.close();
		return buf.toString();
	}
}
