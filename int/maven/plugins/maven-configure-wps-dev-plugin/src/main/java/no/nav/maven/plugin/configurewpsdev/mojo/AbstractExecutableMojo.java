package no.nav.maven.plugin.configurewpsdev.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Abstract class using the template pattern for child mojos.
 * 
 * @author r
 */
public abstract class AbstractExecutableMojo extends AbstractCommandLineMojo {

	/**
	 * @parameter
	 */
	private String executable;
	
	protected abstract void runExecutable(final Commandline commandLine) throws MojoExecutionException, MojoFailureException;

	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
		Commandline commandLine = new Commandline();
		commandLine.setExecutable(executable);
		runExecutable(commandLine);
	}
}