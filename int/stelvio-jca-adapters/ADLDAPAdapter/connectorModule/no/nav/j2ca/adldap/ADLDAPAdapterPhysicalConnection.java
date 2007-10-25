package no.nav.j2ca.adldap;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import no.nav.j2ca.adldap.exception.ADLDAPAdapterConnectionFailedException;



/**
 * @author lsb2812
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ADLDAPAdapterPhysicalConnection
{

	//	for logging
	private static Logger log = Logger.getLogger(ADLDAPAdapterPhysicalConnection.class.getName());
	private static final String CLASSNAME = "ADLDAPAdapterPhysicalConnection"; 

    LdapContext session = null;

	
    /**
     * Constructor
     */
    public ADLDAPAdapterPhysicalConnection()
    {
    }

    /**
     * @return LDAPContext
     */
    public LdapContext getSession()
    {
        return session;
    }

    
    /**
     * @return void
     */
    public void closeSession()
    {
        try {
			session.close();
		} catch (Exception e) {
			log.logp(Level.SEVERE, CLASSNAME, "closeSession()","Could not close connection to LDAP server - Exception: " + e);
		}
    }

    /**
     * @param session
     */
    public void setSession(LdapContext session)
    {
        this.session = session;
    }

    /**
     * @param serverURL
     * @param userName
     * @param password
     * @param authentication
     * @param pooling
     * @return LDAPConnection
     * @throws ADLDAPAdapterConnectionFailedException
     */
    public LdapContext createSession(String serverURL, String username, String password, String authentication, String serverbaseddistinguishedname, String serverbinddistinguishedname,String pooling ) throws ADLDAPAdapterConnectionFailedException
    {
    	LdapContext session = null;
    	log.logp(Level.FINE, CLASSNAME, "createSession()","Entering");
    	log.logp(Level.FINE, CLASSNAME, "createSession()","Establish session with following parameters serverURL="+serverURL+" serverUserId="+username+" serverBasedDistinguishedName="+ serverbaseddistinguishedname+ " serverBindDistinguishedName="+ serverbinddistinguishedname+ " serverAuthenticationMode="+authentication+ " serverConnectionPooling="+ pooling );
    	
    	Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		 
		//set security credentials, note using simple cleartext authentication
		env.put(Context.SECURITY_AUTHENTICATION, authentication);
		
		// username format CN=srvPensjon,OU=ServiceAccounts,DC=test,DC=local
		String principal = "CN="+username+","+serverbinddistinguishedname;
		env.put(Context.SECURITY_PRINCIPAL,principal);
		env.put(Context.SECURITY_CREDENTIALS,password);
		
		//connection pooling intern LDAP?
		if (pooling == null || pooling.length() < 1)
		 pooling = "false";	
		
		env.put("com.sun.jndi.ldap.connect.pool", pooling);
				
		//connect to my domain controller
		env.put(Context.PROVIDER_URL,serverURL);

		try {
	 		//Create the initial directory context
			session = new InitialLdapContext(env,null);
			
			//Test the connection
			LdapContext ctxTest = (LdapContext)session.lookup(serverbaseddistinguishedname);
			ctxTest.close();
			log.logp(Level.FINE, CLASSNAME, "createSession()","Session established to LDAP with context = " + session.toString());
			
		} catch (NamingException e) {
			log.logp(Level.SEVERE, CLASSNAME, "createSession()","Could not establish connection to LDAP server(" + serverURL + ") Exception: " + e + " Explanation: " + e.getExplanation());
			throw new ADLDAPAdapterConnectionFailedException(ADLDAPAdapterConstants.ERR_MSG01 + "#Exception: " + e + "#Explanation: " + e.getExplanation(), e);
		}
		log.logp(Level.FINE, CLASSNAME, "createSession()","Exit");
		return session;
    }

}