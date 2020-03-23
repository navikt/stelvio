package no.stelvio.presentation.security.context;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A filter that populates the <code>SecurityContext</code> with an authentication.
 * object using the principal obtained from java security. The filter is dependent on that the
 * <code>SecurityContextHolder</code> is set up with a <code>SecurityContext</code> in order to perform its intended
 * operation.
 *
 * @version $Id$
 */
public class SpringSecurityContextFilter extends OncePerRequestFilter {
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
	 * Sets the <code>Authentication</code> object in the spring security context with the
	 * <code>java.security.Principal</code>.
	 *
	 * @param principal the principal to generate a token from
	 */
	private void setAuthenticationObject(Principal principal) {
		Object credentials = "";
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("Granted"));

		// This authentication object is fully trusted when created with the 3 param contructor,
		// i.e. isAuthenticated=true.
		UsernamePasswordAuthenticationToken token =
				new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
		SecurityContextHolder.getContext().setAuthentication(token);
	}
}
