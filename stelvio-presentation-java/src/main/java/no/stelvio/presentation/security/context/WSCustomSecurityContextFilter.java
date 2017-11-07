package no.stelvio.presentation.security.context;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.support.SimpleSecurityContext;
import no.stelvio.common.security.ws.WSCustomSubject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * {@inheritDoc}
 * 
 * A servlet filter which inherits the AbstractSecurityContextFilter and implements the populateSecurityContext() method. The
 * SecurityContext is populated with attributes from the <code>WSCustomSubject</code> class.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id: WSCustomSecurityContextFilter.java $
 * @see AbstractSecurityContextFilter
 * @see WSCustomSubject
 */
public class WSCustomSecurityContextFilter extends AbstractSecurityContextFilter {

	private static final Log LOGGER = LogFactory.getLog(WSCustomSecurityContextFilter.class);

	/** {@inheritDoc} */
	@Override
	public SecurityContext populateSecurityContext(HttpServletRequest req) {
		HttpSession session = req.getSession();
		SecurityContext securityContext;

		// If a role-enumeration has been specified in the initparams a role-validator
		// will be created and added to the securitycontext.

		String user = WSCustomSubject.getUserId();
		String authorizedAs = WSCustomSubject.getAuthorizedAs();
		List<String> roles = getUserRoles(req);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("WSCustomSubject.userId: " + user);
			LOGGER.debug("WSCustomSubject.authorizedAs: " + authorizedAs);
		}

		if (validator != null) {
			securityContext = new SimpleSecurityContext(user, authorizedAs, roles, validator);
		} else {
			securityContext = new SimpleSecurityContext(user, authorizedAs, roles);
		}

		// Is the user logged in?
		if (user != null) {
			setStartPageAfterLogin(session);
		}

		return securityContext;
	}
}
