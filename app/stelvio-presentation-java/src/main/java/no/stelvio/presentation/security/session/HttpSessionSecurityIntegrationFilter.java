package no.stelvio.presentation.security.session;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Servlet filter which checks the current HttpSession for a binding to an autenticated user. If the binding is not present,
 * such a binding will be added. When a binding is incorrect the HttpSession will be invalidated and a new session with the
 * correct binding will be created.
 * 
 * The filter's purpose is to help prevent HttpSession hijacking and should be mapped to all requests in applications that have
 * security requirements.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id: HttpSessionSecurityIntegrationFilter.java $
 */
public class HttpSessionSecurityIntegrationFilter extends OncePerRequestFilter {

	private static final Log LOGGER = LogFactory.getLog(HttpSessionSecurityIntegrationFilter.class);
	private static final String SESSION_USER_ID = "no.stelvio.presentation.security.session.SESSION_USER_ID";

	/**
	 * Validates the HttpSession against an authenticated user. This is done by checking if the userid is present in the
	 * HttpSession and validating this against the authenticated user on the request. If a mismatch is detected the current
	 * session will be invalidated and a new clean session with the correct binding will be created. If the HttpSession has not
	 * been created yet no action will be done.
	 * 
	 * @param req
	 *            the HttpServletRequest
	 * @param res
	 *            the HttpServletResponse
	 * @param chain
	 *            the filterchain
	 * @throws ServletException servlet exception
	 * @throws IOException i/o exception
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering filter to validate session against the authenticated user.");
		}
		HttpSession session = req.getSession(false);

		if (session != null) {
			String userId = req.getRemoteUser();
			if (userId != null) {
				String sessionUser = (String) session.getAttribute(SESSION_USER_ID);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Validating the HttpSession against the authenticated user.");
				}
				if (sessionUser != null) {
					if (sessionUser.equalsIgnoreCase(userId)) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("The HttpSession is valid. No action will be performed.");
						}
					} else {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("The HttpSession is invalid. It does not correspond to the correct user.");
							LOGGER.debug("The loggedin user and the user found in the HttpSession are:" + userId + " - "
									+ sessionUser);
							LOGGER.debug("Invalidate old session and create a new session.");
						}
						session.invalidate();
						HttpSession newSession = req.getSession(true);
						newSession.setAttribute(SESSION_USER_ID, userId);
					}

				} else {

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("The HttpSession has not been marked with the authenticated user yet. Marking session.");
					}
					session.setAttribute(SESSION_USER_ID, userId);
				}
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("The user is not authenticated yet. No validation neccessary.");
				}
			}

		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("The session has not been created yet. No validation will be done.");
			}
		}
		// Delegate processing to the next filter or resource in the chain
		chain.doFilter(req, res);
	}

}
