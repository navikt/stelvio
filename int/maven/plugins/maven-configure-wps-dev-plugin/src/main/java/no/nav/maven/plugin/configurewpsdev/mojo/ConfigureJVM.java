package no.nav.maven.plugin.configurewpsdev.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * 
 * @goal configure-jvm
 * 
 * @phase process-sources
 */
public class ConfigureJVM extends AbstractWSAdminMojo {

	@Override
	protected void runWSAdmin(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		Commandline.Argument arg0 = new Commandline.Argument();
		arg0.setLine(scriptsHome+"\\configureJVM.py");
		commandLine.addArg(arg0);
		Commandline.Argument arg1 = new Commandline.Argument();
		arg1.setLine(scriptsHome);
		commandLine.addArg(arg1);
		executeCommand(commandLine);
		
	}

}
