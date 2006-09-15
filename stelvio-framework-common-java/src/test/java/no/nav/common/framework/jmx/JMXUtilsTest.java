package no.nav.common.framework.jmx;

import java.util.Arrays;
import java.util.List;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import junit.framework.TestCase;

import no.nav.common.framework.config.Config;
import no.nav.common.framework.jmx.JMXUtils;

/**
 * JMXUtils unit test case.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1008 $ $Author: psa2920 $ $Date: 2004-08-12 16:06:12 +0200 (Thu, 12 Aug 2004) $
 */
public class JMXUtilsTest extends TestCase {

	/**
	 * Constructor for JMXUtilsTest.
	 * @param arg0
	 */
	public JMXUtilsTest(String arg0) {
		super(arg0);
	}

	public void testGetMBeanServer() {
		MBeanServer server = JMXUtils.getMBeanServer(null);
		assertNotNull("JMXUtils.getMBeanServer(null) should not return null", server);
	}

	public void testManageMBeans() {

		try {
			ObjectName petter = new ObjectName("Config:name=Petter");
			ObjectName ted = new ObjectName("Config:name=Ted");

			MBeanServer server = JMXUtils.getMBeanServer(null);
			server.registerMBean(new Hello(), petter);
			server.registerMBean(new Hello(), ted);

			// Set managed attributes
			server.setAttribute(petter, new Attribute("Name", "person7553f5959484"));
			server.setAttribute(ted, new Attribute("Name", "Ted Andre Sanne"));

			// Get managed attributes
			super.assertEquals("Petters navn er riktig", "person7553f5959484", server.getAttribute(petter, "Name"));
			super.assertEquals("Teds navn er riktig", "Ted Andre Sanne", server.getAttribute(ted, "Name"));

			// Invoke operation
			server.invoke(petter, "print", null, null);
			server.invoke(ted, "print", null, null);

		} catch (MalformedObjectNameException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		} catch (InstanceAlreadyExistsException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		} catch (AttributeNotFoundException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		} catch (InvalidAttributeValueException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		} catch (MBeanException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		} catch (ReflectionException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		}

	}

	public void testGetMBeanNames() {

		String[] filenames = new String[] { "test-services.xml", "test-services2.xml" };

		try {
			MBeanServer server = JMXUtils.getMBeanServer(null);

			Config.getConfig(filenames[0]);
			Config.getConfig(filenames[1]);
			String classLoaderId =
				Thread.currentThread().getContextClassLoader().getClass().getName()
					+ "@"
					+ Integer.toHexString(Thread.currentThread().getContextClassLoader().hashCode());
			String[] names = JMXUtils.getMBeanNames(server, "Config:type=Spring,*");

			List l = Arrays.asList(names);
			if (null != names && names.length == 2) {
				for (int i = 0; i < names.length; i++) {
					assertTrue(
						"The name does not match",
						l.contains("Config:type=Spring,filename=" + filenames[i] + ",classloader=" + classLoaderId));
				}
			} else {
				super.fail("It should have bean 2 names");
			}
		} catch (MalformedObjectNameException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		}
	}

	public void testGetMBeans() {
		try {

			MBeanServer server = JMXUtils.getMBeanServer(null);

			ObjectName[] objectNames = JMXUtils.getMBeans(server, "Config:*");
			if (null != objectNames) {
				for (int i = 0; i < objectNames.length; i++) {
					System.out.println("==================================================");
					MBeanInfo info = server.getMBeanInfo(objectNames[i]);
					MBeanAttributeInfo[] attributes = info.getAttributes();
					if (null != attributes) {
						for (int j = 0; j < attributes.length; j++) {
							System.out.println(objectNames[i].getCanonicalName() + ": " + attributes[j].getName() + ":");
							System.out.println("--------------------------------------------------");
							System.out.println(server.getAttribute(objectNames[i], attributes[j].getName()));
						}
					}
				}
			}
		} catch (MalformedObjectNameException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		} catch (MBeanException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		} catch (ReflectionException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		} catch (AttributeNotFoundException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		} catch (IntrospectionException e) {
			super.fail(e.getMessage());
			e.printStackTrace();
		}
	}

	public interface HelloMBean {

		// name attribute exposed for management
		String getName();
		void setName(String name);

		// print operation exposed for management
		void print();
	}

	public class Hello implements HelloMBean {

		private String name = "";

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void print() {
			System.out.println("Hello " + name + "!!");
		}

	}

}
