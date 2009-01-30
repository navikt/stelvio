package no.nav.maven.plugin.artifact.modifier.mojo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import no.nav.maven.plugin.artifact.modifier.configuration.ArtifactConfiguration;
import no.nav.maven.plugin.artifact.modifier.constants.Constants;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.EndpointType;
import no.nav.pensjonsprogrammet.wpsconfiguration.EndpointsType;
import no.nav.pensjonsprogrammet.wpsconfiguration.WebservicesType;
import noNamespace.PortQnameBindingsType;
import noNamespace.ServiceRefsType;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.xmlbeans.XmlException;

import com.ibm.websphere.appserver.schemas.x502.wscbnd.ClientBindingDocument;
import com.sun.java.xml.ns.j2Ee.ApplicationDocument;
import com.sun.java.xml.ns.j2Ee.ModuleType;
import com.sun.java.xml.ns.j2Ee.PathType;



/**
 * Goal that loads the configuration for all artifacts into the jvm 
 * 
 * @author test@example.com
 * 
 * @goal load-artifact-configuration
 * @requiresDependencyResolution
 */
public class LoadArtifactConfigurationMojo extends ArtifactModifierMojo {
    
	/**
     * Name of module configuration artifact.
     *
     * @parameter default-value="wpsconfiguration"
     */
	private String moduleConfigurationArtifactName;
	
	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
		
		if(ArtifactConfiguration.isConfigurationLoaded() == true) {
			return;
		} else {
			for(Artifact a : dependencyArtifacts) {
				if(a.getArtifactId().equals(moduleConfigurationArtifactName)) {
					File extractedFolder = jarArchiveManager.unArchive(a.getFile(), scriptDirectory);
					ArtifactConfiguration.loadConfiguration(new File(extractedFolder, "moduleconfig"));
				}
			}
			
			if(ArtifactConfiguration.isConfigurationLoaded() == false) {
				throw new RuntimeException("The depoyment does not contain dependency to a wps configuration");
			}
		}
	}
}	