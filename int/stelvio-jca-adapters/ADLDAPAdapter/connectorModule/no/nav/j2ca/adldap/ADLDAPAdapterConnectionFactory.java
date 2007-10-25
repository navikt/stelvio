package no.nav.j2ca.adldap;

import com.ibm.j2ca.base.WBIConnectionFactory;
import com.ibm.j2ca.extension.logging.LogUtils;

public class ADLDAPAdapterConnectionFactory extends WBIConnectionFactory {


	public ADLDAPAdapterConnectionFactory(javax.resource.spi.ConnectionManager connMngr,
			com.ibm.j2ca.base.WBIManagedConnectionFactory mcf) 
	{
	    super(connMngr, mcf);
	}

    public void setLogUtils(LogUtils logUtils)
    {
        super.setLogUtils(logUtils);
    }	
	
}