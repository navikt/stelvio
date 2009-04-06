package no.nav.maven.plugin.artifact.modifier.mojo;

import java.io.File;

import no.nav.maven.commons.configuration.ArtifactConfiguration;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal that loads the configuration for all artifacts into the jvm for later retrieval.
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
	
    /**
     * Loads all configuration elements into the ArtifactConfiguration store. A check to see if the configuration is
     * already loaded is supplied, because other goals may as well load the configuration. 
     *
     * @throws MojoExecutionException if the plugin failes to run. Causes an "BUILD ERROR" message
     * @throws MojoFailureException if the plugin failes to run. Causes an "BUILD FAILURE" message
     */
	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
		boolean foundBusConfiguration = false;
		
		if(ArtifactConfiguration.isConfigurationLoaded() == true) {
			return;
		}
		
		for(Artifact a : dependencyArtifacts) {
			if(a.getArtifactId().equals(moduleConfigurationArtifactName)) {
				File scriptsFolder = new File(baseDirectory,scriptDirectory);
				if(scriptsFolder.exists() == false) {
					scriptsFolder.mkdirs();
				}
				File extractedFolder = jarArchiveManager.unArchive(a.getFile(), scriptsFolder);
				ArtifactConfiguration.loadConfiguration(new File(extractedFolder, "moduleconfig"), environment);
				foundBusConfiguration = true;
				break;
			}
		}
		
		if(foundBusConfiguration == false) {
			throw new MojoFailureException(" The pom must refer to a valid \"busconfiguration\" artifact. The artifact was not found as a dependency");
		}
		
		File dest = new File(targetDirectory);
		if(dest.exists() && dest.isDirectory()) {
			for( File f : dest.listFiles())
				f.delete();
		}
		
	}
}	