package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that extracts the esb-release artifact
 * 
 * @author test@example.com
 * 
 * @goal extract-esb-release
 * @requiresDependencyResolution
 */
public class ExtractESBRelease extends WebsphereUpdaterMojo {

	/**
	 * @parameter expression="${esb-release-version}"
	 * @required
	 */
	protected String esbReleaseVersion;

	@Override
	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {

		File earDirectory = new File(targetDirectory + "/EARFilesToDeploy");

		for (Artifact a : artifacts) {
			if (a.getType() == "ear") {
				jarArchiveManager.unArchive(a.getFile(), earDirectory);
				getLog().info("[INFO] Successfully extracted esb-" + esbReleaseVersion + " into " + earDirectory + ".");
			}
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Extract ESB release";
	}
}
