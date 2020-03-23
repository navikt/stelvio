package no.stelvio.presentation.security.page.authentication;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.stelvio.common.security.SecurityContextHolder;
import no.stelvio.presentation.security.page.constants.Constants;

/**
 * <p>
 * Authentication Servlet that enforces J2EE authentication. J2EE authentication
 * is enforced through a webconstraint that protects this servlet with J2EE
 * authorization. Just configure this servlet to be protected by a J2EE role
 * that everybody is a member of.
 * </p>
 * <b>web.xml configuration</b>
 * <p>
 * SERVLET
 * </p>
 * 
 * <pre>
 *  &lt;servlet&gt;
 *  	&lt;servlet-name&gt;JsfAuthenticationServlet&lt;/servlet-name&gt;
 *  	&lt;servlet-class&gt;no.stelvio.web.security.page.authentication.JeeAuthenticationServlet&lt;/servlet-class&gt;
 *  &lt;/servlet&gt;
 * </pre>
 * 
 * SERVLET MAPPING
 * 
 * <pre>
 *  &lt;servlet-mapping&gt;
 *  	&lt;servlet-name&gt;JsfAuthenticationServlet&lt;/servlet-name&gt;
 *  	&lt;url-pattern&gt;/jsfauthentication&lt;/url-pattern&gt;
 *  &lt;/servlet-mapping&gt;
 * </pre>
 * 
 * <p>
 * The servlet must be mapped to <b>/jsfauthentication</b> to ensure that the
 * redirect for authentication works. Note that choosen a value different from
 * this will make the authentication fail.
 * </p>
 * SERVLET AUTHORIZATION
 * 
 * <pre>
 *  &lt;security-constraint&gt;
 *    &lt;web-resource-collection&gt;
 *      &lt;web-resource-name&gt;usersOnly&lt;/web-resource-name&gt;
 *      &lt;url-pattern&gt;/jsfauthentication&lt;/url-pattern&gt;
 *    &lt;/web-resource-collection&gt;
 *    &lt;auth-constraint&gt;
 *      &lt;role-name&gt;role_name&lt;/role-name&gt;
 *    &lt;/auth-constraint&gt;
 *  &lt;/security-constraint&gt;
 * </pre>
 * 
 * The role name that is used to enforce J2EE authorization on the Servlet
 * should be chosen so that all users of a system are granted permission. The
 * idea of the authentication servlet is to only enforce authentication but not
 * to decide about whether or not a user has access to a JSF page source. The
 * authorization decision is performed in the J2EE Secure View Handler. <br>
 * <br>
 * 
 * @version $Id$
 */

public class JeeAuthenticationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_SECURITY_MESSAGE
			= "An error occured after attempting to log in. You are not authorized to view this page.";

	/**
	 * Redirects the response to the page stored in the session attribute
	 * specified by the constant <code>JSFPAGE_TOGO_AFTER_AUTHENTICATION</code>.
	 */
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Check if the user has logged in.
		if (SecurityContextHolder.currentSecurityContext().getUserId() != null) {
			String root = request.getContextPath();
			String url = response.encodeRedirectURL((String) request
					.getSession(false).getAttribute(
							Constants.JSFPAGE_TOGO_AFTER_AUTHENTICATION));
			response.sendRedirect(root + url);
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN,
					DEFAULT_SECURITY_MESSAGE);
		}
	}
}
