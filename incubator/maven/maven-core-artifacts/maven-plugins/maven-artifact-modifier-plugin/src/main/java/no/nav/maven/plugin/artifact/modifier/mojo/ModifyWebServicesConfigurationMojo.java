package no.nav.maven.plugin.artifact.modifier.mojo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import no.nav.busconfiguration.configuration.ArtifactConfiguration;
import no.nav.busconfiguration.constants.Constants;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.EndpointType;
import no.nav.pensjonsprogrammet.wpsconfiguration.EndpointsType;
import no.nav.pensjonsprogrammet.wpsconfiguration.WebservicesType;
import noNamespace.PortQnameBindingsType;
import noNamespace.ServiceRefsType;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.xmlbeans.XmlException;

import com.ibm.websphere.appserver.schemas.x502.wscbnd.ClientBindingDocument;
import com.sun.java.xml.ns.j2Ee.ApplicationDocument;
import com.sun.java.xml.ns.j2Ee.ModuleType;
import com.sun.java.xml.ns.j2Ee.PathType;

/**
 * Goal that updates the webservices settings in the artifacts
 * 
 * @author test@example.com
 * 
 * @goal modify-web-services-configuration
 * @requiresDependencyResolution
 */
public class ModifyWebServicesConfigurationMojo extends ArtifactModifierMojo {

	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
				
		for(Artifact a : artifacts) {
			if(a.getType().equals(Constants.EAR_ARTIFACT_TYPE)) {
				ConfigurationType configuration = ArtifactConfiguration.getConfiguration(a.getArtifactId());
				if(configuration != null &&  configuration.getWebservices() != null) {
					File extractedFolder = earArchiveManager.unArchive(a.getFile(), (File)null);
					updateWebServices(configuration.getWebservices(), extractedFolder, a);
					earArchiveManager.archive(extractedFolder, targetDirectory, a.getFile().getName());
				}
			}
		}
	}
	
	private final void updateWebServices(final WebservicesType webserviceConfiguration, File folder, final Artifact a) {
		File applicationXml = new File(new File(folder, Constants.J2E_MANIFEST_DIRECTORY), Constants.J2E_APPLICATION_XML_FILE);
		ApplicationDocument applicationDocument = null;

		try {
			applicationDocument = ApplicationDocument.Factory.parse(applicationXml);
		} catch (XmlException e) {
			throw new RuntimeException("An XML exception occured reading file", e);
		} catch (IOException e) {
			throw new RuntimeException("An IO exception occured reading file", e);
		}
		
		java.util.List<ModuleType> modules = applicationDocument.getApplication().getModuleList();
		for(ModuleType m : modules) {
			PathType ejb = m.getEjb();
			if(ejb!=null) {		
				File extractedFolder = jarArchiveManager.unArchive(new File(folder, ejb.getStringValue()), null);
				if(webserviceConfiguration.getEndpoints() != null) {
					updateWebServicesEndpoints(webserviceConfiguration.getEndpoints(), extractedFolder);
					jarArchiveManager.archive(extractedFolder, folder.getAbsolutePath(), ejb.getStringValue());
				}
			}
		}
	}
	
	private final void updateWebServicesEndpoints(final EndpointsType endpointsConfiguration, final File folder) {
		File clientBindingXml = new File(new File(folder, Constants.J2E_MANIFEST_DIRECTORY), Constants.IBM_WS_CLIENT_BINDINGS_FILE);
		ClientBindingDocument clientBindingDocument = null;
		
		try {
			clientBindingDocument = ClientBindingDocument.Factory.parse(clientBindingXml);
		} catch (XmlException e) {
			throw new RuntimeException("An XML exception occured reading file", e);
		} catch (IOException e) {
			throw new RuntimeException("An IO exception occured reading file", e);
		}
		
		for(EndpointType e : endpointsConfiguration.getEndpointList()) {
			updateWebServiceEndpoint(e, clientBindingDocument);
		}
		
		try {
			clientBindingDocument.save(clientBindingXml);
		} catch (IOException e) {
			throw new RuntimeException("An IO exception occured during file save", e);
		}
	}
	
	private final void updateWebServiceEndpoint(EndpointType endpointConfiguration, ClientBindingDocument clientBindingDocument) {
		
		List<ServiceRefsType> serviceRefs = clientBindingDocument.getClientBinding().getComponentScopedRefs().getServiceRefsList();
		
		for(ServiceRefsType s : serviceRefs) {
			if(s.getServiceRefLink().equals(endpointConfiguration.getName())) {
				PortQnameBindingsType portQnameBindingType = s.getPortQnameBindings();
				portQnameBindingType.setOverriddenEndpointURI(endpointConfiguration.getValue());	
			}
		}
	}
}
