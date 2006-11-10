package no.stelvio.was61.securityextension;

import java.util.Properties;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

import javax.naming.InitialContext;

import javax.security.auth.Subject;

import javax.servlet.RequestDispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.websphere.security.UserRegistry;
import com.ibm.websphere.security.WebTrustAssociationException;
import com.ibm.websphere.security.WebTrustAssociationFailedException;
import com.ibm.wsspi.security.tai.TAIResult;
import com.ibm.wsspi.security.tai.TrustAssociationInterceptor;

import com.ibm.wsspi.security.token.AttributeNameConstants;

/*
 * 
 * Dette er en veldig basic testimplementasjon. 
 * Den vil behandle alle inkommende requests som komer fra en firefox browser.
 * For mer informasjon om hvordan implementasjon og konfigurasjon av denne klassen
 * skal gjøres, se PP42030. Denne klassen viser bare eksempelbruk.
 * 
 */

public class TAIPensjon implements TrustAssociationInterceptor {

	public void cleanup() {
		

	}

	public String getType() {
		return this.getClass().getName();
	}

	public String getVersion() {
		return "0.00000004";
		
	}

	public int initialize(Properties props)
			throws WebTrustAssociationFailedException {
		
		return 0;
	}

	public boolean isTargetInterceptor(HttpServletRequest req)
			throws WebTrustAssociationException {
		if ( req.getHeader( "USER-AGENT" ).indexOf( "Firefox" ) != -1 ) {
			
			return true;
		}
		
		return false;
	}

	public TAIResult negotiateValidateandEstablishTrust(
			HttpServletRequest req, HttpServletResponse res)
			throws WebTrustAssociationFailedException {
		HttpSession s = req.getSession();
		String userid = "";
		try {
			// Henter ut logon_type for å skille mellom hva slags innlogging som blir gjort
			String logon_type = (String) s.getAttribute( "logon_type" );
			
			if ( logon_type == null || logon_type.equals( "" ) ) {
				// Ingen login, sendes videre.
				s.setAttribute( "logon_type", "PIN" );
				s.setAttribute( "next_page", req.getContextPath() );
				RequestDispatcher disp = req.getRequestDispatcher( "/login2.jsp" );
				disp.forward( req, res );
				return TAIResult.create(HttpServletResponse.SC_CONTINUE );
			}
			
			if ( logon_type.equals( "PIN" ) ) {
				String tmp_id = (String) s.getAttribute( "user_name");
				if ( tmp_id == null || tmp_id.equals( "" ) ) {
					s.setAttribute( "ERROR_MSG", "No username specified" );
					RequestDispatcher disp = req.getRequestDispatcher( "/login2.jsp" );
					disp.forward( req, res );
					return TAIResult.create(HttpServletResponse.SC_CONTINUE );
				}
				System.out.println( "Vi har brukernavn" );
				userid = tmp_id;
				
			} else if ( logon_type.equals( "SAML" ) ) {
				
			}
			
		} catch ( Exception e ) {
			throw new WebTrustAssociationFailedException( "Noe gikk galt! " + e.getMessage() );
		}
		System.out.println( "Lager subject" );
		Subject subject = createSubject( userid, s );
		if ( subject == null ) {
			return TAIResult.create(HttpServletResponse.SC_UNAUTHORIZED);
		}
		System.out.println( "Subject funka, sender videre" );
		return TAIResult.create(HttpServletResponse.SC_OK, "notused", subject);
		//return null;
		
	}
	
	
	private Subject createSubject( String userid, HttpSession s ) {
		Subject subject = new Subject();
		try {
			
			// Her lages Subjectet
			
			InitialContext ctx = new InitialContext();
			
			// For å hente ut korrekte gruppenavn, gjør oppslag i userregistry slik at mappingen 
			// blir korrekt
			UserRegistry ad_reg = (UserRegistry) ctx.lookup("UserRegistry");
			
			// Hent ut fødselsnummer fra requesten og bruk som unik id
			// Bruk dene pluss en timpestamp som cache_key
			String uniqueid = "fødselsnummer";
			String key = uniqueid + "TAIRemoved" + String.valueOf(System.currentTimeMillis());
			
			// Hent ut grupper fra userregistry. Viktig at det er UniqueGroupId som blir brukt
			List groups = new ArrayList(); 
			groups.add( ad_reg.getUniqueGroupId( "Leder" ) );
			
			// Bygg opp hashtabellen men informasjonen og legg til subjectet.
			Hashtable hashtable = new Hashtable();
			hashtable.put(AttributeNameConstants.WSCREDENTIAL_UNIQUEID, uniqueid);
			hashtable.put(AttributeNameConstants.WSCREDENTIAL_SECURITYNAME, userid);
			hashtable.put(AttributeNameConstants.WSCREDENTIAL_GROUPS, groups);			
			hashtable.put(AttributeNameConstants.WSCREDENTIAL_CACHE_KEY, key);
			subject.getPublicCredentials().add(hashtable);
			
		} catch ( Exception e ) {
			e.printStackTrace();
			subject = null;
		}
		// Returner det ferdige subjektet tilbake
		return subject;
	}
	

}
