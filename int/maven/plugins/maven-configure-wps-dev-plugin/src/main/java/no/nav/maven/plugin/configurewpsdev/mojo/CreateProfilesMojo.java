package no.nav.maven.plugin.configurewpsdev.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

public class CreateProfilesMojo extends CommandLineExecuteMojo {

	@Override
	protected void applyToWebSphere(Commandline commandLine)
			throws MojoExecutionException, MojoFailureException {
		
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "CreateProfilesMojo";
	}


}
