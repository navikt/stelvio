package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;
import java.io.IOException;

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
	 * @parameter expression="${environment}"
	 * @required
	 */
	protected String environment;

	protected final void doExecute() throws MojoExecutionException,
			MojoFailureException {
		boolean foundBusConfiguration = false;

		if (ArtifactConfiguration.isConfigurationLoaded() == true) {
			return;
		}

		for (Artifact a : dependencyArtifacts) {
			if (a.getArtifactId().equals(moduleConfigurationArtifactName)) {
				File scriptsFolder = new File(baseDirectory, busConfigurationExtractDirectory);
				File extractedFolder = null;
				if (scriptsFolder.exists() == false
						|| new File(scriptsFolder, a.getVersion()).exists() == false) {
					extractedFolder = jarArchiveManager.unArchive(a.getFile(),
							scriptsFolder);
				}
				ArtifactConfiguration.loadConfiguration(new File(
						extractedFolder, "moduleconfig"), environment);
				foundBusConfiguration = true;

				/* Add the version in an empty file */
				File versionFile = new File(scriptsFolder, a.getVersion());
				try {
					versionFile.createNewFile();
				} catch (IOException e) {
					throw new MojoFailureException(
							"An error occured creating a version file in the scripts directory");
				}
				break;
			}
		}

		if (foundBusConfiguration == false) {
			throw new MojoFailureException(
					" The pom must refer to a valid \"busconfiguration\" artifact. The artifact was not found as a dependency");
		}

	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Load Websphere Configuration";
	}
}