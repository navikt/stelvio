package no.nav.presentation.pensjon.common.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;

public class SecurityFilter implements Filter {

	public void destroy() {
		

	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpreq = (HttpServletRequest) req;
		HttpServletResponse httpres = (HttpServletResponse) res;
		HttpSession s = httpreq.getSession();
		
		List grupper = (List) s.getAttribute( "GROUPS" );
		
		// Hent ut diverse info om brukeren. Her er uthenting av grupper vist
		// Hvis gruppene ikke er satt på sesjonen, hent ut fra AD dersom han 
		// Er en intern bruker. Vi skal også sjekke TPS for informasjon. Sjekk
		// PP42030 for informasjon om hva som skal sjekkes i dette filteret. 
		// Se også PP42041 for informasjon om brukeobjektet som skal bygges opp.
		
		/*
		if ( grupper == null ) {
		  InitialContext ctx = new InitialContext();
		  UserRegistry ad_reg = (UserRegistry) ctx.lookup("UserRegistry");
		  List l = ad_reg.getGroupsForUser(httpreq.getUserPrincipal().getName());
		  grupper = new ArrayList(l.size());
		    
		  for ( Iterator i = l.iterator(); i.hasNext();;)
		    grupper.add( ad_reg.getUniqueGroupId( (String) i.next() ) );
		  } 
		} 
		 */
		chain.doFilter( req, res );
	}

	public void init(FilterConfig arg0) throws ServletException {
		

	}

}
