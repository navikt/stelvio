package no.nav.j2ca.adldap;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.resource.ResourceException;

import com.ibm.j2ca.base.WBIResourceAdapterMetadata;

public class ADLDAPAdapterResourceAdapterMetaData extends WBIResourceAdapterMetadata 
{
	
		WBIResourceAdapterMetadata adldapMetadata = null;
		
		//	for logging
		private static Logger log = Logger.getLogger(ADLDAPAdapterResourceAdapterMetaData.class.getName());
		private static final String CLASSNAME = "ADLDAPAdapterResourceAdapterMetaData"; 
	
		/**
		 * @throws ResourceException
		 */
		public ADLDAPAdapterResourceAdapterMetaData() throws ResourceException
		{
	    	WBIResourceAdapterMetadata adldapMetadata = new WBIResourceAdapterMetadata("NAV ADLDAP Adapter", "NAV", "6.1.0", false);
	    	log.logp(Level.FINE, CLASSNAME, "ADLDAPAdapterResourceAdapterMetaData()", "Name:" + adldapMetadata.getAdapterName());
	    	log.logp(Level.FINE, CLASSNAME, "ADLDAPAdapterResourceAdapterMetaData()", "Version:" + adldapMetadata.getAdapterVersion());
	    	log.logp(Level.FINE, CLASSNAME, "ADLDAPAdapterResourceAdapterMetaData()", "Vnedor:" + adldapMetadata.getAdapterVendorName());
		}

		/**
		 * @param name
		 * @param vendor
		 * @param version
		 * @param supportsLocalTx
		 * @throws ResourceException
		 */
		public ADLDAPAdapterResourceAdapterMetaData(String name, String vendor, String version, boolean supportsLocalTx) throws ResourceException
		{
			super(name, vendor, version, supportsLocalTx);
		}	
}