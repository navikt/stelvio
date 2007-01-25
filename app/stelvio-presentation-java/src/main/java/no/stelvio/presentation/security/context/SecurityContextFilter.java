package no.stelvio.presentation.security.context;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.SecurityContextHolder;
import no.stelvio.common.security.support.SimpleSecurityContext;
import no.stelvio.presentation.security.context.parse.SecurityRole;
import no.stelvio.presentation.security.context.parse.WebAppRoles;
import no.stelvio.presentation.security.context.parse.WebXmlParser;
import no.stelvio.presentation.security.page.constants.Constants;

/**
 * Servlet-filter responsible for populating a <code>SecurityContext</code> and place it
 * in a <code>SecurityContextHolder</code> on the current thread. This includes:
 * 
 * <li> Reading in all the security roles from web.xml and comparing these with the roles of the logged-in user. 
 * These are then stored in the HttpSession and put on the context.</li>
 * <li>Retrieving the user id of the logged-in user, place it in the HttpSession and populate the context.</li>
 * <br>
 * @author persondab2f89862d3, Accenture
 * @version $Id: SecurityContextFilter.java $ 
 */ 
public class SecurityContextFilter extends OncePerRequestFilter {

	private static final String SECURITY_CONTEXT = SecurityContext.class.getName();
	private static final String USER_ID = "USER_ID";
	private static final String USER_ROLES = "USER_ROLES";

	protected static final Log logger = LogFactory.getLog(SecurityContextFilter.class);
	protected static final String REDIRECT_TO_AFTER_LOGIN = "";

	private List<SecurityRole> roller;
	
	/**  
	 * Initializes the filter by reading in and parsing the security role elements
	 * from web.xml using the <code>WebXmlParser</code>.
	 */
	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();    //To change body of overridden methods use File | Settings | File Templates.

		try {
			URL url = getFilterConfig().getServletContext().getResource("/WEB-INF/web.xml");
			WebXmlParser parser = new WebXmlParser(url);
			WebAppRoles roles = parser.getWebAppRoles();
			roller = roles.getSecurityRoles();

			if (logger.isDebugEnabled()) {
				Iterator iterator = roles.getSecurityRolesIterator();

				while (iterator.hasNext()) {
					SecurityRole element = (SecurityRole) iterator.next();
					logger.debug("Get role from web.xml <security-role> element: " + element.getRoleName());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("An error occured while initializing the filter: "
					+ "Could not parse and retrieve the security roles from web.xml.", e);
		}
	}

	
	/** {@inheritDoc} */
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		try {
			// Only import the security context from session if both the session
			// and a persisted context exists.
			HttpSession session = req.getSession(false);

			if (null != session) {
				Object context = session.getAttribute(SECURITY_CONTEXT);
				if (null != context) {
					SecurityContextHolder.setSecurityContext((SecurityContext) context);
				} else {
					if (logger.isInfoEnabled()) {
						logger.info("Session exists, but SecurityContext was not persisted");
					}
				}
			}


			SecurityContext securityContext = populateSecurityContext(req);
			SecurityContextHolder.setSecurityContext(securityContext);

			if (logger.isDebugEnabled()) {
				logger.debug("User on request:" + req.getRemoteUser());
				logger.debug("User on SecurityContext:" + SecurityContextHolder.currentSecurityContext().getUserId());
			}

			// Delegate processing to the next filter or resource in the chain
			chain.doFilter(req, res);

			// Session might have bean constructed, deleted or invalidated during
			// processing further down the chain, so check again
			session = req.getSession(false);

			if (null != session) {
				try {
					session.setAttribute(SECURITY_CONTEXT, SecurityContextHolder.currentSecurityContext());
				} catch (IllegalStateException ise) {
					if (logger.isInfoEnabled()) {
						logger.info("Session was invalidated, could not persist SecurityContext", ise);
					}
				}
			}
		} catch (Exception e) {
			throw new ServletException("An error occured while updating the SecurityContext", e);
		} finally {
			// Always reset the SecurityContext, just to be on the safe side
			SecurityContextHolder.resetSecurityContext();
		}
	}
	/**
	 * Private helper method used to retrieve and set values from the HttpSession and finally create and
	 * return a new <code>SecurityContext</code> with these values.
	 * @param req the HttpServletRequest
	 * @return a poulated <code>SecurityContext</code>.
	 */
	private SecurityContext populateSecurityContext(HttpServletRequest req) {
		HttpSession session = req.getSession();
		SecurityContext securityContext;

		if (req.getRemoteUser() != null) {
			if (session.getAttribute(USER_ID) == null) {
				session.setAttribute(USER_ID, req.getRemoteUser());
			} else {
				String user = (String) session.getAttribute(USER_ID);

				if (!user.equals(req.getRemoteUser())) {
					session.setAttribute(USER_ID, req.getRemoteUser());
				}
			}

			//legger roller på sesjonen hvis de ikke ligger der fra før
			if (session.getAttribute(USER_ROLES) == null || ((List) session.getAttribute(USER_ROLES)).size() == 0) {
				addRoles(session, req);

				if (checkForURLManipulation(session)) {
					redirectAfterLogin(session);
				}
			}
		}

		securityContext = new SimpleSecurityContext((String) session.getAttribute(USER_ID),
				(List<String>) session.getAttribute(USER_ROLES));

		if (logger.isDebugEnabled()) {
			logger.debug("Roller i sesjonen: " + session.getAttribute(USER_ROLES));
		}

		return securityContext;
	}

	/**
	 * Makes sure that user is redirected to the start flow after login.
	 *
	 * @param session the HttpSession with attribute describing where to redirect to after authentication
	 */
	private void redirectAfterLogin(HttpSession session) {
		if (session != null) {
			session.setAttribute(Constants.JSFPAGE_TOGO_AFTER_AUTHENTICATION, REDIRECT_TO_AFTER_LOGIN);
		}
	}

	/**
	 * Checks if the user has tried to access another address than the context root before logging in.
	 *
	 * @param session the HttpSession with attribute describing where to redirect to after authentication
	 * @return <code>true</code> if the URL has been manipulated, <code>false</code> otherwise.
	 */
	private boolean checkForURLManipulation(HttpSession session) {
		return session != null && (session.getAttribute(Constants.JSFPAGE_TOGO_AFTER_AUTHENTICATION) != null);
	}

	/**
	 * Adds the roles of the user to the session. The list with the sum of existing roles is set in web.xml.
	 *
	 * @param session the HttpSession
	 * @param httpreq the HttpServletRequest to be processed
	 */
	private void addRoles(HttpSession session, HttpServletRequest httpreq) {
		List<String> rolleList = new ArrayList<String>();

		for (SecurityRole securityRole : roller) {
			String roleName = securityRole.getRoleName();

			if (httpreq.isUserInRole(roleName)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Adding role: " + roleName);
				}

				rolleList.add(roleName);
			}
		}

		session.setAttribute(USER_ROLES, rolleList);
	}
}