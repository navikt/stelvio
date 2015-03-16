package no.stelvio.presentation.security.session;

import java.io.IOException;

import javax.security.auth.Subject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.stelvio.common.security.ws.WSCustomSubject;
import no.stelvio.presentation.security.logout.LogoutService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.WSSecurityHelper;
import com.ibm.websphere.security.auth.WSSubject;
import com.ibm.websphere.security.cred.WSCredential;

/**
 * Servlet filter which checks the SSO token in the request against the token stored in the current Subject.
 * If a mismatch is detected the current session and the LTPA token will be invalidated.
 * If no SSO token is found on the request no action will be done.
 * 
 * The filter's purpose is to prevent users from manipulating the SSO token
 * 
 */
public class OpenAMSessionFilter extends OncePerRequestFilter {

	private static final String SSO_COOKIE_NAME = "nav-esso";
	private static final String LTPA_COOKIE_NAME = "LtpaToken2";
    private static final Log LOGGER = LogFactory.getLog(OpenAMSessionFilter.class);
    private static final String SSOTOKEN = "no.stelvio.presentation.security.sso.ibm.WebsphereSubjectMapper.SSOTOKEN";
    
    private LogoutService logoutService;

	/**
	 * Validates the SSO token in the request against the token stored in the current Subject.
	 * If a mismatch is detected the current session and the LTPA token will be invalidated. 
	 * If no SSO token is found on the request no action will be done.
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
			LOGGER.debug("Entering filter to validate that the SSO cookie value is unchanged");
		}
		String ltpaRequestCookie = getRequestEksternSsoToken(req, LTPA_COOKIE_NAME);		
		String ssoRequestCookie = getRequestEksternSsoToken(req, SSO_COOKIE_NAME);
		// if only LTPA cookie, then revoke it to prevent hijacking
		if(ltpaRequestCookie != null && ssoRequestCookie==null ) {
		    if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Revoking LTPA cookie to prevent hijacking: " + ltpaRequestCookie);
            }
		    WSSecurityHelper.revokeSSOCookies(req, res);
		}

		if (ssoRequestCookie != null) {
		    try {
                Subject subject = WSSubject.getCallerSubject();
                String ssoSubjectCookie = getSubjectEksternSsoToken(subject);
                if (ssoSubjectCookie != null && !ssoRequestCookie.equals(ssoSubjectCookie)) {
                	// Additional step to revoke SSOcookie because ibm_security_logout did not work properly anymore.
                	// Should really be replaced with HttpServletRequest.logout()
                	WSSecurityHelper.revokeSSOCookies(req, res);
                	// invalidate session, ltpa and logout subject                	
                    logoutService.logout(req, res);
                    return;
                }                
                
            } catch (WSSecurityException e) {
                throw new ServletException("Exception when comparing current and authenticated SSO token ", e);
            } 
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("The request has no SSO cookie. No validation of SSO cookie will be done.");
			}
		}
		// Delegate processing to the next filter or resource in the chain
		chain.doFilter(req, res);
	}
	
	/**
	 * Helper method to extract the SSO token from the JAAS Subject
	 * 
	 * @param subject the Subject to get SSO token from
	 * @return a String with the value of the SSO token or null if no token found
	 */
	private String getSubjectEksternSsoToken(Subject subject) {

	    if (subject != null) {
            Object[] creds = subject.getPublicCredentials().toArray();
            if (LOGGER.isDebugEnabled()) {
                for (Object object : creds) {
                    LOGGER.debug("Getting the WSCredential: " + object.toString());
                }
            }
            WSCredential cred = null;
            Object auth = null;
            try {
                cred = WSCustomSubject.getWSCredential();
                auth = cred.get(SSOTOKEN);
            } catch (Exception e) {
                LOGGER.error("Error getting SSO token from Subject", e);
                return null;
            }
            return (String) auth;
	    }
	    return null;

    }

	/**
     * Helper method to extract value of a cookie from the request
     * 
     * @param request the request to get SSO token from
     * @param cookieName the cookie to search for
     * @return a String with the value of the SSO token or null if no token found
     */
	private String getRequestEksternSsoToken(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
	
   /**
     * 
     * Mutator
     * 
     * @param logoutService
     *            the logoutService to set
     */
    public void setLogoutService(LogoutService logoutService) {
        this.logoutService = logoutService;
    }


}
