package no.nav.datapower.xmlmgmt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.jdom.Element;
import org.jdom.JDOMException;

import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.DPFileUtils;

public class XMLMgmtRequestFactory {
	
	private class AMPCmd {}
	
	private class GetDomainListCmd extends AMPCmd{
		private static final String TEMPLATE = "xmlmgmt-commands/get-domain-list.xml";
	}
	
	private class SOMACmd {
		private static final String DOMAIN_XPATH = "/request/@domain";
	}
	
	private class DoImportCmd extends SOMACmd{
		
		private static final String DO_IMPORT_SOMAv2004_TEMPLATE = "xmlmgmt-commands/do-import.xml";
		private static final String DO_IMPORT_TEMPLATE = 
									"xmlmgmt-commands/do-import-with-deployment-policy.xml";
		private static final String DEPLOYMENT_POLICY_XPATH =  "/request/do-import/@deployment-policy";
		private static final String SOURCE_TYPE_XPATH = "/request/do-import/@source-type";
		private static final String INPUT_FILE_XPATH  = "/request/do-import/input-file";
	}
	
	private class DoActionCmd extends SOMACmd{
		private static final String TEMPLATE = "xmlmgmt-commands/do-action.xml";
		private static final String CREATE_DIR_XPATH =  "/request/do-action/CreateDir/Dir";
		private static final String RESTART_DOMAIN_XPATH =  "/request/do-action/RestartThisDomain";
		private static final String SAVE_CONFIG_XPATH =  "/request/do-action/SaveConfig";
	}
	private class SetFileCmd extends SOMACmd{
		private static final String TEMPLATE = "xmlmgmt-commands/set-file.xml";
		private static final String SET_FILE_NAME_XPATH =  "/request/set-file/@name";
		private static final String SET_FILE_CONTENT_XPATH =  "/request/set-file";
	}
	private class SetConfigCmd extends SOMACmd{
		private static final String DOMAIN_TEMPLATE = "xmlmgmt-commands/set-config-domain.xml";
		private static final String HOST_ALIAS_TEMPLATE = "xmlmgmt-commands/set-config-hostalias.xml";
		private static final String DOMAIN_NAME_XPATH =  "/request/set-config/Domain/@name";
		private static final String HOST_ALIAS_NAME_XPATH =  "/request/set-config/HostAlias/@name";
		private static final String HOST_ALIAS_IP_ADDRESS_XPATH =  "/request/set-config/HostAlias/IPAddress";
	}
	private class DelConfigCmd extends SOMACmd{
		private static final String DELETE_DOMAIN_TEMPLATE = "xmlmgmt-commands/del-config-domain.xml";
		private static final String DOMAIN_NAME_XPATH =  "/request/del-config/Domain/@name";
	}
	
	
	private static Properties getBaseSOMARequestConfig(String domain){
		Properties p = new Properties();
		p.setProperty(SOMACmd.DOMAIN_XPATH, domain);
		return p;
	}
	
	private static XMLMgmtRequest merge(List<XMLMgmtRequest> requestList, String elementToMerge){
		Iterator<XMLMgmtRequest> requests = requestList.iterator();
		if(requests.hasNext()){
			XMLMgmtRequest merged = requests.next();
			while(requests.hasNext() && merged != null){
				merged = XMLMgmtRequest.merge(merged, requests.next(), elementToMerge);
			}
			return merged;
		} else {
			return null;
		}
		
	}
	
	public static XMLMgmtRequest createImportConfigRequest(String domain, File configFile, String deploymentPolicyName) throws XMLMgmtException {
		ImportFormat format = configFile.getName().endsWith(".zip") ? 
				ImportFormat.ZIP : ImportFormat.XML;
		
		String base64EncodedFile = XMLMgmtUtil.getBase64EncodedFile(configFile);
		if(base64EncodedFile == null){
			throw new XMLMgmtException("Could not base64 encode file " + configFile);
		}
		
		return createImportConfigRequest(domain, base64EncodedFile, format, deploymentPolicyName);
	}
	
	public static XMLMgmtRequest createImportConfigRequest(String domain, String base64EncodedFile, ImportFormat format, String deploymentPolicyName) throws XMLMgmtException {	
		Properties config = getBaseSOMARequestConfig(domain);
		config.setProperty(DoImportCmd.SOURCE_TYPE_XPATH, format.name());
		config.setProperty(DoImportCmd.INPUT_FILE_XPATH, base64EncodedFile);
		if(deploymentPolicyName != null){
			config.setProperty(DoImportCmd.DEPLOYMENT_POLICY_XPATH, deploymentPolicyName);
			return new XMLMgmtRequest(DoImportCmd.DO_IMPORT_TEMPLATE,config);
		} else {
			return new XMLMgmtRequest(DoImportCmd.DO_IMPORT_SOMAv2004_TEMPLATE,config);
		}
	}
	
	public static XMLMgmtRequest createCreateDirectoriesRequest(String domain, File rootDirectory, DeviceFileStore fileStore) throws XMLMgmtException{
		try {
			List<String> dirList = XMLMgmtUtil.getCreateDirectoryList(rootDirectory, fileStore);
			ArrayList<XMLMgmtRequest> requestList = new ArrayList<XMLMgmtRequest>();

			XMLMgmtRequest baseRequest = new XMLMgmtRequest(DoActionCmd.TEMPLATE);		
			for (String string : dirList) {
				Properties p = getBaseSOMARequestConfig(domain);
				p.setProperty(DoActionCmd.CREATE_DIR_XPATH,string);
				XMLMgmtRequest request = baseRequest.updateNodeTree(p);
				requestList.add(request);
			}
			
			//Merge all requests into one
			return merge(requestList, "do-action");
			
		} catch (IOException e){
			throw new XMLMgmtException("Could get the directories to create.", e);
		} catch (JDOMException e){
			throw new XMLMgmtException("Could not create XMLMgmtRequest.", e);
		}
	}
	
	public static XMLMgmtRequest createSetFilesRequest(String domain, File source, DeviceFileStore location, String... excludes)throws XMLMgmtException{
		try{
			Map<String,String> map = XMLMgmtUtil.getBase64EncodedFileSet(source,location, excludes);	
			Set<Entry<String, String>> set = map.entrySet();
			
			ArrayList<XMLMgmtRequest> requestList = new ArrayList<XMLMgmtRequest>();

			XMLMgmtRequest baseRequest = new XMLMgmtRequest(SetFileCmd.TEMPLATE);		
			for (Entry<String, String> entry : set) {
				Properties p = getBaseSOMARequestConfig(domain);
				p.setProperty(SetFileCmd.SET_FILE_NAME_XPATH, entry.getKey());
				p.setProperty(SetFileCmd.SET_FILE_CONTENT_XPATH, entry.getValue());
				XMLMgmtRequest request = baseRequest.updateNodeTree(p);
				requestList.add(request);
			}		
			//Merge all requests into one
			
			return merge(requestList, "request");	
		
		} catch (IOException e){
			throw new XMLMgmtException("Could get the list of files to import.", e);
		} catch (JDOMException e){
			throw new XMLMgmtException("Could not create XMLMgmtRequest.", e);
		}	
	}
	public static XMLMgmtRequest createRestartDomainRequest(String domain){	
		Properties p = getBaseSOMARequestConfig(domain);
		p.setProperty(DoActionCmd.RESTART_DOMAIN_XPATH, "");
		XMLMgmtRequest request = new XMLMgmtRequest(DoActionCmd.TEMPLATE, p);
		return request;
		
	}
	public static XMLMgmtRequest createSaveConfigRequest(String domain){
		Properties p = getBaseSOMARequestConfig(domain);
		p.setProperty(DoActionCmd.SAVE_CONFIG_XPATH, "");
		XMLMgmtRequest request = new XMLMgmtRequest(DoActionCmd.TEMPLATE, p);
		return request;
	}
	
	public static XMLMgmtRequest createCreateDomainRequest(String domainToCreate){	
		Properties p = getBaseSOMARequestConfig("default");
		p.setProperty(SetConfigCmd.DOMAIN_NAME_XPATH, domainToCreate);
		XMLMgmtRequest request = new XMLMgmtRequest(SetConfigCmd.DOMAIN_TEMPLATE, p);
		return request;
	}
	
	public static XMLMgmtRequest createDeleteDomainRequest(String domainToDelete){
		Properties p = getBaseSOMARequestConfig("default");
		p.setProperty(DelConfigCmd.DOMAIN_NAME_XPATH, domainToDelete);
		XMLMgmtRequest request = new XMLMgmtRequest(DelConfigCmd.DELETE_DOMAIN_TEMPLATE, p);
		return request;
	}
	
	public static XMLMgmtRequest createSetHostAliasRequest(String hostAlias, String ipAddress){
		Properties p = getBaseSOMARequestConfig("default");
		p.setProperty(SetConfigCmd.HOST_ALIAS_NAME_XPATH, hostAlias);
		p.setProperty(SetConfigCmd.HOST_ALIAS_IP_ADDRESS_XPATH, ipAddress);
		XMLMgmtRequest request = new XMLMgmtRequest(SetConfigCmd.HOST_ALIAS_TEMPLATE, p);
		return request;
	}
	
	public static XMLMgmtRequest createGetDomainListRequest(){
		return new XMLMgmtRequest(GetDomainListCmd.TEMPLATE);
	}

}
