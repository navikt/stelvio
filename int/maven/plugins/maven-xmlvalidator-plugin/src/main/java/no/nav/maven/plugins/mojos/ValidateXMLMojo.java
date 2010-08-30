package no.nav.maven.plugins.mojos;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import no.nav.maven.plugins.utils.ErrorHandlerImpl;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.xml.sax.SAXException;

/**
 * Goal that validates the provided XML document against the provided XML schema definition.
 * Terminates the execution with exitcode 1 on failure.
 * 
 * @goal validate-xml
 * 
 * @author test@example.com
 */
public class ValidateXMLMojo extends AbstractMojo {

	/**
	 * @parameter expression="${xsdFile}"
	 * @required
	 */
	private String xsdFile;

	/**
	 * @parameter expression="${xmlFile}"
	 * @required
	 */
	private String xmlFile;
	
	/**
	 * @parameter expression="${validation.skip}"
	 */
	private boolean skip;

	public void execute() throws MojoExecutionException, MojoFailureException {
	
		if (skip){
			getLog().info("##########################################");
			getLog().info("### Skipping XML Schema validation ... ###");
			getLog().info("##########################################");
			return;
		}
		
		try {
			validate(xsdFile, xmlFile);
		} catch (SAXException e) {
			throw new MojoFailureException("[ERROR] " + e);
		} catch (IOException e) {
			throw new MojoFailureException("[ERROR] " + e);
		} catch (ParserConfigurationException e) {
			throw new MojoFailureException("[ERROR] " + e);
		}
	}

	private void validate(String xsdFile, String xmlFile) throws MojoFailureException, ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setValidating(true);
		factory.setNamespaceAware(true);
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", xsdFile);

		DocumentBuilder parser = factory.newDocumentBuilder();

		ErrorHandlerImpl ehi = new ErrorHandlerImpl();

		parser.setErrorHandler(ehi);
		
		getLog().info("##########################################");
		getLog().info("### Starting XML Schema validation ... ###");
		getLog().info("##########################################");
		getLog().info("Validating XML document, " + xmlFile + " against schema definition, " + xsdFile + "...");
		
		parser.parse(xmlFile);
		
		// If validation failed, the errorhandler will terminate the execution with exitcode 1.
		getLog().info("XML successfully validated.");
	}

}
