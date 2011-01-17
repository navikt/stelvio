package no.stelvio.maven.build.plugin.utils;

import java.io.File;

import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * This utility class makes and runs cleartool commands from commandline
 * 
 * @author test@example.com
 */
public class CleartoolCommandLine {

	/**
	 * Runs cleartool commands
	 * @param workingDir - where to run command from
	 * @param subcommand - cleartool subcommand with all parameters
	 * @return result code
	 * @throws MojoFailureException
	 */
	public static int runClearToolCommand(String workingDir, String subcommand) throws MojoFailureException{
		Commandline cleartoolCommand = new Commandline();
		File workDir = new File(workingDir);
		cleartoolCommand.setWorkingDirectory(workDir);
		Commandline.Argument arg = new Commandline.Argument();
		String command = "cleartool " + subcommand;
		arg.setLine(command);
		cleartoolCommand.addArg(arg);
		return CommandLineUtil.executeCommand(cleartoolCommand);
	}
}
