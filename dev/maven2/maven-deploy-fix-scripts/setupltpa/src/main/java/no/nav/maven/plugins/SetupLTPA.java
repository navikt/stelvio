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
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

/**
 * Goal which touches a timestamp file.
 * 
 * @goal setupLTPA
 * 
 * @phase process-sources
 */
public class SetupLTPA extends AbstractMojo {
	/**
	 * This parameter is the workingarea where the modules are extracted from
	 * subversion.
	 * 
	 * @parameter expression="${workingarea}"
	 * @required
	 */
	private File workingArea; // = new File("E:\\tmp2\\target\\classes\\builds\\eardist\\temp");

	/**
	 * This parameter is the workingarea where the modules are extracted from
	 * subversion.
	 * 
	 * @parameter expression="${envfile}"
	 * @required
	 */
	private File envFile; //= new File("E:\\tmp2\\src\\main\\resources\\scripts\\environments\\SystestKjempen.properties");

	/**
	 * 
	 * @parameter expression="${module}"
	 * @required
	 */
	private String module; // = "pkort";

	private Properties props;

	private String roleId = null;

	private String roleIdTraf = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {

		try {
			readEnvFile();
			if (!(props.containsKey("roleNamePSAK") && 
				  props.containsKey("roleNamePSELV") && 
				  props.containsKey("usernameTrafikanten") && 
				  props.contains("roleNamePKORT"))) {
				throw new MojoExecutionException(
						"Environment doesn't contain definition for roleNamePSAK, roleNamePSELV or usernameTrafikanten, update environment file!");
			}
			if (module.compareToIgnoreCase("psak") == 0) {
				createAppXml(props.getProperty("roleNamePSAK"), props
						.getProperty("roleNamePSAK"));
			} else if (module.compareToIgnoreCase("pselv") == 0){
				createAppXml(props.getProperty("roleNamePSELV"), props
						.getProperty("roleNamePSELV"));
			} else if (module.compareToIgnoreCase("pkort") == 0){
				createAppXml(props.getProperty("roleNamePKORT"), props
						.getProperty("roleNamePKORT"));
			}

			createBndFile();

			getLog().info("All Done!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new MojoExecutionException(
					"Error performing security setup!", e);
		}
	}

	private String[] processExportFile(File export) throws Exception {
		Document doc;
		SAXReader reader;
		XPath search;
		Map uris = new HashMap();
		String wsDescNameLink, pcNameLink;

		uris.put("scdl", "http://www.ibm.com/xmlns/prod/websphere/scdl/6.0.0");
		uris.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		uris
				.put("_",
						"http://nav-cons-pen-psak-henvendelse/no/nav/inf/Binding");
		reader = new SAXReader();
		doc = reader.read(export);

		/**
		 * extracting service and port attributes from export file
		 */
		search = DocumentHelper
				.createXPath("/scdl:export/esbBinding[@xsi:type='webservice:WebServiceExportBinding']");
		search.setNamespaceURIs(uris);
		Element binding = (Element) search.selectSingleNode(doc);

		if (binding != null) {
			Attribute attr = binding.attribute("service");
			if (attr != null) {
				wsDescNameLink = attr.getValue().split(":")[1];
			} else {
				throw new DocumentException(
						"Could not find service name attribute in webservice:WebServiceExportBinding!");
			}
			attr = binding.attribute("port");
			if (attr != null) {
				pcNameLink = attr.getValue().split(":")[1];
			} else {
				throw new DocumentException(
						"Could not find port name attribute in webservice:WebServiceExportBinding!");
			}
		} else {
			throw new DocumentException(
					"Could not find webservice:WebServiceExportBinding in export file!");
		}

		return new String[] { wsDescNameLink, pcNameLink };
	}

	private File[] getConsModules() {
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.matches("nav-cons-.*");
			}
		};
		return workingArea.listFiles(filter);
	}

	private File[] getExportFiles(File path) {
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.endsWith(".export");
			}
		};
		return path.listFiles(filter);
	}

	private String getExportString(String wsDescNameLink, String PcNameLink) {
		String exportString;
		exportString = "<wsDescExtensions>\n"
				+ "<wsDescExt wsDescNameLink=\"#WSDESCNAMELINK#\">\n"
				+ "<pcBinding pcNameLink=\"#PCNAMELINK#\">\n"
				+ "<serverServiceConfig>\n"
				+ "<securityRequestConsumerServiceConfig>\n"
				+ "<caller name=\"LTPA_Caller\" part=\"\" uri=\"http://www.ibm.com/websphere/appserver/tokentype/5.0.2\" localName=\"LTPA\"/>\n"
				+ "<requiredSecurityToken name=\"LTPA\" uri=\"http://www.ibm.com/websphere/appserver/tokentype/5.0.2\" localName=\"LTPA\" usage=\"Required\"/>\n"
				+ "</securityRequestConsumerServiceConfig>\n"
				+ "</serverServiceConfig>\n"
				+ "</pcBinding>\n"
				+ "</wsDescExt>\n"
				+ "</wsDescExtensions>\n"
				+ "<wsDescBindings>\n"
				+ "<wsdescBindings wsDescNameLink=\"#WSDESCNAMELINK#\">\n"
				+ "<pcBindings pcNameLink=\"#PCNAMELINK#\">\n"
				+ "<securityRequestConsumerBindingConfig>\n"
				+ "<tokenConsumer classname=\"com.ibm.wsspi.wssecurity.token.LTPATokenConsumer\" name=\"LTPA_Token_Con\">\n"
				+ "<valueType localName=\"LTPA\" uri=\"http://www.ibm.com/websphere/appserver/tokentype/5.0.2\" name=\"LTPA Token\"/>\n"
				+ "<partReference part=\"LTPA\"/>\n" + "</tokenConsumer>\n"
				+ "</securityRequestConsumerBindingConfig>\n"
				+ "</pcBindings>\n" + "</wsdescBindings>\n"
				+ "</wsDescBindings>\n";
		return exportString.replaceAll("#WSDESCNAMELINK#", wsDescNameLink)
				.replaceAll("#PCNAMELINK#", PcNameLink);
	}

	private void createSca2Jee(File consModulePath) throws Exception {
		BufferedWriter writer;
		String content = "", line = "";
		List exports = new ArrayList();

		/**
		 * searching through export files and extracting wsdescnamelink and
		 * pcnamelink
		 */
		File[] exportFiles = getExportFiles(consModulePath);
		getLog().info("found " + exportFiles.length + " export file(s).");
		for (int i = 0; i < exportFiles.length; i++) {
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
		content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<scaj2ee:IntegrationModuleDeploymentConfiguration xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:scaj2ee=\"http://www.ibm.com/xmlns/prod/websphere/sca/j2ee/6.0.2\">\n"
				+ "<wsExports>\n";
		for (Iterator iter = exports.iterator(); iter.hasNext();) {
			content += (String) iter.next();
		}
		content += "</wsExports>\n</scaj2ee:IntegrationModuleDeploymentConfiguration>";

		File sca2jee = new File(consModulePath.getAbsolutePath()
				+ "/ibm-deploy.sca2jee");
		try {
			writer = new BufferedWriter(new FileWriter(sca2jee));
			writer.write(content);
			writer.close();

			// configuring xml layout
			SAXReader reader = new SAXReader();
			Document doc = reader.read(sca2jee);
			sca2jee.delete();

			XMLWriter xmlwriter = new XMLWriter(new FileWriter(sca2jee
					.getAbsoluteFile()), OutputFormat.createPrettyPrint());
			xmlwriter.write(doc.getRootElement());
			xmlwriter.close();
		} catch (Exception e) {
			throw new IOException(
					"Error writing ibm-deploy.sca2jee to disk: \n\n"
							+ e.getMessage());
		}
	}

	private void readEnvFile() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(envFile));
		props = new Properties();
		props.load(new FileInputStream(envFile));

		// Properties class doesn't trim property values, need to do manual
		// trimming
		Enumeration enumeration = props.keys();
		String key = null;
		while (enumeration.hasMoreElements()) {
			key = enumeration.nextElement().toString();
			props.setProperty(key, props.getProperty(key).trim());
		}
	}

	private void createAppXml(String strDesc, String strRoleName)
			throws DocumentException, IOException {
		Document doc;
		SAXReader reader;
		Element root, role, desc, name;
		Attribute id;
		XPath search;
		Map uris = new HashMap();

		getLog().info("Editing application.xml");

		reader = new SAXReader();
		doc = reader.read(new File(workingArea.getAbsolutePath()
				+ "/META-INF/application.xml"));

		// checking if role already defined
		uris = new HashMap();
		uris.put("ns", "http://java.sun.com/xml/ns/j2ee");
		search = DocumentHelper.createXPath("/ns:application/ns:security-role");
		search.setNamespaceURIs(uris);
		List roles = search.selectNodes(doc);
		if (!roles.isEmpty()) {
			for (Iterator iter = roles.iterator(); iter.hasNext();) {
				role = (Element) iter.next();
				name = role.element("role-name");
				if (name != null && name.getText().compareTo(strRoleName) == 0) {
					id = role.attribute("id");
					if (id == null) {
						throw new DocumentException(
								"Invalid role defined in application.xml (contains no id attribute!)");
					}
					roleId = id.getValue().replaceAll("SecurityRole_", "");
					getLog()
							.info(
									strRoleName
											+ " already defined in application.xml, storing role id: "
											+ roleId);
					break;
				} else if (name != null
						&& name.getText().compareTo(
								props.getProperty("roleNameTrafikanten")) == 0) {
					id = role.attribute("id");
					if (id == null) {
						throw new DocumentException(
								"Invalid role defined in application.xml (contains no id attribute!)");
					}
					roleIdTraf = id.getValue().replaceAll("SecurityRole_", "");
					getLog()
							.info(
									props.getProperty("roleNameTrafikanten")
											+ " already defined in application.xml, storing role id: "
											+ roleIdTraf);
					break;
				}
			}
		}

		if (roleId == null) {
			root = doc.getRootElement();
			role = root.addElement("security-role");
			roleId = (new Date()).getTime() + "";
			role.addAttribute("id", "SecurityRole_" + roleId);
			desc = role.addElement("description");
			name = role.addElement("role-name");
			desc.addText(strDesc);
			name.addText(strRoleName);
			getLog().info(
					"Created new security role (" + roleId + ") for "
							+ strRoleName);
		}

		if (roleIdTraf == null) {
			root = doc.getRootElement();
			role = root.addElement("security-role");
			roleIdTraf = (new Date()).getTime() + 1 + "";
			role.addAttribute("id", "SecurityRole_" + roleIdTraf);
			desc = role.addElement("description");
			name = role.addElement("role-name");
			desc.addText(props.getProperty("roleNameTrafikanten"));
			name.addText(props.getProperty("roleNameTrafikanten"));
			getLog().info(
					"Created new security role (" + roleIdTraf + ") for "
							+ props.getProperty("roleNameTrafikanten"));
		}

		writeXml(doc, new File(workingArea.getAbsolutePath()
				+ "/META-INF/application.xml"));
	}

	private void createBndFile() throws IOException, DocumentException {
		Document doc;
		SAXReader reader;
		Element root, table, auths, role, groups;
		String content = null;
		/**
		 * writing application-bnd basic structure
		 */
		if (module.compareToIgnoreCase("psak") == 0) {
			// checking if roles are mapped to special case EVERYONE
			if (props.getProperty("groupNamePSAK").contains("Everyone")) {
				content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<applicationbnd:ApplicationBinding xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:applicationbnd=\"applicationbnd.xmi\" xmi:id=\"ApplicationBinding_1188827937406\">\n"
						+ "<authorizationTable xmi:id=\"AuthorizationTable_1188827937406\">\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#ROLEID#\">\n"
						+ "<specialSubjects xmi:type=\"com.ibm.ejs.models.base.bindings.applicationbnd:Everyone\" xmi:id=\"Everyone_1210146126606\" name=\"Everyone\"/>"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#ROLEID#\"/>"
						+ "</authorizations>\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#TRAF_ROLEID#\">\n"
						+ "<users xmi:id=\"User_#TRAF_ROLEID#\" name=\"#TRAF_USERNAME#\"/>\n"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#TRAF_ROLEID#\"/>\n"
						+ "</authorizations>\n"
						+ "</authorizationTable>\n"
						+ "<application href=\"META-INF/application.xml#Application_ID\"/>\n"
						+ "</applicationbnd:ApplicationBinding>";
			} else { // normal group mapping
				content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<applicationbnd:ApplicationBinding xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:applicationbnd=\"applicationbnd.xmi\" xmi:id=\"ApplicationBinding_1188827937406\">\n"
						+ "<authorizationTable xmi:id=\"AuthorizationTable_1188827937406\">\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#ROLEID#\">\n"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#ROLEID#\"/>\n"
						+ "<groups xmi:id=\"Group_1189082314109\" name=\"#GRPNAME#\"/>\n"
						+ "</authorizations>\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#TRAF_ROLEID#\">\n"
						+ "<users xmi:id=\"User_#TRAF_ROLEID#\" name=\"#TRAF_USERNAME#\"/>\n"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#TRAF_ROLEID#\"/>\n"
						+ "</authorizations>\n"
						+ "</authorizationTable>\n"
						+ "<application href=\"META-INF/application.xml#Application_ID\"/>\n"
						+ "</applicationbnd:ApplicationBinding>";
			}

			content = content.replaceAll("#ROLEID#", roleId);
			content = content.replaceAll("#GRPNAME#", props
					.getProperty("groupNamePSAK"));
			content = content.replaceAll("#USERNAME#", props
					.getProperty("usernamePSELV"));

			content = content.replaceAll("#TRAF_ROLEID#", roleIdTraf);
			content = content.replaceAll("#TRAF_USERNAME#", props
					.getProperty("usernameTrafikanten"));
		} else if (module.compareToIgnoreCase("pselv") == 0){
			// checking if roles are mapped to special case EVERYONE
			if (props.getProperty("groupNamePSELV").contains("\"Everyone\"")) {
				content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<applicationbnd:ApplicationBinding xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:applicationbnd=\"applicationbnd.xmi\" xmi:id=\"ApplicationBinding_1188827937406\">\n"
						+ "<authorizationTable xmi:id=\"AuthorizationTable_1188827937406\">\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#ROLEID#\">\n"
						+ "<specialSubjects xmi:type=\"com.ibm.ejs.models.base.bindings.applicationbnd:Everyone\" xmi:id=\"Everyone_1210146126606\" name=\"Everyone\"/>"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#ROLEID#\"/>"
						+ "</authorizations>\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#TRAF_ROLEID#\">\n"
						+ "<users xmi:id=\"User_#TRAF_ROLEID#\" name=\"#TRAF_USERNAME#\"/>\n"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#TRAF_ROLEID#\"/>\n"
						+ "</authorizations>\n"
						+ "</authorizationTable>\n"
						+ "<application href=\"META-INF/application.xml#Application_ID\"/>\n"
						+ "</applicationbnd:ApplicationBinding>";
			} else {	//normal group mapping
				content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<applicationbnd:ApplicationBinding xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:applicationbnd=\"applicationbnd.xmi\" xmi:id=\"ApplicationBinding_1188827937406\">\n"
						+ "<authorizationTable xmi:id=\"AuthorizationTable_1188827937406\">\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#ROLEID#\">\n"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#ROLEID#\"/>\n"
						+ "<groups xmi:id=\"Group_1189082314109\" name=\"#GRPNAME#\"/>\n"
						+ "</authorizations>\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#TRAF_ROLEID#\">\n"
						+ "<users xmi:id=\"User_#TRAF_ROLEID#\" name=\"#TRAF_USERNAME#\"/>\n"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#TRAF_ROLEID#\"/>\n"
						+ "</authorizations>\n"
						+ "</authorizationTable>\n"
						+ "<application href=\"META-INF/application.xml#Application_ID\"/>\n"
						+ "</applicationbnd:ApplicationBinding>";
			}
			content = content.replaceAll("#ROLEID#", roleId);
			content = content.replaceAll("#GRPNAME#", props
					.getProperty("groupNamePSELV"));
			content = content.replaceAll("#USERNAME#", props
					.getProperty("usernamePSELV"));

			content = content.replaceAll("#TRAF_ROLEID#", roleIdTraf);
			content = content.replaceAll("#TRAF_USERNAME#", props
					.getProperty("usernameTrafikanten"));
		} else if (module.compareToIgnoreCase("pkort") == 0){
			//checking if roles are mapped to special case EVERYONE
			if (props.getProperty("groupNamePKORT").contains("\"Everyone\"")) {
				content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<applicationbnd:ApplicationBinding xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:applicationbnd=\"applicationbnd.xmi\" xmi:id=\"ApplicationBinding_1188827937406\">\n"
						+ "<authorizationTable xmi:id=\"AuthorizationTable_1188827937406\">\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#ROLEID#\">\n"
						+ "<specialSubjects xmi:type=\"com.ibm.ejs.models.base.bindings.applicationbnd:Everyone\" xmi:id=\"Everyone_1210146126606\" name=\"Everyone\"/>"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#ROLEID#\"/>"
						+ "</authorizations>\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#TRAF_ROLEID#\">\n"
						+ "<users xmi:id=\"User_#TRAF_ROLEID#\" name=\"#TRAF_USERNAME#\"/>\n"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#TRAF_ROLEID#\"/>\n"
						+ "</authorizations>\n"
						+ "</authorizationTable>\n"
						+ "<application href=\"META-INF/application.xml#Application_ID\"/>\n"
						+ "</applicationbnd:ApplicationBinding>";
			} else {	//normal group mapping
				content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<applicationbnd:ApplicationBinding xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:applicationbnd=\"applicationbnd.xmi\" xmi:id=\"ApplicationBinding_1188827937406\">\n"
						+ "<authorizationTable xmi:id=\"AuthorizationTable_1188827937406\">\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#ROLEID#\">\n"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#ROLEID#\"/>\n"
						+ "<groups xmi:id=\"Group_1189082314109\" name=\"#GRPNAME#\"/>\n"
						+ "</authorizations>\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#TRAF_ROLEID#\">\n"
						+ "<users xmi:id=\"User_#TRAF_ROLEID#\" name=\"#TRAF_USERNAME#\"/>\n"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#TRAF_ROLEID#\"/>\n"
						+ "</authorizations>\n"
						+ "</authorizationTable>\n"
						+ "<application href=\"META-INF/application.xml#Application_ID\"/>\n"
						+ "</applicationbnd:ApplicationBinding>";
			}
			content = content.replaceAll("#ROLEID#", roleId);
			content = content.replaceAll("#GRPNAME#", props
					.getProperty("groupNamePKORT"));
			content = content.replaceAll("#USERNAME#", props
					.getProperty("usernamePKORT"));

			content = content.replaceAll("#TRAF_ROLEID#", roleIdTraf);
			content = content.replaceAll("#TRAF_USERNAME#", props
					.getProperty("usernameTrafikanten"));
		}

		getLog().info("Writing ibm-application-bnd.xml");
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				workingArea.getAbsolutePath()
						+ "/META-INF/ibm-application-bnd.xmi")));
		writer.write(content);
		writer.close();
	}

	private void writeXml(Document doc, File out) throws IOException {
		out.delete();
		XMLWriter writer = new XMLWriter(new FileWriter(out.getAbsoluteFile()),
				OutputFormat.createPrettyPrint());
		writer.write(doc.getRootElement());
		writer.close();
	}

}
