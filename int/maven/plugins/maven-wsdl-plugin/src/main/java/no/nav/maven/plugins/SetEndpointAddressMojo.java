package no.nav.maven.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.factory.WSDLFactory;

import no.stelvio.ibm.websphere.esb.WSDLUtils;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;


/**
 * Goal which sets the endpoint address of all WSDL ports
 * 
 * @goal set-endpoint-address
 * @requiresDependencyResolution compile
 */
@SuppressWarnings("unchecked")
public class SetEndpointAddressMojo extends AbstractMojo {
	
	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @parameter default-value="http://www.example.org/"
	 */
	private String endpointAddress;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (!"pom".equals(project.getPackaging())) {
			try {
				Collection<Definition> wsdls = WSDLUtils.getWSDLs(new File(project.getBuild().getOutputDirectory()));
				for (Definition definition : wsdls) {
					setEndpointAddress(definition, endpointAddress);
					WSDLFactory.newInstance().newWSDLWriter().writeWSDL(definition, new FileOutputStream(new File(URI.create(definition.getDocumentBaseURI().replace(" ", "%20")))));
				}
			} catch (IOException e) {
				throw new MojoExecutionException("An error occured while reading WSDL files", e);
			} catch (WSDLException e) {
				throw new MojoExecutionException("An error occured while reading WSDL files", e);
			}
		} else {
			getLog().debug("Skipping set-endpoint-address because packaging is pom");
		}
	}
	
	
	/**
	 * Set endpoint address of all ports
	 */
	public void setEndpointAddress(Definition definition, String endpointAddress) {
		Collection<Service> services = definition.getServices().values();
		for (Service service : services) {
			Collection<Port> ports = service.getPorts().values();
			for (Port port : ports) {
				for (ExtensibilityElement extensibilityElement : (List<ExtensibilityElement>) port.getExtensibilityElements()) {
					if (extensibilityElement instanceof SOAPAddress) {
						((SOAPAddress) extensibilityElement).setLocationURI(endpointAddress);
					}
				}
			}
		}
	}

}
