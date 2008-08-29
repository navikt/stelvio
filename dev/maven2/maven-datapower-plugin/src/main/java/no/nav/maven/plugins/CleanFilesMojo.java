package no.nav.maven.plugins;

import no.nav.datapower.xmlmgmt.DeviceFileStore;
import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * Goal which deletes all files from the specified locations in the specified domain
 * 
 * @goal cleanFiles
 * 
 * @author utvikler
 *
 */
public class CleanFilesMojo extends AbstractDeviceMgmtMojo {
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing CleanFilesMojo");
		try {
			getXMLMgmtSession().removeDirs(DeviceFileStore.LOCAL, "aaa", "wsdl", "xslt");
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to delete files in the local:/// directory on the DataPower device",e);
		}
	}
}
