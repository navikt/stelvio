package no.stelvio.presentation.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.error.LoggableException;
import no.stelvio.common.error.StelvioException;
import no.stelvio.common.error.SystemUnrecoverableException;
import no.stelvio.common.util.SequenceNumberGenerator;

/**
 * AbstractFilter is a convenient class that an <i>Intercepting Filter</i> implementation
 * may extend without having to implement the mandatory init and destroy methods.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: AbstractFilter.java 2109 2005-03-09 10:35:40Z psa2920 $
 */
public abstract class AbstractFilter implements Filter {

	// The log
	protected final Log log;

	// The filter configuration 
	protected FilterConfig filterConfig = null;

	/**
	 * Constructs the filter and initializes the logger.
	 */
	public AbstractFilter() {
		log = LogFactory.getLog(this.getClass());
	}

	/**
	 * Initializes this filter before processing can start. If a sub class needs
	 * additional initialization, it should override the {@link #doInit()} method.
	 * 
	 * <p/>
	 * 
	 * This is a template method that performs the following ordered steps:
	 * 
	 * <ol>
	 * 	   <li> logs that initialization started </li>
	 * 	   <li> assigns the FilterConfig to the protected {@link #filterConfig} variable </li>
	 * 	   <li> calls the protected {@link #doInit()} method that may be overridden by the sub class</li>
	 * 	   <li> logs that initialization ended </li>
	 * </ol>
	 * 
	 * The concrete filter should be configured in web.xml as follows:
	 * 
	 * <p/>
	 * 
	 * <pre>
	 * <filter>
	 *    <filter-name>RequestContextFilter</filter-name>
	 *    <display-name>RequestContextFilter</display-name>
	 *    <description>Request Context Filter</description>
	 *    <filter-class>no.stelvio.web.filter.RequestContextFilter</filter-class>
	 *    <init-param>
	 *       <param-name>screenId</param-name>
	 *       <param-value>BISYS</param-value>
	 *    </init-param>
	 *    <init-param>
	 *       <param-name>moduleId</param-name>
	 *       <param-value>BISYS</param-value>
	 *    </init-param>
	 *    <init-param>
	 *       <param-name>userId</param-name>
	 *       <param-value>NAV9999</param-value>
	 *    </init-param>
	 * </filter>
	 * </pre>
	 * 
	 * <p/>
	 * 
	 * {@inheritDoc}
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public final void init(FilterConfig filterConfig) throws ServletException {
		try {
			this.filterConfig = filterConfig;
			
			setRequestContext("Startup");
			
			if (log.isInfoEnabled()) {
				log.info("Initializing " + filterConfig.getFilterName() + " ...");
			}
			
			doInit();
			
			if (log.isInfoEnabled()) {
				log.info(filterConfig.getFilterName() + " initialized");
			}
		}
		catch (Exception e) {
			throw new ServletException("An error occured while initiating the filter", e);
		}
		finally {
			RequestContext.remove();
		}
	}

	/**
	 * {@inheritDoc}
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		HttpServletRequest httpServletRequest = null;
		HttpServletResponse httpServletResponse = null;
		try {
			httpServletRequest = (HttpServletRequest) request;
			httpServletResponse = (HttpServletResponse) response;
		} catch (ClassCastException cce) {
			throw new ServletException(
				"The filter named "
					+ filterConfig.getFilterName()
					+ " of type "
					+ this.getClass()
					+ " is unable to process the current request/response",
				cce);
		}
		try {
			doFilter(httpServletRequest, httpServletResponse, chain);
		} catch (RuntimeException re) {

			// Create a new top level error
			SystemUnrecoverableException se = null;
//				new UnrecoverableException(re, new String[] { filterConfig.getFilterName()});TODO: handled differently

			// Handle the error
//			ErrorHandler.handleException(se); TODO: handled differently

			//	Loop through the exception chain to build a nested message for the system exception.
			StringBuffer message = new StringBuffer();
			StringBuffer tabs = new StringBuffer("\t");
			Throwable t = se;
			while (null != t) {
				if (t instanceof LoggableException) {
//					message.append(ErrorHandler.getMessage(t)); TODO: handled differently
					message.append("(feilnummer=").append(((StelvioException) t).getErrorId()).append(")");
				} else {
					message.append(t.getLocalizedMessage());
				}
				message.append("\n").append(tabs);
				tabs.append("\t");
				t = t.getCause();
			}
			throw new ServletException(message.toString());
		}

	}

	/**
	 * Convenient method called by {@link #doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)},
	 * to avoid casting to the HTTP specialized request and response in every 
	 * filter implementation that should only process HTTP requests and responses. 
	 * 
	 * @param request 			the HttpServletRequest to be processed
	 * @param response 			the HttpServletResponse to be processed
	 * @param chain 			the chain of filters and resources
	 * @throws IOException		if an input or output exception occurs
	 * @throws ServletException	if an exception occurs that interferes with the normal operation
	 * 
	 * @see #doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	protected abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws IOException, ServletException;

	/**
	 * Destroys the filter. If a sub class needs
	 * additional initialization, it should override the {@link #doDestroy()} method.
	 * 
	 * <p/>
	 * 
	 * This is a template method that performs the following ordered steps:
	 * <ol>
	 * 	   <li> logs that destruction started </li>
	 * 	   <li> calls the protected {@link #doDestroy()} method that may be overridden by the sub class</li>
	 * 	   <li> logs that destruction ended </li>
	 * </ol>
	 * 
	 * {@inheritDoc}
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		try {
			setRequestContext("Shutdown");
			if (log.isInfoEnabled()) {
				log.info("Destroying " + filterConfig.getFilterName() + " ...");
			}
			doDestroy();
			if (log.isInfoEnabled()) {
				log.info(filterConfig.getFilterName() + " destroyed");
			}
		}
		finally {
			RequestContext.remove();
		}
	}

	/**
	 * Should be overrided by sub classes that needs to perform additional initialization.
	 * 
	 * @see #init(javax.servlet.FilterConfig)
	 */
	protected void doInit() {
	}

	/**
	 * Should be overrided by sub classes that needs to perform additional clean up during destruction.
	 * 
	 * @see #destroy()
	 */
	protected void doDestroy() {
	}

	/**
	 * Initialize request context to ensure proper logging.
	 * 
	 * @param processId the process id, either "Startup" or "Shutdown" is used.
	 */
	private void setRequestContext(String processId) {
		RequestContext.setScreenId(filterConfig.getInitParameter("screenId"));
		RequestContext.setModuleId(filterConfig.getInitParameter("moduleId"));
		RequestContext.setProcessId(processId);
		RequestContext.setTransactionId(String.valueOf(SequenceNumberGenerator.getNextId("Transaction")));
		RequestContext.setUserId(filterConfig.getInitParameter("userId"));
	}
}
