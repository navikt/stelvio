package no.nav.maven.plugins.mojos;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Goal which loads properties from a environment XML file (specified in pom.xml) into project scope for interpolation.
 * 
 * @goal load-xml-properties
 * 
 * @author test@example.com
 * 
 */
public class LoadXMLPropertiesMojo extends AbstractMojo {

	/**
	 * The maven project
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * Property files to load properties from
	 * 
	 * @parameter
	 * @required
	 */
	private File[] files;

	/**
	 * @parameter expression="${zone}"
	 * @required
	 */
	private String zone;

	/**
	 * Prefix used to represent the tree structure.
	 */
	private final String USED_PREFIX = "/";

	private String name;
	private String value;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			
			for (File file : files) {
				if (file.exists()) {

					getLog().info("#################################################################");
					getLog().info("### Exposing environment file properties to Maven context ... ###");
					getLog().info("#################################################################");

					getLog().info("Loading property file: " + file);
					getLog().info("Using following resources: " + project.getResources().toString());

					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document xml = dBuilder.parse(file);

					xml.getDocumentElement().normalize();

					getLog().info("Parsing environment file...");
					getLog().info("Found following properties: ");

					// No prefix for first run
					traverseNode(xml.getDocumentElement(), null);

				} else {
					throw new MojoExecutionException("[ERROR] Environment file, " + file.getAbsolutePath() + " does not exist.");
				}
			}
		} catch (SAXException e) {
			getLog().error(e);
		} catch (ParserConfigurationException e) {
			getLog().error(e);
		} catch (IOException e) {
			getLog().error(e);
		}
	}

	/**
	 * Recursive method to traverse the tree and loads the properties found into the project scope with the given prefix to represent the tree structure
	 */
	private void traverseNode(Node node, String prefix) {
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {

			Node childNode = nodeList.item(i);

			name = node.getNodeName();
			value = childNode.getNodeValue();

			if (childNode.getNodeType() == Node.TEXT_NODE && childNode.getNodeValue().trim().length() > 0) {

				String propertyName;

				// In order to have one environment file for each environment, we need to separate the zone specific properties during the parse-time.
				if ((prefix.contains("app-config") && prefix.contains(zone)) || (prefix.contains("servers") && prefix.contains(zone)) ) {
					getLog().info("Detected zone specfic value, " + name + ". Modifying ...");
					String prefixModified = prefix.replace("/" + zone, "");
					propertyName = prefixModified + USED_PREFIX + name;
					getLog().info(propertyName + "=" + value);
					project.getProperties().put(propertyName, value);
				} else {
					propertyName = prefix + USED_PREFIX + name;
					getLog().info(propertyName + "=" + value);
					project.getProperties().put(propertyName, value);
				}

			} else if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				if (prefix == null) { // First run
					traverseNode(childNode, name);
				} else {
					traverseNode(childNode, prefix + USED_PREFIX + name);
				}
			}
		}
	}
}
