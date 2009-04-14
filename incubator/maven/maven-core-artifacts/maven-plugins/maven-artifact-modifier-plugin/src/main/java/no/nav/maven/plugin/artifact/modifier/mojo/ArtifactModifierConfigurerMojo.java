package no.nav.maven.plugin.artifact.modifier.mojo;

import java.io.File;
import java.util.StringTokenizer;

import no.nav.maven.commons.configuration.ArtifactConfiguration;
import no.nav.maven.commons.constants.Constants;
import no.nav.maven.plugin.artifact.modifier.utils.EarFile;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * Abstract class using the template pattern for child mojos doing real configuration work.
 * The class functionality retrieves the right configuration from the configuration store and calls
 * applyConfiguration on the leaf class. 
 * 
 * @author test@example.com 
 */
public abstract class ArtifactModifierConfigurerMojo extends ArtifactModifierMojo {
	
	protected abstract void applyConfiguration(File artifact, ConfigurationType configuration);
	
    /**
     * Makes sure that both the global and the environment specific configuration is applied to the target module.
     *
     * @throws MojoExecutionException if the plugin failes to run. Causes an "BUILD ERROR" message
     * @throws MojoFailureException if the plugin failes to run. Causes an "BUILD FAILURE" message
     */
	public void doExecute() throws MojoExecutionException, MojoFailureException {
		
		if(ArtifactConfiguration.isConfigurationLoaded() == false) {
			getLog().warn("The deployment does not contain any module xml configuration");
		}
		
		/* TODO: Clean out the target folder, old stuff might be lying around. 
		 * Consequences??
		 * */
		
		
		for(Artifact a : artifacts) {
			if(a.getType().equals(Constants.EAR_ARTIFACT_TYPE)) {
				File destination = copyArtifactToTarget(a);
				iterateOverConfiguration(a, destination, true);			
				iterateOverConfiguration(a, destination, false);
			}
		}
	}
	
    /**
     * Copies an artifact (transitive is valid) to the target (build) directory.
     *
     * @return the file handle after it is copied to the projects target (build) directory.
     * @param a The artifact to copy to target (build) directory.
     */
	private final File copyArtifactToTarget(Artifact a) {
		File source = new File(a.getFile().getAbsolutePath());
		
		
		File destFolder = new File(targetDirectory);
		if(destFolder.exists() == false) {
			destFolder.mkdir();
		}
		
		File dest = new File(targetDirectory, a.getFile().getName());
		
		if(dest.exists() == false) {
			EarFile.copyFile(source, dest);
		}
		
		return dest;
	}

    /**
     * This method will iterate over all configuration objects for the module. The configuration naming uses the
     * convenience over configuration pattern. For example, a configuration called "nav.xml" will be applied for all 
     * module artifact names starting with the name "nav". A configuration called "cons.xml" will be applied for all
     * module artifact names starting with the name "nav-cons". After iterating over all configuration elements, the
     * configuration for the module itself is applied if a configuration file has the same name as the artifact name itself.
     *
     * @return the file handle after it is copied to the projects target (build) directory.
     * @param a the configuration will be applied to this artifact.
     * @param destination The modified artifact will reside in this file
     * @param global signals if the configuration is a global element or an environment element.
     */
	private final void iterateOverConfiguration(final Artifact a, final File destination, final boolean global) {
		ConfigurationType configuration = null;

		StringTokenizer tokenizer = new StringTokenizer(a.getArtifactId(), Constants.ARTIFACT_MODIFIER_SEPARATOR);

		while(tokenizer.hasMoreTokens()) {
			String tok=tokenizer.nextToken();
			if(global == true) {
				configuration = ArtifactConfiguration.getConfiguration(tok);
			} else {
				configuration = ArtifactConfiguration.getEnvConfiguration(tok);
			}
			
			if(configuration != null) {
				applyConfiguration(destination, configuration);
			}
		}

		if(global == true) {
			configuration = ArtifactConfiguration.getConfiguration(a.getArtifactId());
		} else {
			configuration = ArtifactConfiguration.getEnvConfiguration(a.getArtifactId());
		}
		
		if(configuration != null) {
			applyConfiguration(destination, configuration);
		}
	}
}
