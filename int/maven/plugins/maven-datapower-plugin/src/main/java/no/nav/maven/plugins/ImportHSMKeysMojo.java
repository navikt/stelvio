package no.nav.maven.plugins;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import no.nav.datapower.xmlmgmt.DeviceFileStore;
import no.nav.datapower.xmlmgmt.XMLMgmtException;
import no.nav.maven.config.KeyInfo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which imports keys into the Hardware Security Module (HSM).
 *
 * @goal importHSMKeys
 * 
 * @author Petter Solberg, Accenture
 * @author Christian Askeland, Accenture
 */

public class ImportHSMKeysMojo extends AbstractDeviceMgmtMojo {
	
	/**
	 * An array with the key info
	 * @parameter
	 */
	private KeyInfo[] keyInfos;

	private static final String CERTIFICATE_DRIECTORY = "cert:///";
	private static final String RESPONSE_START = "<dp:result>";
	private static final String RESPONSE_END = "</dp:result>";
	private static final String RESPONSE_OK = "OK";
	
	@Override
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing ImportHSMKeysMojo");
		
		getLog().info("Importing HSM " + keyInfos.length + " keys.");
		
		for(KeyInfo k : keyInfos){
			getLog().info("Key: " + k.getName());
			if(k.isValid()){
				importKey(k.getName(), k.getFileName(), k.getPassword(), k.isKwkExport());
			}
		}
	}


	/**
	 * Imports a key to the HSM module.
	 * @param keyName	Name of the key in the HSM module
	 * @param keyFileName	File-name of the key in the domain
	 * @param keyPassword	Password for the key
	 * @param keyKwkExport	If the key should be exportable to another HSM by a key wrapping key (KWK)
	 * @throws MojoExecutionException If an error occurs.
	 */
	private void importKey(String keyName, String keyFileName, String keyPassword, boolean keyKwkExport)	throws MojoExecutionException {
		boolean kwkExportable = keyKwkExport;
		
		getLog().debug("Importing '" + keyFileName + "' as '" + keyName +"' with kwkExportable set to '" + kwkExportable + "'");
		
		try {
			String response = getXMLMgmtSession().importHSMKey(keyName, CERTIFICATE_DRIECTORY + keyFileName, keyPassword, kwkExportable);			
			
			// Check if the response is OK by verifying there is a <dp:result> containing "OK". If not true, return an error
			int resultBeginningIndex = response.indexOf(RESPONSE_START, 0);
			int resultEndIndex = response.indexOf(RESPONSE_END, 0);
			
			if(resultBeginningIndex > 0 && resultEndIndex > resultBeginningIndex){
				if(response.substring(resultBeginningIndex, resultEndIndex).indexOf(RESPONSE_OK) > 0){
					return;
				}
			}
			getLog().error("Importing '" + keyFileName + "' as '" + keyName +"' with kwkExportable set to '" + kwkExportable + "' failed\r\nGot response:" + response);
			throw new MojoExecutionException("Failed to import HSM key \"" + keyName + "\" for domain '" + getDomain() + "' - Response: " + response);
			
		} catch (XMLMgmtException e) {
			getLog().error("Importing '" + keyFileName + "' as '" + keyName +"' with kwkExportable set to '" + kwkExportable + "' failed");
			getLog().error(e.getLocalizedMessage());
			throw new MojoExecutionException("Failed to import HSM key \"" + keyName + "\" for domain '" + getDomain() + "'",e);
		}
	}
}
