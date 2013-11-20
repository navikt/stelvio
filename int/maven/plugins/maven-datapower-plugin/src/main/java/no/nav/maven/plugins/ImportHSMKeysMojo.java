package no.nav.maven.plugins;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import no.nav.datapower.xmlmgmt.DeviceFileStore;
import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which imports keys into the Hardware Security Module (HSM).
 *
 * @goal importHSMKeys
 * 
 * @author Petter Solberg, Accenture
 */

public class ImportHSMKeysMojo extends AbstractDeviceMgmtMojo {

	/**
	 * A map containing the key information to be inported into the HSM.
	 * @parameter expression="${hsmImport.keyInfo}" alias="keyInfo"
	 */
	private Map<String, String> keyInfo;

	private static final String[] KEY_INFO_ARGUMENTS = {"name", "fileName", "password", "kwkExport"};
	private static final String CERTIFICATE_DRIECTORY = "cert:///";
	private static final String KWK_TRUE = "true";
	private static final String RESPONSE_START = "<dp:result>";
	private static final String RESPONSE_END = "</dp:result>";
	private static final String RESPONSE_OK = "OK";
	
	/**
	 * Maximum number of possible keys to import to the HSM. This limit can be set to an arbitrary positive number.
	 */
	private static final int MAXIMUM_ARGUMENTS = 10;
	
	@Override
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing ImportHSMKeysMojo");
		
		HashSet<String> importedKeys = new HashSet<String>();
		
		// Iterate over all possible arguments an check if they are present.
		getLog().debug("Importing HSM keys");
		for (int i = 1; i < MAXIMUM_ARGUMENTS; i++) {
			if(keyArgumentExists(i)){
				
				String keyName = KEY_INFO_ARGUMENTS[0];
				String keyFileName = KEY_INFO_ARGUMENTS[1];
				String keyPassword = KEY_INFO_ARGUMENTS[2];
				String keyKwkExport = KEY_INFO_ARGUMENTS[3];
				if(i!=1){
					keyName += i;
					keyFileName += i;
					keyPassword += i;
					keyKwkExport += i;
				}
				importKey(keyInfo.get(keyName), keyInfo.get(keyFileName), keyInfo.get(keyPassword), keyInfo.get(keyKwkExport));
				importedKeys.add(keyInfo.get(keyFileName));
			}
		}
		
		// DELETE ALL FILES IN CERT:/// DIRECTORY
		for (Iterator<String> iterator = importedKeys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			getLog().debug("Deleting key '" + key + "'");
			try {
				String response = getXMLMgmtSession().deleteFile(key, DeviceFileStore.CERT);
				
				// Verify the deletion.
				int resultBeginningIndex =response.indexOf("<dp:result>", 0);
				int resultEndIndex = response.indexOf("</dp:result>", 0);
				
				if(resultBeginningIndex > 0 && resultEndIndex > resultBeginningIndex){
					if(response.substring(resultBeginningIndex, resultEndIndex).indexOf("OK") > 0){
						continue;
					}
				}
				getLog().error("Removal of key '" + key + "' failed. Got response:\r\n" + response);
				throw new MojoExecutionException("Failed to delete key '" + key + "'");
				
			} catch (XMLMgmtException e) {
				throw new MojoExecutionException("Failed to delete key '" + key + "'",e);
			}
		}
	}
	
	/**
	 * Checks if the i'th key is given as an argument.
	 * @param i the key index
	 * @return True if the key is given as an argument
	 */
	private boolean keyArgumentExists(int i) {
		boolean exists = true;
		for (int j = 0; j < KEY_INFO_ARGUMENTS.length; j++) {
			String keyPrefix =KEY_INFO_ARGUMENTS[j];
			if(i!=1){
				keyPrefix+=i;				
			}
			if(!keyInfo.containsKey(keyPrefix)){
				exists = false;
			}
		}
		return exists;
	}

	/**
	 * Imports a key to the HSM module.
	 * @param keyName	Name of the key in the HSM module
	 * @param keyFileName	File-name of the key in the domain
	 * @param keyPassword	Password for the key
	 * @param keyKwkExport	If the key should be exportable to another HSM by a key wrapping key (KWK)
	 * @throws MojoExecutionException If an error occurs.
	 */
	private void importKey(String keyName, String keyFileName, String keyPassword, String keyKwkExport)	throws MojoExecutionException {
		boolean kwkExportable = false;
		if(keyKwkExport.equals(KWK_TRUE)){
			kwkExportable = true;
		}
		getLog().debug("Importing '" + keyFileName + "' as '" + keyName +"' with kwkExportable set to '" + kwkExportable + "'");
		try {
			String response = getXMLMgmtSession().importHSMKey(keyName, CERTIFICATE_DRIECTORY + keyFileName, keyPassword, kwkExportable);			
			
			// Check if the response is OK by verifying there is a <dp:result> containing "OK". If not true, return an error
			int resultBeginningIndex =response.indexOf(RESPONSE_START, 0);
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
