package no.nav.maven.plugins.datapower;

import no.nav.datapower.xmlmgmt.XMLMgmtException;
import no.nav.datapower.xmlmgmt.XMLMgmtRequest;
import no.nav.datapower.xmlmgmt.XMLMgmtRequestFactory;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
/**
 * Goal which creates the specified domain if it does not already exist.
 *
 * @goal createDomain
 * 
 */
public class CreateDomainMojo extends AbstractDeploymentMojo {

	private static final String AMP_ENDPOINT = "/service/mgmt/amp/1.0";
	
	@Override
	protected void doExecute() throws MojoExecutionException,
			MojoFailureException {
		try {
			if(getDomain() != null && !getDomain().equalsIgnoreCase("default")){
				getLog().info("Getting domain list from device.");
				if(!domainExists(getDomain())){
					getLog().info("Sending create domain request for domain "+getDomain()+".");
					XMLMgmtRequest createDomain = XMLMgmtRequestFactory.createCreateDomainRequest(getDomain());
					getXMLMgmtSession().doSoapRequest(createDomain);
				} else {
					getLog().info("Domain "+getDomain()+ " already exist. Skipping create.");
				}
			}
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to save configuration to domain '" + getDomain() + "'",e);
		}

	}
	
	private boolean domainExists(String domain) throws XMLMgmtException{
		String xmlmgmtEndpoint = getXmlMgmtEndpoint();
		XMLMgmtRequest getDomainList = XMLMgmtRequestFactory.createGetDomainListRequest();
		setXmlMgmtEndpoint(AMP_ENDPOINT);
		String domainList = createXMLMgmtSession().doSoapRequest(getDomainList);
		setXmlMgmtEndpoint(xmlmgmtEndpoint);
		return domainList == null ? true : domainList.contains("Domain>"+domain+"</");
	}
	
}
