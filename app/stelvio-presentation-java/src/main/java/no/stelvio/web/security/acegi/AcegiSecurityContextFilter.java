
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
 * TODO
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
		//Først opprette securityContext
		/*
		 Viktig at 
		 HttpSessionContextIntegrationFilter kjøres før dette filteret 
		 slik at securityContexten er satt opp.. Evt implementer i dette filteret..
		 * */
//		hent ut detaljer fra brukerkonteksten
		Principal principal = request.getUserPrincipal();
		//--------------------------------------
		// Gjøres bare en gang! sjekk om det allerede finnes et authentication objekt i
		// securityContexten.
		//------------------------------------
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || ( principal != null && !principal.equals(auth.getPrincipal())))
		{ 
			System.out.println("----- Start AcegiSecurityContextFilter -----");
			Object credentials = "";
			GrantedAuthority granted = new GrantedAuthorityImpl("Granted");
			GrantedAuthority[] authorities = {granted};
		
			//Dette authentication objektet er nå Fully trusted dvs at isAuthenticated = true.
			//Må bruke konstruktøren med 3 argumenter..
			UsernamePasswordAuthenticationToken token = 
					new UsernamePasswordAuthenticationToken(principal,credentials,authorities);
					
			//Populere securityContext med authentication object
			SecurityContextHolder.getContext().setAuthentication(token);
			System.out.println("----- End AcegiSecurityContextFilter -----");
			//---------------------------------------------------------------
		}
		
		chain.doFilter(request, response);
		
	}
}
