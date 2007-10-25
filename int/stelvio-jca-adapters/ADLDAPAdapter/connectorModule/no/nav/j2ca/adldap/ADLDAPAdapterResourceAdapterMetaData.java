package no.nav.j2ca.adldap;

import javax.resource.ResourceException;

import com.ibm.j2ca.base.WBIResourceAdapterMetadata;

public class ADLDAPAdapterResourceAdapterMetaData extends WBIResourceAdapterMetadata 
{
		/**
		 * @throws ResourceException
		 */
		public ADLDAPAdapterResourceAdapterMetaData() throws ResourceException
		{
			super("NAV AD Adapter for LDAP", "NAV", "6.0.2", true);
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