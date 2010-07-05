package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;
import java.io.IOException;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;
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

		try {
			
			getLog().info("[INFO] Loading artifacts into " + deployableArtifactsHome + " ...");
			
			for (Artifact a : artifacts) {
				if (a.getType().equals("ear")) {
					File src = new File(a.getFile().getAbsolutePath());
					FileUtils.copyFileToDirectory(src, new File(deployableArtifactsHome));
				}
			}
			
			getLog().info("[INFO] Successfully loaded the artifacts of esb-" + esbReleaseVersion + " into " + deployableArtifactsHome + ".");
			
		} catch (IOException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Extract ESB release";
	}
}
