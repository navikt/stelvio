package no.nav.maven.plugins.datapower;

import no.nav.datapower.xmlmgmt.XMLMgmtException;
import no.nav.datapower.xmlmgmt.XMLMgmtRequestFactory;
import no.nav.datapower.xmlmgmt.XMLMgmtRequest;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which saves the current running configuration of the specified domain.
 *
 * @goal saveConfig
 * 
 */
public class SaveConfigMojo extends AbstractDeploymentMojo {

	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		try {
			XMLMgmtRequest request = XMLMgmtRequestFactory.createSaveConfigRequest(getDomain());
			String response = getXMLMgmtSession().doSoapRequest(request);
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to save configuration to domain '" + getDomain() + "'",e);
		}
	}
}
