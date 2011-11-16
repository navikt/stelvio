package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author test@example.com
 * 
 * This util class performs the XML parsing + mining for the XML files in the moduleconfig directory.
 */

public class XMLUtils {

	/**
	 * Returns the complete string with all user/group role-mapping + runas on the form:
	 * _rolename::user1 user2... userx::group1 group2... groupx::runas-username runas-password|rolename...
	 */
	public static String parseRoleMappings(String env, String envClass, String fileName, String moduleConfigPath, File file) throws SAXException, IOException, ParserConfigurationException {

		Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

		NodeList roles = xml.getElementsByTagName("role");

		StringBuilder s = new StringBuilder();

		for (int i = 0; i < roles.getLength(); i++) {

			NodeList roleElements = roles.item(i).getChildNodes();
			for (int j = 0; j < roleElements.getLength(); j++) {

				Node roleElement = roleElements.item(j);

				if (roleElement.getNodeName().equals("name")) {
					if (s.length() == 0)
						s.append(roleElement.getChildNodes().item(0).getNodeValue());
					else
						s.append("\" \"" + roleElement.getChildNodes().item(0).getNodeValue());
				} else if (roleElement.getNodeName().equals("users")) {

					NodeList users = roleElement.getChildNodes();
					boolean first = true;

					for (int k = 0; k < users.getLength(); k++) {
						Node user = users.item(k);
						if (user.getNodeName().equals("user")) {
							NodeList userChildren = user.getChildNodes();
							for (int l = 0; l < userChildren.getLength(); l++) {
								Node userChild = userChildren.item(l);
								if (userChild.getNodeName().equals("name")) {
									if (first) {
										s.append("::_user::" + userChild.getChildNodes().item(0).getNodeValue());
										first = false;
									} else {
										s.append("|" + userChild.getChildNodes().item(0).getNodeValue());
									}
								}
							}
						}
					}
				} else if (roleElement.getNodeName().equals("groups")) {

					NodeList groups = roleElement.getChildNodes();
					boolean first = true;

					for (int k = 0; k < groups.getLength(); k++) {
						Node group = groups.item(k);

						if (group.getNodeName().equals("group")) {
							NodeList groupChildren = group.getChildNodes();
							for (int l = 0; l < groupChildren.getLength(); l++) {
								Node groupChild = groupChildren.item(l);
								if (groupChild.getNodeName().equals("name")) {
									if (first) {
										s.append("::_groups::" + groupChild.getChildNodes().item(0).getNodeValue());
										first = false;
									} else {
										s.append("|" + groupChild.getChildNodes().item(0).getNodeValue());
									}
								}
							}
						}
					}
				} else if (roleElement.getNodeName().equals("runas")) {

					NodeList runas = roleElement.getChildNodes();

					for (int m = 0; m < runas.getLength(); m++) {
						if (runas.item(m).getNodeName().equals("username")) {
							s.append("::_runas::" + runas.item(m).getChildNodes().item(0).getNodeValue());
						} else if (runas.item(m).getNodeName().equals("password")) {
							s.append("|" + runas.item(m).getChildNodes().item(0).getNodeValue());
						}
					}
				}
			}
		}

		return s.toString();
	}
	
	/**
	 * returns <modulename>;username=<username>;password=<passwd>;policyBinding=<bindin> 
	 */
	public static String parseUsernameTokenDetails(File file) throws SAXException, IOException, ParserConfigurationException {
		Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

		NodeList usernametoken = xml.getElementsByTagName("usernametoken");

		if (usernametoken.getLength() == 0) {
			return null;
		}
		
		String moduleName = file.getName().replace(".xml", "");
		
		StringBuilder returnString = new StringBuilder();
		returnString.append(moduleName+";");
		
		for (int i = 0; i < usernametoken.getLength(); i++) {
			NodeList usernametokenElements = usernametoken.item(i).getChildNodes();
			for (int j = 0; j < usernametokenElements.getLength(); j++) {
				if (usernametokenElements.item(j).getNodeName().equals("username")) {
					returnString.append("username="+usernametokenElements.item(j).getChildNodes().item(0).getNodeValue()+";");
				} else if (usernametokenElements.item(j).getNodeName().equals("password")) {
					returnString.append("password="+usernametokenElements.item(j).getChildNodes().item(0).getNodeValue()+";");
				} else if (usernametokenElements.item(j).getNodeName().equals("policyBinding")) {
					returnString.append("policyBinding="+usernametokenElements.item(j).getChildNodes().item(0).getNodeValue());
				}
			}
		}
				
		return returnString.toString();
	}
	
	/**
	 * Returns the complete string with all endpoint names and values for a given module on the format:
	 * modulename::name::value;modulename ... 
	 */
	public static String parseWebServiceEndpoints(File file) throws SAXException, IOException, ParserConfigurationException, FactoryConfigurationError {

		Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

		NodeList endpoints = xml.getElementsByTagName("endpoint");

		if (endpoints.getLength() == 0) {
			return null;
		}

		String moduleName = file.getName().replace(".xml", "");

		StringBuilder returnString = new StringBuilder();

		for (int i = 0; i < endpoints.getLength(); i++) {
			
			if (returnString.length() != 0) {
				returnString.append(";");
			} 

			returnString.append(moduleName);

			NodeList endpointElements = endpoints.item(i).getChildNodes();

			for (int j = 0; j < endpointElements.getLength(); j++) {

				Node endpointElement = endpointElements.item(j);
				if (endpointElement.getNodeName().equals("name")) {
					returnString.append("::" + endpointElement.getChildNodes().item(0).getTextContent());
				}
				if (endpointElement.getNodeName().equals("value")) {
					returnString.append("::" + endpointElement.getChildNodes().item(0).getTextContent());
				}
			}
		}

		return returnString.toString();
	}
	
	/**
	 * Returns the complete string with all activation specifications for a given module on the format:
	 * name::maxconcurrency;name ... 
	 */
	public static String parseActivationSpecs(File file) throws SAXException, IOException, ParserConfigurationException, FactoryConfigurationError {

		Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

		NodeList aSpecs = xml.getElementsByTagName("activationspecification");

		if (aSpecs.getLength() == 0) {
			return null;
		}

		StringBuilder returnString = new StringBuilder();

		for (int i = 0; i < aSpecs.getLength(); i++) {
			
			NodeList aSpecElements = aSpecs.item(i).getChildNodes();

			for (int j = 0; j < aSpecElements.getLength(); j++) {

				Node endpointElement = aSpecElements.item(j);
				if (endpointElement.getNodeName().equals("name")) {
					returnString.append(endpointElement.getChildNodes().item(0).getTextContent());
				}
				if (endpointElement.getNodeName().equals("maxconcurrency")) {
					returnString.append("::" + endpointElement.getChildNodes().item(0).getTextContent());
				}
			}
		}
		return returnString.toString();
	}
	
	
}
