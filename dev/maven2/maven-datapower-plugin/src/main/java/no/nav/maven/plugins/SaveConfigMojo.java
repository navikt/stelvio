package no.nav.maven.plugins;

import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which saves the current running configuration of the specified domain.
 *
 * @goal saveConfig
 * 
 */
public class SaveConfigMojo extends AbstractDeviceMgmtMojo {

	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		try {
			String response = getXMLMgmtSession().saveConfig();
			getLog().debug(response);
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to save configuration to domain '" + getDomain() + "'",e);
		}
	}
}
