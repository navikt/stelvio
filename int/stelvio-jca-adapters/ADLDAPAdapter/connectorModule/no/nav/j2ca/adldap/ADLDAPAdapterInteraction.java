package no.nav.j2ca.adldap;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

import no.nav.j2ca.adldap.exception.ADLDAPAdapterConnectionFailedException;

import com.ibm.j2ca.base.DataObjectRecord;
import com.ibm.j2ca.base.WBIInteraction;
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
        
        adldapMngConn = null;
        adldapMngConnFac = null;
        
		log.logp(Level.FINE, CLASSNAME, "ADLDAPAdapterInteraction()", "Initialize managed connection for ADLDAPAdapter.");
		adldapMngConn = (ADLDAPAdapterManagedConnection) connection.getManagedConnection();
		adldapMngConnFac = (ADLDAPAdapterManagedConnectionFactory) adldapMngConn.getManagedConnectionFactory();
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
	public javax.resource.cci.Record execute(InteractionSpec ispec, Record record) throws javax.resource.ResourceException 
	{
		log.logp(Level.FINE, CLASSNAME, "execute()", "Entering.");			
		
		ADLDAPAdapterPhysicalConnection phyConn = (ADLDAPAdapterPhysicalConnection)adldapMngConn.getPhysicalConnection();
		String functionName = ((ADLDAPAdapterInteractionSpec)ispec).getFunctionName();
        if (functionName != null) {
        	log.logp(Level.FINE, CLASSNAME, "execute()", "Function name for operation is set to " + functionName);
        } else {
        	log.logp(Level.FINE, CLASSNAME, "execute()", "Function name for the operation is NOT set.");
        }
			
        LdapContext context = null;
		if (adldapMngConn.getPhysicalConnection() != null) {
			if (log.getLevel().equals(Level.FINE)) {
				log.logp(Level.FINE, CLASSNAME, "execute()", "Managed connection and physical connection should be available.");
			}
			context = phyConn.getSession();
		} else {
			log.logp(Level.FINE, CLASSNAME, "execute()", "No managed connection is available or the adapter connection factory definition is setup wrong. Check the connection parameter!");
        	throw new ADLDAPAdapterConnectionFailedException(ADLDAPAdapterConstants.ERR_MSG04);
        }

		//check once again that we have a connection because getSession might be returned null
		if (context != null) {	
 			log.logp(Level.FINE, CLASSNAME, "execute()", "Use connection for server=" + adldapMngConnFac.getServerURL() + " and connectioncontext=" + adldapMngConnFac.hashCode() + ".");
        } else {
			log.logp(Level.FINE, CLASSNAME, "execute()", "No connection or might be timeout connection for server=" + adldapMngConnFac.getServerURL() + ".");
        	throw new ADLDAPAdapterConnectionFailedException(ADLDAPAdapterConstants.ERR_MSG01);
        }

		DataObjectRecord outRecord = new DataObjectRecord();
		
		try {
			String provider = adldapMngConnFac.getDirectoryProvider();
			log.logp(Level.FINE, CLASSNAME, "execute()", "Working with provider " + provider);
			
			if ("ndu".equals(provider)) {
				ADLDAPAdapterRecord result = executeNDUAD(context, record);
				outRecord.setDataObject(result.getDataObject());
				log.logp(Level.FINE, CLASSNAME, "execute()", "Succesfully queryed LDAP with " + result.getSAMAccountName());
			} else if ("minside".equals(provider)) {
				MinsideAdapterRecord result = executeMinside(context, record);
				outRecord.setDataObject(result.getDataObject());
				log.logp(Level.FINE, CLASSNAME, "execute()", "Succesfully queryed LDAP with " + result.getFnr());
			} else {
				log.logp(Level.WARNING, CLASSNAME, "execute()", "Raising NotSupportedException. Directory provider is empty");
				throw new NotSupportedException("The directory provider is not defined. Please see the connection factory's custom properties");
			}
			
		} catch (NamingException e) {
			log.logp(Level.SEVERE, CLASSNAME, "execute()", "Exception searching directory: " + e);
		}

		try {
			context.close();
		} catch (NamingException sc) {
			log.logp(Level.SEVERE, CLASSNAME, "execute()", "LDAP session close failed with: " + sc);
		}

		
		log.logp(Level.FINE, CLASSNAME, "execute()", "Exit.");
		return outRecord;
	}
	
	/**
	 * This method is a delegate method for the inherited method execute(). As this adapter is used
	 * for several directory provides, each of the provides will need some custom logics. Both for
	 * handling with different kinds of business object, but as well for various kind of exception
	 * handling based on different business logic (e.g. is an empty result the cause of a exception?)
	 * 
	 * @param context
	 * @param record
	 * @return
	 * @throws NamingException
	 * @throws ServiceBusinessException if no user is returned
	 */
	private ADLDAPAdapterRecord executeNDUAD(LdapContext context, Record record) throws NamingException {
		ADLDAPAdapterRecord boInRecord = new ADLDAPAdapterRecord(record);
        ADLDAPAdapterRecord boOutRecord = new ADLDAPAdapterRecord();
        String s1 = boInRecord.getSAMAccountName();
        log.logp(Level.FINE, CLASSNAME, "executeNDUAD()", "Got request record with name=" + boInRecord.getRecordName());
        log.logp(Level.FINE, CLASSNAME, "executeNDUAD()", "Use RequestObj "+ ADLDAPAdapterConstants.NDU_BO_SAMACCOUNTNAME + "=" + s1);

        
		Attributes attrs = context.getAttributes(adldapMngConnFac.getServerBasedDistinguishedName());	
		
		//Create the search controls 
		SearchControls searchCtls = new SearchControls();
	
		//Specify the attributes to return
		String returnedAtts[]={ADLDAPAdapterConstants.NDU_BO_SAMACCOUNTNAME, ADLDAPAdapterConstants.NDU_BO_DISPLAYNAME, ADLDAPAdapterConstants.NDU_BO_GIVENNAME, ADLDAPAdapterConstants.NDU_BO_SN, ADLDAPAdapterConstants.NDU_BO_MAIL};
		searchCtls.setReturningAttributes(returnedAtts);
	
		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
 
		String searchFilter = "(&("+boInRecord.getADobjectClass()+")("+ADLDAPAdapterConstants.NDU_BO_SAMACCOUNTNAME+ "="+ s1+ "))";
 
		//Specify the Base for the search
		String searchBase = adldapMngConnFac.getServerSearchBaseContext(); 

		log.logp(Level.FINE, CLASSNAME, "executeNDUAD()", "Query: searchBase="+searchBase+" searchFilter="+searchFilter+" searchCtls="+searchCtls);
		
		//Search for objects using the filter
		NamingEnumeration answer = context.search(searchBase, searchFilter, searchCtls);

		//Check if we got a result
		if (!answer.hasMoreElements()) {
			log.logp(Level.FINE, CLASSNAME, "executeNDUAD()", "Raise fault " + ADLDAPAdapterConstants.NDU_FAULT_BASE + " because query returned no result set.");
			DataObject errorObject = DataFactory.INSTANCE.create(ADLDAPAdapterConstants.NDU_FAULT_NS, "FaultADNAVAnsattIkkeFunnet");
			errorObject.setString("errorMessage",  "NAVAnsatt med ident " + s1 + " ikke registrert i AD, Feil ident");
			errorObject.setString("errorType", "ServiceBusinessException");
			errorObject.setString("errorSource", ADLDAPAdapterConstants.NDU_MODULE_NAME);
			errorObject.setString("rootCause", "");
			throw new ServiceBusinessException(errorObject);
		}
		
		//Loop through the search results
		while (answer.hasMoreElements()) {
			SearchResult sr = (SearchResult)answer.next();
	 
			// Print out some of the attributes, catch the exception if the attributes have no values
			attrs = sr.getAttributes();
			if (attrs != null) {
				String sAction = "";
				try {
					if ( attrs.get(ADLDAPAdapterConstants.NDU_BO_SAMACCOUNTNAME) != null ) {
						sAction = ADLDAPAdapterConstants.NDU_BO_SAMACCOUNTNAME;
						boOutRecord.setSAMAccountName((String)attrs.get(ADLDAPAdapterConstants.NDU_BO_SAMACCOUNTNAME).get());
					}
	
					if ( attrs.get(ADLDAPAdapterConstants.NDU_BO_DISPLAYNAME) != null ) {
						sAction = ADLDAPAdapterConstants.NDU_BO_DISPLAYNAME;
						boOutRecord.setDisplayName((String)attrs.get(ADLDAPAdapterConstants.NDU_BO_DISPLAYNAME).get());
					}						
				
					
					if ( attrs.get(ADLDAPAdapterConstants.NDU_BO_GIVENNAME) != null ) {
						sAction = ADLDAPAdapterConstants.NDU_BO_GIVENNAME;
						boOutRecord.setGivenName((String)attrs.get(ADLDAPAdapterConstants.NDU_BO_GIVENNAME).get());
					}
	
					if ( attrs.get(ADLDAPAdapterConstants.NDU_BO_SN) != null ) {
						sAction = ADLDAPAdapterConstants.NDU_BO_SN;
						boOutRecord.setSn((String)attrs.get(ADLDAPAdapterConstants.NDU_BO_SN).get());
					}
					
					if ( attrs.get(ADLDAPAdapterConstants.NDU_BO_MAIL) != null ) {
						sAction = ADLDAPAdapterConstants.NDU_BO_MAIL;
						boOutRecord.setMail((String)attrs.get(ADLDAPAdapterConstants.NDU_BO_MAIL).get());
					}
	
				} catch (NullPointerException e) {
					log.logp(Level.SEVERE, CLASSNAME, "executeNDUAD()", "Catch NullPointerException at attribut="+sAction);
					throw new ServiceRuntimeException(ADLDAPAdapterConstants.ERR_MSG03 + " #attribute=" + sAction);
				}	
			}
		}
		return boOutRecord;
	}
	
	/**
	 * This method is a delegate method for the inherited method execute(). As this adapter is used
	 * for several directory provides, each of the provides will need some custom logics. Both for
	 * handling with different kinds of business object, but as well for various kind of exception
	 * handling based on different business logic (e.g. is an empty result the cause of a exception?)
	 * 
	 * @param context
	 * @param record
	 * @return
	 * @throws NamingException
	 * @throws ServiceBusinessException if no user is returned
	 */
	private MinsideAdapterRecord executeMinside(LdapContext context, Record record) throws NamingException {
		
		// Create the search controls 
		SearchControls searchCtls = new SearchControls();
		
		// Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		
		DataObject dataobject = ((DataObjectRecord)record).getDataObject();
		String criteria = dataobject.getString(ADLDAPAdapterConstants.MINSIDE_BO_FNR);
		String searchFilter = "(uid=" + criteria + ")";
		
		// Specify the Base for the search
		String searchBase = adldapMngConnFac.getServerSearchBaseContext();
		
		// Search for objects using the filter
		log.logp(Level.FINE, CLASSNAME, "executeMinside()", "Searching for entity with uid = '" + criteria + "'");
		NamingEnumeration answer = context.search(searchBase, searchFilter, searchCtls);
		
		MinsideAdapterRecord out = new MinsideAdapterRecord();
		if (!answer.hasMoreElements()) {
			log.logp(Level.WARNING, CLASSNAME, "executeMinside()", "Raise fault " + ADLDAPAdapterConstants.MINSIDE_FAULT_NOUSER + " because query returned no result set.");
			DataObject errorObject = DataFactory.INSTANCE.create(ADLDAPAdapterConstants.MINSIDE_FAULT_NS, ADLDAPAdapterConstants.MINSIDE_FAULT_NOUSER);
			errorObject.setString("errorMessage",  "MinSide have not entry with uid='" + criteria + "'");
			errorObject.setString("errorType", "ServiceBusinessException");
			errorObject.setString("errorSource", ADLDAPAdapterConstants.MINSIDE_MODULE_NAME);
			errorObject.setString("rootCause", "");
			throw new ServiceBusinessException(errorObject);				
		} 
		SearchResult sr = (SearchResult) answer.next();
		Attributes attributes = sr.getAttributes();
		
		// Mapping attributes one by one. From the interface point of view is it 
		// sufficient with object only having the fnr attribute
		if (null != attributes) {
			Attribute fnr = attributes.get(ADLDAPAdapterConstants.MINSIDE_LDAP_UID);
			if (null != fnr) {
				out.setFnr((String) fnr.get());
			}
			
			Attribute navn = attributes.get(ADLDAPAdapterConstants.MINSIDE_LDAP_NAME);
			if (null != navn) {
				out.setNavn((String) navn.get());
			}
			
			Attribute adresse = attributes.get(ADLDAPAdapterConstants.MINSIDE_LDAP_ADRESSE);
			if (null != adresse) {
				out.setAdresse((String) adresse.get());
			}
			
			Attribute sprak = attributes.get(ADLDAPAdapterConstants.MINSIDE_LDAP_SPRAK);
			if (null != sprak) {
				out.setSprak((String) sprak.get());
			}
		}
		
		if (answer.hasMoreElements()) {
			// The search criteria does not result in one single entry. As the interface support 
			// one and one entry only the crieria must also result in one and one only entry
			log.logp(Level.WARNING, CLASSNAME, "executeMinside()", "Raise fault " + ADLDAPAdapterConstants.MINSIDE_FAULT_BASE + ". " + ADLDAPAdapterConstants.ERR_MSG05);
			DataObject errorObject = DataFactory.INSTANCE.create(ADLDAPAdapterConstants.MINSIDE_FAULT_NS, ADLDAPAdapterConstants.MINSIDE_FAULT_BASE);
			errorObject.setString("errorMessage",  ADLDAPAdapterConstants.ERR_MSG05);
			errorObject.setString("errorType", "ServiceBusinessException");
			errorObject.setString("errorSource", ADLDAPAdapterConstants.MINSIDE_MODULE_NAME);
			errorObject.setString("rootCause", "");
			throw new ServiceBusinessException(errorObject);
		}
		
		return out;
	}
	
}