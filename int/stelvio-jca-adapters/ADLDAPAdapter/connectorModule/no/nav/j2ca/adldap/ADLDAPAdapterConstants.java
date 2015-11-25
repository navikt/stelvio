package no.nav.j2ca.adldap;

/**
 * @author lsb2812
 *
 */
public interface ADLDAPAdapterConstants
{
	// Constants for error messages
	public final static String ERR_MSG01 = "Tilkoblingen til AD ikke mulig.";
	public final static String ERR_MSG02 = "En feil oppstår til AD opersajon ikke mulig.";
	public final static String ERR_MSG03 = "Problemer med attributtene returnert fra direcotry";
	public final static String ERR_MSG04 = "Problemer med AdapterManagedConnection #Tilkoblingen til AD ikke mulig. ELLER # Feil setup av J2 connection factory.";
	public final static String ERR_MSG05 = "The search criteria does not result in one single entry. The interface support one and one entry only the crieria."; 
	
	// Constants related to the directory provider NDU
	public final static String NDU_BO_NAMESPACE 	= "http://nav-lib-sik-ad/no/nav/lib/sik/ad/asbo";
	public final static String NDU_BO_NAME 			= "adNAVAnsatt";
	
	public final static String NDU_FAULT_NS 		= "http://nav-lib-sik-ad/no/nav/lib/sik/ad/fault";
	public final static String NDU_FAULT_BASE 		= "FaultADBase";
	public final static String NDU_MODULE_NAME 		= "nav-prod-sik-ad";
	
	public final static String NDU_BO_SAMACCOUNTNAME= "sAMAccountName";
	public final static String NDU_BO_DISPLAYNAME 	= "displayName";
	public final static String NDU_BO_GIVENNAME 	= "givenName";
	public final static String NDU_BO_SN 			= "sn";
	public final static String NDU_BO_MAIL 			= "mail";
	
	// Constants related to the directory provider NDULIST
	public final static String NDULIST_BO_NAME 			= "adNAVAnsattListe";
	public final static String NDULIST_BO_LIST 			= "ansattListe";
	public final static String NDULIST_BO_NOT_FOUND_LIST= "ikkeFunnetListe";
	
	// Constants related to the directory provider MinSide
	public final static String MINSIDE_FAULT_NS 	= "http://nav-lib-sto-minside/nav/lib/sto/minside/fault"; 
	public final static String MINSIDE_FAULT_BASE 	= "FaultMinsideBase"; 
	public final static String MINSIDE_FAULT_NOUSER = "FaultMinsidePersonenHarIkkeBruker";
	public final static String MINSIDE_MODULE_NAME 	= "nav-prod-sto-minside";
	
	public final static String MINSIDE_BO_NAMESPACE = "http://nav-lib-sto-minside/nav/lib/sto/minside/asbo";
	public final static String MINSIDE_BO_NAME 		= "minsideMinsideBrukerInfo";
	
	public final static String MINSIDE_BO_FNR 		= "fnr";
	public final static String MINSIDE_BO_NAVN 		= "navn";
	public final static String MINSIDE_BO_ADRESSE 	= "adresse";
	public final static String MINSIDE_BO_SPRAK 	= "sprak";
	
	public final static String MINSIDE_LDAP_UID 	= "uid";
	public final static String MINSIDE_LDAP_NAME 	= "givenName";
	public final static String MINSIDE_LDAP_ADRESSE	= "mail";
	public final static String MINSIDE_LDAP_SPRAK 	= "preferredLanguage";
	
}