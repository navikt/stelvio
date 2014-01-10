package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.NonZeroSshExitCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SshCommands {
	private static Integer SERVER_ALREADY_STARTED_EXIT_CODE = 255;
	private static Integer SERVER_ALREADY_STOPPED_EXIT_CODE = 246;

	private final static Logger logger = LoggerFactory.getLogger(SshCommands.class);

	static void checkIfDirExists(SshUser sshUser, String dir) {
		SshUtil.executeCommand(sshUser, "/usr/bin/test -d " + dir);
	}

	/**
	 * @throws no.nav.maven.plugin.wpsdeploy.plugin.exceptions.NonZeroSshExitCode
	 */
	public static void backupConfig(SshUser sshUser) {
		String baseDir = SshUtil.getBaseDir(sshUser); //TODO: getBaseDir() kan slettes etter at BPM8.5 går i prod
		String cmd = baseDir + "backupConfig.sh " + baseDir + "ConfigBackup_`date +%Y.%m.%d-%H.%M.%S`.zip -nostop";
		SshUtil.executeCommand(sshUser, cmd);
	}

	/**
	 * @throws no.nav.maven.plugin.wpsdeploy.plugin.exceptions.NonZeroSshExitCode
	 */
	public static void bounceDeploymentManager(SshUser sshUser, String dmgrUsername, String dmgrPassword) {
		stopDeploymentManager(sshUser, dmgrUsername, dmgrPassword);
		startDeploymentManager(sshUser);
	}

	static void startDeploymentManager(SshUser sshUser) {
		String cmd = SshUtil.getBaseDir(sshUser) + "startManager.sh"; //TODO: getBaseDir() kan slettes etter at BPM8.5 går i prod
		try {
			SshUtil.executeCommand(sshUser, cmd);
		} catch (NonZeroSshExitCode e){
			if (e.getExitCode() == SERVER_ALREADY_STARTED_EXIT_CODE) {
				logger.info("Server was already running! (exitcode " + SERVER_ALREADY_STARTED_EXIT_CODE + ")");
			} else {
				throw e;
			}
		}
	}

	private static void stopDeploymentManager(SshUser sshUser, String dmgrUsername, String dmgrPassword) {
		String cmd = SshUtil.getBaseDir(sshUser) + "stopManager.sh -username "+dmgrUsername+" -password "+dmgrPassword+" -timeout 600"; //TODO: getBaseDir() kan slettes etter at BPM8.5 går i prod
		try {
			SshUtil.executeCommand(sshUser, cmd);
		} catch (NonZeroSshExitCode e){
			if (e.getExitCode() == SERVER_ALREADY_STOPPED_EXIT_CODE){
				logger.info("Server was already stopped! (exitcode " + SERVER_ALREADY_STOPPED_EXIT_CODE + ")");
			} else {
				throw e;
			}
		}
	}

	/**
	 * Checks if there is enough disk space available
	 * @return true if there is enough space, false otherwise
	 * @throws java.io.IOException
	 */
	public static boolean checkDiskSpace(SshUser sshUser) {
		String dir = "/opt/";
		String cmd = "df " + dir;
		logger.info("Checking disk space usage in " + dir);
		String commandOutput = SshUtil.executeCommand(sshUser, cmd);
		String spaceAvailable = extractSize(commandOutput.split("\n")[2].trim());
		return compareSizeStrings(spaceAvailable, "1000000") > 0;
	}

	/**
	 * Compares numbers from strings
	 * @param a
	 * @param b
	 * @return positiv if a>b, negativ if a&ltb or error (message is written out), 0 if either a=b
	 */
	static int compareSizeStrings(String a, String b){
		int number_a = 0;
		int number_b = 0;
		try{
			number_a = Integer.parseInt(a);
			number_b = Integer.parseInt(b);
			return number_a - number_b;
		}catch (NumberFormatException e){
			throw new RuntimeException("[ERROR] Size is in incorrect format");
		}
	}

	private static String extractSize(String sizeString){
		StringBuilder output = new StringBuilder(sizeString.trim());
		int index;
		while ((index = output.indexOf(" ")) >=0 )
			output.setCharAt(index, '_');
		for (int i=0; i<2; i++){
			output.replace(0, output.indexOf("_"), "");
			while (output.charAt(0) == '_')
				output.deleteCharAt(0);
		}

		return output.substring(0,output.indexOf("_"));
	}
}
