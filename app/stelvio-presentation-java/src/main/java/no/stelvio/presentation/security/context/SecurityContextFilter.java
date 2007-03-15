package no.stelvio.presentation.security.context;

import java.io.IOException;
import java.lang.reflect.Method;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.xml.sax.SAXException;

import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.SecurityContextHolder;
import no.stelvio.common.security.support.SimpleSecurityContext;
import no.stelvio.common.security.validation.RoleValidator;
import no.stelvio.common.security.validation.support.RoleValidatorUtil;
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
 * <li>Optionally inject a RoleValidator into the SecurityContext which can validate the roles sent in 
 * to rolechecks against a ValidRole enumeration implementation.</li>
 * <br>
 * <br>
 * The following optional initparameters are available to this filter:
 * <br>
 * <br>
 * <li><b>no.stelvio.presentation.security.START_PAGE</b> - Specifies the page to go to after login. Value: relative path to page. </li>
 * <br>
 * <br>
 * <li><b>no.stelvio.presentation.security.ALLOW_URL_MANIPULATION</b> - Specifies wheter or not the users should be allowed to go to the requested
 * page suggested by the URL after they have logged in (If this page is protected). Value: true or false</li>
 * <br>
 * <br>
 * <li><b>no.stelvio.presentation.security.context.VALID_ROLES_ENUM</b> - Specifies that the rolenames used in rolechecks should be evaluated against
 * the enumeration represented by this value (The enumeration must be defined by its fully qualified class name
 * and it must be a ValidRole implementation). The enumaration will be used to create a RoleValidator that will be
 * set into the SecurityContext. Value: fully qualified classname to a ValidRole enumeration.</li>
 * <br>
 * <br>
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id: SecurityContextFilter.java $ 
 * @see SecurityContext
 * @see RoleValidator
 * @see ValidRole
 */ 
public class SecurityContextFilter extends OncePerRequestFilter {

	private static final Log logger = LogFactory.getLog(SecurityContextFilter.class);
	//Session constants
	private static final String SECURITY_CONTEXT = SecurityContext.class.getName();
	private static final String USER_ID = "USER_ID";
	private static final String USER_ROLES = "USER_ROLES";
	//Init parameter constants
	private static final String VALID_ROLES_ENUM = "no.stelvio.presentation.security.context.VALID_ROLES_ENUM";
	private static final String START_PAGE_AFTER_LOGIN = "no.stelvio.presentation.security.START_PAGE"; 
	private static final String ALLOW_URL_MANIPULATION = "no.stelvio.presentation.security.ALLOW_URL_MANIPULATION";
	
	private RoleValidator validator;
	private String startPage;
	private boolean allowURLManipulation;
	 
	private List<SecurityRole> securityRoles;
	private static Method setSecurityContextMethod;
	private static Method resetSecurityContextMethod;
	
	/**
	 *	Finds and creates the methods for setting and resetting the SecurityContext through reflection.  
	 */
	static {
		setSecurityContextMethod = ReflectionUtils.findMethod(SecurityContextHolder.class, "setSecurityContext", new Class[]{SecurityContext.class});
		setSecurityContextMethod.setAccessible(true);
		resetSecurityContextMethod = ReflectionUtils.findMethod(SecurityContextHolder.class, "resetSecurityContext", new Class[]{});
		resetSecurityContextMethod.setAccessible(true);
	}
	
	/**
	 * Gets the optional init parameters for this filter:
	 * <li>START_PAGE_AFTER_LOGIN - Specifies the page to go to after login. (Relative path)</li>
	 * <br>
	 * <br>
	 * <li>ALLOW_URL_MANIPULATION - Specifies wheter or not the users should be allowed to go to the requested
	 * page suggested by the URL after they have logged in (If this page is protected).</li>
	 * <br>
	 * <br>
	 * <li>VALID_ROLES_ENUM - Specifies that the rolenames used in rolechecks should be evaluated against
	 * the enumeration represented by this value (The enumeration must be defined by its fully qualified class name
	 * and it must be a ValidRole implementation). The enumaration will be used to create a RoleValidator that will be
	 * set into the SecurityContext.</li>
	 * <br>
	 * <br>
	 * @throws ClassNotFoundException if the value of the VALID_ROLES_ENUM parameter does not represent a valid class. 
	 */
	private void getInitParameters() throws ClassNotFoundException {
		this.startPage = getFilterConfig().getInitParameter(START_PAGE_AFTER_LOGIN);
		this.allowURLManipulation = getFilterConfig().getInitParameter(ALLOW_URL_MANIPULATION) != null ?
		getFilterConfig().getInitParameter(ALLOW_URL_MANIPULATION).equalsIgnoreCase("true") : false;
		
		//initialize and set the validator
		String enumName = getFilterConfig().getInitParameter(VALID_ROLES_ENUM);
		Class enumClass = RoleValidatorUtil.getClassFromString(enumName);
		this.validator = RoleValidatorUtil.createValidatorFromEnum(enumClass);
	}
	
	/**
	 * Reads in and parses the security role elements from web.xml using the <code>WebXmlParser</code>.
	 * @throws MalformedURLException if the URL to web.xml is invalid
	 * @throws SAXException if parsing of the xml file fails
	 * @throws IOException if an input/output error occurs
	 */
	private void getRolesFromWebXml() throws MalformedURLException, SAXException, IOException {
		URL url = getFilterConfig().getServletContext().getResource("/WEB-INF/web.xml");
		WebXmlParser parser = new WebXmlParser(url);
		WebAppRoles roles = parser.getWebAppRoles();
		securityRoles = roles.getSecurityRoles();

		if (logger.isDebugEnabled()) {
			logger.debug("Initparameter " + ALLOW_URL_MANIPULATION + " has been set to:" + this.allowURLManipulation);
			logger.debug("Initparameter " + START_PAGE_AFTER_LOGIN + " has been set to:" + this.startPage);
			
			Iterator iterator = roles.getSecurityRolesIterator();
			while (iterator.hasNext()) {
				SecurityRole element = (SecurityRole) iterator.next();
				logger.debug("Get role from web.xml <security-role> element: " + element.getRoleName());
			}
		}
	}
	
	/**  
	 * Initializes the filter by reading in and parsing the security role elements
	 * from web.xml using the <code>WebXmlParser</code>.
	 */
	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();    
		
		try {
			getInitParameters();
			getRolesFromWebXml();
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
					setSecurityContext((SecurityContext) context); 
					//SecurityContextHolder.setSecurityContext((SecurityContext) context);
				} else {
					if (logger.isInfoEnabled()) {
						logger.info("Session exists, but SecurityContext was not persisted");
					}
				}
			}


			SecurityContext securityContext = populateSecurityContext(req);
			setSecurityContext(securityContext);
			//SecurityContextHolder.setSecurityContext(securityContext);
			
			if (logger.isDebugEnabled()) {
				logger.debug("User on request:" + req.getRemoteUser());
				if(SecurityContextHolder.currentSecurityContext() != null){
					logger.debug("User on SecurityContext:" + SecurityContextHolder.currentSecurityContext().getUserId());
				}
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
		} finally {
			// Always reset the SecurityContext, just to be on the safe side
			resetSecurityContext();
		}
	}
	private void setSecurityContext(SecurityContext securityContext){
		ReflectionUtils.invokeMethod(setSecurityContextMethod, null, new Object[]{ securityContext });
	}
	private void resetSecurityContext(){
		ReflectionUtils.invokeMethod(resetSecurityContextMethod, null);
	}
	/**
	 * Private helper method used to retrieve and set values from the HttpSession and finally create and
	 * return a new <code>SecurityContext</code> with these values.
	 * @param req the HttpServletRequest
	 * @return a poulated <code>SecurityContext</code>.
	 */
	@SuppressWarnings(value={"unchecked"})
	private SecurityContext populateSecurityContext(HttpServletRequest req) {
		HttpSession session = req.getSession();
		SecurityContext securityContext;
		//Checks if the user has logged in.
		if (req.getRemoteUser() != null) {
			//Set the user id in session if not already there
			if (session.getAttribute(USER_ID) == null) {
				session.setAttribute(USER_ID, req.getRemoteUser());
			} else {
				String user = (String) session.getAttribute(USER_ID);
				//Set userid on session if it is not equal to the one already there.
				if (!user.equals(req.getRemoteUser())) {
					session.setAttribute(USER_ID, req.getRemoteUser());
				}
			}

			//put roles in session if they are not already there
			if (session.getAttribute(USER_ROLES) == null || ((List) session.getAttribute(USER_ROLES)).size() == 0) {
				addRoles(session, req);

				if (checkForURLManipulation(session)) {
					redirectAfterLogin(session);
				}
			}
		}
		//If a role-enumeration has been specified in the initparams a role-validator 
		//will be created and added to the securitycontext.
		if(this.validator != null){
			securityContext = new SimpleSecurityContext((String) session.getAttribute(USER_ID),
					(List<String>) session.getAttribute(USER_ROLES), validator);
		} else {
			securityContext = new SimpleSecurityContext((String) session.getAttribute(USER_ID),
					(List<String>) session.getAttribute(USER_ROLES));
		}
			
		if (logger.isDebugEnabled()) {
			logger.debug("Roles in session: " + session.getAttribute(USER_ROLES));
		}

		return securityContext;
	}

	/**
	 * Makes sure that user is redirected to the start flow after login.
	 *
	 * @param session the HttpSession with attribute describing where to redirect to after authentication
	 */
	private void redirectAfterLogin(HttpSession session) {
		if (session != null && !this.allowURLManipulation) {
			this.startPage = this.startPage == null ? "" : this.startPage;
			session.setAttribute(Constants.JSFPAGE_TOGO_AFTER_AUTHENTICATION, this.startPage);
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
		List<String> roleList = new ArrayList<String>();

		for (SecurityRole securityRole : securityRoles) {
			String roleName = securityRole.getRoleName();

			if (httpreq.isUserInRole(roleName)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Adding role: " + roleName);
				}

				roleList.add(roleName);
			}
		}

		session.setAttribute(USER_ROLES, roleList);
	}
}
