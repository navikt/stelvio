package no.stelvio.presentation.security.eai.support;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.presentation.security.eai.AuthenticationFailureException;

/**
 * A class used to set the neccessary HTTP headers on the HttpServletResponse in order to reauthenticate with IBM Tivoli Access
 * Manager Webseal. Primarily used to reauthenticate the user to act as another user with a certain authorization type.
 * 
 * @see ReAuthenticationHeaders
 * @see AbstractAuthentication
 */
public class ReAuthentication extends AbstractAuthentication {

	private final Log LOGGER = LogFactory.getLog(this.getClass());

	private boolean insertJunctionInRedirUrl = true;

	@Override
	public void authenticate() {

		HttpSession session = request.getSession();

		StelvioSecurityEaiSessionMap securityMap = getSecurityMap(session);
		String user = request.getRemoteUser();
		Object level = request.getHeader(WebsealHeaderNames.AUTHENTICATION_LEVEL);

		if (user != null && securityMap != null) {

			String authorizedAs = (String) securityMap.get(StelvioSecurityEaiSessionMap.Keys.AUTHORIZED_AS.getName());
			String authorizationType = (String) securityMap.get(StelvioSecurityEaiSessionMap.Keys.AUTHORIZATION_TYPE.getName());
			String postAuthUrl = (String) securityMap.get(StelvioSecurityEaiSessionMap.Keys.POST_AUTHENTICATION_URL.getName());

			ReAuthenticationHeaders headers = new ReAuthenticationHeaders(this.getConfig());
			// User identity headers
			headers.setUserIdentity(this.getAccessManagerUser());
			headers.setOriginalUserId(user);
			if (isInsertJunctionInRedirUrl()) {
				headers.setEaiRedirUrlHeader(getWebsealJunction() + postAuthUrl);
			} else {
				headers.setEaiRedirUrlHeader(postAuthUrl);
			}
			headers.setAuthenticationLevel((String) level);
			headers.setAuthorizedAs(authorizedAs);
			headers.setAuthorizationType(authorizationType);
			addHeaders(headers);
		} else {
			throw new AuthenticationFailureException("Could not process the re-authentication request.");
		}
	}

	/**
	 * Gets the name of the Webseal junction from the cookie WebsealHeaderNames.WEBSEAL_JUNCTION_COOKIE.
	 * 
	 * @return the webseal junction name
	 */
	protected String getWebsealJunction() {

		Cookie[] cookies = request.getCookies();
		String junction = "";
		for (Cookie cookie : cookies) {
			LOGGER.debug("Checking cookies for junction name cookie.");
			if (cookie.getName().equals(WebsealHeaderNames.WEBSEAL_JUNCTION_COOKIE)) {
				LOGGER.debug("Found junction cookie:" + cookie.getName() + "=" + cookie.getValue());
				junction = cookie.getValue();
			}
		}
		if (junction.startsWith("%2F")) {
			junction = junction.replaceAll("%2F", "/");
		}
		return junction;
	}

	/**
	 * Gets the StelvioSecurityEaiSessionMap from HttpSession.
	 * 
	 * @param session
	 *            the HttpSession
	 * @return the StelvioSecurityEaiSessionMap if present.
	 */
	protected StelvioSecurityEaiSessionMap getSecurityMap(HttpSession session) {

		Object sessionMap = session.getAttribute(StelvioSecurityEaiSessionMap.SESSIONMAP_KEY);
		StelvioSecurityEaiSessionMap securityMap = null;
		if (sessionMap instanceof StelvioSecurityEaiSessionMap) {
			securityMap = (StelvioSecurityEaiSessionMap) sessionMap;
		}
		return securityMap;

	}

	/**
	 * Adds the ReAuthenticationHeaders to the HttpServletResponse.
	 * 
	 * @param headers
	 *            the ReAuthenticationHeaders to add.
	 */
	public void addHeaders(ReAuthenticationHeaders headers) {

		if (headers != null) {
			Map<String, String> map = headers.getHeaders();

			if (log.isDebugEnabled()) {
				log.debug("Attempting to send eai specific headers. Map size is: " + map.size());
			}

			if (!map.isEmpty()) {

				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Entry entry = (Entry) iter.next();

					if (log.isDebugEnabled()) {
						log.debug("Adding header to response: " + entry.getKey() + " - " + entry.getValue());
					}

					response.setHeader((String) entry.getKey(), (String) entry.getValue());
				}
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Could not send headers as the EaiHeaders are null.");
			}
		}
	}

	/**
	 * Returns true or false depending on if a Webseal junction should be inserted into the url to which the user is sent after
	 * reauthentication.
	 * 
	 * @return true or false
	 */
	public boolean isInsertJunctionInRedirUrl() {
		return insertJunctionInRedirUrl;
	}

	/**
	 * Sets whether or not if a Webseal junction should be inserted into the url to which the user is sent after
	 * reauthentication.
	 * 
	 * @param insertJunctionInRedirUrl
	 *            true or false
	 */
	public void setInsertJunctionInRedirUrl(boolean insertJunctionInRedirUrl) {
		this.insertJunctionInRedirUrl = insertJunctionInRedirUrl;
	}

}
