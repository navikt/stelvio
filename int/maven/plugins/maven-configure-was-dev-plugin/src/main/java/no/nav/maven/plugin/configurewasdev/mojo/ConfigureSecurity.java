package no.nav.maven.plugin.configurewasdev.mojo;


import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;


/**
 * 
 * @goal configure-security
 * 
 * @phase process-sources
 */
public class ConfigureSecurity extends AbstractWSAdminMojo {
	
	
	@Override
	protected void runWSAdmin(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		
	
		
		Commandline.Argument arg0 = new Commandline.Argument();
		arg0.setLine(scriptsHome+"\\setupSecurity.py");
		commandLine.addArg(arg0);
		Commandline.Argument arg1 = new Commandline.Argument();
		arg1.setLine(scriptsHome);
		commandLine.addArg(arg1);
		Commandline.Argument arg2 = new Commandline.Argument();
		arg2.setLine(targetFolder.toString());
		commandLine.addArg(arg2);
		executeCommand(commandLine);

	}

}
