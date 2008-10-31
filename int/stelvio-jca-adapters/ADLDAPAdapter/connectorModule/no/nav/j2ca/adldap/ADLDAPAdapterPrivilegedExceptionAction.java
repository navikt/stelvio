package no.nav.j2ca.adldap;

import java.security.PrivilegedExceptionAction;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ldap.LdapContext;

/**
 * @author LSB2812
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ADLDAPAdapterPrivilegedExceptionAction implements PrivilegedExceptionAction
{

	//	for logging
	private static Logger log = Logger.getLogger(ADLDAPAdapterPrivilegedExceptionAction.class.getName());
	private static final String CLASSNAME = "ADLDAPAdapterPrivilegedExceptionAction"; 
	
	
    private String actionName;
    private Object actionParams[];
	
    /**
     * 
     */
    public ADLDAPAdapterPrivilegedExceptionAction()
    {
        actionName = null;
        actionParams = null;
    }

    /**
     * @param action
     */
    public ADLDAPAdapterPrivilegedExceptionAction(String action)
    {
        actionName = null;
        actionParams = null;
        actionName = action;
    }

    /**
     * @param action
     * @param params
     */
    public ADLDAPAdapterPrivilegedExceptionAction(String action, Object params[])
    {
        actionName = null;
        actionParams = null;
        actionName = action;
        actionParams = params;
    }

    /**
     * @param action
     */
    public void setActionName(String action)
    {
        actionName = action;
    }

    /**
     * @return
     */
    public String getActionName()
    {
        return actionName;
    }

    /**
     * @param params
     */
    public void setActionParams(Object params[])
    {
        actionParams = params;
    }

    /* (non-Javadoc)
     * @see java.security.PrivilegedExceptionAction#run()
     */
    public Object run() throws Exception
    {
        if(actionName != null)
        {
            if(actionName.equalsIgnoreCase("GET_LDAP_METADATA"))
            {
            	log.logp(Level.FINE, CLASSNAME, "run", "GET_LDAP_METADATA metadata entered.");
            	ADLDAPAdapterResourceAdapterMetaData adldapMd = new ADLDAPAdapterResourceAdapterMetaData();
            	log.logp(Level.FINE, CLASSNAME, "run", "GET_LDAP_METADATA metadata exit.");
                return adldapMd;
            }

            if(actionName.equalsIgnoreCase("CREATE_LDAP_SESSION"))
            {

            	log.logp(Level.FINE, CLASSNAME, "run", "CREATE_LDAP_SESSION metadata entered.");
            	ADLDAPAdapterManagedConnectionFactory adldapmcf = (ADLDAPAdapterManagedConnectionFactory)actionParams[0];
                ADLDAPAdapterPhysicalConnection phyCon = new ADLDAPAdapterPhysicalConnection();
                LdapContext session = phyCon.createSession(adldapmcf.getServerURL(), adldapmcf.getServerUserId(), adldapmcf.getServerUserIdPassword(), adldapmcf.getServerAuthenticationMode(), adldapmcf.getServerBasedDistinguishedName(),adldapmcf.getServerBindDistinguishedName(),adldapmcf.getServerConnectionPooling());
                log.logp(Level.FINE, CLASSNAME, "run", "CREATE_LDAP_SESSION metadata exit.");
                return session;
            }
        }
        return null;
    }
}