package no.nav.maven.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 * Will modify every WSDL of the output directory
 * Will remove:
 * - the element "plassholder" with type "xsd:anyType" of anonymous global complex types 
 * 
 * Typically used to compensate for RSA UML-to-WSDL transformation's
 * limitation on one-way vs request/response messages
 * 
 * @goal remove-placeholders
 */
public class RemovePlaceholdersMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	public void execute() throws MojoExecutionException, MojoFailureException {
		if (!"pom".equals(project.getPackaging())) {
			try {
				for (File wsdlFile : (List<File>) FileUtils.getFiles(new File(project.getBuild().getOutputDirectory()), "**/*.wsdl", null)) {
					Document document = new SAXBuilder().build(wsdlFile);
					XPath xPath = XPath.newInstance("/wsdl:definitions/wsdl:types/xsd:schema/xsd:element/xsd:complexType/xsd:sequence/xsd:element[@name='plassholder' and @type='xsd:anyType']");
					xPath.addNamespace("wsdl", "http://schemas.xmlsoap.org/wsdl/");
					xPath.addNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
					List nodes = xPath.selectNodes(document);
					for (Object node : nodes) {
						if (node instanceof Element) {
							Element element = (Element) node;
							getLog().info("Je="+element.getAttributeValue("name"));
							element.getParent().removeContent(element);
						}
					}
					XMLOutputter outputter = new XMLOutputter();
					outputter.output(document, new FileOutputStream(wsdlFile));
				}
			} catch (IOException e) {
				throw new MojoExecutionException("An error occured while reading WSDL files", e);
			} catch (JDOMException e) {
				throw new MojoExecutionException("An error occured while parsing WSDL files", e);
			}
		} else {
			getLog().debug("Skipping remove-placeholders because packaging is pom");
		}
	}

}
