package no.nav.maven.plugin.websphere.plugin.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * This util class performs the XML parsing + mining for the XML files in the moduleconfig directory.
 * 
 * @author test@example.com
 *
 */

public class XML {

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {

		XML xml = new XML();

		// koble inn findFile her.

		File consXml = new File("C:/cons.xml");
		System.out.println(xml.getRoleMappingString(consXml));
	}
	
	/**
	 * Returns the complete string with all user/group role-mapping + runas on the form:
	 * rolename::user1 user2... userx::group1 group2... groupx::runas-username runas-password||rolename...
	 */
	private String getRoleMappingString(File file) throws SAXException, IOException, ParserConfigurationException {

		Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

		NodeList roles = xml.getElementsByTagName("role");

		StringBuilder s = new StringBuilder();

		for (int i = 0; i < roles.getLength(); i++) {

			NodeList roleElements = roles.item(i).getChildNodes();
			for (int j = 0; j < roleElements.getLength(); j++) {

				Node roleElement = roleElements.item(j);

				if (roleElement.getNodeName().equals("name")) {
					System.out.println("Found role: " + roleElement.getChildNodes().item(0).getNodeValue());
					if (s.length() == 0)
						s.append(roleElement.getChildNodes().item(0).getNodeValue());
					else
						s.append("||" + roleElement.getChildNodes().item(0).getNodeValue());
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
									System.out.println("--- user: " + userChild.getChildNodes().item(0).getNodeValue());
									if (first) {
										s.append("::" + userChild.getChildNodes().item(0).getNodeValue());
										first = false;
									} else {
										s.append(" " + userChild.getChildNodes().item(0).getNodeValue());
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
									System.out.println("--- group: " + groupChild.getChildNodes().item(0).getNodeValue());
									if (first) {
										s.append("::" + groupChild.getChildNodes().item(0).getNodeValue());
										first = false;
									} else {
										s.append(" " + groupChild.getChildNodes().item(0).getNodeValue());
									}
								}
							}
						}
					}
				} else if (roleElement.getNodeName().equals("runas")) {

					NodeList runas = roleElement.getChildNodes();

					for (int m = 0; m < runas.getLength(); m++) {
						if (runas.item(m).getNodeName().equals("username")) {
							System.out.println("--- runas username: " + runas.item(m).getChildNodes().item(0).getNodeValue());
							s.append("::" + runas.item(m).getChildNodes().item(0).getNodeValue());
						} else if (runas.item(m).getNodeName().equals("password")) {
							System.out.println("--- runas password: " + runas.item(m).getChildNodes().item(0).getNodeValue());
							s.append(" " + runas.item(m).getChildNodes().item(0).getNodeValue());
						}
					}
				}
			}
		}

		return s.toString();
	}
}
