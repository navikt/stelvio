package no.nav.maven.plugins.pi;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Goal that generates a project interchange zip-file for the current project
 * 
 * @author test@example.com
 * 
 * @goal project-interchange
 */
public class ProjectInterchangeMojo extends AbstractMojo {
	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	public void execute() throws MojoExecutionException {
	}
}
