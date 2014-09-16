package no.nav.maven.plugins;

import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which updates Vera with version numbers.
 *
 * @goal writeConfigVersion
 * 
 */
public class WriteConfigVersionMojo extends AbstractDeviceMgmtMojo {

	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		try {
			getLog().info("WriteConfigVersionMojo is not created yet.");
			getLog().info("Tried to write for domain " + getDomain() + ".");
		} catch (Exception e) {
			throw new MojoExecutionException("Failed to write config version for domain " + getDomain() + "'",e);
		}
	}
}
