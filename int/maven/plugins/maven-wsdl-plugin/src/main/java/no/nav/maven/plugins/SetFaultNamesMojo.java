package no.nav.maven.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.PortType;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.xml.namespace.QName;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import no.stelvio.ibm.websphere.esb.WSDLUtils;

/**
 * Will modify every PortType in every WSDL of the output directory
 * Will set:
 * - fault names to $faultPartName$
 * - fault message QName local-parts to $operationName$_$faultPartName$
 * 
 * Typically used to compensate for RSA UML-to-WSDL transformation's
 * limitation on fault names
 * 
 * @goal set-fault-names
 */
public class SetFaultNamesMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	public void execute() throws MojoExecutionException, MojoFailureException {
		
		if (!"pom".equals(project.getPackaging())) {
			try {
				Collection<Definition> wsdls = WSDLUtils.getWSDLs(new File(project.getBuild().getOutputDirectory()));
				for (Definition definition : wsdls) {
					setFaultNames(definition);
					WSDLFactory.newInstance().newWSDLWriter().writeWSDL(definition, new FileOutputStream(new File(URI.create(definition.getDocumentBaseURI().replace(" ", "%20")))));
				}
			} catch (IOException e) {
				throw new MojoExecutionException("An error occured while reading WSDL files", e);
			} catch (WSDLException e) {
				throw new MojoExecutionException("An error occured while reading WSDL files", e);
			}
		} else {
			getLog().debug("Skipping set-fault-names because packaging is pom");
		}
	}

	private void setFaultNames(Definition definition) throws MojoExecutionException {
		for (PortType portType : (Collection<PortType>) definition.getAllPortTypes().values()) {
			for (Operation operation : (Collection<Operation>) portType.getOperations()) {
				for (Fault fault : (Collection<Fault>) operation.getFaults().values()) {
					Message faultMessage = fault.getMessage();
					if (faultMessage.getParts().size() != 1) {
						throw new MojoExecutionException("WSDL Message " + faultMessage.getQName() + " had " + faultMessage.getParts().size() + " instead of one part");
					}
					for (Part part : (Collection<Part>) faultMessage.getParts().values()) {
						String faultName = part.getName();
						getLog().debug("Changig fault name '" + fault.getName() + "' in operation '" + operation.getName() + "' to '" + faultName + "'");
						fault.setName(faultName);
						QName oldQName = faultMessage.getQName();
						QName newQName = new QName(oldQName.getNamespaceURI(), operation.getName() + "_" + faultName, oldQName.getPrefix());
						getLog().debug("Changig message QName from " + oldQName + " to " + newQName);
						faultMessage.setQName(newQName);
					}
				}
			}
		}
	}

}
