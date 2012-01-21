package no.nav.maven.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.factory.WSDLFactory;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import no.stelvio.ibm.websphere.esb.WSDLUtils;


/**
 * Goal which sets the SOAP action in a WSDL file. Blank if unset.
 * 
 * @goal set-soap-action
 * @requiresDependencyResolution compile
 */
@SuppressWarnings("unchecked")
public class SetSoapActionMojo extends AbstractMojo {
	
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
					setSOAPActions(definition);
					WSDLFactory.newInstance().newWSDLWriter().writeWSDL(definition, new FileOutputStream(new File(URI.create(definition.getDocumentBaseURI().replace(" ", "%20")))));
				}
			} catch (IOException e) {
				throw new MojoExecutionException("An error occured while reading WSDL files", e);
			} catch (WSDLException e) {
				throw new MojoExecutionException("An error occured while reading WSDL files", e);
			}
		} else {
			getLog().debug("Skipping set-soap-action because packaging is pom");
		}
	}
	
	
	/**
	 * Set blank SOAP action for all operations
	 */
	public void setSOAPActions(Definition definition) {
		Collection<Service> services = definition.getServices().values();
		for (Service service : services) {
			Collection<Port> ports = service.getPorts().values();
			for (Port port : ports) {
				Binding binding = port.getBinding();
				for (BindingOperation bindingOperation : (List<BindingOperation>) binding.getBindingOperations()) {
					for (ExtensibilityElement extensibilityElement : (List<ExtensibilityElement>) bindingOperation.getExtensibilityElements()) {
						if (extensibilityElement instanceof SOAPOperation) {
							((SOAPOperation) extensibilityElement).setSoapActionURI("");
						}
					}
				}
			}
		}
	}

}
