package no.stelvio.presentation.security.page.definition;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.SecurityContextHolder;
import no.stelvio.presentation.security.page.PageAccessDeniedException;
import no.stelvio.presentation.security.page.PageAuthenticationFailedException;
import no.stelvio.presentation.security.page.PageAuthenticationRequest;
import no.stelvio.presentation.security.page.PageAuthenticationRequiredException;
import no.stelvio.presentation.security.page.PageProtocolSwitchFailedException;
import no.stelvio.presentation.security.page.PageSecurityFileNotFoundException;
import no.stelvio.presentation.security.page.constants.Constants;
import no.stelvio.presentation.security.page.definition.parse.SecurityConfiguration;
import no.stelvio.presentation.security.page.definition.parse.support.JeeRole;
import no.stelvio.presentation.security.page.definition.parse.support.JeeRoles;
import no.stelvio.presentation.security.page.definition.parse.support.JsfApplication;
import no.stelvio.presentation.security.page.definition.parse.support.JsfPage;
import no.stelvio.presentation.security.page.definition.parse.support.SecurityConfigurationXml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.execution.RequestContextHolder;

/**
 * This class reads in and maintains the security definitions from the <code>SecurityConfiguration</code> interface (the
 * concrete implementation <code>SecurityConfigurationXml</code> is set to default if no other source is specified) It
 * provides methods which can handle authorization of these security constraints against JEE-security and methods which can
 * handle a protocol switch for secure channel communication.
 * 
 * @version $Id$
 */
public class JeeSecurityObject {
	private static final Log LOGGER = LogFactory.getLog(JeeSecurityObject.class);

	private SecurityConfiguration securityConfiguration = null;

	private JsfApplication secureJsfApplication = null; // object that holds the

	// secured page
	// definitions

	private boolean isKeepSSLMode = true; // default

	private boolean pageRequiresSSL = false; // default

	private String httpPort = "80"; // default

	private String httpsPort = "443"; // default

	/** Default constructor. */
	public JeeSecurityObject() {
	}

	/**
	 * Optional constructor to set the SecurityConfiguration source.
	 * 
	 * @param config
	 *            the SecurityConfiguration
	 */
	public JeeSecurityObject(SecurityConfiguration config) {
		this.securityConfiguration = config;
	}

	/**
	 * Sets the SecurityConfiguration source, if not set a <code>SecurityConfigurationXml</code> will be set by default.
	 * 
	 * @param config
	 *            the SecurityConfiguration
	 */
	public void setSecurityConfiguration(SecurityConfiguration config) {
		this.securityConfiguration = config;
	}

	/**
	 * Initializes the Security Object by obtaining the security definitions from the <code>SecurityConfiguration</code>
	 * source. If no such source is set a new <code>SecurityConfigurationXml</code> will be created as default.
	 * 
	 * @param request request
	 */
	public void initializeSecurityDefinitions(HttpServletRequest request) {

		// Access faces-security configuration, initially parse or reparse
		if (this.securityConfiguration == null) {
			try {
				ServletContext ctx = request.getSession().getServletContext();
				String config = ctx.getInitParameter(Constants.SECURITY_CONFIG_FILE);
				URL url = ctx.getResource(config);

				if (url != null) {
					this.securityConfiguration = new SecurityConfigurationXml(url);
				} else {
					throw new PageSecurityFileNotFoundException(
							"Could not find the security configuration file corresponding to the context param '"
									+ Constants.SECURITY_CONFIG_FILE + "'");
				}
			} catch (MalformedURLException e) {
				throw new PageSecurityFileNotFoundException("Could not find the security configuration file '"
						+ Constants.SECURITY_CONFIG_FILE + "'", e);
			} catch (NullPointerException e) {
				throw new PageSecurityFileNotFoundException("Could not find the context param '"
						+ Constants.SECURITY_CONFIG_FILE + "'", e);
			}
		}

		if (this.secureJsfApplication == null) {
			this.secureJsfApplication = this.securityConfiguration.getJsfApplication();
			this.isKeepSSLMode = this.secureJsfApplication.isKeepSSL();
			this.httpPort = this.secureJsfApplication.getHttpPort();
			this.httpsPort = this.secureJsfApplication.getHttpsPort();
		}
	}

	/**
	 * Shared method used to determine JSF page authorization requirements and if a user should be allowed to access a page.
	 * Users that are not authenticated will be enforced to authenticate if the page requires authentication.
	 * 
	 * @param flowId
	 *            the requested page
	 * @param session
	 *            the HttpSession
	 * @param request
	 *            the HttpServletRequest
	 * @return <code>true</code> if the user is granted page access.
	 * @throws PageAccessDeniedException
	 *             if access to a page is denied.
	 * @throws PageAuthenticationFailedException
	 *             if the authentication process fails, i.e. cannot redirection fails.
	 */
	public boolean authorizePageAccess(String flowId, HttpSession session, HttpServletRequest request)
			throws PageAccessDeniedException, PageAuthenticationFailedException {

		JsfPage page = getPageObject(flowId);

		/*
		 * if SSL mode should be kept after first time the application switches to SSL then SSL communication is enforced an all
		 * subsequent pages, independently of their requirement for secure connections
		 */
		this.pageRequiresSSL = page.requiresSSL();

		if (page.requiresAuthorization() || page.requiresAuthentication()) {

			if (isAuthenticated()) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("User is authenticated to page with flowId: " + flowId);
				}

				if (page.requiresAuthorization()) {
					if (checkUserAuthorization(page)) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("User is authorized to access page with flowId: " + flowId);
						}
						return true;
					} else {
						throw new PageAccessDeniedException(page, "Access denied (not authorized) to view page with flowId: "
								+ flowId);
					}
				} else {
					return true;
				}
			} else {
				throw new PageAuthenticationRequiredException(getAuthenticationRequest(request, flowId),
						"Not authenticated to view page with flow id: " + flowId);
			}
		} else {
			/* all pages that have no authentication or authorization requirement are public */
			return true;
		}
	}

	/**
	 * Get authentication request.
	 * 
	 * @param request request
	 * @param flowId flow id
	 * @return request
	 */
	private PageAuthenticationRequest getAuthenticationRequest(HttpServletRequest request, String flowId) {

		PageAuthenticationRequest authRequest = new PageAuthenticationRequest("/jsfauthentication");

		String params = request.getQueryString() != null ? "?" + request.getQueryString() : "";

		String originPageURI = request.getContextPath() + params;

		Map map = request.getParameterMap();

		authRequest.setJsfViewId(flowId);
		authRequest.setUrlUponSuccessfulAuthentication(originPageURI);
		authRequest.setOriginalPageParameterMap(map);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("User must be authenticated. Return URL upon successful authentication is " + originPageURI);
		}

		return authRequest;
	}

	/**
	 * Is authenticated.
	 * 
	 * @return true if authenticated
	 */
	private boolean isAuthenticated() {
		return (SecurityContextHolder.currentSecurityContext().getUserId() != null);
	}

	/**
	 * Determines the security constraints like, authentication, authorization and SSL requirements for a viewId. This object
	 * also knows about the required J2EE security roles for page access and whether the role evaluation should be AND or OR.
	 * All security information and the page ViewId are exposed as methods.
	 * 
	 * @param viewId
	 *            the viewId
	 * @return object that contains all the security constraints of a ViewId
	 */
	public JsfPage getPageObject(String viewId) {
		// by default pages don't require SSL
		this.pageRequiresSSL = false;
		// try and find the requested page in the security settings
		JsfPage page = this.secureJsfApplication.findJsfPageDefinition(viewId);

		if (page == null) {
			String[] wildcardAuthorization = null;

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Performing wildcard search for security configuration of " + viewId);
			}

			// if we cannot find an exact match, look for wildcard matches
			StringTokenizer st = new StringTokenizer(viewId, "/");
			int tokencount = st.countTokens();

			wildcardAuthorization = new String[tokencount];

			// initialize array
			for (int i = 0; i < tokencount; i++) {
				wildcardAuthorization[i] = "";
			}

			// array is created in the form
			// a[3] = /path1/path2/path3
			// a[2] = /path1/path2
			// a[1] = /path1
			// a[0] = ""
			int loops = 0;

			while (st.hasMoreTokens()) {
				String s = st.nextToken();
				// start with [1] so that [0] can be used for root wildcard '/*'
				for (int i = 1 + loops; i < tokencount; i++) {
					wildcardAuthorization[i] = wildcardAuthorization[i] + "/" + s;
				}
				++loops;
			}

			// add wildcard for root
			wildcardAuthorization[0] = "";

			for (int i = wildcardAuthorization.length - 1; i > -1; i--) {
				String protectedViews = wildcardAuthorization[i] + "/*";

				page = this.secureJsfApplication.findJsfPageDefinition(protectedViews);
				if (page != null) {

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Wildcard search, found security configuration for " + protectedViews);
					}

					break;
				}
			}
		}

		// JSF Pages that are not explicitly mentioned in the faces security
		// configurationor or contained in a wildcard match are considered
		// publicly accessible
		if (page == null) {
			page = new JsfPage();
			page.setRequiresAuthentication(false);
			page.setRequiresAuthorization(false);
		}

		// set page name in case wildcard search was used
		page.setPageName(viewId);
		return page;
	}

	/**
	 * Determines if the authenticated user is member of the required J2EE roles. The J2EE roles can be ORed or ANDed together.
	 * The normal J2EE container managed security uses OR. AND requires the user to be a member of all the listed roles to be
	 * authorized for page access
	 * 
	 * @param page
	 *            the JsfPage object containing the security constraints for one page
	 * @return <code>true</code> if the user is authorized to view the page, false otherwise.
	 */
	private boolean checkUserAuthorization(JsfPage page) {

		boolean authorized = false;
		List<JeeRoles> roleSets = page.getRoleSets();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Checking user authorization for the page '" + page.getPageName() + "' with the following rolesets"
					+ roleSets);
		}
		for (JeeRoles roles : roleSets) {

			JeeRoles j2eeRoles = roles;
			Iterator<JeeRole> rolesIterator = j2eeRoles.getRoles() != null ? j2eeRoles.getRoles().iterator() : null;

			boolean concatenateRolesWithOR = !Constants.J2EE_ROLE_CONCATINATION_AND.equals(j2eeRoles.getRoleConcatenationType());
			SecurityContext context = SecurityContextHolder.currentSecurityContext();

			while (rolesIterator != null && rolesIterator.hasNext()) {
				String roleName = rolesIterator.next().getRole();
				authorized = context.isUserInRole(roleName);
				if (authorized && concatenateRolesWithOR) {
					break;
				}
				if (!authorized && !concatenateRolesWithOR) {
					break;
				}
			}

			if (authorized) {
				break;
			}
		}
		return authorized;
	}

	/**
	 * Determines if the requested page requires SSL and if the current protocol meets this need. If not the protocol is
	 * switched between http and https
	 * 
	 * @param viewId
	 *            id of the requested page
	 * @param pageRequiresSSL
	 *            true if the page requires SLL
	 * @throws PageProtocolSwitchFailedException
	 *             if redirecting from one protocol to the other fails.
	 */
	public void handleProtocolSwitch(String viewId, boolean pageRequiresSSL) throws PageProtocolSwitchFailedException {

		String toHttpsMsg = "Redirection to secure channel for page '{0}' with HTTPS port '{1}' failed.";
		String toHttpMsg = "Redirection to normal channel for page '{0}' with HTTP port '{1}' failed.";

		ExternalContext exctx = ExternalContextHolder.getExternalContext();
		boolean isSecureSSLChannel = ((HttpServletRequest) exctx.getNativeRequest()).isSecure();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Page requires SSL = " + this.pageRequiresSSL + ", channel is secure = " + isSecureSSLChannel
					+ ", is keep SSL = " + this.isKeepSSLMode);
		}

		// page requires SSL and SSL is not active. Switch to SSL.
		if (pageRequiresSSL && !isSecureSSLChannel) {
			try {
				switchToHttps(viewId);
			} catch (IOException io) {
				throw new PageProtocolSwitchFailedException(pageRequiresSSL ? toHttpsMsg : toHttpMsg + " ViewId '" + viewId
						+ "', Port '" + this.httpPort, io);
			}
		}

		// switch to HTTP if page doesn't require SSL and channel isn't secure
		// and isKeepSSLMode is false
		if (!pageRequiresSSL && !this.isKeepSSLMode && isSecureSSLChannel) {
			try {
				switchToHttp(viewId);
			} catch (IOException io) {
				throw new PageProtocolSwitchFailedException(pageRequiresSSL ? toHttpsMsg : toHttpMsg + " ViewId '" + viewId
						+ "', Port '" + this.httpPort, io);
			}
		}
	}

	/**
	 * Switches from https to http using a redirect call.
	 * 
	 * @param viewId
	 *            id of the requested page
	 * @throws IOException
	 *             if redirection fails.
	 */
	private void switchToHttp(String viewId) throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exctx = RequestContextHolder.getRequestContext().getExternalContext();
		ViewHandler vh = facesContext.getApplication().getViewHandler();

		String pageURI = vh.getActionURL(FacesContext.getCurrentInstance(), viewId);
		String remoteHost = getHostNameFromRequest();
		String port = this.httpPort.equalsIgnoreCase("80") ? "" : ":" + this.httpPort;
		String url = "http://" + remoteHost + port + pageURI;

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Redirecting to http URL " + url);
		}

		exctx.requestExternalRedirect(url);
	}

	/**
	 * switches to https using a redirect call.
	 * 
	 * @param viewId
	 *            id of the requested page
	 * @throws IOException
	 *             if redirection fails.
	 */
	private void switchToHttps(String viewId) throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exctx = ExternalContextHolder.getExternalContext();
		ViewHandler vh = facesContext.getApplication().getViewHandler();

		String pageURI = vh.getActionURL(FacesContext.getCurrentInstance(), viewId);
		String remoteHost = getHostNameFromRequest();
		String port = this.httpsPort.equalsIgnoreCase("443") ? "" : ":" + this.httpsPort;
		String url = "https://" + remoteHost + port + pageURI;

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Redirecting to https URL " + url);
		}

		exctx.requestExternalRedirect(url);
	}

	/**
	 * Gets the host name from the current request.
	 * 
	 * @return the hostname of the page request
	 * @throws MalformedURLException
	 *             if getRequestURL is malformed
	 */
	private String getHostNameFromRequest() throws MalformedURLException {
		ExternalContext exctx = ExternalContextHolder.getExternalContext();
		String requestUrlString = ((HttpServletRequest) exctx.getNativeRequest()).getRequestURL().toString();
		URL requestUrl = null;
		requestUrl = new URL(requestUrlString);
		String remoteHost = requestUrl.getHost();

		return remoteHost;
	}
}
