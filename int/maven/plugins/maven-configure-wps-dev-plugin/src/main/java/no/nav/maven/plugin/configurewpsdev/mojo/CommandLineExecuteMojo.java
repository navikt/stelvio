package no.nav.maven.plugin.configurewpsdev.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Abstract class using the template pattern for child mojos.
 * 
 * @author r
 */
public abstract class CommandLineExecuteMojo extends CommandLineMojo {
	
	protected abstract void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException;

	protected final void doExecute() throws MojoExecutionException, MojoFailureException {

		/* Given that the variable wid.runtime is set correctly in settings.xml */
		Commandline commandLine = new Commandline();
		commandLine.setExecutable("C>=manafosgs!");
		
//		Commandline.Argument arg1 = new Commandline.Argument();
//		arg1.setLine("-host " + dmgrHostname);
//		commandLine.addArg(arg1);

		applyToWebSphere(commandLine);
	}
}