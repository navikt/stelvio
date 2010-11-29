package no.stelvio.presentation.security.sso.ibm;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingException;

import com.ibm.websphere.security.CustomRegistryException;
import com.ibm.websphere.security.EntryNotFoundException;

public class MockWebsphereSubjectMapper extends WebsphereSubjectMapper {


	/* (non-Javadoc)
	 * @see no.stelvio.presentation.security.sso.ibm.WebsphereSubjectMapper#getLdapGroups(java.util.List)
	 */
	@Override
	protected List<String> getLdapGroups(List<String> groups) throws EntryNotFoundException, NamingException, CustomRegistryException, RemoteException {
		
		return groups;
	}
	
}
