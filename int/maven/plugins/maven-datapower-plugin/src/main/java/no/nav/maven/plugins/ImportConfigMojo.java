package no.nav.maven.plugins;

import java.io.File;

import no.nav.datapower.xmlmgmt.ImportFormat;
import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which imports a DataPower configuration into a specified device.
 *
 * @goal importConfig
 * 
 */
public class ImportConfigMojo extends AbstractDeviceMgmtMojo {


	/**
	 * @parameter expression="${format}" default-value="XML" alias="format"
	 */
	private String importFormat;
	

	/**
	 * @parameter expression="${config}" alias="config"
	 */
	private File configFile;
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		try {
			String response = getXMLMgmtSession().importConfig(configFile, ImportFormat.valueOf(importFormat));
			getLog().debug(response);
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to import configuration", e);
		}
	}
}
