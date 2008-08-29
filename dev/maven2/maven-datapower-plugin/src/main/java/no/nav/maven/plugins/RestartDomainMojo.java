package no.nav.maven.plugins;

import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which executes a restart of the specified domain.
 *
 * @goal restartDomain
 * 
 */
public class RestartDomainMojo extends AbstractDeviceMgmtMojo {

	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		try {
			String response = getXMLMgmtSession().restartDomain();
			getLog().debug(response);
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed restart domain '" + getDomain() + "'",e);
		}
	}
}
