package no.stelvio.web.framework.servlet;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

/**
 * Inserts the specified key-value pairs into its servlet context so these key-value pairs can be used from for example a JSP 
 * or servlet.
 *
 * @author personf8e9850ed756
 * @version $Revision: 2650 $, $Date: 2005-11-27 08:01:09 +0100 (Sun, 27 Nov 2005) $
 */
public class ServletContextAttributeSetter implements ServletContextAware, InitializingBean {
	private static final Log log = LogFactory.getLog(ServletContextAttributeSetter.class);
	private ServletContext servletContext;
	private Properties attributes;

	/**
	 * Invoked by a BeanFactory after it has set all bean properties supplied (and satisfied BeanFactoryAware and
	 * ApplicationContextAware).
	 * <p>
	 * This method allows the bean instance to perform initialization only possible when all bean properties have been set and
	 * to throw an exception in the event of misconfiguration.
	 */
	public void afterPropertiesSet() {
		if (log.isDebugEnabled()) {
			log.debug("Setting attributes on servlet context: " + attributes);
		}

		for (Enumeration enumerator = attributes.propertyNames(); enumerator.hasMoreElements();) {
			String propertyName =  (String) enumerator.nextElement();

			servletContext.setAttribute(propertyName, attributes.getProperty(propertyName));
		}
	}

	/**
	 * Sets the attributes that should be inserted into the servlet context.
	 *
	 * @param attributes
	 */
	public void setAttributes(final Properties attributes) {
		this.attributes = new Properties();
		this.attributes.putAll(attributes);
	}

	/**
	 * Set the ServletContext that this object runs in.
	 * <p>
	 * Invoked after population of normal bean properties but before an init callback like InitializingBean's afterPropertiesSet
	 * or a custom init-method. Invoked after ApplicationContextAware's setApplicationContext.
	 *
	 * @param servletContext ServletContext object to be used by this object
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
