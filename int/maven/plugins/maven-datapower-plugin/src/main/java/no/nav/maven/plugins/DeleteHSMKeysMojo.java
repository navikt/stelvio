package no.nav.maven.plugins;

import java.util.ArrayList;
import java.util.Iterator;

import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which permanently deletes HSM keys in the specified domain.
 *
 * @goal deleteHSMKeys
 * 
 * @author Petter Solberg, Accenture
 */

public class DeleteHSMKeysMojo extends AbstractDeviceMgmtMojo {

	private static final String HSM_KEY_STATUS_START = "<HSMKeyStatus xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\">";
	private static final String HSM_KEY_STATUS_END = "</HSMKeyStatus>";
	private static final String KEY_HANDLE_START = "<KeyHandle>";
	private static final String KEY_HANDLE_END = "</KeyHandle>";
	private static final String KEY_TYPE_START = "<KeyType>";
	private static final String KEY_TYPE_END = "</KeyType>";
	private static final String RESPONSE_START = "<dp:result>";
	private static final String RESPONSE_END = "</dp:result>";
	private static final String RESPONSE_OK = "OK";
	
	@Override
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing DeleteHSMKeysMojo");
		try {
			// Get the status of the HSM keys.
			String response = getXMLMgmtSession().getStatus("HSMKeyStatus");
			getLog().debug("HSM Status response:\r\n" + response);		
			
			ArrayList<String> hsmKeyStatusList = getHSMkeyStatusList(response);
			getLog().info("Deleting " + hsmKeyStatusList.size() + " old HSM-keys");
			for (Iterator<String> iterator = hsmKeyStatusList.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				deleteHSMKey(string);
			}
			
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed get HSM key status");
		}
		
	}
	/**
	 * Returns an ArrrayList of multiple HSM key-status information.
	 * @param The response String from a HSM status query.
	 * @return A list containing Information about each key.
	 */
	private ArrayList<String> getHSMkeyStatusList(String response){
		String[] keyInfo =response.split(HSM_KEY_STATUS_START);
		ArrayList<String> keyInfoList = new ArrayList<String>();
		
		for (int i = 0; i < keyInfo.length; i++) {
			if(keyInfo[i].contains(HSM_KEY_STATUS_END)){
				keyInfoList.add(keyInfo[i].split(HSM_KEY_STATUS_END)[0]);
			}
		}
		return keyInfoList;
	}
	
	/**
	 * Delete a HSM key from the information contained in the keyStatus.
	 * @param keyStatus Key status information
	 * @throws MojoExecutionException If key deletion did not succeed
	 */
	private void deleteHSMKey(String keyStatus) throws MojoExecutionException{
		
		// Extract the key handle
		int kHStartIndex = keyStatus.indexOf(KEY_HANDLE_START);
		int kHEndIndex = keyStatus.indexOf(KEY_HANDLE_END);
		if(kHStartIndex < 0 || kHEndIndex < kHStartIndex){
			throw new MojoExecutionException("Failed to delete HSM key with keyStatus '" + keyStatus + "' - Could not parse keyStatus");
		}
		String keyHandle = keyStatus.substring(kHStartIndex + KEY_HANDLE_START.length(), kHEndIndex);
		
		// Extract key type
		int kTStartIndex = keyStatus.indexOf(KEY_TYPE_START);
		int kTEndIndex = keyStatus.indexOf(KEY_TYPE_END);
		if(kTStartIndex < 0 || kTEndIndex < kTStartIndex){
			throw new MojoExecutionException("Failed to delete HSM key with keyStatus '" + keyStatus + "' - Could not parse keyType");
		}
		String keyType = keyStatus.substring(kTStartIndex + KEY_TYPE_START.length(), kTEndIndex);
		
		getLog().debug("Deleting HSM key with keyHandle '" + keyHandle + "'");
		
		// Send a request to delete the key
		try {
			String response = getXMLMgmtSession().deleteHSMKey(keyHandle, keyType);			
			
			// Check if the response is OK by verifying there is a <dp:result> containing "OK". If not true, return an error
			int resultBeginningIndex =response.indexOf(RESPONSE_START, 0);
			int resultEndIndex = response.indexOf(RESPONSE_END, 0);
			if(resultBeginningIndex > 0 && resultEndIndex > resultBeginningIndex){
				if(response.substring(resultBeginningIndex, resultEndIndex).indexOf(RESPONSE_OK) > 0){
					return;
				}
			}
			throw new MojoExecutionException("Failed to delete HSM key with keyHanle '" + keyHandle + "' of type '" + keyType + "'- Response: " + response);
			
		} catch (XMLMgmtException e) {
			throw new MojoExecutionException("Failed to delete HSM key with keyHanle '" + keyHandle + "' of type '" + keyType + "'");
		}
	}
}
