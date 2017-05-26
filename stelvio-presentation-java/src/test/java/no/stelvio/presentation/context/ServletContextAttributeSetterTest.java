package no.stelvio.presentation.context;

import java.util.Properties;

import junit.framework.TestCase;
import org.springframework.mock.web.MockServletContext;

/**
 * Unit test for {@link ServletContextAttributeSetter}.
 *
 * @author personf8e9850ed756
 * @version $Id$
 */
public class ServletContextAttributeSetterTest extends TestCase {
	private ServletContextAttributeSetter servletContextAttributeSetter;
	private MockServletContext servletContext;

	/**
	 * Test PropertiesAreSetInServletContext.
	 */
	public void testPropertiesAreSetInServletContext() {
		final Properties properties = new Properties();
		properties.put("key1", "value1");
		properties.put("key2", "value2");

		servletContextAttributeSetter.setAttributes(properties);
		servletContextAttributeSetter.afterPropertiesSet();

		assertEquals("Wrong value set;", "value1", servletContext.getAttribute("key1"));
		assertEquals("Wrong value set;", "value2", servletContext.getAttribute("key2"));
	}

	/**
	 * Set up.
	 */
	protected void setUp() {
		servletContext = new MockServletContext();

		servletContextAttributeSetter = new ServletContextAttributeSetter();
		servletContextAttributeSetter.setServletContext(servletContext);
	}
}
