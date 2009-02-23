package no.nav.maven.plugin.artifact.modifier.mojo;

import java.io.File;

import no.nav.maven.commons.configuration.ArtifactConfiguration;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

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
     * @parameter default-value="busconfiguration"
     */
	private String moduleConfigurationArtifactName;
	
	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
		
		if(ArtifactConfiguration.isConfigurationLoaded() == true) {
			return;
		}
		
		for(Artifact a : dependencyArtifacts) {
			if(a.getArtifactId().equals(moduleConfigurationArtifactName)) {
				File extractedFolder = jarArchiveManager.unArchive(a.getFile(), new File(baseDirectory,scriptDirectory));
				ArtifactConfiguration.loadConfiguration(new File(extractedFolder, "moduleconfig"), environment);
				break;
			}
		}
				
		if(ArtifactConfiguration.isConfigurationLoaded() == false) {
			getLog().warn("The depoyment does not contain dependency to a wps configuration");
		}
	}
}	