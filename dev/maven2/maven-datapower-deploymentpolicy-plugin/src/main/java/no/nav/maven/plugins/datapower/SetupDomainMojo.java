package no.nav.maven.plugins.datapower;

import no.nav.datapower.xmlmgmt.XMLMgmtException;
import no.nav.datapower.xmlmgmt.XMLMgmtRequestFactory;
import no.nav.datapower.xmlmgmt.XMLMgmtRequest;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
/**
 * Goal which deletes the specified domain and creates a fresh new domain with the same name.
 * In addition it will setup the required host aliases.
 *
 * @goal setupDomain
 * 
 */
public class SetupDomainMojo extends AbstractDeploymentMojo {

	/**
     * The hostAlias used by the domain
     * 
     * @parameter expression="${hostAlias}" alias="hostAlias"
     * 
     */    
    private String hostAlias;
    
    /**
     * The ipAddress used by the domain which should be mapped to the hostAliaas
     * 
     * @parameter expression="${ipAddress}" alias="ipAddress"
     * 
     */    
    private String ipAddress;
	
	@Override
	protected void doExecute() throws MojoExecutionException,
			MojoFailureException {
		try {
			if(!getDomain().equalsIgnoreCase("default")){
				if(hostAlias != null && ipAddress != null ){
					XMLMgmtRequest request = XMLMgmtRequestFactory.createSetHostAliasRequest(hostAlias, ipAddress);
					getXMLMgmtSession().doSoapRequest(request);
				}
				XMLMgmtRequest delDomain = XMLMgmtRequestFactory.createDeleteDomainRequest(getDomain());
				XMLMgmtRequest createDomain = XMLMgmtRequestFactory.createCreateDomainRequest(getDomain());
				getXMLMgmtSession().doSoapRequest(delDomain);
				getXMLMgmtSession().doSoapRequest(createDomain);
			}
			
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to save configuration to domain '" + getDomain() + "'",e);
		}

	}

}
