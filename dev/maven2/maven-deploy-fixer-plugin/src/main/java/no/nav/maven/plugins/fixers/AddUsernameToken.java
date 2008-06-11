package no.nav.maven.plugins.fixers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import no.nav.maven.plugins.common.utils.EarUtils;
import no.nav.maven.plugins.common.utils.XMLUtils;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

public class AddUsernameToken extends AbstractMojo {
	private File consModuleFolder = null;
	private File webservicesExt = null;
	private File webservicesBnd = null;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("-------------------- Start AddUsernameToken for [" + consModuleFolder.getName() + "]--------------------");
		if(!filesExist()){
			getLog().warn("[" + consModuleFolder.getName() + "] does not contain any webservice security information!");
			return;
		}
		try {
			setupIbmWebservicesExt();
			
			setupIbmWebservicesBnd();
		} catch (DocumentException e) {
			throw new MojoExecutionException("Error parsing XML document!", e);
		} catch (IOException e) {
			throw new MojoExecutionException("An unexpected IO error occured", e);
		}
		getLog().info("-------------------- End AddUsernameToken --------------------");
	}
	
	private boolean filesExist(){
		webservicesExt = new File(consModuleFolder.getAbsolutePath() + EarUtils.EJB_SUBPATH + "/META-INF/ibm-webservices-ext.xmi");
		webservicesBnd = new File(consModuleFolder.getAbsolutePath() + EarUtils.EJB_SUBPATH + "/META-INF/ibm-webservices-bnd.xmi");
		
		return (webservicesBnd.exists() && webservicesExt.exists());
	}

	private void setupIbmWebservicesExt() throws DocumentException, IOException{
		Document doc = null;
		Element pcBinding, serviceConfig, usernameCaller, ltpaCaller, usernameToken, ltpaToken;
		XPath search = null;
		SAXReader reader = null;
		Map<String, String> uris = null;

		reader = new SAXReader();
		uris = new HashMap<String, String>();
		
		doc = reader.read(webservicesExt);
		uris.put("wsext", "http://www.ibm.com/websphere/appserver/schemas/5.0.2/wsext.xmi");

		search = doc.createXPath("//pcBinding");
		search.setNamespaceURIs(uris);
		pcBinding = (Element)search.selectSingleNode(doc);
		if(pcBinding != null){
			pcBinding.clearContent();
			
			serviceConfig = pcBinding.addElement("serverServiceConfig").addElement("securityRequestConsumerServiceConfig");
			
			usernameCaller = serviceConfig.addElement("caller");
			usernameCaller.addAttribute("name", "UsernameTokenCaller");
			usernameCaller.addAttribute("part", "");
			usernameCaller.addAttribute("uri", "");
			usernameCaller.addAttribute("localName", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken");
			
			ltpaCaller = serviceConfig.addElement("caller");
			ltpaCaller.addAttribute("name", "LTPATokenCaller");
			ltpaCaller.addAttribute("part", "");
			ltpaCaller.addAttribute("uri", "http://www.ibm.com/websphere/appserver/tokentype/5.0.2");
			ltpaCaller.addAttribute("localName", "LTPA");
			
			usernameToken = serviceConfig.addElement("requiredSecurityToken");
			usernameToken.addAttribute("name", "OptionalUsernameToken");
			usernameToken.addAttribute("uri", "");
			usernameToken.addAttribute("localName", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken");

			ltpaToken = serviceConfig.addElement("requiredSecurityToken");
			ltpaToken.addAttribute("name", "OptionalLTPAToken");
			ltpaToken.addAttribute("uri", "http://www.ibm.com/websphere/appserver/tokentype/5.0.2");
			ltpaToken.addAttribute("localName", "LTPA");
			
			getLog().info("'ibm-webservices-ext.xmi' updated!");
			XMLUtils.writeXMLDocument(doc, webservicesExt);
		}else{
			getLog().warn(consModuleFolder.getName() + " does not contain 'pcBinding' definition in 'ibm-webservices-ext.xmi'");
		}
	}
	
	private void setupIbmWebservicesBnd() throws DocumentException, IOException{
		Document doc = null;
		Element pcBinding, bindingConfig, tokenConsumer, tokenConsumer2, valueType, jaasConfig, partRef;
		XPath search = null;
		SAXReader reader = null;
		Map<String, String> uris = null;

		reader = new SAXReader();
		uris = new HashMap<String, String>();
		
		doc = reader.read(webservicesBnd);
		uris.put("wsbnd", "http://www.ibm.com/websphere/appserver/schemas/5.0.2/wsbnd.xmi");

		search = doc.createXPath("//pcBindings");
		search.setNamespaceURIs(uris);
		pcBinding = (Element)search.selectSingleNode(doc);
		if(pcBinding != null){
			pcBinding.clearContent();
			
			bindingConfig = pcBinding.addElement("securityRequestConsumerBindingConfig");
			
			tokenConsumer = bindingConfig.addElement("tokenConsumer");
			tokenConsumer.addAttribute("classname", "com.ibm.wsspi.wssecurity.token.UsernameTokenConsumer");
			tokenConsumer.addAttribute("name", "UsernameTokenConsumer");
			
			valueType = tokenConsumer.addElement("valueType");
			valueType.addAttribute("localName", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken");
			valueType.addAttribute("uri", "");
			valueType.addAttribute("name", "UsernameToken");
			
			jaasConfig = tokenConsumer.addElement("jAASConfig");
			jaasConfig.addAttribute("configName", "system.wssecurity.UsernameToken");
			
			partRef = tokenConsumer.addElement("partReference");
			partRef.addAttribute("part", "OptionalUsernameToken");
			
			tokenConsumer2 = bindingConfig.addElement("tokenConsumer");
			tokenConsumer2.addAttribute("classname", "com.ibm.wsspi.wssecurity.token.LTPATokenConsumer");
			tokenConsumer2.addAttribute("name", "LTPATokenConsumer");
			
			valueType = tokenConsumer2.addElement("valueType");
			valueType.addAttribute("localName", "LTPA");
			valueType.addAttribute("uri", "http://www.ibm.com/websphere/appserver/tokentype/5.0.2");
			valueType.addAttribute("name", "LTPA Token");
			
			partRef = tokenConsumer2.addElement("partReference");
			partRef.addAttribute("part", "OptionalLTPAToken");
			
			getLog().info("'ibm-webservices-bnd.xmi' updated!");
			XMLUtils.writeXMLDocument(doc, webservicesBnd);
		}else{
			getLog().warn(consModuleFolder.getName() + " does not contain 'securityRequestConsumerBindingConfig' definition in 'ibm-webservices-ext.xmi'");
		}
	}

	public File getConsModuleFolder() {
		return consModuleFolder;
	}

	public void setConsModuleFolder(File consModuleFolder) {
		this.consModuleFolder = consModuleFolder;
	}
	
}
