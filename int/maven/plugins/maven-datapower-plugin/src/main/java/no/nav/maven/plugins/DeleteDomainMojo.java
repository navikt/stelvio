package no.nav.maven.plugins;

import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * Goal which deletes the specified domain
 * 
 * @goal deleteDomain
 * 
 * @author person4fdbf4cece95
 *
 */
public class DeleteDomainMojo extends AbstractDeviceMgmtMojo {
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing DeleteDomainMojo");
		try {
			getXMLMgmtSession().deleteDomain(getDomain());
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to delete the application domain" + getDomain(),e);
		}
	}
}
