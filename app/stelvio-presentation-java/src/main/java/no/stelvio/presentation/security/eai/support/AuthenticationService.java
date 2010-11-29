package no.stelvio.presentation.security.eai.support;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.context.ExternalContextHolder;

/**
 * A utility class for setting authentication attributes in a StelvioSecurityEaiSessionMap in the HttpSession and redirecting to
 * an authentication url.
 * 
 * @author persondab2f89862d3, Accenture
 * @see StelvioSecurityEaiSessionMap
 */
public class AuthenticationService {

	private static final Log LOG = LogFactory.getLog(AuthenticationService.class);

	private String authenticationUrl;

	/**
	 * Gets whether or not the user is allowed to reauthenticate as himself. Created to prevent too many reauthentication
	 * attempts without actually beeing authorized as another user.
	 * 
	 * @param request
	 *            the current HttpServletRequest
	 * @return true if no constraints have been set, false otherwise
	 */
	public boolean isReAuthenticationAsSelfAllowed(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Object sessionMap = session.getAttribute(StelvioSecurityEaiSessionMap.SESSIONMAP_KEY);
		StelvioSecurityEaiSessionMap securityMap;
		if (sessionMap != null && sessionMap instanceof StelvioSecurityEaiSessionMap) {
			securityMap = (StelvioSecurityEaiSessionMap) sessionMap;
			Object object = securityMap.get(StelvioSecurityEaiSessionMap.Keys.ALLOW_REAUTHENTICATION_AS_SELF.getName());
			boolean allowed = object != null ? (Boolean) object : true;
			return allowed;
		} else {
			return true;
		}
	}

	/**
	 * Sets whether or not the current user should be allowed to reauthenticate as himself.
	 * 
	 * @param request
	 *            the current HttpServletRequest
	 * @param allowed
	 *            true or false
	 */
	protected void setReAuthenticationAsSelfAllowed(HttpServletRequest request, boolean allowed) {
		HttpSession session = request.getSession(false);
		Object sessionMap = session.getAttribute(StelvioSecurityEaiSessionMap.SESSIONMAP_KEY);
		StelvioSecurityEaiSessionMap securityMap;
		if (sessionMap != null && sessionMap instanceof StelvioSecurityEaiSessionMap) {
			securityMap = (StelvioSecurityEaiSessionMap) sessionMap;
		} else {
			securityMap = new StelvioSecurityEaiSessionMap();
		}
		securityMap.put(StelvioSecurityEaiSessionMap.Keys.ALLOW_REAUTHENTICATION_AS_SELF.getName(), allowed);
		session.setAttribute(StelvioSecurityEaiSessionMap.SESSIONMAP_KEY, securityMap);
	}

	/**
	 * Sends a reauthentication request to the authentication url using the HttpServletResponse from the ExternalContext. The
	 * authorizedAs parameter represent the username of a another user to which the current user should be authorized to act on
	 * behalf of. The authorizationType parameter represent the type of authorization given.
	 * 
	 * @param authorizedAs
	 *            the username of the user that the current user is authorized as.
	 * @param authorizationType
	 *            the type of authorization given
	 * @throws IOException
	 *             if the redirect fails.
	 */
	public void sendReAuthenticationRequest(String authorizedAs, String authorizationType) throws IOException {
		ExternalContext externalContext = ExternalContextHolder.getExternalContext();
		if (externalContext == null) {
			LOG.warn("The external context is null in no.stelvio.presentation.security.eai.support.AuthenticationService." 
					+ "sendReAuthenticationRequest(String, String) and request and response was not found for the authorised user " 
					+ authorizedAs + ".");
		} else {
			HttpServletRequest request = (HttpServletRequest) externalContext.getNativeRequest();
			HttpServletResponse response = (HttpServletResponse) externalContext.getNativeResponse();
			sendReAuthenticationRequest(authorizedAs, authorizationType, request, response);
		}
	}

	/**
	 * Private helper method which ensures that the authentication url is set with a context root.
	 * 
	 * @param request
	 *            the HttpServletRequest
	 * @return the authenticationUrl
	 */
	private String buildAuthenticationUrl(HttpServletRequest request) {
		if (this.authenticationUrl != null) {
			HttpServletRequest req = request;
			String context = req.getContextPath();
			if (!context.equals("") && !authenticationUrl.startsWith(context)) {

				authenticationUrl = authenticationUrl.startsWith("/") ? context + authenticationUrl : context + "/"
						+ authenticationUrl;
			}
			return authenticationUrl;
		}
		return null;
	}

	/**
	 * Private helper method which updates the StelvioSecurityEaiSessionMap with the authorizedAs and authorizationType
	 * parameters. In addition the StelvioSecurityEaiSessionMap.Keys.POST_AUTHENTICATION_URL, which represent the url that the
	 * user should be sent to after reauthentication, is set to the current url.
	 * 
	 * @param request
	 *            the HttpServletRequest
	 * @param authorizedAs
	 *            the username of the user that the current user is authorized as.
	 * @param authorizationType
	 *            the type of authorization given
	 */
	private void updateSessionMap(HttpServletRequest request, String authorizedAs, String authorizationType) {
		HttpSession session = request.getSession(false);
		Object sessionMap = session.getAttribute(StelvioSecurityEaiSessionMap.SESSIONMAP_KEY);
		StelvioSecurityEaiSessionMap securityMap;
		if (sessionMap != null && sessionMap instanceof StelvioSecurityEaiSessionMap) {
			securityMap = (StelvioSecurityEaiSessionMap) sessionMap;
		} else {
			securityMap = new StelvioSecurityEaiSessionMap();
		}
		securityMap.put(StelvioSecurityEaiSessionMap.Keys.AUTHORIZED_AS.getName(), authorizedAs);
		securityMap.put(StelvioSecurityEaiSessionMap.Keys.AUTHORIZATION_TYPE.getName(), authorizationType);
		securityMap.put(StelvioSecurityEaiSessionMap.Keys.POST_AUTHENTICATION_URL.getName(), getCurrentUrl(request));
		session.setAttribute(StelvioSecurityEaiSessionMap.SESSIONMAP_KEY, securityMap);
	}

	/**
	 * Returns the current url.
	 * 
	 * @param request request
	 * @return the current url.
	 */
	private String getCurrentUrl(HttpServletRequest request) {
		HttpServletRequest req = request;
		StringBuffer url = new StringBuffer(req.getRequestURI());
		// req.getRequestURL();

		String queryString = req.getQueryString();
		if (queryString != null) {
			url.append("?" + queryString);
		}
		return url.toString();
	}

	/**
	 * Sends a reauthentication request to the authentication url using the HttpServletResponse parameter. The authorizedAs
	 * parameter represent the username of a another user to which the current user should be authorized to act on behalf of.
	 * The authorizationType parameter represent the type of authorization given.
	 * 
	 * @param authorizedAs
	 *            the username of the user that the current user is authorized as.
	 * @param authorizationType
	 *            the type of authorization given
	 * @param request
	 *            the HttpServletResponse
	 * @param response
	 *            the HttpServletRequest
	 * @throws IOException
	 *             if an error occurs while redirecting
	 */
	public void sendReAuthenticationRequest(String authorizedAs, String authorizationType, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		updateSessionMap(request, authorizedAs, authorizationType);
		if (authorizedAs != null && authorizedAs.equals(request.getRemoteUser())) {
			LOG.debug("The user " + authorizedAs
					+ "has sent a reauthentication request as himself with the authorizationType '" + authorizationType + "'.");
			// The authorizationType is blank, the user is therefore allowed to reauthenticate as
			// himself at a later stage.
			if (authorizationType != null && authorizationType.equals("")) {
				setReAuthenticationAsSelfAllowed(request, true);
				LOG.debug("The authorization type is blank. The user will be allowed to reauthenticate"
						+ " at a later stage.");
			} else {
				setReAuthenticationAsSelfAllowed(request, false);
				LOG.debug("The user has requested to reauthenticate with the authorization type " + authorizationType
						+ ". The user will not be allowed to reauthenticate as himself until authorized"
						+ " as another user or supplying a blank authorization type.");
			}
		} else {
			setReAuthenticationAsSelfAllowed(request, true);
		}
		
		response.sendRedirect(buildAuthenticationUrl(request));		
		ExternalContext externalContext = ExternalContextHolder.getExternalContext();
		if (externalContext == null) {
			LOG.warn("The external context is null in no.stelvio.presentation.security.eai.support.AuthenticationService." 
					+ "sendReAuthenticationRequest(String, String, HttpServletRequest, HttpServletResponse) and the " 
					+ "recordResponseComplete() was not executed for the authorised user " + authorizedAs + ".");
		} else {
			externalContext.recordResponseComplete();
		}
	}

	/**
	 * Gets the authenticationUrl that authentication and reauthentication requests are sent to.
	 * 
	 * @return the authenticationUrl
	 */
	public String getAuthenticationUrl() {
		return authenticationUrl;
	}

	/**
	 * Sets the authenticationUrl that authentication and reauthentication requests are sent to.
	 * 
	 * @param authenticationUrl
	 *            the authenticationUrl
	 */
	public void setAuthenticationUrl(String authenticationUrl) {
		this.authenticationUrl = authenticationUrl;
	}

}
