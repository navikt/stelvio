package no.nav.j2ca.adldap;

import java.security.PrivilegedActionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import javax.resource.ResourceException;

import no.nav.j2ca.adldap.exception.ADLDAPAdapterConnectionFailedException;

import com.ibm.j2ca.base.WBIConnectionRequestInfo;
import com.ibm.j2ca.base.WBIManagedConnection;
import com.ibm.j2ca.base.WBIManagedConnectionFactory;
import com.ibm.j2ca.base.WBIManagedConnectionMetaData;
import com.ibm.ws.security.util.AccessController;

/**
 * @author lsb2812
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ADLDAPAdapterManagedConnection extends WBIManagedConnection {
	
	//	for logging
	private static Logger log = Logger.getLogger(ADLDAPAdapterManagedConnection.class.getName());
	private static final String CLASSNAME = "ADLDAPAdapterManagedConnection"; 
	
	// parameter
	private String serverURL;
	private String adminName;
	private String adminPassword;
	private String searchBase;
	private String domainLockoutDurationPolicy;
	private static final String DOMAIN = "DC=test,DC=local";
	
	// persona2c5e3b49756 new
	private ADLDAPAdapterManagedConnectionFactory adldapmcf;
    private Object physicalConnection;
	
	
	/**
	 * @param managedConnectionFactory
	 * @param subject
	 * @param connReqInfo
	 * @throws javax.resource.ResourceException
	 */
	public ADLDAPAdapterManagedConnection(com.ibm.j2ca.base.WBIManagedConnectionFactory mcf, javax.security.auth.Subject subject, com.ibm.j2ca.base.WBIConnectionRequestInfo connReqInfo)
			throws javax.resource.ResourceException {
		
		super((WBIManagedConnectionFactory) mcf, subject, (WBIConnectionRequestInfo)connReqInfo);
		log.logp(Level.FINE, CLASSNAME, "ADLDAPAdapterManagedConnection","Entering.");
		adldapmcf = null;
		adldapmcf = (ADLDAPAdapterManagedConnectionFactory) mcf;
		log.logp(Level.FINE, CLASSNAME, "ADLDAPAdapterManagedConnection","Context="+mcf.getClass());
		log.logp(Level.FINE, CLASSNAME, "ADLDAPAdapterManagedConnection","Exit.");
	}
	
	
	/* (non-Javadoc)
	 * @see javax.resource.spi.ManagedConnection#getMetaData()
	 */
	public javax.resource.spi.ManagedConnectionMetaData getMetaData()
			throws javax.resource.ResourceException {
		
		return new WBIManagedConnectionMetaData("NAV AD Adapter for LDAP", null , 1 , null);
	}

	/* (non-Javadoc)
	 * @see com.ibm.j2ca.base.WBIManagedConnection#getWBIConnection(javax.resource.spi.security.PasswordCredential, boolean)
	 */
	public java.lang.Object getWBIConnection(javax.resource.spi.security.PasswordCredential pc, boolean reauthenticate)	throws javax.resource.ResourceException {			
		
		log.logp(Level.FINE, CLASSNAME, "getWBIConnection","Entering.");
	
        String userName = null;
        String password = null;
        boolean auth = true;

        // implement security from the jca context
        if(pc != null)
        {
            userName = pc.getUserName();
            password = new String(pc.getPassword());
            log.logp(Level.FINE, CLASSNAME, "getWBIConnection","Get security credential from metadata userName="+userName);
        }
		
        if(userName == null || userName.length() <= 0)
            userName = adldapmcf.getUserName();
        if(password == null || password.length() <= 0)
            password = adldapmcf.getPassword();
        if(userName == null || password == null || userName.length() <= 0 || password.length() <= 0)
        {	
            auth = false;
            log.logp(Level.FINE, CLASSNAME, "getWBIConnection()","AD LDAP Adapter doesn't run in a security context.");
        }   
        
        boolean setPhysicalConnection = false;
        log.logp(Level.FINE, CLASSNAME, "getWBIConnection()","Establish physical connection to LDAP server.");
        LdapContext session = null;
        ADLDAPAdapterPrivilegedExceptionAction lpea = new ADLDAPAdapterPrivilegedExceptionAction(); 
        
        lpea.setActionName("CREATE_LDAP_SESSION");
        Object params[] = {adldapmcf, new Boolean(auth)};
        lpea.setActionParams(params);	
        try {
			session = (LdapContext) AccessController.doPrivileged(lpea);
		} catch (PrivilegedActionException e) {
			log.logp(Level.SEVERE, CLASSNAME, "getWBIConnection()","PrivilegedActionException: " + e);
			throw new ADLDAPAdapterConnectionFailedException(e.getMessage());
		}
		if(session != null)
		{	
			log.logp(Level.FINE, CLASSNAME, "getWBIConnection()","Physical connection property is set to true.");
			setPhysicalConnection = true;
		}
		else
		{	
			log.logp(Level.FINE, CLASSNAME, "getWBIConnection()","Physical connection property is set to false.");
			setPhysicalConnection = false;
		}
	
		if(setPhysicalConnection)
	    {
	       ADLDAPAdapterPhysicalConnection phyConn = new ADLDAPAdapterPhysicalConnection();
	       phyConn.setSession(session);
	       setPhysicalConnection(phyConn);
	       log.logp(Level.FINE, CLASSNAME, "getWBIConnection()","Physical connection object is set.");
	    } 

		ADLDAPAdapterConnection connectionHandle = new ADLDAPAdapterConnection(this);
		log.logp(Level.FINE, CLASSNAME, "getWBIConnection()","Exit");
		return connectionHandle;
	}
	
	/* (non-Javadoc)
	 * @see javax.resource.spi.ManagedConnection#destroy()
	 */
	public void destroy() throws javax.resource.ResourceException {
		// close session context
		try{
			ADLDAPAdapterPhysicalConnection phyConn = (ADLDAPAdapterPhysicalConnection) getPhysicalConnection();
			phyConn.session.close();
			phyConn.setSession(null);
			log.logp(Level.FINE, CLASSNAME, "destroy()","Closing ctx and setting context to null succeeded.");
		}catch (NamingException e) {
			log.logp(Level.SEVERE, CLASSNAME, "destroy()","Exception during destroy of LDAP session. Exception: " + e);

		} 
	}

	/* (non-Javadoc)
	 * @see javax.resource.spi.ManagedConnection#cleanup()
	 */
	public void cleanup()throws ResourceException
	{
		super.cleanup();
    }
	
	/**
	 * @return
	 */
	public Object getPhysicalConnection()
	{
	  return physicalConnection;
	}

	public void setPhysicalConnection(Object physicalConnection)
	{
	  this.physicalConnection = physicalConnection;
	}

}