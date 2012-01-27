package no.nav.maven.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.xml.namespace.QName;

import no.stelvio.ibm.websphere.esb.WSDLUtils;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;


/**
 * Goal which replaces targetNamespace "http://no.nav" with "http://nav.no" in binding WSDL
 * 
 * @goal reverse-namespace-hostname
 */
@SuppressWarnings("unchecked")
public class ReverseNamespaceHostnameMojo extends AbstractMojo {
	
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
					reverseNamespaceHostname(definition);
					WSDLFactory.newInstance().newWSDLWriter().writeWSDL(definition, new FileOutputStream(new File(URI.create(definition.getDocumentBaseURI().replace(" ", "%20")))));
				}
			} catch (IOException e) {
				throw new MojoExecutionException("An error occured while reading WSDL files", e);
			} catch (WSDLException e) {
				throw new MojoExecutionException("An error occured while reading WSDL files", e);
			}
		} else {
			getLog().debug("Skipping reverse-namespace-hostname packaging is pom");
		}
	}
	
	
	/**
	 * Replace targetNamespaces "http://no.nav" with "http://nav.no" in binding WSDL
	 */
	public void reverseNamespaceHostname(Definition definition) {
		if (definition.getServices().size() > 0) {
			String namespace = definition.getTargetNamespace();
			if (namespace.startsWith("http://no.nav")) {
				String newNamespace = namespace.replaceFirst("http://no.nav", "http://nav.no");
				definition.setTargetNamespace(newNamespace);
				definition.removeNamespace("tns");
				definition.addNamespace("tns", newNamespace);
				for (Binding binding : (Collection<Binding>) definition.getBindings().values()) {
					QName qName = binding.getQName();
					QName newQName = new QName(newNamespace, qName.getLocalPart(), "tns");
					binding.setQName(newQName);
				}
			}
		}
	}

}
