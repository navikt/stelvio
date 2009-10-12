package no.nav.maven.plugins.datapower;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;


import no.nav.datapower.xmlmgmt.ImportFormat;
import no.nav.datapower.xmlmgmt.XMLMgmtDeploymentPolicy;
import no.nav.datapower.xmlmgmt.XMLMgmtException;
import no.nav.datapower.xmlmgmt.XMLMgmtRequestFactory;
import no.nav.datapower.xmlmgmt.XMLMgmtRequest;
import no.nav.datapower.xmlmgmt.XMLMgmtUtil;


import org.apache.commons.io.FileUtils;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdom.JDOMException;

/**
 * Goal which imports a DataPower deployment policy into a specified device.
 *
 * @goal importDeploymentPolicy
 * 
 */
public class ImportDeploymentPolicyMojo extends AbstractDeploymentMojo {

	/**
	 * @parameter expression="${deploymentPolicyFile}" alias="deploymentPolicyFile"
	 */
	private File deploymentPolicyFile;
	
	/**
	 * @parameter expression="${overriddenProperties}" alias="overriddenProperties"
	 */
	private File overriddenProperties;
	
	protected XMLMgmtRequest createImportDeploymentPolicyRequest(File deploymentPolicyFile, File overriddenProperties) throws XMLMgmtException {
		ImportFormat format = deploymentPolicyFile.getName().endsWith(".zip") ? 
				ImportFormat.ZIP : ImportFormat.XML;
		
		XMLMgmtDeploymentPolicy policy = overrideDeploymentPolicy(deploymentPolicyFile, overriddenProperties);
		
		String base64EncodedFile = XMLMgmtUtil.getBase64EncodedString(policy.getDeploymentPolicyXML());
		if(base64EncodedFile == null){
			throw new XMLMgmtException("Could not base64 encode file " + deploymentPolicyFile);
		}
		return XMLMgmtRequestFactory.createImportConfigRequest(getDomain(), base64EncodedFile, format, null);
	}
	
	protected XMLMgmtDeploymentPolicy overrideDeploymentPolicy(File deploymentPolicyFile, File overridesFile) throws XMLMgmtException {
		try {
			Properties overrides = new Properties();
			overrides.load(FileUtils.openInputStream(overridesFile));
			XMLMgmtDeploymentPolicy deploymentPolicy = new XMLMgmtDeploymentPolicy(deploymentPolicyFile);
			if(overrides != null){
				deploymentPolicy.overrideProperties(overrides);
			}
			return deploymentPolicy;	
		} catch (IOException e) {
			throw new XMLMgmtException("Failed to override deployment policy values.",e);
			//REMOVE JDOM EXCEPTION LATER
		} catch (JDOMException e){
			throw new XMLMgmtException("Failed to override deployment policy values.",e);
		}
		
	}
	
	@Override
	protected void doExecute() throws MojoExecutionException,
			MojoFailureException {
		
		try {
			
			XMLMgmtRequest request = createImportDeploymentPolicyRequest(this.deploymentPolicyFile, this.overriddenProperties);
			String response = getXMLMgmtSession().doSoapRequest(request);
			//getLog().debug("Response importDeploymentPolicy:\n" + response);
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to import deployment policy.", e);
		}	
	}
	
	
	
	
	
}
