package no.nav.maven.plugin.wid;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Cleans up the wid configuration files.
 * 
 * @author <a href="mailto:test@example.com">Erik Godding Boye</a>
 * @goal clean-projects
 * @execute goal="clean"
 * @aggregator
 */
public class CleanProjectsMojo extends AbstractMojo {
	/**
	 * @parameter default-value="${project.basedir}/libraries"
	 * @required
	 */
	private File dependencyProjectsDirectory;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		FileUtils.deleteQuietly(dependencyProjectsDirectory);
	}
}
