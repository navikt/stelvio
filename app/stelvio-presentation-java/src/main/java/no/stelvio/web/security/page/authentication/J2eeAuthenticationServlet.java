package no.stelvio.web.security.page.authentication;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.stelvio.web.security.page.constants.Constants;

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
 * <pre>
 * &lt;servlet&gt;
 * 	&lt;servlet-name&gt;JsfAuthenticationServlet&lt;/servlet-name&gt;
 * 	&lt;servlet-class&gt;no.stelvio.web.security.page.authentication.J2eeAuthenticationServlet&lt;/servlet-class&gt;
 * &lt;/servlet&gt;
 * </pre>
 * SERVLET MAPPING
 * <pre>
 * &lt;servlet-mapping&gt;
 * 	&lt;servlet-name&gt;JsfAuthenticationServlet&lt;/servlet-name&gt;
 * 	&lt;url-pattern&gt;/jsfauthentication&lt;/url-pattern&gt;
 * &lt;/servlet-mapping&gt;
 * </pre>
 * <p>
 * The servlet must be mapped to <b>/jsfauthentication</b> to ensure that the redirect
 * for authentication works. Note that choosen a value different from this will make
 * the authentication fail.
 * </p>
 * SERVLET AUTHORIZATION
 * <pre>
 * &lt;security-constraint&gt;
 * &nbsp;&nbsp;&lt;web-resource-collection&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;web-resource-name&gt;usersOnly&lt;/web-resource-name&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;url-pattern&gt;/jsfauthentication&lt;/url-pattern&gt;
 * &nbsp;&nbsp;&lt;/web-resource-collection&gt;
 * &nbsp;&nbsp;&lt;auth-constraint&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;role-name&gt;role_name&lt;/role-name&gt;
 * &nbsp;&nbsp;&lt;/auth-constraint&gt;
 * &lt;/security-constraint&gt;
 * </pre>
 * The role name that is used to enforce J2EE authorization on the Servlet should
 * be chosen so that all users of a system are granted permission. The idea of the
 * authentication servlet is to only enforce authentication but not to decide about
 * whether or not a user has access to a JSF page source. The authorization decision 
 * is performed in the J2EE Secure View Handler. 
 * <br>
 * <br>
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */

public class J2eeAuthenticationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    
    /**
     * Redirects the response to the page stored in the session attribute 
     * specified by constant <code>JSFPAGE_TOGO_AFTER_AUTHENTICATION</code>.
     * 
     * {@inheritDoc}
     */
    @Override
	public void service(HttpServletRequest request, 
                        HttpServletResponse response) throws ServletException, 
                                                             IOException {
        String url = response.encodeRedirectURL((String)request.getSession(false).getAttribute(Constants.JSFPAGE_TOGO_AFTER_AUTHENTICATION));
        response.sendRedirect(url);
    } 
}
