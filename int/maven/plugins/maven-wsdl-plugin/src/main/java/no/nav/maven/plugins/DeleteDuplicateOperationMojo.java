package no.nav.maven.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;

import no.stelvio.ibm.websphere.esb.WSDLUtils;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;


/**
 * Goal which deletes duplicate operations in binding created by RSA 8.0.0 (fixed in 8.0.4)
 * 
 * @goal delete-duplicate-operation
 */
@SuppressWarnings("unchecked")
public class DeleteDuplicateOperationMojo extends AbstractMojo {
	
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
			getLog().debug("Skipping delete-duplicate-operation packaging is pom");
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
				List<BindingOperation> toBeDeleted = new ArrayList<BindingOperation>();
				for (BindingOperation bindingOperation : (List<BindingOperation>) binding.getBindingOperations()) {
					if (bindingOperation.getBindingInput().getExtensibilityElements().size() == 0) {
						toBeDeleted.add(bindingOperation);
					}
				}
				for (BindingOperation bindingOperation : toBeDeleted) {
					binding.getBindingOperations().remove(bindingOperation);
				}
			}
		}
	}

}
