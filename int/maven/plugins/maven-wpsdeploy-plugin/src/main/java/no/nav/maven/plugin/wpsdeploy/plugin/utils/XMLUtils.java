package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
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
	 * Returns a list of artifacts in the pom file
	 * 
	 */
	public static HashSet<Artifact> parsePomDependencies(File file, ArtifactFactory artifactFactory) throws SAXException, IOException, ParserConfigurationException, FactoryConfigurationError {

		Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

		NodeList dependency = xml.getElementsByTagName("dependency");

		if (dependency.getLength() == 0) return null;
		
		HashSet<Artifact> artifactList = new HashSet<Artifact>();

		for (int i = 0; i < dependency.getLength(); i++) {
			
			NodeList dependencyChildren = dependency.item(i).getChildNodes();
			
			String artifactId = getChildNodeValue(dependencyChildren,"artifactId");
			String groupId = getChildNodeValue(dependencyChildren, "groupId");
			String version = getChildNodeValue(dependencyChildren, "version");
			String type = getChildNodeValue(dependencyChildren, "type");
			
			Artifact a = artifactFactory.createArtifact(groupId, artifactId, version, null, type);
			artifactList.add(a);
		}
		return artifactList;
	}

	private static String getChildNodeValue(NodeList children, String nodeName) {
		String returnString = null;
		for (int j = 0; j < children.getLength(); j++) {

			Node endpointElement = children.item(j);
			if (endpointElement.getNodeName().equals(nodeName)) {
				returnString = endpointElement.getChildNodes().item(0).getTextContent();
			}
		}
		return returnString;
	}
}
