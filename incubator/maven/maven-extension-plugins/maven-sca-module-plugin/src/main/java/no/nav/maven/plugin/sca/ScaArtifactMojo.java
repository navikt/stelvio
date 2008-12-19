package no.nav.maven.plugin.sca;

import java.io.File;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Goal which sets the main artifact file, and sets the artifact handler. The
 * latter is a bug in Maven Core.
 * 
 * @author test@example.com
 * 
 * @goal sca-artifact
 */
public class ScaArtifactMojo extends AbstractMojo {
	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @parameter expression="${component.org.apache.maven.artifact.handler.ArtifactHandler#sca-library-jar}"
	 * @required
	 * @readonly
	 */
	private ArtifactHandler scaLibraryArtifactHandler;

	/**
	 * @parameter expression="${component.org.apache.maven.artifact.handler.ArtifactHandler#sca-module-ear}"
	 * @required
	 * @readonly
	 */
	private ArtifactHandler scaModuleArtifactHandler;

	/**
	 * {@inheritDoc}
	 */
	public void execute() throws MojoExecutionException {
		Build build = project.getBuild();

		File artifactFile = null;
		ArtifactHandler artifactHandler = null;

		String packaging = project.getPackaging();
		if ("sca-library-jar".equals(packaging)) {
			artifactFile = new File(build.getDirectory(), build.getFinalName() + ".jar");
			artifactHandler = scaLibraryArtifactHandler;
		} else if ("sca-module-ear".equals(packaging)) {
			artifactFile = new File(build.getDirectory(), build.getFinalName() + ".ear");
			artifactHandler = scaModuleArtifactHandler;
		} else {
			getLog().warn("Packaging <" + packaging + "> is an unsupported packaging for this plugin.");
		}

		if (artifactFile != null && artifactFile.exists()) {
			Artifact artifact = project.getArtifact();
			artifact.setFile(artifactFile);
			artifact.setArtifactHandler(artifactHandler);
		}
	}
}
