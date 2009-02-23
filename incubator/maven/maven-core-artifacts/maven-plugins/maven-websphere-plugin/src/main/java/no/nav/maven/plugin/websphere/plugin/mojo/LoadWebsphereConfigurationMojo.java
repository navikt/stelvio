package no.nav.maven.plugin.websphere.plugin.mojo;

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
 * @goal load-runtime-configuration
 * @requiresDependencyResolution
 */
public class LoadWebsphereConfigurationMojo extends WebsphereMojo {
    
	/**
     * Name of module configuration artifact.
     *
     * @parameter default-value="busconfiguration"
     */
	private String moduleConfigurationArtifactName;
	
	/**
	 * @parameter expression="${environment}"
	 * @required
	 */
	protected String environment;
	
	
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