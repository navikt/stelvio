package no.stelvio.web.security.page.util;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Iterator;
import java.util.StringTokenizer;

import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.web.security.page.PageAccessDeniedException;
import no.stelvio.web.security.page.PageAuthenticationFailedException;
import no.stelvio.web.security.page.PageProtocolSwitchFailedException;
import no.stelvio.web.security.page.PageSecurityFileNotFoundException;
import no.stelvio.web.security.page.constants.Constants;
import no.stelvio.web.security.page.parse.J2EERole;
import no.stelvio.web.security.page.parse.J2EERoles;
import no.stelvio.web.security.page.parse.JSFApplication;
import no.stelvio.web.security.page.parse.JSFPage;
import no.stelvio.web.security.page.parse.SecurityConfiguration;
import no.stelvio.web.security.page.parse.SecurityConfigurationXML;

/**
 * This class reads in and maintains the security definitions from the
 * <code>SecurityConfiguration</code> interface (the concrete implementation
 * <code>SecurityConfigurationXML</code> is set to default if no other source
 * is specified) It provides methods which can handle authorization of these
 * security constraints against JEE-security and methods which can handle a
 * protocol switch for secure channel communication.
 * 
 * @author persondab2f89862d3
 * @version $Id$
 */
public class J2eeSecurityObject {
	private static final Log log = LogFactory.getLog(J2eeSecurityObject.class);

	private SecurityConfiguration securityConfiguration = null;

	private JSFApplication secureJsfApplication = null; // object that holds the

	// secured page
	// definitions

	private boolean isKeepSSLMode = false; // default

	private boolean pageRequiresSSL = false; // default

	private String httpPort = "80"; // default

	private String httpsPort = "443"; // default

	/** Default constructor. */
	public J2eeSecurityObject() {
	}

	/**
	 * Optional constructor to set the SecurityConfiguration source.
	 * 
	 * @param config
	 *            the SecurityConfiguration
	 */
	public J2eeSecurityObject(SecurityConfiguration config) {
		this.securityConfiguration = config;
	}

	/**
	 * Sets the SecurityConfiguration source, if not set a
	 * <code>SecurityConfigurationXML</code> will be set by default.
	 * 
	 * @param config
	 *            the SecurityConfiguration
	 */
	public void setSecurityConfiguration(SecurityConfiguration config) {
		this.securityConfiguration = config;
	}

	/**
	 * Initializes the Security Object by obtaining the security definitions
	 * from the <code>SecurityConfiguration</code> source. If no such source
	 * is set a new <code>SecurityConfigurationXML</code> will be created as
	 * default.
	 * 
	 * @throws PageSecurityFileNotFoundException
	 *             if no configuration file is found when using the default
	 *             SecurityConfiguration <code>SecurityConfigurationXML</code>.
	 * 
	 */
	public void initializeSecurityDefinitions() {

		ExternalContext exctx = FacesContext.getCurrentInstance()
				.getExternalContext();
		// Access faces-security configuration, initially parse or reparse
		if (this.securityConfiguration == null) {
			String configFile = exctx
					.getInitParameter(Constants.SECURITY_CONFIG_FILE);
			try {
				URL url = exctx.getResource(configFile);
				this.securityConfiguration = new SecurityConfigurationXML(url);
			} catch (MalformedURLException e) {
				throw new PageSecurityFileNotFoundException(e, configFile);
			}
		}
		if (this.secureJsfApplication == null) {

			this.secureJsfApplication = this.securityConfiguration
					.getJsfApplication();
			this.isKeepSSLMode = this.secureJsfApplication.isKeepSSL();
			this.httpPort = this.secureJsfApplication.getHttpPort();
			this.httpsPort = this.secureJsfApplication.getHttpsPort();
		}
	}

	/**
	 * Shared method used to determine JSF page authorization requirements and
	 * if a user should be allowed to access a page. Users that are not
	 * authenticated will be enforced to authenticate if the page requires
	 * authentication.
	 * 
	 * @param viewId
	 *            the requested page
	 * @param session
	 *            the HttpSession
	 * @return <code>true</code> if the user is granted page access.
	 * @throws PageAccessDeniedException
	 *             if access to a page is denied.
	 * @throws PageAuthenticationFailedException
	 *             if the authentication process fails, i.e. cannot redirection
	 *             fails.
	 */
	public boolean authorizePageAccess(String viewId, HttpSession session)
			throws PageAccessDeniedException, PageAuthenticationFailedException {

		boolean pageRequiresAuthorization = true;
		boolean pageRequiresAuthentication = true;

		JSFPage page = getPageObject(viewId);
		pageRequiresAuthorization = page.requiresAuthorization();
		pageRequiresAuthentication = pageRequiresAuthorization
				|| page.requiresAuthentication();
		// if SSL mode should be kept after first time the application switches
		// to SSL
		// then SSL communication is enforced an all subsequent pages,
		// independently of
		// their requirement for secure connections
		this.pageRequiresSSL = page.requiresSSL();

		if (pageRequiresAuthentication) {

			boolean authenticated = false;
			try {
				authenticated = authenticateRequest(viewId, session);
			} catch (Exception e) {
				throw new PageAuthenticationFailedException(e, viewId, page);
			}
			if (log.isDebugEnabled()) {
				log.debug("User is authenticated in authorization of " + viewId
						+ " : " + authenticated + "");
			}
			if (authenticated && pageRequiresAuthorization) {

				boolean userIsAuthorized = pageRequiresAuthorization ? checkUserAuthorization(page)
						: true;
				if (log.isDebugEnabled()) {
					log.debug("User is authorized to access page " + viewId
							+ " : " + userIsAuthorized);
				}
				if (userIsAuthorized) {
					return userIsAuthorized;
				} else {
					throw new PageAccessDeniedException(viewId, page);
				}
			}
			return authenticated;
		}
		// all pages that have no authentication or authorization requirement
		// are public
		return true;
	}

	/**
	 * Determines if the user is authenticated and if not, redirects him to the
	 * authentication servlet. The authentication servlet performs container
	 * managed authentication and redirects the request back to the viewId it
	 * was contacted by.
	 * 
	 * @param viewId
	 *            the viewId of the requested page
	 * @param session
	 *            the HttpSession used to store the viewId of the requested
	 *            page.
	 * @return <code>true</code> if the user is authenticated
	 * @throws IOException
	 *             if the redirection fails.
	 */
	private boolean authenticateRequest(String viewId, HttpSession session)
			throws IOException {

		ExternalContext exctx = FacesContext.getCurrentInstance()
				.getExternalContext();
		String usr = exctx.getRemoteUser();

		if (usr == null) {

			ViewHandler vh = FacesContext.getCurrentInstance().getApplication()
					.getViewHandler();
			String params = ((HttpServletRequest) exctx.getRequest())
					.getQueryString() != null ? "?"
					+ ((HttpServletRequest) exctx.getRequest())
							.getQueryString() : "";
			String originPageURI = vh.getActionURL(FacesContext
					.getCurrentInstance(), viewId)
					+ params;

			if (log.isDebugEnabled()) {
				log
						.debug("User must be authenticated. Return URL upon successful authentication is "
								+ originPageURI);
			}
			session.setAttribute(Constants.JSFPAGE_TOGO_AFTER_AUTHENTICATION,
					originPageURI);

			String j2eeContextPath = ((HttpServletRequest) exctx.getRequest())
					.getContextPath();

			// if page requires SSL and authentication, the J2EE logon is
			// requested using
			// https as well
			if (this.pageRequiresSSL
					&& !((HttpServletRequest) exctx.getRequest()).isSecure()) {
				// suppress port mentioning if default port 443
				String port = this.httpsPort.equalsIgnoreCase("443") ? ""
						: this.httpsPort;
				String authenticationEnforcerURL = "https://"
						+ getHostNameFromRequest() + ":" + port
						+ j2eeContextPath + "/jsfauthentication";
				if (log.isDebugEnabled()) {
					log.debug("Authentication Servlet request URL is "
							+ authenticationEnforcerURL);
				}
				exctx.redirect(authenticationEnforcerURL);
			} else {
				// page does not require https or is in https mode already
				exctx.redirect(j2eeContextPath + "/jsfauthentication");
			}
		}
		return true;
	}

	/**
	 * Determines the security constraints like, authentication, authorization
	 * and SSL requirements for a viewId. This object also knows about the
	 * required J2EE security roles for page access and whether the role
	 * evaluation should be AND or OR. All security information and the page
	 * ViewId are exposed as methods.
	 * 
	 * @param viewId
	 *            the viewId
	 * @return object that contains all the security constraints of a ViewId
	 */
	public JSFPage getPageObject(String viewId) {
		// by default pages don't require SSL
		this.pageRequiresSSL = false;
		// try and find the requested page in the security settings
		JSFPage page = this.secureJsfApplication.findJsfPageDefinition(viewId);

		if (page == null) {
			String[] wildcardAuthorization = null;
			if (log.isDebugEnabled()) {
				log
						.debug("Performing wildcard search for security configuration of "
								+ viewId);
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
					wildcardAuthorization[i] = wildcardAuthorization[i] + "/"
							+ s;
				}
				++loops;
			}
			// add wildcard for root
			wildcardAuthorization[0] = "";

			for (int i = wildcardAuthorization.length - 1; i > -1; i--) {
				String protectedViews = wildcardAuthorization[i] + "/*";
				page = this.secureJsfApplication
						.findJsfPageDefinition(protectedViews);
				if (page != null) {
					if (log.isDebugEnabled()) {
						log
								.debug("Wildcard search, found security configuration for "
										+ protectedViews);
					}
					break;
				}
			}
		}
		// JSF Pages that are not explicitly mentioned in the faces security
		// configurationor or contained in a wildcard match are considered
		// publicly accessible
		if (page == null) {
			page = new JSFPage();
			page.setRequiresAuthentication(false);
			page.setRequiresAuthorization(false);
		}
		// set page name in case wildcard search was used
		page.setPageName(viewId);
		return page;
	}
	
	/**
	 * Determines if the authenticated user is member of the required J2EE
	 * roles. The J2EE roles can be ORed or ANDed together. The normal J2EE
	 * container managed security uses OR. AND requires the user to be a member
	 * of all the listed roles to be authorized for page access
	 * 
	 * @param page
	 *            the JSFPage object containing the security constraints for one
	 *            page
	 * @return <code>true</code> if the user is authorized to view the page,
	 *         false otherwise.
	 */
	private boolean checkUserAuthorization(JSFPage page) {

		ExternalContext exctx = FacesContext.getCurrentInstance()
				.getExternalContext();
		J2EERoles j2eeRoles = page.getRoles();
		Iterator<J2EERole> rolesIterator = j2eeRoles.getRoles() != null ? j2eeRoles
				.getRoles().iterator()
				: null;

		boolean authorized = false;
		boolean concatenateRolesWithOR = j2eeRoles.getRoleConcatenationType() != Constants.J2EE_ROLE_CONCATINATION_AND ? true
				: false;

		while (rolesIterator != null && rolesIterator.hasNext()) {
			String roleName = rolesIterator.next().getRole();
			authorized = exctx.isUserInRole(roleName);
			if (authorized && concatenateRolesWithOR) {
				break;
			}
			if (!authorized && !concatenateRolesWithOR) {
				break;
			}
		}
		return authorized;
	}

	/**
	 * Determines if the requested page requires SSL and if the current protocol
	 * meets this need. If not the protocol is switched between http and https
	 * 
	 * @param viewId
	 *            id of the requested page
	 * @param pageRequiresSSL
	 *            true if the page requires SLL
	 * @throws PageProtocolSwitchFailedException
	 *             if redirecting from one protocol to the other fails.
	 */
	public void handleProtocolSwitch(String viewId, boolean pageRequiresSSL)
			throws PageProtocolSwitchFailedException {

		ExternalContext exctx = FacesContext.getCurrentInstance()
				.getExternalContext();
		boolean isSecureSSLChannel = ((HttpServletRequest) exctx.getRequest())
				.isSecure();

		if (log.isDebugEnabled()) {
			log.debug("Page requires SSL = " + pageRequiresSSL
					+ ", channel is secure = " + isSecureSSLChannel
					+ ", is keep SSL = " + this.isKeepSSLMode);
		}
		// page requires SSL and SSL is not active. Switch to SSL.
		if (pageRequiresSSL && !isSecureSSLChannel) {

			try {
				switchToHttps(viewId);
			} catch (IOException io) {
				throw new PageProtocolSwitchFailedException(io, viewId,
						pageRequiresSSL, this.httpsPort);
			}
		}
		// switch to HTTP if page doesn't require SSL and channel isn't secure
		// and isKeepSSLMode is false
		if (!pageRequiresSSL && !this.isKeepSSLMode && isSecureSSLChannel) {

			try {
				switchToHttp(viewId);
			} catch (IOException io) {
				throw new PageProtocolSwitchFailedException(io, viewId,
						pageRequiresSSL, this.httpsPort);
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
		ExternalContext exctx = facesContext.getExternalContext();
		ViewHandler vh = facesContext.getApplication().getViewHandler();

		String pageURI = vh.getActionURL(FacesContext.getCurrentInstance(),
				viewId);
		String remoteHost = getHostNameFromRequest();
		String port = this.httpPort.equalsIgnoreCase("80") ? "" : ":"
				+ this.httpPort;
		String url = "http://" + remoteHost + port + pageURI;

		if (log.isDebugEnabled()) {
			log.debug("Redirecting to http URL " + url);
		}
		exctx.redirect(url);
	}

	/**
	 * switches to https using a redirect call
	 * 
	 * @param viewId
	 *            id of the requested page
	 * @throws IOException
	 *            if redirection fails.
	 */
	private void switchToHttps(String viewId) throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exctx = facesContext.getExternalContext();
		ViewHandler vh = facesContext.getApplication().getViewHandler();

		String pageURI = vh.getActionURL(FacesContext.getCurrentInstance(),
				viewId);
		String remoteHost = getHostNameFromRequest();
		String port = this.httpsPort.equalsIgnoreCase("443") ? "" : ":"
				+ this.httpsPort;
		String url = "https://" + remoteHost + port + pageURI;

		if (log.isDebugEnabled()) {
			log.debug("Redirecting to https URL " + url);
		}
		exctx.redirect(url);
	}

	/**
	 * Gets the host name from the current request.
	 * 
	 * @return the hostname of the page request
	 * @throws MalformedURLException
	 *             if getRequestURL is malformed
	 */
	private String getHostNameFromRequest() throws MalformedURLException {
		ExternalContext exctx = FacesContext.getCurrentInstance()
				.getExternalContext();
		String requestUrlString = ((HttpServletRequest) exctx.getRequest())
				.getRequestURL().toString();
		URL requestUrl = null;
		requestUrl = new URL(requestUrlString);
		String remoteHost = requestUrl.getHost();
		return remoteHost;
	}

}
