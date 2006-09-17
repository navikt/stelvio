package no.stelvio.web.framework.servlet;

import java.util.Properties;

import no.stelvio.web.framework.servlet.ServletContextAttributeSetter;

import junit.framework.TestCase;
import servletunit.ServletContextSimulator;

/**
 * Unit test for {@link ServletContextAttributeSetter}.
 *
 * @author personf8e9850ed756
 * @version $Revision: 2598 $, $Date: 2005-10-31 13:31:23 +0100 (Mon, 31 Oct 2005) $
 */
public class ServletContextAttributeSetterTest extends TestCase {
	private ServletContextAttributeSetter servletContextAttributeSetter;
	private ServletContextSimulator servletContext;

	public void testPropertiesAreSetInServletContext() {
		final Properties properties = new Properties();
		properties.put("key1", "value1");
		properties.put("key2", "value2");

		servletContextAttributeSetter.setAttributes(properties);
		servletContextAttributeSetter.afterPropertiesSet();

		assertEquals("Wrong value set;", "value1", servletContext.getAttribute("key1"));
		assertEquals("Wrong value set;", "value2", servletContext.getAttribute("key2"));
	}

	protected void setUp() {
		servletContext = new ServletContextSimulator();

		servletContextAttributeSetter = new ServletContextAttributeSetter();
		servletContextAttributeSetter.setServletContext(servletContext);
	}
}
