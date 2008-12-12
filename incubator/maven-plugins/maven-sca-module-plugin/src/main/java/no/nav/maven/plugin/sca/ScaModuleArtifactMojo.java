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
 * @goal sca-module-artifact
 * @requiresProject
 */
public class ScaModuleArtifactMojo extends AbstractMojo {
	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @parameter expression="${component.org.apache.maven.artifact.handler.ArtifactHandler#sca-module-ear}"
	 * @required
	 * @readonly
	 */
	private ArtifactHandler artifactHandler;

	public void execute() throws MojoExecutionException {
		Build build = project.getBuild();
		Artifact artifact = project.getArtifact();
		File earFile = new File(build.getDirectory(), build.getFinalName() + ".ear");
		artifact.setFile(earFile);
		artifact.setArtifactHandler(artifactHandler);
	}
}
