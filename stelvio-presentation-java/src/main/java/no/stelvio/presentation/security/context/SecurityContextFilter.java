package no.stelvio.presentation.security.context;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.support.SimpleSecurityContext;


/**
 * A servlet filter which inherits the AbstractSecurityContextFilter and implements
 * the populateSecurityContext() method. The SecurityContext is populated with
 * attributes from the HttpServletRequest. 
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id: SecurityContextFilter.java $
 * @see AbstractSecurityContextFilter
 */
public class SecurityContextFilter extends AbstractSecurityContextFilter {
	
	@Override
	SecurityContext populateSecurityContext(HttpServletRequest req) {
		HttpSession session = req.getSession();
		SecurityContext securityContext;
			
		//If a role-enumeration has been specified in the initparams a role-validator
		// will be created and added to the securitycontext.
		String user = req.getRemoteUser();
		List<String> roles = getUserRoles(req);
		if (validator != null) {
			securityContext = new SimpleSecurityContext(user, user, roles, validator);
		} else {
			securityContext = new SimpleSecurityContext(user, user, roles);
		}
		
		//Is the user logged in?
		if (req.getRemoteUser() != null) {
			setStartPageAfterLogin(session);
		}	
		
		return securityContext;
	}
}