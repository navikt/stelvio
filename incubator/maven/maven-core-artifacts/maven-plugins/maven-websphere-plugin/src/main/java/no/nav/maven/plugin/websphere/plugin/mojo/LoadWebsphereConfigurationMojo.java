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
	}
	
	@Override
	protected String getGoalPrettyPrint() {
		return "Load Websphere Configuration";
	}
}	