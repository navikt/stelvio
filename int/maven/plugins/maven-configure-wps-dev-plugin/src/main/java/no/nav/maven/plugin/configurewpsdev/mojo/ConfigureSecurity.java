package no.nav.maven.plugin.configurewpsdev.mojo;

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
		Commandline.Argument arg3 = new Commandline.Argument();
		arg3.setLine(adminUser);
		commandLine.addArg(arg3);
		Commandline.Argument arg4 = new Commandline.Argument();
		arg4.setLine(adminPasswd);
		commandLine.addArg(arg4);
		executeCommand(commandLine);
		

	}

}
