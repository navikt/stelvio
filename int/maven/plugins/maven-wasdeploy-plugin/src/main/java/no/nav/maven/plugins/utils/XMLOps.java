package no.nav.maven.plugins.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Handles the XML operations of the deployment
 * 
 * @author test@example.com
 */
public class XMLOps {

	/*
	 * Adds the username/password elements inside the consumerFacadeBase for the pen/pselv-context.xml files
	 */
	public static void fixContext(String filePath) throws ParserConfigurationException, IOException, TransformerException, SAXException {

		System.out.println("[INFO] ##############################################");
		System.out.println("[INFO] ### Fixing PSAK/PSELV-context.xml file ... ###");
		System.out.println("[INFO] ##############################################");
		System.out.println("[INFO] ### For file: " + filePath);
		
		Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filePath);

		String usernameValue, passwordValue;

		if (filePath.contains("cfg-pen-context.xml")) {
			
			usernameValue = "${cons.pen.serviceusername}";
			passwordValue = "${cons.pen.servicepassword}";
			
		} else {
			
			usernameValue = "${cons.pselv.serviceusername}";
			passwordValue = "${cons.pselv.servicepassword}";
			
		}

		NodeList nl = xml.getElementsByTagName("bean");

		for (int i = 0; i < nl.getLength(); i++) {

			Element ele = (Element) nl.item(i);

			if (ele.getAttribute("id").equals("consumerFacadeBase")) {
				
				Element serviceUsername = xml.createElement("property");
				serviceUsername.setAttribute("name", "serviceUsername");
				serviceUsername.setAttribute("value", usernameValue);

				Element servicePassword = xml.createElement("property");
				servicePassword.setAttribute("name", "servicePassword");
				servicePassword.setAttribute("value", passwordValue);

				ele.appendChild(serviceUsername);
				ele.appendChild(servicePassword);
			}
		}

		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.METHOD, "xml");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");

		DOMSource xmlSource = new DOMSource(xml);
		StreamResult output = new StreamResult(filePath);

		tf.transform(xmlSource, output);

		System.out.println("[INFO] ### FIX-CONTEXT ### Successfully altered file: " + filePath);
	}
	
	/*
	 * Changes the session timeout for PSELV internsone
	 */
	public static void fixPSELVSessionTimeout(String filePath, String timeout) throws ParserConfigurationException, SAXException, IOException, TransformerException {

		System.out.println("[INFO] ##################################################################");
		System.out.println("[INFO] ### web.xml - Setting the PSELV internsone session timeout ... ###");
		System.out.println("[INFO] ##################################################################");
		
		Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filePath);
		
		xml.getElementsByTagName("session-timeout").item(0).setTextContent(timeout);
		
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.METHOD, "xml");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");

		DOMSource xmlSource = new DOMSource(xml);
		StreamResult output = new StreamResult(filePath);

		tf.transform(xmlSource, output);

		System.out.println("[INFO] ### FIX-SESSION-TIMEOUT ### Successfully set PSELV web session timeout to: " + timeout);
	}
	
	/*
	 * Changes PSELV login URL to redirect to access denied page
	 */
	public static void fixPSELVLoginConfig(String filePath, String url) throws ParserConfigurationException, SAXException, IOException, TransformerException {

		System.out.println("[INFO] ###########################################################");
		System.out.println("[INFO] ## web.xml - Changing the PSELV login configuration ... ###");
		System.out.println("[INFO] ###########################################################");
		
		Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filePath);
		
		xml.getElementsByTagName("form-login-page").item(0).setTextContent(url);
		xml.getElementsByTagName("form-error-page").item(0).setTextContent(url);
		
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.METHOD, "xml");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");

		DOMSource xmlSource = new DOMSource(xml);
		StreamResult output = new StreamResult(filePath);

		tf.transform(xmlSource, output);

		System.out.println("[INFO] ### FIX-LOGIN-CONFIG ### Successfully changed login config to: " + url);
	}
	
	/*
	 *  Creates a new "ibm-application-bnd.xmi" file at the given location, with the given template and application mapping
	 */
	public static void fixRoleMapping(File template, File ibmBndFileLocation, File applicationMapping) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		
		System.out.println("[INFO] ###################################################");
		System.out.println("[INFO] ### ibm-application-bnd.xmi being generated ... ###");
		System.out.println("[INFO] ###################################################");
		
		if (!template.exists()){
			throw new IOException("[ERROR] Error while generating role mapping file: The template file does not exist.");
		}
		if (!applicationMapping.exists()) {
			throw new IOException("[ERROR] Error while generating role mapping file: The application mapping file does not exist.");
		}
		
		Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(template);

		Node authorizationTable = xml.getElementsByTagName("authorizationTable").item(0);

		HashMap<String, String> map = parseRoleMapping(applicationMapping);
		Iterator iterator = map.keySet().iterator();

		while (iterator.hasNext()) {

			Object o = iterator.next();
			String name = o.toString();
			String roleId = map.get(name);

			String generatedId = new Date().getTime() + "";

			Element authorizations = xml.createElement("authorizations");
			authorizations.setAttribute("xmi:id", "RoleAssignment_" + generatedId);
			Element role = xml.createElement("role");
			role.setAttribute("href", "META-INF/application.xml#SecurityRole_" + roleId);

			if (name.equals("AllAuthenticatedUsers")) {

				Element specialSubjects = xml.createElement("specialSubjects");
				specialSubjects.setAttribute("xmi:type", "applicationbnd:AllAuthenticatedUsers");
				specialSubjects.setAttribute("xmi:id", "AllAuthenticatedUsers_" + generatedId);
				specialSubjects.setAttribute("name", name);

				authorizations.appendChild(specialSubjects);

			} else {

				Element groups = xml.createElement("groups");
				groups.setAttribute("xmi:id", "Group_" + new Date().getTime());
				groups.setAttribute("name", name);

				authorizations.appendChild(groups);
			}

			authorizations.appendChild(role);
			authorizationTable.appendChild(authorizations);
		}

		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.METHOD, "xml");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");

		DOMSource xmlSource = new DOMSource(xml);
		
		Writer w = new BufferedWriter(new FileWriter(ibmBndFileLocation));
		StreamResult output = new StreamResult(w);
		tf.transform(xmlSource, output);
		w.close();
		
		System.out.println("[INFO] ### FIX-ROLE-MAPPING ### Successfully created file: " + ibmBndFileLocation + " based on template: " + template + " and application mapping file : " + applicationMapping);
	}

	/*
	 * Parses the role mapping file, and puts the coherent values in the returned HashMap
	 */
	private static HashMap<String, String> parseRoleMapping(File rolemapping) throws ParserConfigurationException, SAXException, IOException {

		Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(rolemapping);
		NodeList mappings = xml.getElementsByTagName("mapping");

		HashMap<String, String> map = new HashMap<String, String>();
		
		for (int i = 0; i < mappings.getLength(); i++) {
			Element link = (Element) mappings.item(i);
			map.put(link.getAttribute("name"), link.getAttribute("role-id"));
		}

		return map;
	}
	
}
