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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import sun.misc.CompoundEnumeration;

/**
 * Goal which touches a timestamp file.
 *
 * @goal setupLTPA
 * 
 * @phase process-sources
 */
public class SetupLTPA
    extends AbstractMojo
{
	/**
	 * This parameter is the temporary directory where the files are unpacked.
	 * @parameter expression="${workingarea}"
	 * @required
	 */
	private File workingArea; // = new File("F:\\moose_deployment\\services\\rekrutten\\target\\classes\\builds\\workspace");
	

	/* (non-Javadoc)
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {	
		getLog().info("Scanning for consumer modules...");
		File[] cons = getConsModules();
		for(int i = 0; i < cons.length; i++){
			getLog().info("Found " + cons[i].getName() + ", scanning content...");
			try {
				createSca2Jee(cons[i]);
			} catch (Exception e) {
				getLog().error("Error while scanning consumer module:");
				e.printStackTrace();
			}
		}
		getLog().info("All ibm-deploy.sca2jee files generated successfully!");
	}
	
	private String[] processExportFile(File export) throws Exception{
		Document doc;
		SAXReader reader;
		XPath search;
		Map uris = new HashMap();
		String wsDescNameLink, pcNameLink;
		
		uris.put("scdl","http://www.ibm.com/xmlns/prod/websphere/scdl/6.0.0");
		uris.put("xsi","http://www.w3.org/2001/XMLSchema-instance");
		uris.put("_","http://nav-cons-pen-psak-henvendelse/no/nav/inf/Binding");
		reader = new SAXReader();
		doc = reader.read(export);
		
		/**
		 * extracting service and port attributes from export file
		 */
		search = DocumentHelper.createXPath("/scdl:export/esbBinding[@xsi:type='webservice:WebServiceExportBinding']");
		search.setNamespaceURIs(uris);
		Element binding = (Element)search.selectSingleNode(doc);
		
		if(binding != null){
			Attribute attr = binding.attribute("service");
			if(attr != null){
				wsDescNameLink = attr.getValue().split(":")[1];
			}else{
				throw new DocumentException("Could not find service name attribute in webservice:WebServiceExportBinding!");
			}
			attr = binding.attribute("port");
			if(attr != null){
				pcNameLink = attr.getValue().split(":")[1];
			}else{
				throw new DocumentException("Could not find port name attribute in webservice:WebServiceExportBinding!");
			}
		}else{
			throw new DocumentException("Could not find webservice:WebServiceExportBinding in export file!");
		}
		
		return new String[]{wsDescNameLink,pcNameLink};
	}	
	
	private File[] getConsModules(){
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.matches("nav-cons-.*");
			}
		};
		return workingArea.listFiles(filter);
	}
	
	private File[] getExportFiles(File path){
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.endsWith(".export");
			}
		};
		return path.listFiles(filter);
	}
	
	private String getExportString(String wsDescNameLink, String PcNameLink){
		String exportString;
		exportString = "<wsDescExtensions>\n" +
	      "<wsDescExt wsDescNameLink=\"#WSDESCNAMELINK#\">\n" +
	        "<pcBinding pcNameLink=\"#PCNAMELINK#\">\n" +
	          "<serverServiceConfig>\n" +
	            "<securityRequestConsumerServiceConfig>\n" +
	              "<caller name=\"LTPA_Caller\" part=\"\" uri=\"http://www.ibm.com/websphere/appserver/tokentype/5.0.2\" localName=\"LTPA\"/>\n" +
	              "<requiredSecurityToken name=\"LTPA\" uri=\"http://www.ibm.com/websphere/appserver/tokentype/5.0.2\" localName=\"LTPA\" usage=\"Required\"/>\n" +
	            "</securityRequestConsumerServiceConfig>\n" +
	          "</serverServiceConfig>\n" +
	        "</pcBinding>\n" +
	      "</wsDescExt>\n" +
	    "</wsDescExtensions>\n" +
	    "<wsDescBindings>\n" +
	      "<wsdescBindings wsDescNameLink=\"#WSDESCNAMELINK#\">\n" +
	        "<pcBindings pcNameLink=\"#PCNAMELINK#\">\n" +
	          "<securityRequestConsumerBindingConfig>\n" +
	            "<tokenConsumer classname=\"com.ibm.wsspi.wssecurity.token.LTPATokenConsumer\" name=\"LTPA_Token_Con\">\n" +
	              "<valueType localName=\"LTPA\" uri=\"http://www.ibm.com/websphere/appserver/tokentype/5.0.2\" name=\"LTPA Token\"/>\n" +
	              "<partReference part=\"LTPA\"/>\n" +
	            "</tokenConsumer>\n" +
	          "</securityRequestConsumerBindingConfig>\n" +
	        "</pcBindings>\n" +
	      "</wsdescBindings>\n" +
	    "</wsDescBindings>\n";
		return exportString.replaceAll("#WSDESCNAMELINK#", wsDescNameLink).replaceAll("#PCNAMELINK#", PcNameLink);
	}
	
	private void createSca2Jee(File consModulePath) throws Exception{
		BufferedWriter writer;
		String content = "", line = "";
		List exports = new ArrayList();
		
		/**
		 * searching through export files and extracting wsdescnamelink and pcnamelink
		 */
		File[] exportFiles = getExportFiles(consModulePath);
		getLog().info("found " + exportFiles.length + " export file(s).");
		for(int i = 0; i < exportFiles.length; i++){
			try {
				getLog().info("processing " + exportFiles[i].getName() + "...");
				String[] result = processExportFile(exportFiles[i]);
				exports.add(getExportString(result[0], result[1]));
				getLog().info("Done!");
			} catch (Exception e) {
				throw new Exception("Error processing export files!", e);
			}
		}
		
		/**
		 * constructing ibm-deploy.sca2jee content
		 */
		content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				  "<scaj2ee:IntegrationModuleDeploymentConfiguration xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:scaj2ee=\"http://www.ibm.com/xmlns/prod/websphere/sca/j2ee/6.0.2\">\n" +
				  "<wsExports>\n";
		for(Iterator iter = exports.iterator(); iter.hasNext();){
			content += (String)iter.next();
		}
		content += "</wsExports>\n</scaj2ee:IntegrationModuleDeploymentConfiguration>";

		File sca2jee = new File(consModulePath.getAbsolutePath() + "/ibm-deploy.sca2jee");
		try {
			writer = new BufferedWriter(new FileWriter(sca2jee));
			writer.write(content);
			writer.close();
			
			//configuring xml layout
			SAXReader reader = new SAXReader();
			Document doc = reader.read(sca2jee);
			sca2jee.delete();
			
			XMLWriter xmlwriter = new XMLWriter(new FileWriter(sca2jee.getAbsoluteFile()),OutputFormat.createPrettyPrint());
			xmlwriter.write(doc.getRootElement());
			xmlwriter.close();
		} catch (Exception e) {
			throw new IOException("Error writing ibm-deploy.sca2jee to disk: \n\n" + e.getMessage());
		}
	}
}
