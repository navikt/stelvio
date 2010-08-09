package no.nav.maven.plugin.artifact.modifier.mojo;

import java.io.File;
import java.io.IOException;

import no.nav.maven.commons.configuration.ArtifactConfiguration;
import no.nav.maven.commons.constants.Constants;
import no.nav.maven.plugins.descriptor.utils.EarFile;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Abstract class using the template pattern for child mojos doing real
 * configuration work. The class functionality retrieves the right configuration
 * from the configuration store and calls applyConfiguration on the leaf class.
 * 
 * @author test@example.com
 */
public abstract class ArtifactModifierConfigurerMojo extends ArtifactModifierMojo {

	private String earDirectory;
	
	protected abstract void applyConfiguration(File artifact, ConfigurationType configuration);

	/**
	 * Makes sure that both the global and the environment specific
	 * configuration is applied to the target module.
	 * 
	 * @throws MojoExecutionException
	 *             if the plugin failes to run. Causes an "BUILD ERROR" message
	 * @throws MojoFailureException
	 *             if the plugin failes to run. Causes an "BUILD FAILURE"
	 *             message
	 */
	public void doExecute() throws MojoExecutionException, MojoFailureException {

		earDirectory = targetDirectory + "/EARFilesToDeploy";
		
		if (ArtifactConfiguration.isConfigurationLoaded() == false) {
			getLog().warn("The deployment does not contain any module xml configuration");
		}

		for (Artifact a : artifacts) {
			if (a.getType().equals(Constants.EAR_ARTIFACT_TYPE)) {
				copyArtifactToTarget(a);
				File destination = copyArtifactToTarget(a);
				for (ConfigurationType configuration : ArtifactConfiguration.getAllConfigurations(a.getArtifactId())) {
					applyConfiguration(destination, configuration);
				}
			}
		}
	}

	/**
	 * Copies an artifact (transitive is valid) to the target (build) directory.
	 * 
	 * @return the file handle after it is copied to the projects target (build)
	 *         directory.
	 * @param a
	 *            The artifact to copy to target (build) directory.
	 */
	private final File copyArtifactToTarget(Artifact a) {
		File source = new File(a.getFile().getAbsolutePath());

		File destFolder = new File(earDirectory);
		if (destFolder.exists() == false) {
			destFolder.mkdir();
		}

		File dest = new File(earDirectory, a.getFile().getName());

		if (dest.exists() == false) {
			EarFile.copyFile(source, dest);

			/*
			 * Communicate to the deploy scripts that other versions of this
			 * module will not be uninstalled. The regexp for module match is
			 * artifactId-(\d+\.)+\d+(-SNAPSHOT)?$
			 */
			if (properties.containsKey(a.getArtifactId())) {
				String property = properties.getProperty(a.getArtifactId());
				if (("leave").equals(property) == true) {
					try {
						new File(earDirectory, a.getArtifactId() + "-" + a.getVersion() + ".leave").createNewFile();
					} catch (IOException e) {
						throw new RuntimeException("An error occured creating a leave file for artifact", e);
					}
				}
			}

// EJB projects does not exist in WPS 7.0 modules
//			/*
//			 * Communicate to the deploy scripts that this module contains
//			 * business processes. Modules containing business processes must be
//			 * handled in a special manner during uninstall.
//			 */
//			EARFile earFile = EarFile.openEarFile(dest.getAbsolutePath());
//			EjbJarAssemblyDescriptorEditor ejbjar = new EjbJarAssemblyDescriptorEditor((Archive) earFile.getEJBJarFiles()
//					.get(0));
//			if (ejbjar.containsBusinessProcesses() == true) {
//				try {
//					new File(targetDirectory, a.getArtifactId() + "-" + a.getVersion() + ".bp").createNewFile();
//				} catch (IOException e) {
//					throw new RuntimeException("An error occured creating a bp file for artifact", e);
//				}
//			}
//			EarFile.closeEarFile(earFile);
		}

		return dest;
	}
}
