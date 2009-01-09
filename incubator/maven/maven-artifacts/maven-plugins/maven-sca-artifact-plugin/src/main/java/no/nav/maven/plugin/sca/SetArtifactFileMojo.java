package no.nav.maven.plugin.sca;

import java.io.File;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Goal which sets the main artifact file.
 * 
 * @author test@example.com
 * 
 * @goal set-artifact-file
 */
public class SetArtifactFileMojo extends AbstractMojo {
	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * {@inheritDoc}
	 */
	public void execute() throws MojoExecutionException {
		Build build = project.getBuild();
		Artifact artifact = project.getArtifact();
		String extension = artifact.getArtifactHandler().getExtension();
		File artifactFile = new File(build.getDirectory(), build.getFinalName()
				+ "." + extension);
		artifact.setFile(artifactFile);
	}
}
