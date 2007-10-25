package no.nav.j2ca.adldap;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

import no.nav.j2ca.adldap.exception.ADLDAPAdapterConnectionFailedException;

import com.ibm.j2ca.base.WBIConnection;
import com.ibm.j2ca.base.WBIInteraction;
import com.ibm.j2ca.base.WBIRecord;
import com.ibm.j2ca.base.WBIResourceAdapter;
import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.sdo.DataFactory;
import commonj.sdo.DataObject;

public class ADLDAPAdapterInteraction extends WBIInteraction {
	
	//	for logging
	private static Logger log = Logger.getLogger(ADLDAPAdapterInteraction.class.getName());
	private static final String CLASSNAME = "ADLDAPAdapterInteraction"; 
	
    private ADLDAPAdapterManagedConnection adldapMngConn;
    private ADLDAPAdapterManagedConnectionFactory adldapMngConnFac;
    
    boolean isBiDi;    

    /**
	 * @param WBIConnection connection
	 */
	public ADLDAPAdapterInteraction(com.ibm.j2ca.base.WBIConnection connection)throws ResourceException {
		
        super(connection);
        WBIConnection wbiconnection = connection;		
        
        adldapMngConn = null;
        adldapMngConnFac = null;
        
		log.logp(Level.FINE, CLASSNAME, "ADLDAPAdapterInteraction()", "Initialize managed connection for ADLDAPAdapter.");
		adldapMngConn = (ADLDAPAdapterManagedConnection) connection.getManagedConnection();
		adldapMngConnFac = (ADLDAPAdapterManagedConnectionFactory) adldapMngConn.getManagedConnectionFactory();
		isBiDi = !((WBIResourceAdapter)adldapMngConnFac.getResourceAdapter()).getBiDiContextTurnBiDiOff().booleanValue();
		log.logp(Level.FINE, CLASSNAME, "ADLDAPAdapterInteraction()", "Done initialize managed connection for ADLDAPAdapter.");
	}

    /* (non-Javadoc)
     * @see javax.resource.cci.Interaction#execute(javax.resource.cci.InteractionSpec, javax.resource.cci.Record, javax.resource.cci.Record)
     */
    public boolean execute(InteractionSpec ispec, Record inRecord, Record outRecord) throws ResourceException
	{
    	throw new NotSupportedException();
	}
	
	/* (non-Javadoc)
	 * @see javax.resource.cci.Interaction#execute(javax.resource.cci.InteractionSpec, javax.resource.cci.Record)
	 */
	public javax.resource.cci.Record execute(InteractionSpec ispec, Record inRecord) throws javax.resource.ResourceException 
	{
		InteractionSpec interactionspec = ispec;
		Record record = inRecord;
		boolean fromMngConn = false;
		WBIRecord outRecord = new WBIRecord();
		
		log.logp(Level.FINE, CLASSNAME, "execute()", "Entering.");			
		
		LdapContext session = null;
		String sAction = "";
        ADLDAPAdapterRecord boInRecord = new ADLDAPAdapterRecord(record);
        ADLDAPAdapterRecord boOutRecord = new ADLDAPAdapterRecord();
		ADLDAPAdapterPhysicalConnection phyConn = (ADLDAPAdapterPhysicalConnection)adldapMngConn.getPhysicalConnection();
		String functionName = ((ADLDAPAdapterInteractionSpec)ispec).getFunctionName();
        if (functionName != null)
        	log.logp(Level.FINE, CLASSNAME, "execute()", "Function name for operation is set to " + functionName);
        else
        	log.logp(Level.FINE, CLASSNAME, "execute()", "Function name for the operation is NOT set.");
			
		if (adldapMngConn.getPhysicalConnection() != null)
		{
			if (log.getLevel().equals(Level.FINE))
				log.logp(Level.FINE, CLASSNAME, "execute()", "Managed connection and physical connection should be available.");
			session = phyConn.getSession();
		}
        else
        {
			fromMngConn = false;
        	log.logp(Level.FINE, CLASSNAME, "execute()", "No managed connection is available or the adapter connection factory definition is setup wrong. Check the connection parameter!");
        	throw new ADLDAPAdapterConnectionFailedException(ADLDAPAdapterConstants.ERR_MSG04);
        }

		//check once again that we have a connection because getSession might be returned null
		if (session != null)
        {	
 			fromMngConn = true;
 			log.logp(Level.FINE, CLASSNAME, "execute()", "Use connection for server=" + adldapMngConnFac.getServerURL() + " and connectioncontext=" + adldapMngConnFac.hashCode() + ".");
        }
        else
        {
			fromMngConn = false;
        	log.logp(Level.FINE, CLASSNAME, "execute()", "No connection or might be timeout connection for server=" + adldapMngConnFac.getServerURL() + ".");
        	throw new ADLDAPAdapterConnectionFailedException(ADLDAPAdapterConstants.ERR_MSG01);
        }

		
		
		log.logp(Level.FINE, CLASSNAME, "execute()", "Got request record with name=" + boInRecord.getRecordName());
        
        String s1 = boInRecord.getSAMAccountName();
        log.logp(Level.FINE, CLASSNAME, "execute()", "Use RequestObj "+ ADLDAPAdapterConstants.BO_SAMACCOUNTNAME + "=" + s1);

        
        // implement the real adapter functionality
        try 
		{			
			Attributes attrs = session.getAttributes(adldapMngConnFac.getServerBasedDistinguishedName());	
			
			//Create the search controls 
			SearchControls searchCtls = new SearchControls();
		
			//Specify the attributes to return
			String returnedAtts[]={ADLDAPAdapterConstants.BO_SAMACCOUNTNAME, ADLDAPAdapterConstants.BO_DISPLAYNAME, ADLDAPAdapterConstants.BO_GIVENNAME, ADLDAPAdapterConstants.BO_SN, ADLDAPAdapterConstants.BO_MAIL};
			searchCtls.setReturningAttributes(returnedAtts);
		
			//Specify the search scope
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	 
			String searchFilter = "(&("+boInRecord.getADobjectClass()+")("+ADLDAPAdapterConstants.BO_SAMACCOUNTNAME+ "="+ s1+ "))";
	 
			//Specify the Base for the search
			String searchBase = adldapMngConnFac.getServerSearchBaseContext(); 
	
			log.logp(Level.FINE, CLASSNAME, "execute()", "Query: searchBase="+searchBase+" searchFilter="+searchFilter+" searchCtls="+searchCtls);
			
			//Search for objects using the filter
			NamingEnumeration answer = session.search(searchBase, searchFilter, searchCtls);

			//Check if we got a result
			if (!answer.hasMoreElements()) 
			{
				log.logp(Level.FINE, CLASSNAME, "execute()", "Raise fault " + ADLDAPAdapterConstants.BO_FAULTNAME + " because query returned no result set.");
				DataObject errorObject = DataFactory.INSTANCE.create(ADLDAPAdapterConstants.BO_FAULTNAMESPACE, "FaultADNAVAnsattIkkeFunnet");
				errorObject.setString("errorMessage",  "NAVAnsatt med ident " + s1 + " ikke registrert i AD, Feil ident");
				errorObject.setString("errorType", "ServiceBusinessException");
				errorObject.setString("errorSource", ADLDAPAdapterConstants.CURRENT_MODULE_NAME);
				errorObject.setString("rootCause", "");
				throw new ServiceBusinessException(errorObject);
			}
			
			//Loop through the search results
			while (answer.hasMoreElements()) 
			{
				SearchResult sr = (SearchResult)answer.next();
 
				// Print out some of the attributes, catch the exception if the attributes have no values
				attrs = sr.getAttributes();
				if (attrs != null) {

					try {
						if ( attrs.get(ADLDAPAdapterConstants.BO_SAMACCOUNTNAME) != null ) {
							sAction = ADLDAPAdapterConstants.BO_SAMACCOUNTNAME;
							boOutRecord.setSAMAccountName((String)attrs.get(ADLDAPAdapterConstants.BO_SAMACCOUNTNAME).get());
						}

						if ( attrs.get(ADLDAPAdapterConstants.BO_DISPLAYNAME) != null ) {
							sAction = ADLDAPAdapterConstants.BO_DISPLAYNAME;
							boOutRecord.setDisplayName((String)attrs.get(ADLDAPAdapterConstants.BO_DISPLAYNAME).get());
						}						
					
						
						if ( attrs.get(ADLDAPAdapterConstants.BO_GIVENNAME) != null ) {
							sAction = ADLDAPAdapterConstants.BO_GIVENNAME;
							boOutRecord.setGivenName((String)attrs.get(ADLDAPAdapterConstants.BO_GIVENNAME).get());
						}

						if ( attrs.get(ADLDAPAdapterConstants.BO_SN) != null ) {
							sAction = ADLDAPAdapterConstants.BO_SN;
							boOutRecord.setSn((String)attrs.get(ADLDAPAdapterConstants.BO_SN).get());
						}
						
						if ( attrs.get(ADLDAPAdapterConstants.BO_MAIL) != null ) {
							sAction = ADLDAPAdapterConstants.BO_MAIL;
							boOutRecord.setMail((String)attrs.get(ADLDAPAdapterConstants.BO_MAIL).get());
						}

					} catch (NullPointerException e)	
					{
						log.logp(Level.SEVERE, CLASSNAME, "execute()", "Catch NullPointerException at attribut="+sAction);
						throw new ServiceRuntimeException(ADLDAPAdapterConstants.ERR_MSG03 + " #attribute=" + sAction);
					}	
			
				}
				
			}
			}catch (NamingException e) {
			
				log.logp(Level.SEVERE, CLASSNAME, "execute()", "Exception searching directory: " + e);
				try
				{
					session.close();
				}
				catch (NamingException sc)
				{
					log.logp(Level.SEVERE, CLASSNAME, "execute()", "LDAP session close failed with: " + sc);
				}
			
				throw new ServiceRuntimeException(ADLDAPAdapterConstants.ERR_MSG02, e);
		}
		
		outRecord.setDataObject(boOutRecord.getDataObject());

		if (log.getLevel().equals(Level.FINE))
		{
			log.logp(Level.FINE, CLASSNAME, "execute()", "Succesfully queryed LDAP server return boReResult to caller boOutRecord="+boOutRecord.toString());
		}	
		
		log.logp(Level.FINE, CLASSNAME, "execute()", "Exit.");
		return outRecord;
	}
}