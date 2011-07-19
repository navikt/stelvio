package no.nav.maven.plugins;

import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * Goal which creates a new domain with the specified name
 * 
 * @goal createDomain
 * 
 * @author person4fdbf4cece95
 *
 */
public class CreateDomainMojo extends AbstractDeviceMgmtMojo {
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing CreateDomainMojo");
		try {
			getXMLMgmtSession().createDomain(getDomain());
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to create the application domain" + getDomain(),e);
		}
	}
}
