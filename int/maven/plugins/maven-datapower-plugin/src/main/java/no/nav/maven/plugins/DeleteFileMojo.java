package no.nav.maven.plugins;

import no.nav.datapower.xmlmgmt.DeviceFileStore;
import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * Goal which deletes a specific file from the specified locations in the specified domain
 * 
 * @goal deleteFile
 * 
 * @author person4fdbf4cece95
 *
 */
public class DeleteFileMojo extends AbstractDeviceMgmtMojo {
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing DeleteFileMojo");
		try {
			getXMLMgmtSession().deleteFile("service-registry.xml", DeviceFileStore.LOCAL);
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to delete file in the local:/// directory on the DataPower device",e);
		}
	}
}
