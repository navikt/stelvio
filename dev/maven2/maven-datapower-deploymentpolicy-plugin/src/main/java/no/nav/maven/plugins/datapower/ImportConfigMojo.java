package no.nav.maven.plugins.datapower;
import java.io.File;
import java.net.URL;
import java.util.Properties;

import no.nav.datapower.xmlmgmt.ImportFormat;
import no.nav.datapower.xmlmgmt.XMLMgmtException;
import no.nav.datapower.xmlmgmt.XMLMgmtRequestFactory;
import no.nav.datapower.xmlmgmt.XMLMgmtRequest;
import no.nav.datapower.xmlmgmt.XMLMgmtUtil;


import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;



/**
 * Goal which imports a DataPower configuration into a specified device.
 *
 * @goal importConfig
 * 
 */
public class ImportConfigMojo extends AbstractDeploymentMojo {

	
	
	/**
	 * @parameter expression="${config}" alias="config"
	 */
	private File configFile;
	
	/**
	 * @parameter expression="${deploymentPolicy}" alias="deploymentPolicy"
	 */
	private String deploymentPolicyName;
	

	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		try {
			XMLMgmtRequest request = XMLMgmtRequestFactory.createImportConfigRequest(getDomain(), configFile, deploymentPolicyName);
			String response = getXMLMgmtSession().doSoapRequest(request);
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to import configuration", e);
		}
		
	}
	
}

