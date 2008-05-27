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
	private File envFile; // = new File("E:\\tmp2\\src\\main\\resources\\scripts\\environments\\SystestKjempen.properties");

	/**
	 * 
	 * @parameter expression="${module}" 
	 * @required
	 */
	private String module; // = "psak";

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
				  props.containsKey("roleNamePKORT"))) {
				throw new MojoExecutionException(
						"Environment doesn't contain definition for roleNamePSAK, roleNamePSELV, roleNamePKORT or usernameTrafikanten, update environment file!");
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

	private File[] getConsModules() {
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.matches("nav-cons-.*");
			}
		};
		return workingArea.listFiles(filter);
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
						+ "<com.ibm.ejs.models.base.bindings.applicationbnd:ApplicationBinding xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:com.ibm.ejs.models.base.bindings.applicationbnd=\"applicationbnd.xmi\" xmi:id=\"ApplicationBinding_#ROLEID#\">\n"
						+ "<authorizationTable xmi:id=\"AuthorizationTable_1188827937406\">\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#ROLEID#\">\n"
						+ "<specialSubjects xmi:type=\"com.ibm.ejs.models.base.bindings.applicationbnd:Everyone\" xmi:id=\"Everyone_#ROLEID#\" name=\"Everyone\"/>\n"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#ROLEID#\"/>\n"
						+ "</authorizations>\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#TRAF_ROLEID#\">\n"
						+ "<users xmi:id=\"User_#TRAF_ROLEID#\" name=\"#TRAF_USERNAME#\"/>\n"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#TRAF_ROLEID#\"/>\n"
						+ "</authorizations>\n"
						+ "</authorizationTable>\n"
						+ "<application href=\"META-INF/application.xml#Application_ID\"/>\n"
						+ "</com.ibm.ejs.models.base.bindings.applicationbnd:ApplicationBinding>";
			} else { // normal group mapping
				content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<com.ibm.ejs.models.base.bindings.applicationbnd:ApplicationBinding xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:com.ibm.ejs.models.base.bindings.applicationbnd=\"applicationbnd.xmi\" xmi:id=\"ApplicationBinding_#ROLEID#\">\n"
						+ "<authorizationTable xmi:id=\"AuthorizationTable_#ROLEID#\">\n"
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
						+ "</com.ibm.ejs.models.base.bindings.applicationbnd:ApplicationBinding>";
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
						+ "<com.ibm.ejs.models.base.bindings.applicationbnd:ApplicationBinding xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:com.ibm.ejs.models.base.bindings.applicationbnd=\"applicationbnd.xmi\" xmi:id=\"ApplicationBinding_#ROLEID#\">\n"
						+ "<authorizationTable xmi:id=\"AuthorizationTable_1188827937406\">\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#ROLEID#\">\n"
						+ "<specialSubjects xmi:type=\"com.ibm.ejs.models.base.bindings.applicationbnd:Everyone\" xmi:id=\"Everyone_#ROLEID#\" name=\"Everyone\"/>\n"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#ROLEID#\"/>\n"
						+ "</authorizations>\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#TRAF_ROLEID#\">\n"
						+ "<users xmi:id=\"User_#TRAF_ROLEID#\" name=\"#TRAF_USERNAME#\"/>\n"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#TRAF_ROLEID#\"/>\n"
						+ "</authorizations>\n"
						+ "</authorizationTable>\n"
						+ "<application href=\"META-INF/application.xml#Application_ID\"/>\n"
						+ "</com.ibm.ejs.models.base.bindings.applicationbnd:ApplicationBinding>";
			} else {	//normal group mapping
				content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<com.ibm.ejs.models.base.bindings.applicationbnd:ApplicationBinding xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:com.ibm.ejs.models.base.bindings.applicationbnd=\"applicationbnd.xmi\" xmi:id=\"ApplicationBinding_#ROLEID#\">\n"
						+ "<authorizationTable xmi:id=\"AuthorizationTable_#ROLEID#\">\n"
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
						+ "</com.ibm.ejs.models.base.bindings.applicationbnd:ApplicationBinding>";
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
						+ "<com.ibm.ejs.models.base.bindings.applicationbnd:ApplicationBinding xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:com.ibm.ejs.models.base.bindings.applicationbnd=\"applicationbnd.xmi\" xmi:id=\"ApplicationBinding_#ROLEID#\">\n"
						+ "<authorizationTable xmi:id=\"AuthorizationTable_#ROLEID#\">\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#ROLEID#\">\n"
						+ "<specialSubjects xmi:type=\"com.ibm.ejs.models.base.bindings.applicationbnd:Everyone\" xmi:id=\"Everyone_#ROLEID#\" name=\"Everyone\"/>\n"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#ROLEID#\"/>\n"
						+ "</authorizations>\n"
						+ "<authorizations xmi:id=\"RoleAssignment_#TRAF_ROLEID#\">\n"
						+ "<users xmi:id=\"User_#TRAF_ROLEID#\" name=\"#TRAF_USERNAME#\"/>\n"
						+ "<role href=\"META-INF/application.xml#SecurityRole_#TRAF_ROLEID#\"/>\n"
						+ "</authorizations>\n"
						+ "</authorizationTable>\n"
						+ "<application href=\"META-INF/application.xml#Application_ID\"/>\n"
						+ "</com.ibm.ejs.models.base.bindings.applicationbnd:ApplicationBinding>";
			} else {	//normal group mapping
				content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<com.ibm.ejs.models.base.bindings.applicationbnd:ApplicationBinding xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:com.ibm.ejs.models.base.bindings.applicationbnd=\"applicationbnd.xmi\" xmi:id=\"ApplicationBinding_#ROLEID#\">\n"
						+ "<authorizationTable xmi:id=\"AuthorizationTable_#ROLEID#\">\n"
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
						+ "</com.ibm.ejs.models.base.bindings.applicationbnd:ApplicationBinding>";
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
