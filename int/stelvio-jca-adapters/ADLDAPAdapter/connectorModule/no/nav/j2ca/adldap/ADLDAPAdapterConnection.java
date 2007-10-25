package no.nav.j2ca.adldap;

import javax.resource.ResourceException;
import com.ibm.j2ca.base.WBIConnection;
import com.ibm.j2ca.base.WBIManagedConnection;
import com.ibm.j2ca.extension.logging.LogUtils;

public class ADLDAPAdapterConnection extends WBIConnection {
	
	/**
	 * @param managedConnection
	 * @throws javax.resource.ResourceException
	 * @version 1.0.1 persona2c5e3b49756 updated
	 */
	public ADLDAPAdapterConnection(com.ibm.j2ca.base.WBIManagedConnection managedConnection)
			throws javax.resource.ResourceException {
		super((WBIManagedConnection)managedConnection);
	}

	/* (non-Javadoc)
	 * @see javax.resource.cci.Connection#createInteraction()
	 * @version 1.0.1 persona2c5e3b49756 updated
	 **/
	public javax.resource.cci.Interaction createInteraction()
			throws javax.resource.ResourceException {
		
		super.checkValidity();
		return new ADLDAPAdapterInteraction(this);
	}

	/* (non-Javadoc)
	 * @see javax.resource.cci.Connection#close()
	 * @version 1.0.1 persona2c5e3b49756 generated
	 */
	public void close() throws ResourceException {
		super.close();
	}

	/* (non-Javadoc)
	 * @see com.ibm.j2ca.base.WBIConnection#setLogUtils(com.ibm.j2ca.extension.logging.LogUtils)
	 * @version 1.0.1 persona2c5e3b49756 generated
	 */
	protected void setLogUtils(LogUtils logUtils) {
		super.setLogUtils(logUtils);
	}
}