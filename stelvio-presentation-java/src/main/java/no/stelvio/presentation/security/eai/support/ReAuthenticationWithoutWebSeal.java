package no.stelvio.presentation.security.eai.support;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import no.stelvio.presentation.security.eai.AuthenticationFailureException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A class used to reauthenticate the user to act as another user with a certain authorization type when not in an environment
 * with IBM Tivoli Access Manager Webseal.
 * 
 * @author persondab2f89862d3, Accenture
 * @see ReAuthentication
 */
public class ReAuthenticationWithoutWebSeal extends ReAuthentication {

	private final Log LOGGER = LogFactory.getLog(this.getClass());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void authenticate() {

		super.authenticate();
		HttpSession session = request.getSession();

		StelvioSecurityEaiSessionMap securityMap = getSecurityMap(session);
		String user = request.getRemoteUser();
		Object level = session.getAttribute(WebsealHeaderNames.AUTHENTICATION_LEVEL);

		LOGGER.debug("Attempting to reauthenticate the user " + user + ". The current authentication-level is " + level);

		if (user != null && level != null && securityMap != null) {

			String authorizedAs = (String) securityMap.get(StelvioSecurityEaiSessionMap.Keys.AUTHORIZED_AS.getName());
			String authorizationType = (String) securityMap.get(StelvioSecurityEaiSessionMap.Keys.AUTHORIZATION_TYPE.getName());
			String postAuthUrl = (String) securityMap.get(StelvioSecurityEaiSessionMap.Keys.POST_AUTHENTICATION_URL.getName());

			session.setAttribute(WebsealHeaderNames.AUTHORIZED_AS, authorizedAs);
			session.setAttribute(WebsealHeaderNames.AUTHORIZATION_TYPE, authorizationType);

			LOGGER.debug("The authorizedAs parameter is set to:" + session.getAttribute(WebsealHeaderNames.AUTHORIZED_AS));
			LOGGER.debug("The authorizationType parameter is set to:"
					+ session.getAttribute(WebsealHeaderNames.AUTHORIZATION_TYPE));
			LOGGER.debug("Attempting to clear the Ltpa cookie.");
			Cookie[] cookies = request.getCookies();
			// Cookie ltpaCookie = null;
			for (Cookie cookie : cookies) {
				LOGGER.debug("Cookie: " + cookie.getName());
				if (cookie.getName().startsWith("LtpaToken")) {
					LOGGER.debug("Setting max age on LtpaToken cookie: " + cookie.getName());
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
				}
			}
			try {
				response.sendRedirect(postAuthUrl);
			} catch (IOException e) {
				throw new AuthenticationFailureException("Could not redirect to " + postAuthUrl);
			}

		} else {
			throw new AuthenticationFailureException("Could not process the re-authentication request.");
		}
	}

}
