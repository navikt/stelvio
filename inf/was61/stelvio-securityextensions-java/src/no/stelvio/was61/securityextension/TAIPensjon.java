package no.stelvio.was61.securityextension;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

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

/**
 * TAIPensjon er en interceptor som trigges n�r en bruker som ikke er autentisert 
 * pr�ver � aksessere en beskyttet ressurs i applikasjonen. Det opprettes et 
 * sikkerhetsobjekt basert p� hvor requesten kommer fra (PIN, Portal) og type p�logging 
 * (ekstern, ekstern sakbehandler, fullmakt). Sikkerhetsobjekt opprettes uten at brukerbase
 * av noe slag (LDAP, DB, etc.) kontaktes.
 */
public class TAIPensjon implements TrustAssociationInterceptor {

	/**
	 * Metoden som kan kalles for � rydde opp referanser og lukke igjen 
	 * �pne forbindelser til andre tjenester.
	 */
	public void cleanup() {
	}
	
	/**
	 * Metoden returnerer hva slags type interceptor det er.
	 */
	public String getType() {
		return this.getClass().getName();
	}
	
	/** 
	 * Metoden returnerer versjonen til interceptoren.
	 */
	public String getVersion() {
		return "1.0";	
	}

	/**
	 * Metoden initialiserer TAIen med variabler som er satt opp i administrasjonskonsollet 
	 * til Websphere
	 */
	public int initialize(Properties props)
			throws WebTrustAssociationFailedException {	
		return 0;
	}

	
	/** 
	 * Metoden blir kalt hver gang det gj�res en uautentisert request mot systemet. 
	 * Dette kan v�re ved f�rstegangs p�logging eller n�r sesjonen har utl�pt. Metoden skal 
	 * svare p� om dette er en request den skal behandle. 
	 * 
	 * @param req 	the HttpServletRequest to be processed	 
	 * */ 
	
	public boolean isTargetInterceptor(HttpServletRequest req)
			throws WebTrustAssociationException {
		
		System.out.println("#####isTargetinterceptor#####");
		
		HttpSession s = req.getSession();
		
		String logon_type = (String) s.getAttribute("logon_type");
		System.out.println("login_type: " + logon_type);
		
		if ( logon_type == null ||logon_type.equals( "" )  ) {
			return false;
		}			
		
		//Pinkodep�logging type
		if ( logon_type.equals( "PIN" ) ) {
			return true;
		}		

		//Eksterne brukere
		if ( logon_type.equals( "EKSTERN" ) ) {
			return true;
		}
		
		System.out.println("#####return false#####");
		return false;
	}
	
	
	/** 
	 * Metoden henter ut n�dvendig informasjon som  Websphere trenger for � opprette en
	 * sikkerhetskontekst. Metoden returnerer et TAIResult objekt som inneholder en status 
	 * og informasjonen om opprettelsen av sikkerhetskonteksten.
	 * 
	 * @param req 	the HttpServletRequest to be processed
	 * @param res 	the HttpServletResponse to be processed
	 */	

	public TAIResult negotiateValidateandEstablishTrust(
			HttpServletRequest req, HttpServletResponse res)
			throws WebTrustAssociationFailedException {
		HttpSession session = req.getSession();
		String userid = "";
		String rollePrefix = "0000-g-pensjon_";
		ArrayList<String> grupper = new ArrayList<String>();
		try {
			// Henter ut logon_type for � skille mellom hva slags innlogging som blir gjort
			String logon_type = (String) session.getAttribute( "logon_type" );
			
			if ( logon_type == null || logon_type.equals( "" ) ) {
				// Logon type is missing.
				 return returnToLogon( session, "No logon type", req, res);
			}
			
			if ( logon_type.equals("PIN") ) {
				if (session.getAttribute("level") != null){
					String tmp_level = (String) session.getAttribute("level");
					grupper.add(rollePrefix + tmp_level);
				}
				String tmp_id = (String) session.getAttribute("user_name");
				if ( session.getAttribute("user_name") == null || tmp_id.equals("") ) {
					// Username is missing
					return returnToLogon( session, "No username specified", req, res);
				} else {
					userid = tmp_id;
					System.out.println("Vi har brukernavn");
				}
				grupper.add(rollePrefix + "PIN");
				
			} else if ( logon_type.equals("EKSTERN") ){
				//TODO: sjekk saml-token og legg til roller
				grupper.add(rollePrefix + "EKSTERN");
			} else if ( logon_type.equals("EKSTERN_SAKSBEH") ){
				//TODO: sjekk saml-token og legg til roller 
				grupper.add(rollePrefix + "EKSTERN_SAKSBEH");
				if ( logon_type.equals("FULLMAKT") ){
					//TODO: sjekk saml-token og legg til roller 
					grupper.add(rollePrefix + "FULLMAKT");
				}
			} 
			
		} catch ( Exception e ) {
			throw new WebTrustAssociationFailedException( "Noe gikk galt i TAI! " + e.getMessage() );
		}
		System.out.println( "Lager subject" );
		Subject subject = createSubject( userid, session, grupper);
		if ( subject == null ) {
			return TAIResult.create(HttpServletResponse.SC_UNAUTHORIZED);
		}
		System.out.println("Subject funka, sender videre");
		return TAIResult.create(HttpServletResponse.SC_OK, "notused", subject);
	}
	

	
	/** 
	 * Metoden oppretter et sikkerhetsobjekt som inneholder brukernavn og roller.
	 * 
	 * @param userid	the userID
	 * @param session	the HttpSession
	 * @param roller	list of the roles the user will get
	 */		
	private Subject createSubject( String userid, HttpSession session, List roller) {
		Subject subject = new Subject();
		try {
			
			// Her lages Subjectet
			
			InitialContext ctx = new InitialContext();
			List<String> groups = new ArrayList<String>();
			
			// For � hente ut korrekte gruppenavn, gj�r oppslag i userregistry slik at mappingen 
			// blir korrekt
			UserRegistry ad_reg = (UserRegistry) ctx.lookup("UserRegistry");
			
			// Hent ut f�dselsnummer fra requesten og bruk som unik id
			// Bruk dene pluss en timpestamp som cache_key
			String uniqueid = (String) session.getAttribute("user_name");
			String key = uniqueid + "TAIRemoved" + String.valueOf(System.currentTimeMillis());
			
			// Hent ut grupper fra userregistry. Viktig at det er UniqueGroupId som blir brukt
			Iterator it = roller.iterator();
			while (it.hasNext()) {
				groups.add( ad_reg.getUniqueGroupId( (String)it.next() ) );
			}
			
			// Bygg opp hashtabellen men informasjonen og legg til subjectet.
			Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
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
	
	/** 
	 * Metoden avbryter p�loggigen og sender requesten tilbake til en side slik at korrekt informasjon kan 
	 * oppgis. 
	 * 
	 * @param session	the HttpSession
	 * @param errorMsg	the errormessage to be displayed 
	 * @param req 		the HttpServletRequest to be processed
	 * @param res 		the HttpServletResponse to be processed 
	 */		
	private TAIResult returnToLogon( HttpSession session, String errorMsg, HttpServletRequest req, HttpServletResponse res ) throws Exception {
		session.setAttribute( "ERROR_MSG", errorMsg );
		RequestDispatcher disp = req.getRequestDispatcher( "/loginside.jsp" );
		disp.forward( req, res );
		return TAIResult.create(HttpServletResponse.SC_CONTINUE );
	}

}
