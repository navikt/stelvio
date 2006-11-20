
package no.stelvio.web.security.acegi;

import javax.servlet.*;
import javax.servlet.http.*;

import no.stelvio.web.filter.AbstractFilter;
import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.context.SecurityContextHolder;
import java.io.IOException;

import java.security.*;
/**
 * A filter that populates the <code>org.acegisecurity.context.SecurityContext</code> in the 
 * <code>org.acegisecurity.context.SecurityContextHolder</code> with an <code>org.acegisecurity.Authentication</code> 
 * object using the principal obtained from java security. The filter is dependent on that the 
 * <code>SecurityContextHolder</code> is set up with a <code>SecurityContext</code> in order 
 * to perform its intended operations, so the filter <code>org.acegisecurity.context.HttpSessionContextIntegrationFilter</code>
 * which handles this should be configured before the <code>AcegiSecurityContextFilter</code> in the filterchain.  
 *
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class AcegiSecurityContextFilter extends AbstractFilter
{
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	throws IOException, ServletException 
	{
		Principal principal = request.getUserPrincipal();
		Authentication auth = (SecurityContextHolder.getContext() != null) ?  
								SecurityContextHolder.getContext().getAuthentication() : null;
		if(auth == null && principal != null){ 
			setAuthenticationObject(principal);
		}else{
			if( principal != null && !principal.equals(auth.getPrincipal())){
				setAuthenticationObject(principal);
			}
		}
		chain.doFilter(request, response);		
	}
	
	/**
	 * Sets the <code>org.acegisecurity.Authentication</code> object in the acegi security context
	 * with the <code>java.security.Principal</code>.
	 * @param principal the principal
	 */
	private void setAuthenticationObject(Principal principal){
		Object credentials = "";
		GrantedAuthority granted = new GrantedAuthorityImpl("Granted");
		GrantedAuthority[] authorities = {granted};	
		//This authentication object is fully trusted when created with the 3 param contructor,
		//i.e. isAuthenticated=true.
		UsernamePasswordAuthenticationToken token = 
				new UsernamePasswordAuthenticationToken(principal,credentials,authorities);
		SecurityContextHolder.getContext().setAuthentication(token);
	}
}
