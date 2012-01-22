package no.nav.maven.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.factory.WSDLFactory;

import no.stelvio.ibm.websphere.esb.WSDLUtils;
import no.stelvio.wsdl.addressing.UsingAddressingImpl;
import no.stelvio.wsdl.addressing.UsingAddressingSerializer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Goal which adds UsingAddressing element to enable WS-Addressing
 * 
 * @goal add-using-addressing
 */
@SuppressWarnings("unchecked")
public class AddUsingAddressingMojo extends AbstractMojo {
	
	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;
	
	/**
	 * @parameter
	 */
	private Boolean addressingRequired;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (!"pom".equals(project.getPackaging())) {
			try {
				Collection<Definition> wsdls = WSDLUtils.getWSDLs(new File(project.getBuild().getOutputDirectory()));

				for (Definition definition : wsdls) {
					registerExtension(definition);
					addUsingAddressing(definition);
					WSDLFactory.newInstance().newWSDLWriter().writeWSDL(definition, new FileOutputStream(new File(URI.create(definition.getDocumentBaseURI().replace(" ", "%20")))));
				}
			} catch (IOException e) {
				throw new MojoExecutionException("An error occured while reading or writing WSDL files", e);
			} catch (WSDLException e) {
				throw new MojoExecutionException("An error occured while processing WSDL", e);
			}
		} else {
			getLog().debug("Skipping add-using-addressing because packaging is pom");
		}
	}

	private void registerExtension(Definition definition) throws WSDLException {
		if (definition.getNamespace(UsingAddressingImpl.NS_PREFIX_WSAW) == null) {
			definition.addNamespace(UsingAddressingImpl.NS_PREFIX_WSAW, UsingAddressingImpl.NS_WSAW);
		}
		ExtensionRegistry registry = definition.getExtensionRegistry();
		if (!(registry.querySerializer(Binding.class, UsingAddressingImpl.Q_ELEMENT) instanceof UsingAddressingSerializer)) {
			registry.registerSerializer(Binding.class, UsingAddressingImpl.Q_ELEMENT, new UsingAddressingSerializer());
			registry.mapExtensionTypes(Binding.class, UsingAddressingImpl.Q_ELEMENT, UsingAddressingImpl.class);
		}
	}

	/**
	 * Add UsingAddressing element to WSDL to enable WS-Addressing
	 * 
	 * @throws WSDLException 
	 */
	public void addUsingAddressing(Definition definition) throws WSDLException {
		Collection<Service> services = definition.getServices().values();
		for (Service service : services) {
			Collection<Port> ports = service.getPorts().values();
			for (Port port : ports) {
				Binding binding = port.getBinding();
				List<ExtensibilityElement> removeList = new ArrayList<ExtensibilityElement>();
				for (Object ext : binding.getExtensibilityElements()) {
					// Remove existing elements
					if (UsingAddressingImpl.Q_ELEMENT.equals(((ExtensibilityElement)ext).getElementType())) {
						removeList.add((ExtensibilityElement) ext);
					}
				}
				for (ExtensibilityElement extensibilityElement : removeList) {
					binding.removeExtensibilityElement(extensibilityElement);
				}
				ExtensibilityElement usingAddressing = definition.getExtensionRegistry().createExtension(Binding.class, UsingAddressingImpl.Q_ELEMENT);
				usingAddressing.setRequired(addressingRequired);
				binding.addExtensibilityElement(usingAddressing);
			}
		}
	}

}
