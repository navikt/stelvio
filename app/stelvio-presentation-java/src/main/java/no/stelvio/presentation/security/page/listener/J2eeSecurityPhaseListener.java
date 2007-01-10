package no.stelvio.presentation.security.page.listener;

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.presentation.security.page.PageAccessDeniedException;
import no.stelvio.presentation.security.page.PageAuthenticationFailedException;
import no.stelvio.presentation.security.page.PageProtocolSwitchFailedException;
import no.stelvio.presentation.security.page.PageSecurityFileNotFoundException;
import no.stelvio.presentation.security.page.constants.Constants;
import no.stelvio.presentation.security.page.parse.JSFPage;
import no.stelvio.presentation.security.page.util.J2eeSecurityObject;

/**
 * <p>
 * A JavaServer Faces Phaselistener used to conduct security checks using JEE
 * security during the JSF lifecycle.
 * </p>
 * <p>
 * Container managed security doesn't work well with web frameworks that perform
 * server side page forwards to perform navigation. This JavaServer Faces
 * PhaseListener uses container managed authentication and authorization to
 * solves some of the security issues in J2EE
 * </p>
 * <li> It allows fine grained authorization and authentication including secure
 * channel</li>
 * <li> An application can switch between SSL and non-SSL usage for each
 * individual page</li>
 * <li> If required, a flag can be set so that once SSL is enabled it is kept
 * for all pages</li>
 * <li> Pages could require authentication only</li>
 * <li> J2EE security roles can be ANDed or ORed together</li>
 * <br>
 * <br>
 * <p>
 * The PhaseListener is configured faces-config.xml file:
 * </p>
 * 
 * <pre>
 *   &lt;lifecycle&gt;
 *   	&lt;phase-listener&gt;no.stelvio.web.security.page.listener.J2EESecurityPhaseListener&lt;/phase-listener&gt;
 *   &lt;/lifecycle&gt;
 * </pre>
 * 
 * <p>
 * The phaselistener supports security configuration through an XML file where
 * the security constraints for all pages are defined. A page is registrered
 * with its complete path relative to public_html, e.g. /protected/start.jsp.
 * Wildcards are supported at the end of a path. To protect all pages in a
 * directory "protected", the page entry in the security configuration file
 * needs to be /protected/*.
 * </p>
 * <br>
 * <p>
 * Pages are protected by J2EE roles, where one or many roles can be specified
 * for a JSF page. The role named can be ORed or ANDed together, which is
 * defined in the security configuration file.
 * 
 * Note that all security roles used in the configuration file must either exist
 * in the web.xml deployment descriptor or mapped to other roles in this file.
 * Please refer to the J2EE Servlet specification for how to configure security
 * roles.
 * </p>
 * <br>
 * <p>
 * Authentication is enforced by a redirect to an authentication servlet that
 * needs to be configured in the web.xml configuration file. If a user is
 * already authenticated then the authentication servlet is not called. The
 * authentication servlet allows developers to specify a page as to require
 * authentication but no authorization, which is a common scenario.
 * </p>
 * <br>
 * <p>
 * <b>Context parameters supported by the J2eeSecurityPhaseListener</b>
 * 
 * <pre>
 *   &lt;u&gt;Optional&lt;/u&gt;
 *   
 *   &lt;context-param&gt;
 *   &lt;param-name&gt;no.stelvio.web.security.page.security.use_page_cache&lt;/param-name&gt;
 *   &lt;param-value&gt;true&lt;/param-value&gt;
 *   &lt;/context-param&gt;
 *   
 *   Enables / disables caching of authorized page IDs. The default value is &quot;true&quot; to cache pages, which 
 *   improves performance
 * </pre>
 * <pre>
 *   &lt;context-param&gt;
 *   &lt;param-name&gt;no.stelvio.web.security.page.security.unauthorized_access_error_messages&lt;/param-name&gt;
 *   &lt;param-value&gt;Some friendly message to user&lt;/param-value&gt;
 *   &lt;/context-param&gt;
 *   
 *   Customize a message to the user on unauthorized page attempts. Typically used as a friendly message.
 * </pre>
 * <pre>
 *   &lt;u&gt;Required&lt;/u&gt;
 *   
 *   &lt;context-param&gt;
 *   &lt;param-name&gt;no.stelvio.web.security.page.security_config_file&lt;/param-name&gt;
 *   &lt;param-value&gt;/WEB-INF/faces-security-config.xml&lt;/param-value&gt;
 *   &lt;/context-param&gt;
 *   
 *   Specify the location and name of the security configuration file relative to the web-application.
 * </pre>
 * 
 * <b>JSF Lifecycle:</b> <br>
 * <br>
 * The phaselistener is configured to listen to any phase but page
 * authorizations are only checked before the RENDER_RESPONSE phase and after
 * the INVOKE_APPLICATION and RESTORE_VIEW phases.
 * 
 * <br>
 * <br>
 * 
 * @author persondab2f89862d3
 * @version $Id$
 */
public class J2eeSecurityPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory
			.getLog(J2eeSecurityPhaseListener.class);

	private J2eeSecurityObject j2eeSecurityObject = null;

	private boolean useSessionPageCache = true;

	/**
	 * Handle a notification that the processing for a particular phase of the
	 * request processing lifecycle is about to begin. Responds to the
	 * <code>RENDER_RESPONSE</code> phase by conducting the authorization of a
	 * specific JSF page.
	 * 
	 * The JSF 1.2 specification states that all exceptions thrown in a
	 * phaselistener's before or after phase methods are caught, logged and
	 * swallowed. This means that any exceptions thrown in a phaselistener will
	 * not propagate beyond the afterPhase() and beforePhase() methods. For this
	 * reason this method will catch all exceptions thrown or propagated to it
	 * and pass them on to a custom error-handler.
	 * 
	 * @param phaseEvent
	 *            represents the beginning of processing for a particular phase
	 *            of the request processing lifecycle.
	 */
	public void beforePhase(PhaseEvent phaseEvent) {
		PhaseId phaseid = phaseEvent.getPhaseId();

		if (phaseid == PhaseId.RENDER_RESPONSE) {

			try {

				evaluatePage(phaseEvent);

			} catch (Exception e) {

				exceptionHandler(e, phaseEvent);

			}
		}
	}

	/**
	 * Handle a notification that the processing for a particular phase has just
	 * been completed. Responds to the <code>RESTORE_VIEW</code> and
	 * <code>INVOKE_APPLICATION</code> phases by conducting the authorization
	 * of a specific JSF page.
	 * 
	 * The JSF 1.2 specification states that all exceptions thrown in a
	 * phaselistener's before or after phase methods are caught, logged and
	 * swallowed. This means that any exceptions thrown in a phaselistener will
	 * not propagate beyond the afterPhase() and beforePhase() methods. For this
	 * reason this method will catch all exceptions thrown or propagated to it
	 * and pass them on to a custom error-handler.
	 * 
	 * @param phaseEvent
	 *            represents the ending of processing for a particular phase of
	 *            the request processing lifecycle.
	 */
	public void afterPhase(PhaseEvent phaseEvent) {

		PhaseId phaseid = phaseEvent.getPhaseId();
		if (phaseid == PhaseId.RESTORE_VIEW
				|| phaseid == PhaseId.INVOKE_APPLICATION) {

			try {

				evaluatePage(phaseEvent);

			} catch (Exception e) {

				exceptionHandler(e, phaseEvent);

			}
		}
	}

	/**
	 * Return the identifier of the request processing phase during which this
	 * listener is interested in processing <code>PhaseEvent</code> events.
	 * Legal values are the singleton instances defined by the
	 * <code>PhaseId</code> class, including <code>PhaseId.ANY_PHASE</code>
	 * to indicate an interest in being notified for all standard phases.
	 * 
	 * @return the id of the phase this listener is interested in, which is
	 *         <code>PhaseId.ANY_PHASE</code>
	 */
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

	/**
	 * Checks if the page needs to be evaluated and initiate the handling of the
	 * security requirements. If a user is authorized to view a page, this page
	 * will be cached as long as caching is turned on. Used in both the
	 * <code>beforePhase()<code> 
	 * and the <code>afterPhase()</code> methods.
	 * 
	 * @param phaseEvent used to get the <code>FacesContext</code> for the request being processed.
	 * @throws the exceptions propagated from the handleSecurityMethod().
	 */
	private void evaluatePage(PhaseEvent phaseEvent) {
		// initiate security objects the first time
		if (this.j2eeSecurityObject == null) {
			this.j2eeSecurityObject = new J2eeSecurityObject();
		}

		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		
		if (viewId != null && !pageAlreadyEvaluated(viewId, exctx)) {
			boolean pageAlreadyAuthorized = checkPageCache(viewId);
			
			if (!pageAlreadyAuthorized) {
				boolean pageAuthorized = handleSecurity(phaseEvent);
				
				if (pageAuthorized) {
					Map<String, Boolean> userPageCache = getUserSessionPageAuthorizationCache(this.useSessionPageCache);
					
					if (userPageCache != null) {
						boolean requiresSSL = this.j2eeSecurityObject.getPageObject(viewId).requiresSSL();
						
						if (log.isDebugEnabled()) {
							log.debug("Storing viewId "
											+ viewId
											+ " as authorized view with SSL requirement = "
											+ requiresSSL);
						}
						
						userPageCache.put(viewId, requiresSSL);
					}
				}
			}
		}
	}

	/**
	 * Private helper method which checks if a page has been evaluated before.
	 * 
	 * @param viewId
	 *            the id of a page
	 * @param exctx
	 *            the external context used to extract the id of the previous
	 *            page
	 * @return <code>true</code> if the page has been evaluated before
	 */
	private boolean pageAlreadyEvaluated(String viewId, ExternalContext exctx) {
		HttpSession session = (HttpSession) exctx.getSession(true);
		String previousView = (String) session
				.getAttribute(Constants.PREVIOUS_JSF_PAGE_URL);
		return (previousView != null && previousView.equals(viewId)) ? true
				: false;
	}

	/**
	 * Checks if caching is enabled, obtains the cache and searchs for the
	 * current viewId.
	 * 
	 * @param viewId
	 *            the viewId of the current page
	 */
	private boolean checkPageCache(String viewId) {

		ExternalContext exctx = FacesContext.getCurrentInstance()
				.getExternalContext();
		String initParam = exctx.getInitParameter(Constants.USE_PAGE_CACHE);
		this.useSessionPageCache = (initParam != null && initParam.equalsIgnoreCase("FALSE")) ? false : true;
		Map<String, Boolean> userPageCache = getUserSessionPageAuthorizationCache(this.useSessionPageCache);
		return isPageInCache(this.useSessionPageCache, viewId, userPageCache);
	}

	/**
	 * Private helper method which checks if a page is in the cache, i.e. the
	 * page is already authorized.
	 * 
	 * @param cacheEnabled
	 *            page authorization cache is/not enabled
	 * @param viewId
	 *            the id of a page
	 * @param userPageCache
	 *            the authorized pages cache
	 * @return <code>true<code> if the page is found in the cache
	 */
	private boolean isPageInCache(boolean cacheEnabled, String viewId, Map<String, Boolean> userPageCache) {
		if (!cacheEnabled) {
			return false;
		}
		// check if page was successfully authorized before
		// if the page was authorized then the HashTable returns "true" or
		// "false" indicating if the
		// page requires SSL or not
		Object authorizedViewIdRequiresSSL = null;
		boolean pageAlreadyAuthorized = false;

		if (userPageCache != null) {
			authorizedViewIdRequiresSSL = userPageCache.get(viewId);
			pageAlreadyAuthorized = authorizedViewIdRequiresSSL == null ? false : true;
		}
		
		// if page was authorized before then we don't need to access and parse
		// the security object. This improves
		// performance, assuming that a user page authorization doesn't change
		// while working within the application.
		if (pageAlreadyAuthorized) {
			boolean pageRequiresSSL = ((Boolean) authorizedViewIdRequiresSSL).booleanValue();

			if (log.isDebugEnabled()) {
				log.debug("Page authorization for " + viewId + " was evaluated before.");

				if (pageRequiresSSL) {
						log.debug("User is allowed to access page but SSL is required.");
				} else {
						log.debug("User is allowed to access page. No SSL required.");
				}
			}
			
			// set last visited page
			FacesContext fc = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
			session.setAttribute(Constants.PREVIOUS_JSF_PAGE_URL, viewId);
			
			this.j2eeSecurityObject.handleProtocolSwitch(viewId, pageRequiresSSL);
			
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the page authorization cache from <code>Session</code>. If
	 * caching is enabled and <code>Session</code> does not contain a cache, a
	 * new cache will be created in Session and returned.
	 * 
	 * @param enabled
	 *            page authorization cache is/not enabled
	 * @return the page autorization cache, <code>null</code> if the cache is
	 *         not enabled
	 */
	private Map<String, Boolean> getUserSessionPageAuthorizationCache(boolean enabled) {
		if (enabled) {
			ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Map<String, Boolean>> sessionScopeMap = ectx.getSessionMap();
			Map<String, Boolean> userPageCache = (Map<String, Boolean>) sessionScopeMap.get("userAccessCache");
			
			if (userPageCache == null) {
				userPageCache = new HashMap<String, Boolean>();
				sessionScopeMap.put("userAccessCache", userPageCache);
			}
			
			return userPageCache;
		} else {
			return null;
		}
	}

	/**
	 * Handles the security constraints and ensures that the user is authorized
	 * to view the page.
	 * 
	 * @param phaseEvent
	 *            used to get the <code>FacesContext</code> for the request
	 *            being processed
	 * 
	 * @return <code>true</code> if the user is authorized to view the page
	 * 
	 * @throws PageAccessDeniedException
	 *             if access is denied
	 * @throws PageAuthenticationFailedException
	 *             if authentication redirection has failed
	 * @throws PageSecurityFileNotFoundException
	 *             if security config file cannot be found
	 * @throws PageProtocolSwitchFailedException
	 *             if protocol switch has failed
	 */
	private boolean handleSecurity(PhaseEvent phaseEvent) {
		FacesContext facesContext = phaseEvent.getFacesContext();
		String viewId = facesContext.getViewRoot().getViewId();

		if (viewId != null) {
			ExternalContext exctx = facesContext.getExternalContext();
			HttpSession session = (HttpSession) exctx.getSession(true);
			this.j2eeSecurityObject.initializeSecurityDefinitions();
			// if keep ssl mode is configured in faces-security-config.xml then
			// SSL
			// connection is enforced for all subsequent requests
			boolean authorizedUser = false;

			authorizedUser = this.j2eeSecurityObject.authorizePageAccess(
					viewId, session);

			if (authorizedUser) {
				session.setAttribute(Constants.PREVIOUS_JSF_PAGE_URL, viewId);
				JSFPage page = this.j2eeSecurityObject.getPageObject(viewId);
				this.j2eeSecurityObject.handleProtocolSwitch(viewId, page
						.requiresSSL());
				return true;
			}
		}
		return true;
	}

	/**
	 * Sends an HTTP error on the HttpServletResponse.
	 * 
	 * @param statusCode
	 *            the HttpServletResponse status code.
	 * @param error_message
	 *            the error message
	 */
	private void handleError(int statusCode, String errorMessage) {

		try {
			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			if (errorMessage != null) {
				response.sendError(statusCode, errorMessage);
			} else {
				response.sendError(statusCode);
			}
		} catch (IOException doNothing) {
			log
					.error(
							"SEVERE! Unexpected exception caught when redirecting to error page.'",
							doNothing);
		}
	}

	/**
	 * Checks the type of exception and handles it accordingly.
	 * 
	 * @param e
	 *            the Exception to handle
	 * @param event
	 *            the PhaseEvent used to obtain information about current
	 *            request.
	 */
	private void exceptionHandler(Exception e, PhaseEvent event) {

		if (e instanceof PageSecurityFileNotFoundException) {

			if (log.isDebugEnabled()) {
				log
						.debug("Could not find configuration file with security definitions. Sending HTTP error.");
			}
			handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
					.getMessage());

		} else if (e instanceof PageProtocolSwitchFailedException) {

			if (log.isDebugEnabled()) {
				log.debug("Protocol switch failed. Sending HTTP error.");
			}
			handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
					.getMessage());

		} else if (e instanceof PageAuthenticationFailedException) {

			if (log.isDebugEnabled()) {
				log.debug("Authentication of user failed. Sending HTTP error.");
			}
			handleError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());

		} else if (e instanceof PageAccessDeniedException) {

			if (log.isDebugEnabled()) {
				log
						.debug("User is not authorized to view the page. Sending HTTP error.");
			}
			handleError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());

		} else {

			log.error("Unexpected exception caught at: '"
					+ J2eeSecurityPhaseListener.class + "' in phase '"
					+ event.getPhaseId() + "'.", e);

			handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, null);
		}
	}

	/**
	 * Gets the J2eeSecurityObject which is used to conduct the security checks
	 * and maintain the security definitions of the application.
	 * 
	 * @return the j2eeSecurityObject
	 * @see #setJ2eeSecurityObject()
	 */
	public J2eeSecurityObject getJ2eeSecurityObject() {
		return j2eeSecurityObject;
	}

	/**
	 * Gets the J2eeSecurityObject which is used to conduct the security checks
	 * and maintain the security definitions of the application.
	 * 
	 * @param securityObject
	 *            the j2eeSecurityObject to set
	 * @see #getJ2eeSecurityObject()
	 */
	public void setJ2eeSecurityObject(J2eeSecurityObject securityObject) {
		j2eeSecurityObject = securityObject;
	}

}
