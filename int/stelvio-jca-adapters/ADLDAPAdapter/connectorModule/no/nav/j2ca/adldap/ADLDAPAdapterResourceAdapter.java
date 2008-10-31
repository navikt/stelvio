package no.nav.j2ca.adldap;

import java.security.PrivilegedActionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.CommException;
import javax.resource.spi.InvalidPropertyException;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpointFactory;

import com.ibm.j2ca.base.WBIActivationSpecWithXid;
import com.ibm.j2ca.base.WBIResourceAdapter;
import com.ibm.j2ca.base.WBIPollableResourceAdapterWithXid;
import com.ibm.j2ca.base.WBIResourceAdapterMetadata;
import com.ibm.j2ca.extension.eventmanagement.EventStoreWithXid;
import com.ibm.ws.security.util.AccessController;

public class ADLDAPAdapterResourceAdapter extends WBIResourceAdapter implements
		WBIPollableResourceAdapterWithXid {
	
	
	//	for logging
	private static Logger log = Logger.getLogger(ADLDAPAdapterResourceAdapter.class.getName());
	private static final String CLASSNAME = "ADLDAPAdapterResourceAdapter"; 
	
	// check if Endpoint Activiation configured
	private boolean endpointActivationCompleted;
	
	
	/**
	 * Constructor 
	 */
	public ADLDAPAdapterResourceAdapter() {
		endpointActivationCompleted = false;
	}

	 /* (non-Javadoc)
	 * @see javax.resource.spi.ResourceAdapter#start(javax.resource.spi.BootstrapContext)
	 */
	public void start(BootstrapContext bootstrapCtx) throws ResourceAdapterInternalException
	{
		log.logp(Level.INFO, CLASSNAME, "start()", "NAV ADLDAP adapter received start request.");
		super.start(bootstrapCtx);
	}
	

    /* (non-Javadoc)
     * @see javax.resource.spi.ResourceAdapter#stop()
     */
    public void stop()
    {
    	log.logp(Level.INFO, CLASSNAME, "start()", "NAV ADLDAP adapter received stop request.");
    	super.stop();
    }
    
    /* (non-Javadoc)
     * @see com.ibm.j2ca.base.WBIResourceAdapter#validate()
     */
    public void validate() throws InvalidPropertyException
	{
    	super.validate();
	}

    /* (non-Javadoc)
     * @see javax.resource.spi.ResourceAdapter#endpointActivation(javax.resource.spi.endpoint.MessageEndpointFactory, javax.resource.spi.ActivationSpec)
     */
    public void endpointActivation(MessageEndpointFactory mef, ActivationSpec aspec) throws ResourceException
	{
    	log.logp(Level.INFO, CLASSNAME, "endpointActivation()", "NAV ADLDAP adapter activating endPoint configuration.");
    	endpointActivationCompleted = true;
    }

    /**
     * @return
     */
    public boolean isEndpointActivationCompleted()
    {
    	return endpointActivationCompleted;
    }

    /**
     * @param endpointActivationCompleted
     */
    public void setEndpointActivationCompleted(boolean endpointActivationCompleted)
    {
    	this.endpointActivationCompleted = endpointActivationCompleted;
    }
   	
    /* (non-Javadoc)
     * @see com.ibm.j2ca.base.WBIResourceAdapter#getResourceAdapterMetadata()
     */
    public WBIResourceAdapterMetadata getResourceAdapterMetadata() throws ResourceException
	{
    	ADLDAPAdapterPrivilegedExceptionAction lpea = new ADLDAPAdapterPrivilegedExceptionAction();
    	WBIResourceAdapterMetadata adldapMetadata = null;
    	adldapMetadata = new WBIResourceAdapterMetadata("NAV ADLDAP Adapter", "NAV", "6.1.0", false);

    	try
		{
    		lpea.setActionName("GET_LDAP_METADATA");
    		adldapMetadata = (ADLDAPAdapterResourceAdapterMetaData)AccessController.doPrivileged(lpea);
		}
    	catch(PrivilegedActionException privilegedactionexception)
		{
    		PrivilegedActionException pae = privilegedactionexception;
    		pae.printStackTrace();
		}
    	return adldapMetadata;
	}
    
    /* (non-Javadoc)
     * @see com.ibm.j2ca.base.WBIPollableResourceAdapterWithXid#createEventStore(com.ibm.j2ca.base.WBIActivationSpecWithXid)
     */
    public EventStoreWithXid createEventStore(WBIActivationSpecWithXid aspec)  throws ResourceException, CommException
	{
    	// no polling implemented
    	return null;
	}
}