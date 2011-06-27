package no.nav.maven.plugin.configurewpsdev.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

public class WSAdminMojo extends AbstractWSAdminMojo {
	

	
	@Override
	protected void runWSAdmin(Commandline commandLine)	throws MojoExecutionException, MojoFailureException {
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-username srvpensjon -password Ash5SoxP -f HelloWorld.py");
		commandLine.addArg(arg);
		executeCommand(commandLine);
	}

}
