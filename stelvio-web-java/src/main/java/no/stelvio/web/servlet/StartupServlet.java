package no.stelvio.web.servlet;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.web.context.support.ServletContextAwareProcessor;

import no.stelvio.common.config.Config;
import no.stelvio.common.config.ConfigurationException;
import no.stelvio.common.context.RequestContext;
import no.stelvio.common.error.ErrorHandler;
import no.stelvio.common.performance.MonitorKey;
import no.stelvio.common.performance.PerformanceMonitor;
import no.stelvio.common.util.SequenceNumberGenerator;
import no.stelvio.web.constants.Constants;

/**
 * Servlet for system initialization at startup time using Spring Framework.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: StartupServlet.java 2598 2005-10-31 12:31:23Z skb2930 $
 */
public class StartupServlet extends HttpServlet {

	/** Logger to be used for writing <i>TRACE</i> and <i>DEBUG</i> messages. */
	private static final Log log = LogFactory.getLog(StartupServlet.class);
	private static final MonitorKey MONITOR_KEY = new MonitorKey("StartupServlet", MonitorKey.PRESENTATION);

	/**
	 * Implemented to always throw ServletException.
	 * 
	 * {@inheritDoc}
	 */
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		throw new ServletException("Illegal attempt to invoke service on StartupServlet");
	}

	/**
	 * Initializes all configured franmework and/or application components. 
	 * The servlet should be configured in web.xml as follows:
	 * 
	 * <p/>
	 * 
	 * <pre>
	 * <servlet>
	 *    <servlet-name>StartupServlet</servlet-name>
	 *    <display-name>StartupServlet</display-name>
	 *    <description>Startup Servlet</description>
	 *    <servlet-class>no.stelvio.web.servlet.StartupServlet</servlet-class>
	 *    <init-param>
	 *       <param-name>PRESENTATION_SERVICES</param-name>
	 *       <param-value>presentation-services.xml</param-value>
	 *    </init-param>
	 *    <init-param>
	 *       <param-name>StartupHelperList</param-name>
	 *       <param-value>DefaultLocale,InitErrorHandler,CodesTableManager</param-value>
	 *    </init-param>
	 *    <init-param>
	 *       <param-name>screenId</param-name>
	 *       <param-value>BISYS</param-value>
	 *    </init-param>
	 *    <init-param>
	 *       <param-name>moduleId</param-name>
	 *       <param-value>BISYS</param-value>
	 *    </init-param>
	 *    <init-param>
	 *       <param-name>processId</param-name>
	 *       <param-value>Startup</param-value>
	 *    </init-param>
	 *    <init-param>
	 *       <param-name>userId</param-name>
	 *       <param-value>NAV9999</param-value>
	 *    </init-param>
	 *    <load-on-startup>1</load-on-startup>
	 * </servlet>
	 * </pre>
	 * 
	 * <p/>
	 *
	 * The servlet loops through the comma separated <i>StartupHelperList</i> init-param and treats
	 * each element as a bean id that can be looked up from a Spring Framework XML configuration file 
	 * specified using the <i>PRESENTATION_SERVICES</i> init-param. If the initialization of a specific 
	 * bean requires a method to be invoked in addition to bean construction, this must be configured 
	 * using the Spring Framework init-method functionality.
	 * 
	 * {@inheritDoc}
	 */
	public void init() throws ServletException {
		try {
			RequestContext.setScreenId(getServletConfig().getInitParameter("screenId"));
			RequestContext.setModuleId(getServletConfig().getInitParameter("moduleId"));
			RequestContext.setProcessId(getServletConfig().getInitParameter("processId"));
			RequestContext.setTransactionId(String.valueOf(SequenceNumberGenerator.getNextId("Transaction")));
			RequestContext.setUserId(getServletConfig().getInitParameter("userId"));

			PerformanceMonitor.start(MONITOR_KEY);
			startup();
			PerformanceMonitor.end(MONITOR_KEY);

		} catch (ServletException se) {
			PerformanceMonitor.fail(MONITOR_KEY);
			throw se;
		} catch (RuntimeException re) {
			PerformanceMonitor.fail(MONITOR_KEY);
			throw re;
		}
		finally {
			RequestContext.remove();
		}
	}

	/**
	 * The startup functionality.
	 * 
	 * @throws ServletException if initialization fails
	 */
	void startup() throws ServletException {
		String startupHelperList = getServletConfig().getInitParameter("StartupHelperList");

		if (null != startupHelperList) {
			// Initialize Spring Configuration, if not already initialized
			Config config = (Config) getServletContext().getAttribute(Config.PRESENTATION_SERVICES);

			if (null == config) {
				String filename = getServletConfig().getInitParameter(Constants.PRESENTATION_SERVICES);

				if (log.isDebugEnabled()) {
					log.debug("Initialize Spring Configuration using " + filename);
				}

				try {
					config = Config.getConfig(filename);
					final ConfigurableListableBeanFactory beanFactory = config.getBeanFactory();
					// This will insert the servlet context into all beans in presentation-services.xml that implement the
					// ServletContextAware interface
					beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(getServletContext()));
				} catch (Throwable t) {
					throw new ServletException(
						"Failed to initialize Spring Configuration using " + filename,
						ErrorHandler.handleError(t));
				}

				getServletContext().setAttribute(Config.PRESENTATION_SERVICES, config);
			}

			// Loop through the comma separated startup helper list and treat
			// each element as a bean id that can be looked up from a Spring Framework
			// configuration. If the initialization of a specific bean requires a method
			// to be invoked in addition to bean construction, this must be configured 
			// using the Spring Framework init-method functionality.
			StringTokenizer st = new StringTokenizer(startupHelperList, ",");

			while (st.hasMoreTokens()) {
				String beanId = st.nextToken();

				try {
					if (log.isInfoEnabled()) {
						log.info("Start initialization of " + beanId);
					}

					config.getBean(beanId);

					if (log.isInfoEnabled()) {
						log.info("Completed initialization of " + beanId);
					}
				} catch (ConfigurationException ce) {
					if (log.isWarnEnabled()) {
						log.warn("Failed to initialize " + beanId);
					}

					try {
						// Handle the error and continue if that is possible
						ErrorHandler.handleError(ce);
					} catch (Throwable t) {
						// Abort initialization if the error cannot be handled
						throw new ServletException("Failed to initialize and ErrorHandler could not handle error for " + beanId, ce);
					}
				}
			}
		}
	}
}
