
package no.stelvio.web.security.acegi;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.*;
import javax.servlet.http.*;
import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.beans.factory.InitializingBean;
import java.io.IOException;

import java.security.*;
/**
 * TODO
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class AcegiSecurityContextFilter implements Filter,InitializingBean
{
	
	

	public void afterPropertiesSet() throws Exception 
	{
			
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	throws IOException, ServletException 
	{
		//Først opprette securityContext
		/*
		 Viktig at 
		 HttpSessionContextIntegrationFilter kjøres før dette filteret 
		 slik at securityContexten er satt opp.. Evt implementer i dette filteret..
		 * */
//		hent ut detaljer fra brukerkonteksten
		HttpServletRequest req = (HttpServletRequest)request;
		Principal principal = req.getUserPrincipal();
		//--------------------------------------
		// Gjøres bare en gang! sjekk om det allerede finnes et authentication objekt i
		// securityContexten.
		//------------------------------------
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || ( principal != null && !principal.equals(auth.getPrincipal())))
		{
			Object credentials = "";
			GrantedAuthority granted = new GrantedAuthorityImpl("Granted");
			GrantedAuthority[] authorities = {granted};
		
			//Dette authentication objektet er nå Fully trusted dvs at isAuthenticated = true.
			//Må bruke konstruktøren med 3 argumenter..
			UsernamePasswordAuthenticationToken token = 
					new UsernamePasswordAuthenticationToken(principal,credentials,authorities);
					
			//Populere securityContext med authentication object
			SecurityContextHolder.getContext().setAuthentication(token);
			System.out.println("******************Custom Filter er nå i gang!!!!!!!");
			//---------------------------------------------------------------
		}
		
		chain.doFilter(request, response);
		
	}
	
	/**
		* Does nothing. We use IoC container lifecycle services instead.
		*/
	   public void destroy() {}
	/**
		* Does nothing. We use IoC container lifecycle services instead.
		*
		* @param filterConfig ignored
		*
		* @throws ServletException ignored
		*/
	   public void init(FilterConfig filterConfig) throws ServletException {}
	
}
