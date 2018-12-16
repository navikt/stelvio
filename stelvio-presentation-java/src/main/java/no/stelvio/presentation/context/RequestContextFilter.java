package no.stelvio.presentation.context;

import java.util.Map;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.filter.OncePerRequestFilter;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.ComponentIdHolder;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;

/**
 * RequestContextFilter is an implementation of the <i>Intercepting Filter</i> pattern that is responsible for constructing and
 * destroying the <i>RequestContext</i> for each request and response being processed. This filter should be the first filter in
 * the chain.
 * <p>
 * This filter accesses a Spring ApplicationContext to retrieve the component id. This can only be done if this filter is
 * configured as a Spring bean in web application's application context. (prs-XYZ-context.xml). The filter configuration in the
 * web.xml must be set up with {@link DelegatingFilterProxy} as <code>&lt;filter-class&gt;</code>
 * </p>
 * Below is an example of how the filter is set up correctly with DelegatingFilterProxy forwarding the call to a
 * RequestContextFilter configured as a Spring bean in an application context with
 * <code>bean id="prs.XYZ.requestContextFilter"</code>
 * 
 * <pre>
 * 
 * </pre>
 * 
 * @author person7553f5959484, Accenture
 * @author person983601e0e117, Accenture
 * @version $Id: RequestContextFilter.java 2574 2005-10-20 08:04:22Z psa2920 $
 */
public class RequestContextFilter extends OncePerRequestFilter implements ApplicationContextAware {
	private static final String REQUEST_CONTEXT = RequestContext.class.getName();
	private static final Log LOG = LogFactory.getLog(RequestContextFilter.class);

	/** The Spring ApplicationContext. */
	private ApplicationContext applicationContext;

	/**
	 * Performs the following processing steps. :
	 * <ol>
	 * <li>Loads RequestContext from Session if possible</li>
	 * <li>Updates RequestContext with current processId</li>
	 * <li>Updates RequestContext with current transactionId</li>
	 * <li>Delegates processing to the next filter or resource in the chain</li>
	 * <li>Updates RequestContext with current userId</li>
	 * <li>Stores RequestContext in Session if possible</li>
	 * <li>Resets RequestContext before next time</li>
	 * </ol>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException {
		try {
			
			// get componentId from ApplicationContext
			String componentId = retrieveComponentId();			
			
			// Always update the componentId and transaction id, ScreenId and ModuleId is set to null.
			RequestContext requestContext = new SimpleRequestContext(null, null, String.valueOf(UUID.randomUUID()), 
					componentId);
			RequestContextSetter.setRequestContext(requestContext);
			
			// The screen id can not be set here because the filter runs outside
			// the JSF and SWF context. Screen id is set through a RequestContextPhaseListener (not currently implemented)
			
			// Delegate processing to the next filter or resource in the chain
			chain.doFilter(request, response);			
			
			// UserId is set on RequestContext by a SecurityContext-filter

			// Get session so we can write RequestContext to it
			HttpSession session = request.getSession(false);

			if (null != session) {
				try {
					session.setAttribute(REQUEST_CONTEXT, RequestContextHolder.currentRequestContext());
				} catch (IllegalStateException ise) {
					if (LOG.isInfoEnabled()) {
						LOG.info("Session was invalidated, could not persist RequestContext", ise);
					}
				}
			}
		} catch (Exception e) {
			throw new ServletException("An error occured while updating the RequestContext", e);
		} finally {
			// Always reset the RequestContext, just to be on the safe side
			RequestContextSetter.resetRequestContext();
		}
	}

	/**
	 * Goes through the ApplicationContext to find the ComponentIdHolder that sets the ComponentId.
	 * 
	 * Logs error if no ComponentId has been specified. Logs warn if more than one ComponentId has been specified
	 * 
	 * If more than one componentId is configured in the ApplicationContext, the first will be used. If more than one is
	 * configured with the same name (or no name), the result is unspecified (the last one will probably be used)
	 * 
	 * @return componentId as String - first componentId specified in configuration if more than one is configured in the
	 *         ApplicationContext. <code>null</code> if no componentId is specified
	 * @see ComponentIdHolder
	 */
	private String retrieveComponentId() {
		String componentId = null;

		if (applicationContext == null) {
			// No applicationContext means that this filter hasn't been configured in a spring app ctx
			LOG.error("RequestContextFilter hasn't been set up as a DelegatingFilterProxy in the web.xml."
					+ " The RequestContextFilter must be configured as a Spring bean and accessed by a"
					+ " DelefatingFilterProxy to be able to retrieve a component id");
		} else {

			// Get the ComponentId configured somewhere in the applicationContext
			Map<?, ?> beanNameComponentIdHolderPairs = applicationContext.getBeansOfType(ComponentIdHolder.class, false, true);
			// ComponentId should ALWAYS be configured
			if (beanNameComponentIdHolderPairs == null || beanNameComponentIdHolderPairs.values().size() == 0) {
				LOG.error("No ComponentIdHolder bean  defined in the ApplicationContext."
						+ " The ComponentId hasn't been configured. <null> value will be used.");
			} else {
				// ApplicationId should only be configured once
				int numberOfComponentIds = beanNameComponentIdHolderPairs.values().size();
				if (numberOfComponentIds > 1) {
					LOG.warn("Duplicate [#" + numberOfComponentIds + "] ComponentIdHolder has been specified. "
							+ "Will be using the first entry in the ApplicationContext. "
							+ "Make sure only one ComponentIdHolder is configured per configuration module");
				}
				componentId = (String) ((ComponentIdHolder) beanNameComponentIdHolderPairs.values().iterator().next())
						.getComponentId();
				LOG.debug("Application Id set to " + componentId);
			}
		}
		return componentId;
	}

	/**
	 * Sets the application contact.
	 * 
	 * @param applicationContext
	 *            the application context
	 * @throws BeansException
	 *             if a bean exception occurs
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}