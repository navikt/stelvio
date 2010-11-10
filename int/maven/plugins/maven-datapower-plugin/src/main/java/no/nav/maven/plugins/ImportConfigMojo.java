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
	 * @parameter expression="${config}"
	 * @required
	 */
	private File config;

	protected void doExecute() throws MojoExecutionException, MojoFailureException {

		if (config.isDirectory()) {

			for (File f : config.listFiles()) {
				importConfigFile(f.getAbsolutePath(), getFormat(f));
			}
			
		} else {
			importConfigFile(config.getAbsolutePath(), getFormat(config));
		}

	}

	private void importConfigFile(String configFile, ImportFormat importFormat) throws MojoExecutionException {
		try {

			getLog().info("Importing config file: " + configFile + " ...");
			String response = getXMLMgmtSession().importConfig(configFile, importFormat);
			getLog().debug(response);

		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to import configuration", e);
		}
	}

	private ImportFormat getFormat(File f) throws MojoFailureException {

		if (f.getName().endsWith(".xml")) {
			return ImportFormat.valueOf("XML");
		} else if (f.getName().endsWith(".zip")) {
			return ImportFormat.valueOf("ZIP");
		} else {
			throw new MojoFailureException("Unsupported file format for file: " + f + ". Only XML and ZIP are supported.");
		}
	}

}