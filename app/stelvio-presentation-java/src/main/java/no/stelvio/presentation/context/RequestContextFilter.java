package no.stelvio.presentation.context;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.ComponentIdHolder;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.common.log.MDCOperations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.filter.OncePerRequestFilter;

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
			throws ServletException, IOException {
		try {
			// Only import the request context from session if both the session
			// and a persisted context exists.
			HttpSession session = request.getSession(false);

			if (null != session) {
				Object context = session.getAttribute(REQUEST_CONTEXT);
				if (null != context) {
					RequestContextSetter.setRequestContext((RequestContext) context);
					// Add to MDC
					MDCOperations.setMdcProperties();
				} else {
					if (LOG.isInfoEnabled()) {
						LOG.info("Session exists, but RequestContext was not persisted");
					}
				}
			}

			
			String componentId = retrieveComponentId();
			// Always update the module, process and transaction id
			// -- why do we save it to session if every property is updated?
			// The screen id can not be set here because the filter runs outside
			// the JSF and SWF context. Screen id is set through a RequestContextPhaseListener
			// -- how to find module id? Is this only used when logging?
			RequestContext requestContext = new SimpleRequestContext(null, null, String.valueOf(UUID.randomUUID()), 
					componentId);
			RequestContextSetter.setRequestContext(requestContext);			
			
			// Delegate processing to the next filter or resource in the chain
			chain.doFilter(request, response);			
			
			// UserId is set on RequestContext by a SecurityContext-filter

			// Session might have bean constructed, deleted or invalidated during
			// processing further down the chain, so check again
			session = request.getSession(false);

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
			// Reset MDC
			MDCOperations.resetMdcProperties();
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
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}