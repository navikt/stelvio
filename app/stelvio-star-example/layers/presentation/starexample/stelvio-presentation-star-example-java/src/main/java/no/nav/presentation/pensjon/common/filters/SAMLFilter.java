package no.nav.presentation.pensjon.common.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.RequestDispatcher;

public class SAMLFilter implements Filter {

	public void destroy() {
		

	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		// Hent ut fra request om det er SAML login
		// Hvis ja, sjekk at tokenet er gyldig
		// Koden er kommentert ut da dette krever import av diverse bibliotek.
		// SSOTokenManager og SSOToken ligger i OpenSSO pakka. Se PP42030 for
		// info om hvor man henter dette.
		/*
		try {
			SSOTokenManager tM = SSOTokenManager.getInstance();
			ssoToken = tM.createSSOToken(httpreq);
			if ((ssoToken == null) || ! tM.isValidToken(ssoToken)) {
		    // Brukeren er ikke logget inn lengre, redirect til 
		    // loginside.
		    RequestDispatcher disp = req.getRequestDispatcher(           
		      "/ibm_security_logout” + “?logout=logout” + “&logoutExitPage=http://saml.login.minside");
		    disp.forward( req, res );
		    return;
		  }
		} catch ( Exception e ) {return;}
		
		*/
		//		 Dette kjøres før requesten sendes til websiden
	    
	    chain.doFilter( req, res );

	    // Dette kjøres etter at websiden har skrevet til responsen.


	}

	public void init(FilterConfig props) throws ServletException {
		// TODO Auto-generated method stub

	}

}
