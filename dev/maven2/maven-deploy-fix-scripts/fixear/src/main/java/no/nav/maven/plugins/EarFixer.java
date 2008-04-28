package no.nav.maven.plugins;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Goal which touches a timestamp file.
 *
 * @goal fixear
 * 
 * @phase process-sources 
 */
public class EarFixer
    extends AbstractMojo
{
	/**
	 * This parameter is the temporary directory where the files are unpacked.
	 * @parameter expression="${workingArea}"
	 * @required
	 */
	private File workingArea; // = new File("E:\\Deploy_Temp");
	
	/**
	 * This parameter is the ear source file
	 * @parameter mappingfile="${mappingFile}"
	 * @required
	 */
	private File mappingFile; // = new File("E:\\cc\\DevArch\\DevArch\\tools\\moose\\build_version_mapping.txt");
	
	/**
	 * This parameter is the environment file containing environment properties
	 * @parameter expression="${envFile}"
	 * @required
	 */
	private File environFile; // = new File("E:\\cc\\DevArch\\DevArch\\tools\\moose\\environments\\KompTestKjempen.xml");
	
	/**
	 * What module is being edited
	 * @parameter expression="${mooseId}"
	 * @required
	 */
	private String mooseId; // = "PSELV_K_D3_0079";
	
	
	private String module;
	private String version;
	private File earFile, warFile;
	private Dictionary environment;

	/* (non-Javadoc)
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {	
		try {
			earFile = resolveEarFile();
		} catch (IOException e) {
			throw new MojoExecutionException("Error resolving application version from moose id!", e);
		}finally{
			if(earFile == null) throw new MojoExecutionException("MooseID '" + mooseId + "' not found in mapping file!");
			if(!earFile.exists()) throw new MojoExecutionException(earFile.getAbsolutePath() + " does not exist!");
		}
		
		module = earFile.getName().split("-")[2].toLowerCase();
		
		try {
			
			parseEnvironFile();
			
			extractEarFile(earFile);
			
			if(mooseId.startsWith("PSAK")) fixPSAK();
			
			if(mooseId.startsWith("PSELV")) fixPSELV();
				
			deleteFiles();
			
			compressEarFile();
			
			getLog().info("Cleaning up temporary files...");
			delete(workingArea);
			
			getLog().info("Ear editing completed successfully!");
			
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new MojoExecutionException("Error editing files! See above for details...", e1);
		}
	}
	
	private void fixPSELV() throws IOException, DocumentException{
		if(isSensitiveZone()){ //SENSITIVSONE
			if(isGiant()){ //KJEMPEN
				getLog().info("Configuring PSELV KJEMPEN for SENSITIVSONE...");
				
				fixWebXml(new File(workingArea.getAbsolutePath() + "/war/WEB-INF/web.xml"));
				
				fixPrsCommon(new File(workingArea.getAbsolutePath() + "/war/WEB-INF/spring/prs-common-context.xml"));
				
				fixPrsSecurity(new File(workingArea.getAbsolutePath() + "/war/WEB-INF/security/prs-security-context.xml"));
				
				fixFacesSecurity(new File(workingArea.getAbsolutePath() + "/war/WEB-INF/security/faces-security-config.xml"));
			
			}else{  //REKRUTTEN
				getLog().info("Configuring PSELV REKRUTTEN for SENSITIVSONE...");
				
				fixWebXml(new File(workingArea.getAbsolutePath() + "/war/WEB-INF/web.xml"));
				
				fixPrsCommon(new File(workingArea.getAbsolutePath() + "/war/WEB-INF/spring/prs-common-context.xml"));
				
				fixPrsSecurity(new File(workingArea.getAbsolutePath() + "/war/WEB-INF/security/prs-security-context.xml"));
				
				fixFacesSecurity(new File(workingArea.getAbsolutePath() + "/war/WEB-INF/security/faces-security-config.xml"));
				
				fixBndFile(new File(workingArea.getAbsolutePath() + "/META-INF/ibm-application-bnd.xmi"));
			}
		}else{ //INTERNSONE
			if(isGiant()){ //KJEMPEN
				getLog().info("Configuring PSELV KJEMPEN for INTERNSONE...");
				fixBndFile(new File(workingArea.getAbsolutePath() + "/META-INF/ibm-application-bnd.xmi"));
			}else{ //REKRUTTEN
				getLog().info("Configuring PSELV REKRUTTEN for INTERNSONE...");
				fixBndFile(new File(workingArea.getAbsolutePath() + "/META-INF/ibm-application-bnd.xmi"));
			}
		}
	
		getLog().info("Done!");
	}
	
	private void fixPSAK() throws IOException, DocumentException{
		File log4j, penConfig, psakConfig;
		
		if(isSensitiveZone()){			//SENSITIVSONE
			
			if(isGiant()){				//KJEMPEN
				getLog().info("Configuring PSAK KJEMPEN for SENSITIVSONE...");
				
				//fix pselv link in resource.properties
				fixResources(new File(workingArea.getAbsolutePath() + "/war/WEB-INF/classes/resources_nb_NO.properties"));
				fixBndFile(new File(workingArea.getAbsolutePath() + "/META-INF/ibm-application-bnd.xmi"));
			}else{						//REKRUTTEN
				getLog().info("Configuring PSAK REKRUTTEN for SENSITIVSONE...");

				fixBndFile(new File(workingArea.getAbsolutePath() + "/META-INF/ibm-application-bnd.xmi"));
			}
			
		}else{							//INTERNSONE
			if(isGiant()){ 				//KJEMPEN
				getLog().info("Configuring PSAK KJEMPEN for INTERNSONE...");
				
				//Setting up role binding
				getLog().info("Setting up role binding for INTERNSONE...");
				fixBndFile(new File(workingArea.getAbsolutePath() + "/META-INF/ibm-application-bnd.xmi"));
			}else{						//REKRUTTEN
				getLog().info("Configuring PSAK REKRUTTEN for INTERNSONE...");
				
				fixBndFile(new File(workingArea.getAbsolutePath() + "/META-INF/ibm-application-bnd.xmi"));
			}
		}
		getLog().info("Done!");
	}
	
	private File resolveEarFile() throws IOException{
		if(!mappingFile.exists()) throw new IOException("Mapping file not found!");
		BufferedReader reader = new BufferedReader(new FileReader(mappingFile));
		String line;
		while((line = reader.readLine()) != null){
			if(line.startsWith(mooseId) && line.split("\\|").length == 2){
				version = line.split("\\|")[1];
				getLog().info("Resolved MooseID '" + mooseId + "' to version '" + version + "'");
				if(mooseId.startsWith("PEN")){
					return new File(workingArea.getAbsolutePath(), "nav-pensjon-pen-jee-" + version + ".ear");
				}else if(mooseId.startsWith("PSAK")){
					return new File(workingArea.getAbsolutePath(), "nav-pensjon-psak-jee-" + version + ".ear");
				}else if(mooseId.startsWith("PSELV")){
					return new File(workingArea.getAbsolutePath(),"nav-pensjon-pselv-jee-" + version + ".ear");
				}				
			}
		}
		return null;		
	}
	
	private boolean isGiant(){
		return mooseId.indexOf("_K_") >= 0;
	}
	
	private boolean isSensitiveZone(){
		return ((String)environment.get("server.domain")).toUpperCase().compareTo("SENSITIVSONE") == 0;
	}
	
	public void extractEarFile(File earFile) throws IOException {
		//extracting ear file
		getLog().info("Extracting ear file...");
		//creating unique temp folder for unpacking ear content
		workingArea = new File(workingArea.getAbsolutePath() + "/" + (new Date()).getTime() + "_" + earFile.getName());	
		workingArea.mkdirs();
		ZipUtils.extract2(earFile,workingArea);

		//extracting inner war file
		File[] wars = workingArea.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".war");
			}
		});
		if(wars.length > 0) warFile = wars[0];
		if(warFile != null && warFile.exists()) ZipUtils.extract2(warFile,new File(workingArea.getAbsolutePath() + "/war"));
		
		getLog().info("Done!");
	}
	
	public void compressEarFile() throws IOException{
		String[] temp = earFile.getName().split("-");
		
		getLog().info("Compressing ear file back together...");
		
		//compressing inner war file
		if(new File(workingArea.getAbsolutePath() + "/war").exists()){
			ZipUtils.compress(new File(workingArea.getAbsolutePath() + "/war"),warFile);
		}
		
	
		//removing temporary war content
		System.gc();
		if(new File(workingArea.getAbsolutePath() + "/war").exists()){
			if(!delete(new File(workingArea.getAbsolutePath() + "\\war"))){
				getLog().warn("Unable to delete temporary files!");
			}
		}
		
		
		//compressing outer ear file
		ZipUtils.compress(new File(workingArea.getAbsolutePath()),earFile);
		getLog().info("Done!");
		getLog().info("Clean up temporary files...");
		System.gc();
		if(!delete(workingArea)){
			getLog().warn("Unable to delete temporary files!");
		}
		getLog().info("Done!");
	}
	
	private boolean delete(File path){
		if(path.isDirectory()){
			File[] children = path.listFiles();
			for(int i = 0; i < children.length; i++){
				boolean success = delete(children[i]);
				if(!success){
					return false;
				}
			}
		}
		System.gc();
		return path.delete();
		
	}

	/*
	 * Delete unecessary files from ear 
	 */
	public void deleteFiles(){
		File log4j, xalan, xerces, xmlApi;
		
		if(isGiant()){
			if(module.compareTo("pen") == 0){
				
			}else if(module.compareTo("psak") == 0){
				//delete log4j.properties
				log4j = new File(workingArea.getAbsolutePath() + "/war/WEB-INF/classes/log4j.properties");
				if(log4j.exists()){
					getLog().info("Found /WEB-INF/classes/log4j.properties, deleting");
					log4j.delete();
				}
				
				
			}else if(module.compareTo("pselv") == 0){
				//delete log4j.properties
				log4j = new File(workingArea.getAbsolutePath() + "/war/WEB-INF/classes/log4j.properties");
				if(log4j.exists()){
					getLog().info("Found /WEB-INF/classes/log4j.properties, deleting");
					log4j.delete();
				}
				
				//delete xalan-2.7.0.jar
				xalan = new File(workingArea.getAbsolutePath() + "/lib/xalan-2.7.0.jar");
				if(xalan.exists()){
					getLog().info("/lib/xalan-2.7.0.jar, deleting");
					xalan.delete();
				}
				
				//delete xercesImpl
				xerces = new File(workingArea.getAbsolutePath() + "/lib/xercesImpl-2.6.2.jar");
				if(xerces.exists()){
					getLog().info("/lib/xercesImpl-2.6.2.jar, deleting");
					xerces.delete();
				}
				
				//delete xml-api
				xmlApi = new File(workingArea.getAbsolutePath() + "/lib/xml-apis-1.0.b2.jar");
				if(xmlApi.exists()){
					getLog().info("/lib/xml-apis-1.0.b2.jar, deleting");
					xmlApi.delete();
				}
			}
		}
		
		//deleting any nav-config jars in lib
		File[] configs = new File(workingArea.getAbsolutePath() + "/lib").listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.contains("nav-config");
			}
		});
		if(configs != null && configs.length > 0){
			getLog().info("Deleting config jars...");
			for(File config : configs){
				getLog().info("Deleting " + config.getName());
				config.delete();
			}
		}
	}
	
	public void parseEnvironFile() throws DocumentException, IOException{ 
		Document doc;
		SAXReader reader;
		Element server, websphere, config, prop;
		XPath search;
		
		getLog().info("Reading environment file...");
		
		reader = new SAXReader();
		doc = reader.read(environFile);
		environment = new Hashtable();
		
		search = doc.createXPath("/environment/server");
		server = (Element)search.selectSingleNode(doc);
		for(int i = 0;i < server.elements().size();i++){
			if(!(((Element)server.elements().get(i)).getName().compareTo("ad-mappings")== 0)){
				environment.put("server." + ((Element)server.elements().get(i)).getName(),((Element)server.elements().get(i)).getText());
			}
		}
		
		search = doc.createXPath("/environment/server/ad-mappings/psak");
		server = (Element)search.selectSingleNode(doc);
		for(int i = 0;i < server.elements().size();i++){
			Element mapping = (Element)server.elements().get(i);
			environment.put("server.mapping.psak." + mapping.attributeValue("role-id"), mapping.attributeValue("name"));
		}
		
		search = doc.createXPath("/environment/server/ad-mappings/pselv");
		server = (Element)search.selectSingleNode(doc);
		for(int i = 0;i < server.elements().size();i++){
			Element mapping = (Element)server.elements().get(i);
			environment.put("server.mapping.pselv." + mapping.attributeValue("role-id"), mapping.attributeValue("name"));
		}
		
		search = doc.createXPath("/environment/websphere");
		websphere = (Element)search.selectSingleNode(doc);
		for(int i = 0;i < websphere.elements().size();i++){
			environment.put("websphere." + ((Element)websphere.elements().get(i)).getName(),((Element)websphere.elements().get(i)).getText());
		}
		
		search = doc.createXPath("/environment/config");
		config = (Element)search.selectSingleNode(doc);
		for(int i = 0;i < config.elements().size();i++){
			prop = (Element)config.elements().get(i);
			for(int j = 0;j < prop.elements().size();j++){
				Element subProp = (Element)prop.elements().get(j);
				Attribute nameAttr = (Attribute)subProp.attributes().get(0);
				Attribute valueAttr = (Attribute)subProp.attributes().get(1);
				environment.put(prop.getName() + "." + nameAttr.getValue(), valueAttr.getValue());
			}
		}
		getLog().info("Done!");
	}

	public void fixWebXml(File webXml) throws DocumentException, IOException{
		Document doc;
		SAXReader reader;
		Element filter, mapping, servlet, config, role, child, session;
		XPath search;
		boolean filterFound = false;
		Map uris = new HashMap();
		
		getLog().info("-------------------------------------");
		getLog().info("-            web.xml                -");
		getLog().info("-------------------------------------");
		
		uris.put("ns","http://java.sun.com/xml/ns/j2ee");
		reader = new SAXReader();
		doc = reader.read(webXml);
		
		/**
		 * Searching for InnloggingFilter, and removes it if present
		 */
		search = doc.createXPath("/ns:web-app/ns:filter[ns:filter-name='InnloggingFilter']");
		search.setNamespaceURIs(uris);
		List results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			((Element)results.get(0)).getParent().remove((Element)results.get(0));
			getLog().info("Removed 'InnloggingFilter' filter");
		}else getLog().info("'InnloggingFilter' not found in web.xml");
		
//		/**
//		 * searching for InnloggingPSAKFilter and updates it
//		 */
//		search = doc.createXPath("/ns:web-app/ns:filter[ns:filter-name='InnloggingPSAKFilter']");
//		search.setNamespaceURIs(uris);
//		results = search.selectNodes(doc);
//		if(results.size() >= 1)
//		{
//			filter = (Element)results.get(0);
//			getLog().info("InnloggingPSAKFilter filter already defined, updating filter...");
//			filter.clearContent();
//			Element child1 = filter.addElement("display-name");
//			Element child2 = filter.addElement("filter-name");
//			Element child3 = filter.addElement("filter-class");
//			Element child4 = filter.addElement("init-param");
//			Element subChild1 = child4.addElement("param-name");
//			Element subChild2 = child4.addElement("param-value");
//			child1.setText("InnloggingPSAKFilter");
//			child2.setText("InnloggingPSAKFilter");
//			child3.setText("no.nav.presentation.pensjon.pselv.tilleggsfunksjonalitet.InnloggingPSAKFilter");
//			subChild1.setText("no.nav.presentation.pensjon.pselv.tilleggsfunksjonalitet.InnloggingPSAKFilter.ALLOW_ACCESS");
//			subChild2.setText("VEILEDER");
//		}else{
//			filter = doc.getRootElement().addElement("filter");
//			Element child1 = filter.addElement("display-name");
//			Element child2 = filter.addElement("filter-name");
//			Element child3 = filter.addElement("filter-class");
//			Element child4 = filter.addElement("init-param");
//			Element subChild1 = child4.addElement("param-name");
//			Element subChild2 = child4.addElement("param-value");
//			child1.addText("InnloggingPSAKFilter");
//			child2.addText("InnloggingPSAKFilter");
//			child3.addText("no.nav.presentation.pensjon.pselv.tilleggsfunksjonalitet.InnloggingPSAKFilter");
//			subChild1.addText("no.nav.presentation.pensjon.pselv.tilleggsfunksjonalitet.InnloggingPSAKFilter.ALLOW_ACCESS");
//			subChild2.addText("VEILEDER");
//			getLog().info("InnloggingPSAKFilter not found, created new filter");
//		}

		/**
		 * removing InnloggingFilter mapping
		 **/
		search = doc.createXPath("/ns:web-app/ns:filter-mapping[ns:filter-name='InnloggingFilter']");
		search.setNamespaceURIs(uris);
		results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			((Element)results.get(0)).getParent().remove((Element)results.get(0));
			getLog().info("Removed filter-mapping for InnloggingFilter");
		}else getLog().info("Filter-mapping for InnloggingFilter not found.");

		/**
		 * removing servlets and checking required are defined
		 **/
		search = doc.createXPath("/ns:web-app/ns:servlet[ns:servlet-name='security.eaiRequestHandler']");
		search.setNamespaceURIs(uris);
		results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			((Element)results.get(0)).getParent().remove((Element)results.get(0));
			getLog().info("Removed 'security.eaiRequestHandler' servlet");
		}else getLog().info("'security.eaiRequestHandler' servlet not found.");
		
		search = doc.createXPath("/ns:web-app/ns:servlet[ns:servlet-name='security.stepUpDestinationUrlServlet']");
		search.setNamespaceURIs(uris);
		results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			((Element)results.get(0)).getParent().remove((Element)results.get(0));
			getLog().info("Removed 'security.stepUpDestinationUrlServlet' servlet");
		}else getLog().info("'security.stepUpDestinationUrlServlet' servlet not found");

		/**
		 * removing servlet mappings
		 **/
		search = doc.createXPath("/ns:web-app/ns:servlet-mapping[ns:servlet-name='security.eaiRequestHandler']");
		search.setNamespaceURIs(uris);
		results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			((Element)results.get(0)).getParent().remove((Element)results.get(0));
			getLog().info("Removed 'security.eaiRequestHandler' servlet-mapping");
		}else getLog().info("'security.eaiRequestHandler' servlet-mapping not found.");
		
		search = doc.createXPath("/ns:web-app/ns:servlet-mapping[ns:servlet-name='security.stepUpDestinationUrlServlet']");
		search.setNamespaceURIs(uris);
		results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			((Element)results.get(0)).remove((Element)results.get(0));
			getLog().info("Removed 'security.stepUpDestinationUrlServlet' servlet-mapping");
		}else getLog().info("'security.stepUpDestinationUrlServlet' servlet-mapping not found.");

		/**
		 * change login-config
		 **/
		search = doc.createXPath("/ns:web-app/ns:login-config/ns:form-login-config");
		search.setNamespaceURIs(uris);
		config = (Element)search.selectSingleNode(doc);
		if (config != null)
		{
			if (config.elements().size() == 2)
			{
				((Element)config.elements().get(0)).clearContent();
				((Element)config.elements().get(1)).clearContent();
				((Element)config.elements().get(0)).addText("/tilleggsfunksjonalitet/tilgangnektet.jsf?_flowId=tilgangnektet-flow");
				((Element)config.elements().get(1)).addText("/tilleggsfunksjonalitet/tilgangnektet.jsf?_flowId=tilgangnektet-flow");
				getLog().info("Changed 'login-config'");
			}
		}

		/**
		 * Adding security roles if not existent
		 **/
		search = DocumentHelper.createXPath("/ns:web-app/ns:security-role[ns:role-name='AllAuthenticated']");
		search.setNamespaceURIs(uris);
		results = search.selectNodes(doc);
		if(results.size() >= 1) getLog().info("Security role 'AllAuthenticated' already defined");
		else
		{
			role = doc.addElement("security-role");
			doc.getRootElement().add(role);
			child = doc.addElement("role-name");
			child.addText("AllAuthenticated");
			role.add(child);
			getLog().info("Added 'AllAuthenticated' security-role");
		}
		
		search = DocumentHelper.createXPath("/ns:web-app/ns:security-role[ns:role-name='VEILEDER']");
		search.setNamespaceURIs(uris);
		results = search.selectNodes(doc);
		if(results.size() >= 1) getLog().info("Security role 'VEILEDER' already defined");
		else
		{
			role = doc.addElement("security-role");
			doc.getRootElement().add(role);
			child = doc.addElement("role-name");
			child.addText("VEILEDER");
			role.add(child);
			getLog().info("Added 'VEILEDER' security-role");
		}	
		
		/**
		 * change session timeout
		 **/
		
		search = doc.createXPath("/ns:web-app/ns:session-config");
		search.setNamespaceURIs(uris);
		session = (Element)search.selectSingleNode(doc);
		if (session != null)
		{
			Element timeout = (Element)session.elements().get(0);
			if(timeout == null) throw new DocumentException("Invalid session timeout config (unable to load timeout element)!");
			if(((String)environment.get("server.domain")).toUpperCase().compareTo("SENSITIVSONE") == 0){ //SENSITIVSONE
				timeout.setText("60");
				getLog().info("Session timeout set to 60 minutes (SENSITIVSONE)");
			}else{ //INTERNSONE
				timeout.setText("30");
				getLog().info("Session timeout set to 30 minutes (INTERNSONE)");
			}
		}else{
			getLog().warn("Session timeout config not found!");
		}
		
		writeXml(doc,webXml);
		getLog().info("Finished web.xml!");
	}
	
	public void fixPrsCommon(File prsCommon) throws DocumentException, IOException{
		Document doc;
		SAXReader reader;
		XPath search;
		Element bean, child;
		Attribute attr;
		Map uris = new HashMap();
		
		getLog().info("-------------------------------------");
		getLog().info("-      prs-common-context.xml       -");
		getLog().info("-------------------------------------");
		
		uris.put("ns","http://www.springframework.org/schema/beans");
		reader = new SAXReader();
		doc = reader.read(prsCommon);
		
		search = DocumentHelper.createXPath("/ns:beans/ns:bean[@id='prs.pselv.innloggingFilter']");
		search.setNamespaceURIs(uris);
		List results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			((Element)results.get(0)).getParent().remove((Element)results.get(0));
			getLog().info("Removed bean with id=\"prs.pselv.innloggingFilter\"");
		}else getLog().info("Bean with id=\"prs.pselv.innloggingFilter\" not found");
		
		search = DocumentHelper.createXPath("/ns:beans/ns:bean[@id='logoutService']");
		search.setNamespaceURIs(uris);
		results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			bean = (Element)results.get(0);
			bean.clearContent();
			bean.attributes().clear();
			
			bean.addAttribute("id","logoutService");
			bean.addAttribute("class","no.stelvio.presentation.security.logout.WasLogoutService");
			
			
			Element prop1 = bean.addElement("property");
			Element prop2 = bean.addElement("property");
			prop1.addAttribute("name","logoutHandlerUrl");
			prop1.addAttribute("value","/");
			prop2.addAttribute("name","defaultStartPage");
			prop2.addAttribute("value","/");

			getLog().info("LogoutService bean found, bean updated!");
		}else{
			bean = doc.getRootElement().addElement("bean");
			bean.addAttribute("id","logoutService");
			bean.addAttribute("class","no.stelvio.presentation.security.logout.WasLogoutService");
			
			
			Element prop1 = bean.addElement("property");
			Element prop2 = bean.addElement("property");
			prop1.addAttribute("name","logoutHandlerUrl");
			prop1.addAttribute("value","/");
			prop2.addAttribute("name","defaultStartPage");
			prop2.addAttribute("value","/");
			
			getLog().info("\"prs.pselv.innloggingFilter\" created.");
		}
		
		writeXml(doc,prsCommon);
		getLog().info("Finished prs-common-context.xml!");
	}
	
	public void fixPrsSecurity(File prsSecurity) throws DocumentException, IOException{
		Document doc;
		SAXReader reader;
		XPath search;
		Element bean, child;
		Attribute attr;
		boolean filterFound = false;
		Map uris = new HashMap();
		
		getLog().info("-------------------------------------");
		getLog().info("-     prs-security-context.xml      -");
		getLog().info("-------------------------------------");
		
		uris.put("ns","http://www.springframework.org/schema/beans");
		reader = new SAXReader();
		doc = reader.read(prsSecurity);
		
		search = DocumentHelper.createXPath("/ns:beans/ns:bean[@id='security.eaiRequestHandler']");
		search.setNamespaceURIs(uris);
		List results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			((Element)results.get(0)).getParent().remove((Element)results.get(0));
			getLog().info("Bean with id=\"security.eaiRequestHandler\" removed");
		}else getLog().info("Bean with id=\"security.eaiRequestHandler\" not found.");
		
		search = DocumentHelper.createXPath("/ns:beans/ns:bean[@id='security.eaiHeaderConfig']");
		search.setNamespaceURIs(uris);
		results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			((Element)results.get(0)).getParent().remove((Element)results.get(0));
			getLog().info("Bean with id=\"security.eaiHeaderConfig\" removed");
		}else getLog().info("Bean with id=\"security.eaiHeaderConfig\" not found.");
		
		search = DocumentHelper.createXPath("/ns:beans/ns:bean[@id='security.StepUpAuthentication']");
		search.setNamespaceURIs(uris);
		results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			((Element)results.get(0)).getParent().remove((Element)results.get(0));
			getLog().info("Bean with id=\"security.StepUpAuthentication\" removed");
		}else getLog().info("Bean with id=\"security.StepUpAuthentication\" not found");
		
		search = DocumentHelper.createXPath("/ns:beans/ns:bean[@id='security.stepUpDestinationUrlServlet']");
		search.setNamespaceURIs(uris);
		results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			((Element)results.get(0)).getParent().remove((Element)results.get(0));
			getLog().info("Bean with id=\"security.stepUpDestinationUrlServlet\" removed");
		}else getLog().info("Bean with id=\"security.stepUpDestinationUrlServlet\" not found");
		
		search = DocumentHelper.createXPath("/ns:beans/ns:bean[@id='security.phaselistenerExceptionHandler']");
		search.setNamespaceURIs(uris);
		results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			bean = (Element)results.get(0);
			bean.clearContent();
			
			bean.addAttribute("id","security.phaselistenerExceptionHandler");
			bean.addAttribute("class","no.stelvio.presentation.security.page.error.support.RedirectExceptionHandler");
			
			Element prop1 = bean.addElement("property");
			Element prop2 = bean.addElement("property");
			prop1.addAttribute("name","exceptionHandlerUrl");
			prop1.addAttribute("value","/AccessDenied");
			prop2.addAttribute("name","authenticationLevelHandler");
			prop2.addAttribute("ref","security.authenticationLevelHandler");

			getLog().info("Bean with id=\"security.phaselistenerExceptionHandler\" already defined, bean updated.");
		}else{
			bean = doc.getRootElement().addElement("bean");
			bean.addAttribute("id","security.phaselistenerExceptionHandler");
			bean.addAttribute("class","no.stelvio.presentation.security.page.error.support.RedirectExceptionHandler");
			
			Element prop1 = bean.addElement("property");
			Element prop2 = bean.addElement("property");
			prop1.addAttribute("name","exceptionHandlerUrl");
			prop1.addAttribute("value","/AccessDenied");
			prop2.addAttribute("name","authenticationLevelHandler");
			prop2.addAttribute("ref","security.authenticationLevelHandler");

			getLog().info("\"security.phaselistenerExceptionHandler\" created.");
		}
		
		search = DocumentHelper.createXPath("/ns:beans/ns:bean[@id='security.rethrowExceptionServlet']");
		search.setNamespaceURIs(uris);
		results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			bean = (Element)results.get(0);
			bean.clearContent();
			bean.addAttribute("id","security.rethrowExceptionServlet");
			bean.addAttribute("class","no.stelvio.presentation.security.page.error.servlet.RethrowExceptionServlet");
			
			Element prop1 = bean.addElement("property");
			prop1.addAttribute("name","exceptionHandlerFacade");
			prop1.addAttribute("ref","error.facade");
			
			getLog().info("Bean with id=\"security.rethrowExceptionServlet\" already defined, bean updated.");
		}else{
			bean = doc.getRootElement().addElement("bean");

			bean.addAttribute("id","security.rethrowExceptionServlet");
			bean.addAttribute("class","no.stelvio.presentation.security.page.error.servlet.RethrowExceptionServlet");
			
			Element prop1 = bean.addElement("property");
			prop1.addAttribute("name","exceptionHandlerFacade");
			prop1.addAttribute("ref","error.facade");
			
			getLog().info("\"security.rethrowExceptionServlet\" created.");
		}
		
		search = DocumentHelper.createXPath("/ns:beans/ns:bean[@id='security.SecurityFlowNavigationExceptionHandler']");
		search.setNamespaceURIs(uris);
		results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			bean = (Element)results.get(0);
			bean.clearContent();
			bean.addAttribute("id","security.SecurityFlowNavigationExceptionHandler");
			bean.addAttribute("class","no.stelvio.presentation.security.page.error.support.DefaultPageSecurityExceptionHandler");
			
			Element prop1 = bean.addElement("property");
			prop1.addAttribute("name","exceptionHandlerFacade");
			prop1.addAttribute("ref","error.facade");

			getLog().info("Bean with id=\"security.SecurityFlowNavigationExceptionHandler\" already defined, bean updated.");
		}else{
			bean = (Element)results.get(0);
			
			bean.addAttribute("id","security.SecurityFlowNavigationExceptionHandler");
			bean.addAttribute("class","no.stelvio.presentation.security.page.error.support.DefaultPageSecurityExceptionHandler");
			
			Element prop1 = bean.addElement("property");
			prop1.addAttribute("name","exceptionHandlerFacade");
			prop1.addAttribute("ref","error.facade");

			getLog().info("\"security.SecurityFlowNavigationExceptionHandler\" created.");
		}
		
		writeXml(doc,prsSecurity);
		
		//handling weird multiline demand of security.filterChainProxy bean
		BufferedReader textReader = new BufferedReader(new FileReader(prsSecurity));
		String line = null, content = null;
		StringBuilder bldr = new StringBuilder();
		while((line = textReader.readLine()) != null){
			bldr.append(line + System.getProperty("line.separator"));
		}
		textReader.close();
		content = bldr.toString();
		content = content.replaceAll("CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON ", "CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON" + System.getProperty("line.separator") + "\t\t\t");
		content = content.replaceAll("PATTERN_TYPE_APACHE_ANT ", "PATTERN_TYPE_APACHE_ANT" + System.getProperty("line.separator") + "\t\t\t");
		
		FileWriter writer = new FileWriter(prsSecurity);
		writer.write(content);
		writer.close();
		getLog().info("Finished prs-security-context.xml!");
	}
	
	public void fixFacesSecurity(File facesSecurity) throws DocumentException, IOException{
		Document doc;
		SAXReader reader;
		XPath search;
		Element page, child;
		Attribute attr;
		Map uris = new HashMap();
		
		getLog().info("-------------------------------------");
		getLog().info("-    faces-security-config.xml      -");
		getLog().info("-------------------------------------");
		
		uris.put("ns","no.stelvio.web.security.page");
		reader = new SAXReader();
		doc = reader.read(facesSecurity);
		
		search = DocumentHelper.createXPath("/ns:jsf-application/ns:jsf-page[ns:page-name='/pages/simulering/samleside/samleside.xhtml']");
		search.setNamespaceURIs(uris);
		List results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			page = ((Element)results.get(0));
			page.clearContent();
			page.attributes().clear();
			page.addAttribute("requires-authentication","true");
			page.addAttribute("requires-authorization","true");
			page.addAttribute("requires-ssl","false");
			Element pageName = page.addElement("page-name");
			pageName.addText("/pages/simulering/samleside/samleside.xhtml");
			Element roles = page.addElement("j2ee-roles");
			roles.addAttribute("role-concatenation","OR");
			Element role = roles.addElement("role-name");
			role.addText("VEILEDER");
			getLog().info("jsf-page '/pages/simulering/samleside/samleside.xhtml' found and updated");
		}else
		{
			page = doc.getRootElement().addElement("jsf-page");
			page.clearContent();
			page.attributes().clear();
			page.addAttribute("requires-authentication","true");
			page.addAttribute("requires-authorization","true");
			page.addAttribute("requires-ssl","false");
			Element roles = page.addElement("j2ee-roles");
			roles.addAttribute("role-concatenation","OR");
			Element role = roles.addElement("role-name");
			role.addText("VEILEDER");
			getLog().info("jsf-page '/pages/simulering/samleside/samleside.xhtml' created");
		}
		
		writeXml(doc,facesSecurity);
		getLog().info("Finished faces-security-config.xml...");
	}

	private void writeXml(Document doc, File out) throws IOException
	{
		out.delete();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "UTF-8"));
		XMLWriter writer = new XMLWriter(bw ,OutputFormat.createPrettyPrint());
		writer.write(doc);
		writer.close();
	}

	private void fixResources(File resource) throws IOException{
		String line;
		List content = new ArrayList();
		
		getLog().info("Scanning " + resource.getAbsolutePath() + " for pageid.selvbetjening.externalLink...");
		BufferedReader reader = new BufferedReader(new FileReader(resource));
		while((line = reader.readLine()) != null){
			content.add(line);
		}
		
		for(int i = 0; i < content.size();i++){
			if(((String)content.get(i)).startsWith("pageid.selvbetjening.externalLink")){
				if(environment.get("websphere.app_port") != null && ((String)environment.get("websphere.app_port")).compareTo("") == 0){
					line = "pageid.selvbetjening.externalLink=http://" + environment.get("server.machine_name") + "/pselv/{0}?_flowId={1}&_brukerId={2}&_loggedOnName={3}&_erEgenAnsatt={4}\n";
				}else{
					line = "pageid.selvbetjening.externalLink=http://" + environment.get("server.machine_name") + ":" + environment.get("websphere.app_port") + "/pselv/{0}?_flowId={1}&_brukerId={2}&_loggedOnName={3}&_erEgenAnsatt={4}\n";
				}
				
				getLog().info("Updated to " + line);
				content.remove(i);
				content.add(i,line);
				break;
			}
		}
		
		getLog().info("Done!");
	}

	public void fixBndFile(File bndFile) throws DocumentException, IOException{
		Document doc;
		SAXReader reader;
		XPath search;
		Element table, auth, role, grp, subj;
		Map uris = new HashMap();
		
		getLog().info("Creating ibm-application-bnd.xmi...");
		//	creating basic binding file
		String content = "<applicationbnd:ApplicationBinding xmlns:applicationbnd=\"applicationbnd.xmi\" xmlns:xmi=\"http://www.omg.org/XMI\" xmi:version=\"2.0\" xmi:id=\"ApplicationBinding_1171012982796\">\n" +  
		  "<authorizationTable xmi:id=\"AuthorizationTable_1171012982796\"></authorizationTable>\n" +
		  "<application href=\"META-INF/application.xml#Application_ID\"/>\n" + 
		  "</applicationbnd:ApplicationBinding>";
		bndFile.delete();
		FileWriter writer = new FileWriter(bndFile);
		writer.write(content);
		writer.close();
		
		uris.put("ns","applicationbnd.xmi");
		reader = new SAXReader();
		doc = reader.read(bndFile);
		
		search = DocumentHelper.createXPath("/ns:ApplicationBinding/authorizationTable");
		search.setNamespaceURIs(uris);	
		table = (Element)search.selectSingleNode(doc);
		
		Enumeration enumeration = environment.keys();
		String prefix = "";
		if(module.compareTo("psak") == 0) prefix = "server.mapping.psak.";
		else if(module.compareTo("pselv") == 0) prefix = "server.mapping.pselv.";
		while(enumeration.hasMoreElements()){
			String key = (String)enumeration.nextElement();
			if(key.startsWith(prefix)){
				if(((String)environment.get(key))
						.indexOf("AllAuthenticated") >= 0) //AllAuthenticated user special mapping
					
				{
					String id = new Date().getTime() + "";
					auth = table.addElement("authorizations");
					auth.addAttribute("xmi:id","RoleAssignment_" + id);
					subj = auth.addElement("specialSubjects");
					subj.addAttribute("xmi:type","applicationbnd:AllAuthenticatedUsers");
					subj.addAttribute("xmi:id","AllAuthenticatedUsers_" + id);
					subj.addAttribute("name","AllAuthenticatedUsers");
					role = auth.addElement("role");
					role.addAttribute("href","META-INF/application.xml#SecurityRole_" + key.replaceAll(prefix, ""));
					
				}else //all other mappings are standard
				{
					auth = table.addElement("authorizations");
					auth.addAttribute("xmi:id","RoleAssignment_" + new Date().getTime());
					role = auth.addElement("role");
					role.addAttribute("href","META-INF/application.xml#SecurityRole_" + key.replaceAll(prefix, ""));
					grp = auth.addElement("groups");
					grp.addAttribute("xmi:id","Group_" + new Date().getTime());
					grp.addAttribute("name",(String)environment.get(key));
				}
				getLog().info("Added role mapping for '" + environment.get(key) + "'");
			}
		}

		writeXml(doc,bndFile);
		getLog().info("Done!");
	}
}
