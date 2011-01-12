package no.stelvio.maven.build.plugin.utils;

import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

public class CleartoolCommandLine {

	public static int runClearToolCommand(String workingDir, String subcommand) throws MojoFailureException{
		Commandline commandline = new Commandline();
		commandline.setWorkingDirectory(workingDir);
		Commandline.Argument arg = new Commandline.Argument();
		String command = "cleartool " + subcommand;
		arg.setLine(command);
		commandline.addArg(arg);
		return CommandLineUtil.executeCommand(commandline);
	}
}
