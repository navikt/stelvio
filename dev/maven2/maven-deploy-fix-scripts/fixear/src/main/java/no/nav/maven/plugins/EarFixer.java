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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
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
	 * @parameter expression="${workingarea}"
	 * @required
	 */
	private File workingArea; // = new File("F:\\Temp");
	
	/**
	 * This parameter is the ear source file
	 * @parameter expression="${earfile}"
	 * @required
	 */
	private File earFile; // = new File("F:\\mojo\\nav-pensjon-pselv-jee-1.0.29.D3.ear");
	
	/**
	 * This parameter is the environment file containing environment properties
	 * @parameter expression="${envfile}"
	 * @required
	 */
	private File environFile; // = new File("F:\\mojo\\10.51.9.62.xml");
	
	private Dictionary environment;
	
	private String module;

	/* (non-Javadoc)
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {	
		module = earFile.getName().split("-")[2].toLowerCase();
		try {
			
			parseEnvironFile();
			
			extractEarFile(earFile);
			
			if(((String)environment.get("server.domain")).toUpperCase().compareTo("SENSITIVSONE") == 0 &&
					module.toUpperCase().compareTo("PSELV")==0){
				
				fixWebXml(new File(workingArea.getAbsolutePath() + "/war/WEB-INF/web.xml"));
				
				fixPrsCommon(new File(workingArea.getAbsolutePath() + "/war/WEB-INF/spring/prs-common-context.xml"));
				
				fixPrsSecurity(new File(workingArea.getAbsolutePath() + "/war/WEB-INF/security/prs-security-context.xml"));
				
				fixFacesSecurity(new File(workingArea.getAbsolutePath() + "/war/WEB-INF/security/faces-security-config.xml"));
				
			}else{
				getLog().info("No tweaking needed for " + module + " in internsone");
			}
			
			if(module.toUpperCase().compareTo("PSAK") == 0){
				fixResources(new File(workingArea.getAbsolutePath() + "/war/WEB-INF/classes/resources.properties"));
			}
			
			fixBndFile(new File(workingArea.getAbsolutePath() + "/META-INF/ibm-application-bnd.xmi"));
			
			delete(new File(workingArea.getAbsolutePath() + "/war/WEB-INF/classes/log4j.properties"));
			
			compressEarFile();
			
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new MojoExecutionException("Error editing files! See above for details...", e1);
		}
	}
	
	public void extractEarFile(File earFile) throws IOException {
		//extracting ear file
		getLog().info("Extracting ear file...");
		workingArea = new File(workingArea.getAbsolutePath() + "\\" + earFile.getName());
		workingArea.mkdirs();
		ZipUtils.extract(earFile,workingArea);

		//extracting inner war file
		String[] temp = earFile.getName().split("-");
		if (temp.length != 5) {
			throw new IOException("Unexpected ear file name pattern, expected nav-pensjon-[module]-[version].ear, got " + earFile.getName());
		}
		File warFile = new File(workingArea.getAbsolutePath() + "/nav-presentation-pensjon-" + temp[2] + "-web-" + temp[4].substring(0,temp[4].length()-4) + ".war");
		ZipUtils.extract(warFile,new File(workingArea.getAbsolutePath() + "/war"));
		if(!delete(warFile)) getLog().warn("Unable to delete war file after extracting war content");
		getLog().info("Done!");
	}
	
	public void compressEarFile() throws IOException{
		String[] temp = earFile.getName().split("-");
		
		getLog().info("Compressing ear file back together...");
		
		//compressing inner war file
		ZipUtils.compress(new File(workingArea.getAbsolutePath() + "/war"),new File(workingArea.getAbsolutePath() + "/nav-presentation-pensjon-" + temp[2] + "-web-" + temp[4].substring(0,temp[4].length()-4) + ".war"));
	
		//removing temporary war content
		System.gc();
		if(!delete(new File(workingArea.getAbsolutePath() + "\\war"))){
			getLog().warn("Unable to delete temporary files!");
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
			
			environment.put("server." + ((Element)server.elements().get(i)).getName(),((Element)server.elements().get(i)).getText());
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
		Element filter, mapping, servlet, config, role, child;
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
		
		/**
		 * searching for InnloggingPSAKFilter and updates it
		 */
		search = doc.createXPath("/ns:web-app/ns:filter[ns:filter-name='InnloggingPSAKFilter']");
		search.setNamespaceURIs(uris);
		results = search.selectNodes(doc);
		if(results.size() >= 1)
		{
			filter = (Element)results.get(0);
			getLog().info("InnloggingPSAKFilter filter already defined, updating filter...");
			filter.clearContent();
			Element child1 = filter.addElement("display-name");
			Element child2 = filter.addElement("filter-name");
			Element child3 = filter.addElement("filter-class");
			Element child4 = filter.addElement("init-param");
			Element subChild1 = child4.addElement("param-name");
			Element subChild2 = child4.addElement("param-value");
			child1.setText("InnloggingPSAKFilter");
			child2.setText("InnloggingPSAKFilter");
			child3.setText("no.nav.presentation.pensjon.pselv.tilleggsfunksjonalitet.InnloggingPSAKFilter");
			subChild1.setText("no.nav.presentation.pensjon.pselv.tilleggsfunksjonalitet.InnloggingPSAKFilter.ALLOW_ACCESS");
			subChild2.setText("VEILEDER");
		}else{
			filter = doc.getRootElement().addElement("filter");
			Element child1 = filter.addElement("display-name");
			Element child2 = filter.addElement("filter-name");
			Element child3 = filter.addElement("filter-class");
			Element child4 = filter.addElement("init-param");
			Element subChild1 = child4.addElement("param-name");
			Element subChild2 = child4.addElement("param-value");
			child1.addText("InnloggingPSAKFilter");
			child2.addText("InnloggingPSAKFilter");
			child3.addText("no.nav.presentation.pensjon.pselv.tilleggsfunksjonalitet.InnloggingPSAKFilter");
			subChild1.addText("no.nav.presentation.pensjon.pselv.tilleggsfunksjonalitet.InnloggingPSAKFilter.ALLOW_ACCESS");
			subChild2.addText("VEILEDER");
			getLog().info("InnloggingPSAKFilter not found, created new filter");
		}

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
				((Element)config.elements().get(0)).addText("/pages/tekniskfeilside.jsf");
				((Element)config.elements().get(1)).addText("/pages/tekniskfeilside.jsf");
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
		XMLWriter writer = new XMLWriter(new FileWriter(out.getAbsoluteFile()),OutputFormat.createPrettyPrint());
		writer.write(doc.getRootElement());
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
					line = "pageid.selvbetjening.externalLink=http://" + environment.get("server.machine_name") + "/{0}?_flowId={1}&_brukerId={2}&_loggedOnName={3}\n";
				}else{
					line = "pageid.selvbetjening.externalLink=http://" + environment.get("server.machine_name") + ":" + environment.get("websphere.app_port") + "/{0}?_flowId={1}&_brukerId={2}&_loggedOnName={3}\n";
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
		Map uris = new HashMap();
	
		getLog().info("Scanning ibm-application-bnd.xmi...");
	
		uris.put("ns","applicationbnd.xmi");
		reader = new SAXReader();
		doc = reader.read(bndFile);
		
		search = DocumentHelper.createXPath("/ns:ApplicationBinding/authorizationTable/authorizations/groups");
		search.setNamespaceURIs(uris);
		List auths = search.selectNodes(doc);
		Element grp;
		Attribute attrib;
		for(Iterator iter = auths.iterator();iter.hasNext();){
			grp = (Element)iter.next();
			attrib = grp.attribute("name");
			attrib.setValue((String)environment.get("server.security-roles"));
		}
		
		writeXml(doc,bndFile);
		
		getLog().info("Updated " + auths.size() + " security roles");
		getLog().info("Done!");
		
	}
}
