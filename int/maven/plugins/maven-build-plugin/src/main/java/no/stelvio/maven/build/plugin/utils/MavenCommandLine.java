package no.stelvio.maven.build.plugin.utils;

import java.io.File;

import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

public class MavenCommandLine {

	private final static String BAT_FILE = "mvn_job_exec.bat";
	
	/**
	 * This method runs maven commands
	 * @param workingDir - where pom.xml is
	 * @param command - maven goals with parameters, i.e. WITHOUT mvn. <br />E.g. "clean install -Dparam=value ..."
	 * @return 0 if everything is OK
	 * @throws MojoFailureException
	 */
	public static int PerformMavenCommand(String workingDir, String command) throws MojoFailureException{
		Commandline mavenCommand = new Commandline();
		File workDir = new File(workingDir);
		mavenCommand.setWorkingDirectory(workDir);
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("mvn " + command);
		mavenCommand.addArg(arg);
		return CommandLineUtil.executeCommand(mavenCommand);
	}
	
	/**
	 * Method that runs maven commands via maven_job_exec.bat file
	 * @param scriptDir - path to maven_job_exec.bat
	 * @param workingDir - where pom.xml is
	 * @param command - maven goals with parameters, i.e. WITHOUT mvn. <br />E.g. "clean install -Dparam=value ..."
	 * @return 0 if everything is OK
	 * @throws MojoFailureExceptionn
	 */
	public static int PerformMavenCommand(String scriptDir, String workingDir, String command) throws MojoFailureException{
		Commandline mavenCommand = new Commandline();
		File workDir = new File(workingDir);
		mavenCommand.setWorkingDirectory(workDir);
		Commandline.Argument arg = new Commandline.Argument();
		if (!scriptDir.endsWith("\\") && !scriptDir.endsWith("/")) scriptDir += "/";
		arg.setLine(scriptDir+BAT_FILE+" mvn " + command);
		mavenCommand.addArg(arg);
		return CommandLineUtil.executeCommand(mavenCommand);
	}
	
}
