package no.nav.maven.plugin.artifact.modifier.mojo;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import no.nav.maven.plugins.descriptor.utils.EarFile;
import no.nav.maven.plugins.descriptor.websphere.bnd.IbmWebServiceClientBndEditor;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.EndpointType;
import no.nav.pensjonsprogrammet.wpsconfiguration.EndpointsType;
import no.nav.pensjonsprogrammet.wpsconfiguration.WebservicesType;

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
public final class ModifyWebServicesConfigurationMojo extends ArtifactModifierConfigurerMojo {
	
	protected final void applyConfiguration(File artifact, ConfigurationType configuration) {
		if(configuration.getWebservices() != null) {
			EARFile earFile = EarFile.openEarFile(artifact.getAbsolutePath());
			updateWebServices((EJBJarFile)earFile.getEJBJarFiles().get(0), configuration.getWebservices());
			EarFile.closeEarFile(earFile);
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
			throw new RuntimeException("An error occured saving endpoint URI settings", e);
		}
	}
	
	@Override
	protected String getGoalPrettyPrint() {
		return "Inject artifacts with web services settings";
	}
}
