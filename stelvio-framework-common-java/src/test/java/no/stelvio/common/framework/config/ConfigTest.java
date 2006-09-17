package no.stelvio.common.framework.config;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.Iterator;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.AttributeNotFoundException;
import junit.framework.TestCase;

import no.stelvio.common.framework.config.Config;
import no.stelvio.common.framework.config.ConfigurationException;
import no.stelvio.common.framework.ejb.RemoteServiceDescription;

/**
 * Unit test for Config.
 * 
 * @author person356941106810, Accenture
 * @version $Id: ConfigTest.java 2844 2006-04-25 10:40:25Z psa2920 $
 */
public class ConfigTest extends TestCase {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public void testGetConfig() throws MalformedObjectNameException {
		// assert that there is no server
		ArrayList serverList = MBeanServerFactory.findMBeanServer(null);
		assertEquals("Test 1: Incorrect number of MBeanServers", 0, serverList.size());
		boolean ex = false;
		// test: we get a configuration exception when the filename is invalid
		try {
			Config.getConfig("non-existent");
		} catch (ConfigurationException e) {
			ex = true;
		}
		assertTrue("Test 2: Exception not thrown as expected.", ex);
		ex = false;

		// test: a new MBeanServer is created when there is none
		serverList = MBeanServerFactory.findMBeanServer(null);
		MBeanServer server = (MBeanServer) serverList.get(0);
		// assertEquals("Test 3: Incorrect number of MBeanServers", 1, serverList.size());

		// test: a new MBeanServer is not created when there is one
		// test: the file can be read
		Config cfg1 = Config.getConfig("test-services.xml");

		serverList = MBeanServerFactory.findMBeanServer(null);

		// assertEquals("Test 4: Incorrect number of MBeanServers", 1, serverList.size());

		// test: the MBean is already registered
		//	- Same instance of instance variables
		Config cfg2 = Config.getConfig("test-services.xml");

		Set s = server.queryNames(new ObjectName("Config:type=Spring,*"), null);
		assertEquals("Test 5: Incorrect number of MBean", 1, s.size());

		assertNotSame("Test 5: Config instances are the same.", cfg1, cfg2);
		assertSame("Test 6: BeanFactory instances are not the same.", cfg1.getBeanFactory(), cfg2.getBeanFactory());
		assertSame("Test 7: XML String instances are not the same.", cfg1.getXML(), cfg2.getXML());
		assertSame("Test 8: XML String instances are not the same.", cfg1.getFileName(), cfg2.getFileName());

	}

	public void testGetBean() {
		// test: the bean configured in the file is actually retrieved
		Config cfg = Config.getConfig("test-services.xml");
		Object obj = cfg.getBean("TestBean");
		assertEquals(
				"Test 1: Unexpected bean returned.",
		        "no.stelvio.common.framework.ejb.RemoteServiceDescription",
		        obj.getClass().getName());

		// test: a singleton returns the same instance every time
		assertSame("Test 2: Singleton does not return same instance.", obj, cfg.getBean("TestBean"));
		assertNotSame("Test 3: Non-singleton returns same instance.", cfg.getBean("TestBean2"), cfg.getBean("TestBean2"));
	}

	public void testLoadFile() throws IOException {
		// test: the BeanFactory is updated with new config
		String newXML = readFile("test-services2.xml");
		Config cfg = Config.getConfig("test-services.xml");
		Object obj = cfg.getBean("TestBean");
		assertEquals(
				"Test 1: Unexpected bean returned.",
		        "no.stelvio.common.framework.ejb.RemoteServiceDescription",
		        obj.getClass().getName());

		System.setProperty(Config.CONFIG_CAN_BE_CHANGED_SYSTEM_PROPERTY, "true");
		cfg.update(newXML);
		assertEquals("Test 2: XML does not match.", newXML, cfg.getXML());

		assertFalse("Test 3: Bean is still in repository.", cfg.getBeanFactory().containsBean("TestBean"));
		assertTrue("Test 4: New bean is not in repository.", cfg.getBeanFactory().containsBean("TestBean3"));
	}

	public void testGetXML() throws IOException {
		// test: the xml matches that on file
		Config cfg = Config.getConfig("test-services.xml");
		String xml = cfg.getXML();
		String str = readFile("test-services.xml");
		assertEquals("Test 1: XML is not same as file.", str, xml);
	}

	public void testLoadXML() throws IOException, ReflectionException, InstanceNotFoundException, MBeanException,
	                                 AttributeNotFoundException, MalformedObjectNameException {
		// test: the BeanFactory is updated with new config
		String newXML = readFile("test-services2.xml");
		Config cfg = Config.getConfig("test-services.xml");
		Object obj = cfg.getBean("TestBean");
		assertEquals(
				"Test 1: Unexpected bean returned.",
		        "no.stelvio.common.framework.ejb.RemoteServiceDescription",
		        obj.getClass().getName());

		System.setProperty(Config.CONFIG_CAN_BE_CHANGED_SYSTEM_PROPERTY, "true");
		cfg.update(newXML);
		assertEquals("Test 2: XML does not match.", newXML, cfg.getXML());

		assertFalse("Test 3: Bean is still in repository.", cfg.getBeanFactory().containsBean("TestBean"));
		assertTrue("Test 4: New bean is not in repository.", cfg.getBeanFactory().containsBean("TestBean3"));

		// test: the MBean is updated with the new config
		ArrayList serverList = MBeanServerFactory.findMBeanServer(null);
		MBeanServer server = (MBeanServer) serverList.get(0);
		String classLoaderId =
				Thread.currentThread().getContextClassLoader().getClass().getName()
				+ "@"
				+ Integer.toHexString(Thread.currentThread().getContextClassLoader().hashCode());
		Object beanFac =
				server.getAttribute(
						new ObjectName("Config:type=Spring,filename=test-services.xml,classloader=" + classLoaderId),
				        "BeanFactory");
		assertSame("Test 5: BeanFactory instance was not the same.", cfg.getBeanFactory(), beanFac);

		// test: the XML is invalid
		try {
			cfg.update("HEUY");
			fail("Test 6: ConfigurationException was not thrown");
		} catch (ConfigurationException e) {
			// Should happen
		}

		// test: all other config listeners are updated with the new xml.
		Config cfg2 = Config.getConfig("test-services.xml");
		assertEquals("Test 7: incorrect bean factory object", cfg.getBeanFactory(), cfg2.getBeanFactory());
		newXML = readFile("test-services.xml");

		server.invoke(new ObjectName("Config:type=Spring,filename=test-services.xml,classloader=" + classLoaderId), "update",
		              new String[]{newXML}, new String[]{String.class.getName()});

		assertEquals("Test 8: incorrect bean factory object", cfg.getBeanFactory(), cfg2.getBeanFactory());
		assertEquals("Test 9: incorrect bean factory object", cfg.getXML(), cfg2.getXML());
	}

	public void testSave() throws IOException {
		// test: the XML is written to a file
		String file = readFile("test-services.xml");
		URL url = Thread.currentThread().getContextClassLoader().getResource("test-services.xml");
		int index = url.getPath().lastIndexOf('/');
		String fileName = url.getPath().substring(0, index);
		FileWriter writer = new FileWriter(fileName + "/test-services3.xml", false);
		writer.write(file);
		writer.close();

		Config cfg = Config.getConfig("test-services3.xml");
		assertEquals("Test 1: Initial files was not correct; please reset files", file, cfg.getXML());
		String file2 = readFile("test-services2.xml");
		cfg.setXML(file2);
		cfg.save();
		String file3 = readFile("test-services3.xml");
		assertEquals("Test 2: files did not have matching content.", file3, file2);
		assertFalse("Test 3: Start and end files were the same", file.equals(file3));
	}

	public void testBeanFactoryPostProcessorsAreCalled() {
		System.setProperty(Config.CONFIG_CAN_BE_CHANGED_SYSTEM_PROPERTY, "true");
		Config config = Config.getConfig("test-bean-factory-post-processor.xml");
		RemoteServiceDescription description = (RemoteServiceDescription) config.getBean("TestBean");

		assertEquals("Not the correct jndi name;", "ejb/business/bidrag/BusinessFacadeTestHome", description.getJndiName());
		assertEquals("Not the correct provider url;", "iiop://localhost:2909/", description.getProviderUrl());
		assertEquals("Not the correct initical context factory;", "com.ibm.websphere.naming.WsnInitialContextFactories", description.getInitialContextFactory());
		assertEquals("Not the correct url pgk prefixes;", "com.ibm.runtime:com.ibm.ws.naming:com.ibm.websphere.namings", description.getUrlPkgPrefixes());
	}

	public void testCanChangeConfigIfSystemPropertyIsSetToTrue() {
		System.setProperty(Config.CONFIG_CAN_BE_CHANGED_SYSTEM_PROPERTY, "true");
		assertTrue("Should be possible to change config", Config.canBeChanged());
		System.setProperty(Config.CONFIG_CAN_BE_CHANGED_SYSTEM_PROPERTY, "false");
		assertFalse("Should not be possible to change config", Config.canBeChanged());
		System.setProperty(Config.CONFIG_CAN_BE_CHANGED_SYSTEM_PROPERTY, " ");
		assertFalse("Should not be possible to change config", Config.canBeChanged());
	}

	private String readFile(String fileName) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer buf = new StringBuffer();
		String line;

		while (null != (line = reader.readLine())) {
			buf.append(line).append(LINE_SEPARATOR);
		}

		reader.close();
		return buf.toString();
	}

	/**
	 * Removes the registered MBeanServers.
	 */
	protected void tearDown() throws Exception {
		super.tearDown();

		List servers = MBeanServerFactory.findMBeanServer(null);

		for (Iterator iterator = servers.iterator(); iterator.hasNext();) {
			MBeanServer server = (MBeanServer) iterator.next();

			MBeanServerFactory.releaseMBeanServer(server);
		}
	}
}
