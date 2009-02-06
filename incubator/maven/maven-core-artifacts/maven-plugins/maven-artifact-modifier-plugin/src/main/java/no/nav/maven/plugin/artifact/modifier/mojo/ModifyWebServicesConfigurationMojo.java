package no.nav.maven.plugin.artifact.modifier.mojo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import no.nav.busconfiguration.configuration.ArtifactConfiguration;
import no.nav.busconfiguration.constants.Constants;
import no.nav.maven.plugin.artifact.modifier.utils.EarFile;
import no.nav.maven.plugins.descriptor.websphere.bnd.IbmWebServiceClientBndEditor;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.EndpointType;
import no.nav.pensjonsprogrammet.wpsconfiguration.EndpointsType;
import no.nav.pensjonsprogrammet.wpsconfiguration.WebservicesType;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EARFile;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EJBJarFile;

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
				if(configuration != null && configuration.getWebservices() != null) {
					EARFile earFile = EarFile.openEarFile(a.getFile().getAbsolutePath());
					updateWebServices((EJBJarFile)earFile.getEJBJarFiles().get(0), configuration.getWebservices());
					EarFile.closeEarFile(earFile);
				}
			}
		}
	}
	
	private final void updateWebServices(final Archive ejbFile, final WebservicesType webservicesConfiguration) {
		EndpointsType endPoints = webservicesConfiguration.getEndpoints();
		if(endPoints != null) {
			updateWebServicesEndpoints(ejbFile, endPoints);
		}
	}
	
	private final void updateWebServicesEndpoints(final Archive ejbFile, final EndpointsType endPoints) {
		IbmWebServiceClientBndEditor wscBnd = new IbmWebServiceClientBndEditor(ejbFile);						
		Properties properties= new Properties();
		
		for(EndpointType e : endPoints.getEndpointList()) {
			properties.put(e.getName(), e.getValue());
		}

		wscBnd.setEndpointUri("http://www.dummy.org", properties);
		try {
			wscBnd.save();
		} catch (IOException e) {
			throw new RuntimeException("An error occured savind endpoint URI settings", e);
		}
	}
}
