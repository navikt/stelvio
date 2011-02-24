package no.stelvio.presentation.security.context;

import java.io.IOException;
import java.security.Principal;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A filter that populates the <code>org.acegisecurity.context.SecurityContext</code> in the
 * <code>org.acegisecurity.context.SecurityContextHolder</code> with an <code>org.acegisecurity.Authentication</code>
 * object using the principal obtained from java security. The filter is dependent on that the
 * <code>SecurityContextHolder</code> is set up with a <code>SecurityContext</code> in order to perform its intended
 * operations, so the filter <code>org.acegisecurity.context.HttpSessionContextIntegrationFilter</code> which handles
 * this should be configured before the <code>AcegiSecurityContextFilter</code> in the filterchain.
 *
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class AcegiSecurityContextFilter extends OncePerRequestFilter {
	/** {@inheritDoc} */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		Principal principal = request.getUserPrincipal();
		Authentication auth = (SecurityContextHolder.getContext() != null) 
			? SecurityContextHolder.getContext().getAuthentication() : null;

		if (principal != null) {
			if (auth == null || !principal.equals(auth.getPrincipal())) {
				setAuthenticationObject(principal);
			}
		}

		chain.doFilter(request, response);
	}

	/**
	 * Sets the <code>org.acegisecurity.Authentication</code> object in the Acegi security context with the
	 * <code>java.security.Principal</code>.
	 *
	 * @param principal the principal to generate a token from
	 */
	private void setAuthenticationObject(Principal principal) {
		Object credentials = "";
		GrantedAuthority[] authorities = {new GrantedAuthorityImpl("Granted")};

		// This authentication object is fully trusted when created with the 3 param contructor,
		// i.e. isAuthenticated=true.
		UsernamePasswordAuthenticationToken token =
				new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
		SecurityContextHolder.getContext().setAuthentication(token);
	}
}
