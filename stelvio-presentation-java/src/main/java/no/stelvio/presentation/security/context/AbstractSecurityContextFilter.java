package no.stelvio.presentation.security.context;

import static no.stelvio.common.util.Internal.cast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.xml.sax.SAXException;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.SecurityContextHolder;
import no.stelvio.common.security.support.SecurityContextSetter;
import no.stelvio.common.security.validation.RoleValidator;
import no.stelvio.common.security.validation.support.RoleValidatorUtil;
import no.stelvio.presentation.security.context.parse.SecurityRole;
import no.stelvio.presentation.security.context.parse.WebAppRoles;
import no.stelvio.presentation.security.context.parse.WebXmlParser;
import no.stelvio.presentation.security.page.constants.Constants;

/**
 * Servlet-filter responsible for populating a <code>SecurityContext</code> and place it in a
 * <code>SecurityContextHolder</code> on the current thread. This includes:
 * 
 * <ul>
 * <li>Reading in all the security roles from web.xml and comparing these with the roles of the logged-in user and finally put
 * on the securitycontext.</li>
 * <li>Retrieving the user id of the logged-in user and populate the context.</li>
 * <li>Optionally inject a RoleValidator into the SecurityContext which can validate the roles sent in to rolechecks against a
 * ValidRole enumeration implementation.</li>
 * </ul>
 * 
 * The following optional initparameters are available to this filter:
 * <ul>
 * <li><b>no.stelvio.presentation.security.START_PAGE</b> - Specifies the page to go to after login. Value: relative path to
 * page. </li>
 * <li><b>no.stelvio.presentation.security.ALLOW_URL_MANIPULATION</b> - Specifies wheter or not the users should be allowed to
 * go to the requested page suggested by the URL after they have logged in (If this page is protected). Value: true or false</li>
 * <li><b>no.stelvio.presentation.security.context.VALID_ROLES_ENUM</b> - Specifies that the rolenames used in rolechecks
 * should be evaluated against the enumeration represented by this value (The enumeration must be defined by its fully qualified
 * class name and it must be a ValidRole implementation). The enumaration will be used to create a RoleValidator that will be
 * set into the SecurityContext. Value: fully qualified classname to a ValidRole enumeration.</li>
 * </ul>
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id: AbstractSecurityContextFilter.java $
 * @see SecurityContext
 * @see RoleValidator
 * @see no.stelvio.common.security.validation.ValidRole
 */
abstract class AbstractSecurityContextFilter extends OncePerRequestFilter {
	private static final Log LOGGER = LogFactory.getLog(AbstractSecurityContextFilter.class);

	// Session constants
	private static final String REQUEST_CONTEXT = RequestContext.class.getName();

	// Init parameter constants
	private static final String VALID_ROLES_ENUM = "no.stelvio.presentation.security.context.VALID_ROLES_ENUM";

	private static final String START_PAGE_AFTER_LOGIN = "no.stelvio.presentation.security.START_PAGE";

	/** Validator. */
	protected RoleValidator validator;

	private String startPage;

	private List<SecurityRole> securityRoles;

	/**
	 * Gets the optional init parameters for this filter:
	 * 
	 * <ul>
	 * <li>START_PAGE_AFTER_LOGIN - Specifies the page to go to after login. (Relative path)</li>
	 * <li>ALLOW_URL_MANIPULATION - Specifies wheter or not the users should be allowed to go to the requested page suggested
	 * by the URL after they have logged in (If this page is protected).</li>
	 * <li>VALID_ROLES_ENUM - Specifies that the rolenames used in rolechecks should be evaluated against the enumeration
	 * represented by this value (The enumeration must be defined by its fully qualified class name and it must be a ValidRole
	 * implementation). The enumaration will be used to create a RoleValidator that will be set into the SecurityContext.</li>
	 * </ul>
	 * 
	 * @throws ClassNotFoundException
	 *             if the value of the VALID_ROLES_ENUM parameter does not represent a valid class.
	 */
	private void setupInitParameters() throws ClassNotFoundException {
		if (getFilterConfig() != null) {
			startPage = getFilterConfig().getInitParameter(START_PAGE_AFTER_LOGIN);

			// Initialize and set the validator
			String enumName = getFilterConfig().getInitParameter(VALID_ROLES_ENUM);

			if (enumName != null) {
				Class<Enum> enumClass = cast(Class.forName(enumName));
				validator = RoleValidatorUtil.createValidatorFromEnum(enumClass);
			}
		}
	}

	/**
	 * Reads in and parses the security role elements from <code>web.xml</code> using the <code>WebXmlParser</code>.
	 * 
	 * @throws MalformedURLException
	 *             if the URL to web.xml is invalid.
	 * @throws SAXException
	 *             if parsing of the xml file fails.
	 * @throws IOException
	 *             if an input/output error occurs.
	 */
	private void setupRolesFromWebXml() throws MalformedURLException, SAXException, IOException {
		URL url = getFilterConfig().getServletContext().getResource("/WEB-INF/web.xml");
		WebXmlParser parser = new WebXmlParser(url);
		WebAppRoles roles = parser.getWebAppRoles();
		securityRoles = roles.getSecurityRoles();

		if (LOGGER.isDebugEnabled()) {
			Iterator<SecurityRole> iterator = roles.getSecurityRolesIterator();
			LOGGER.debug("Retrieved roles from web.xml <security-role> element: [" + StringUtils.join(iterator, ',') + "]");
		}
	}

	/**
	 * Initializes the filter by reading in and parsing the security role elements from web.xml using the
	 * <code>WebXmlParser</code>.
	 * 
	 * @throws ServletException
	 *             if exceptions occurs while initializing the filter
	 */
	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();

		try {
			setupInitParameters();
			setupRolesFromWebXml();
		} catch (Exception e) {
			throw new RuntimeException("An error occured while initializing the filter: "
					+ "Could not parse and retrieve the security roles from web.xml.", e);
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		try {

			SecurityContext context = populateSecurityContext(req);
			SecurityContextSetter.setSecurityContext(context);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("User on request:" + req.getRemoteUser());

				if (SecurityContextHolder.currentSecurityContext() != null) {
					LOGGER.debug("User on SecurityContext:" + SecurityContextHolder.currentSecurityContext().getUserId());
				}
			}

			// UserId must be passed through tiers on request context. Only SecurityContextFilter may set it
			setUserIdOnRequestContext(req, SecurityContextHolder.currentSecurityContext().getUserId());

			// Delegate processing to the next filter or resource in the chain
			chain.doFilter(req, res);

		} finally {
			// Always reset the SecurityContext, just to be on the safe side
			SecurityContextSetter.resetSecurityContext();
		}
	}

	/**
	 * Sets the userID on the HttpServletRequest context.
	 * 
	 * @param req
	 *            the HttpServletRequest.
	 * @param userId
	 *            the userId.
	 */
	private void setUserIdOnRequestContext(HttpServletRequest req, String userId) {
		RequestContext requestContext = new SimpleRequestContext.Builder(RequestContextHolder.currentRequestContext()).userId(
				userId).build();
		RequestContextSetter.setRequestContext(requestContext);

		// Set on session in case anyone retrieves it from session
		if (req.getSession(false) != null) {
			req.getSession(false).setAttribute(REQUEST_CONTEXT, requestContext);
		}
	}

	/**
	 * Method used to retrieve and set the values of a user credential on the <code>SecurityContext</code>.
	 * 
	 * @param req
	 *            the HttpServletRequest.
	 * @return a populated <code>SecurityContext</code>.
	 */
	abstract SecurityContext populateSecurityContext(HttpServletRequest req);

	/**
	 * Makes sure that user is redirected to the start flow after login, if the startPage parameter is set.
	 * 
	 * @param session
	 *            the HttpSession with attribute describing where to redirect to after authentication
	 */
	protected void setStartPageAfterLogin(HttpSession session) {
		if (session != null && startPage != null) {
			session.setAttribute(Constants.JSFPAGE_TOGO_AFTER_AUTHENTICATION, startPage);
		}
	}

	/**
	 * Gets all the roles of the current user.
	 * 
	 * @param httpreq
	 *            the HttpServletRequest
	 * @return a list containing the current user's roles.
	 */
	protected List<String> getUserRoles(HttpServletRequest httpreq) {

		List<String> roleList = new ArrayList<>();

		for (SecurityRole securityRole : securityRoles) {
			String roleName = securityRole.getRoleName();

			if (httpreq.isUserInRole(roleName)) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Adding role: " + roleName);
				}

				roleList.add(roleName);
			}
		}
		return roleList;
	}
}
