package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;
import java.io.IOException;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that extracts the bus-configuration artifact
 *  
 * @goal extract-bus-configuration
 * @requiresDependencyResolution
 */
public class ExtractBusConfigMojo extends WebsphereUpdaterMojo {	
	/**
	 * @component roleHint="zip"
	 */
	private UnArchiver unarchiver;

	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		for (Artifact artifact : dependencyArtifacts) {
			if (configurationParts.contains( artifact.getArtifactId() )) {
				String artifactDir = tmpExtractPath + "/" + artifact.getArtifactId();	
				extractArtifact(artifact,  new File(artifactDir));
			}
		}
	}
	
	private void extractArtifact(Artifact artifact, File exctractionDirectory) throws MojoExecutionException {
		try {
			exctractionDirectory.mkdirs();
			unarchiver.setDestDirectory(exctractionDirectory);
			unarchiver.setSourceFile(artifact.getFile());
			unarchiver.extract();
			getLog().info("Successfully extracted " + artifact.getArtifactId() + " into " + exctractionDirectory + ".");
		} catch (ArchiverException e) {
			throw new MojoExecutionException("Error extracting archive", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error extracting archive", e);
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Extract bus configuration";
	}

}
