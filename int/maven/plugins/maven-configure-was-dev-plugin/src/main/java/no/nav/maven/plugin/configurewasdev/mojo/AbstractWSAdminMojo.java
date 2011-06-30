package no.nav.maven.plugin.configurewasdev.mojo;

import java.io.File;


import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

public abstract class AbstractWSAdminMojo extends AbstractCommandLineMojo {

	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	protected File targetFolder;
	
	/**
	 * @parameter expression="${scriptsHome}"
	 */
	protected String scriptsHome;
	
	/**
	 * @parameter expression="${wpsProfilePath}"
	 * @required 
	 */
	protected String wpsProfilePath;
	
	/**
	 * @parameter expression="${adminUser}"
	 * @required 
	 */
	protected String adminUser;
	
	protected abstract void runWSAdmin(final Commandline commandLine) throws MojoExecutionException, MojoFailureException;
	
	
	@Override
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		String executable = wpsProfilePath + "/bin/wsadmin.bat";
		Commandline commandLine = new Commandline();
		commandLine.setExecutable(executable);
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-username "+adminUser+" -password "+adminPasswd+" -f ");
		commandLine.addArg(arg);
		runWSAdmin(commandLine);
	}
}
