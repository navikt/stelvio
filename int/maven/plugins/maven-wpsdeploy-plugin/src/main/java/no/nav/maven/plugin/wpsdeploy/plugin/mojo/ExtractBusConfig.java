package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that extracts the bus-configuration artifact
 * 
 * @author test@example.com
 * 
 * @goal extract-bus-configuration
 * @requiresDependencyResolution
 */
public class ExtractBusConfig extends WebsphereUpdaterMojo {

	@Override
	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {

		for (Artifact a : dependencyArtifacts) {
			if (a.getArtifactId().equals(moduleConfigurationArtifactName)) {
				File busConfigFolder = new File(baseDirectory, busConfigurationExtractDirectory);
				jarArchiveManager.unArchive(a.getFile(), busConfigFolder);
				getLog().info("Successfully extracted " + a.getArtifactId() + " into " + busConfigFolder + ".");
			}
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Extract bus configuration";
	}

}
