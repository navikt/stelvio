package no.nav.maven.plugin.configurewpsdev.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * 
 * @goal execute-commandline
 * 
 * @author test@example.com
 *  
 */
public class ExecuteCommandLineMojo extends AbstractExecutableMojo {
	

	
	/**
	 * @parameter
	 */
	private String arguments;
	
	@Override
	protected void runExecutable(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine(arguments);
		commandLine.addArg(arg);
		executeCommand(commandLine);
	}

//	@Override
//	protected String getGoalPrettyPrint() {
//		return "CreateProfilesMojo";
//	}


}
