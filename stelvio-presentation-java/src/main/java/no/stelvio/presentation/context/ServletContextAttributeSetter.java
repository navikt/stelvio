package no.stelvio.presentation.context;

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
 * 
 * @version $Id$
 */
public class ServletContextAttributeSetter implements ServletContextAware, InitializingBean {
	private static final Log LOG = LogFactory.getLog(ServletContextAttributeSetter.class);
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
		if (LOG.isDebugEnabled()) {
			LOG.debug("Setting attributes on servlet context: " + attributes);
		}

		for (Enumeration<?> enumerator = attributes.propertyNames(); enumerator.hasMoreElements();) {
			String propertyName =  (String) enumerator.nextElement();

			servletContext.setAttribute(propertyName, attributes.getProperty(propertyName));
		}
	}

	/**
	 * Sets the attributes that should be inserted into the servlet context.
	 *
	 * @param attributes the attributes to insert into the servlet context
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
