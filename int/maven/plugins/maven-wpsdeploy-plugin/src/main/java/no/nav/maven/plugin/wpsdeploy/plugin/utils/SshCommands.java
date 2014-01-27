package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.NonZeroSshExitCode;
import no.nav.maven.plugin.wpsdeploy.plugin.models.SshUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SshCommands {
	private static Integer SERVER_ALREADY_STARTED_EXIT_CODE = 255;
	private static Integer SERVER_ALREADY_STOPPED_EXIT_CODE = 246;

	private final static Logger logger = LoggerFactory.getLogger(SshCommands.class);

	static void checkIfDirExists(SshUser sshUser, String dir) {
		SshUtil.executeSshCommand(sshUser, "/usr/bin/test -d " + dir);
	}

	/**
	 * @throws no.nav.maven.plugin.wpsdeploy.plugin.exceptions.NonZeroSshExitCode
	 */
	public static void backupConfig(SshUser sshUser) {
		String baseDir = SshUtil.getBaseDir(sshUser); //TODO: getBaseDir() kan slettes etter at BPM8.5 går i prod
		String cmd = baseDir + "backupConfig.sh " + baseDir + "ConfigBackup_`date +%Y.%m.%d-%H.%M.%S`.zip -nostop";
		SshUtil.executeSshCommand(sshUser, cmd);
	}

	/**
	 * @throws no.nav.maven.plugin.wpsdeploy.plugin.exceptions.NonZeroSshExitCode
	 */
	public static void bounceDeploymentManager(SshUser sshUser, String dmgrUsername, String dmgrPassword) {
		stopDeploymentManager(sshUser, dmgrUsername, dmgrPassword);
		startDeploymentManager(sshUser);
	}

	private static void startDeploymentManager(SshUser sshUser) {
		logger.info("Starting the deployment manager...");
		String cmd = SshUtil.getBaseDir(sshUser) + "startManager.sh"; //TODO: getBaseDir() kan slettes etter at BPM8.5 går i prod
		try {
			SshUtil.executeSshCommand(sshUser, cmd);
		} catch (NonZeroSshExitCode e){
			if (e.getExitCode() == SERVER_ALREADY_STARTED_EXIT_CODE) {
				logger.info("Server was already running! (exitcode " + SERVER_ALREADY_STARTED_EXIT_CODE + ")");
			} else {
				throw e;
			}
		}
	}

	private static void stopDeploymentManager(SshUser sshUser, String dmgrUsername, String dmgrPassword) {
		logger.info("Stopping the deployment manager...");
		String cmd = SshUtil.getBaseDir(sshUser) + "stopManager.sh -username "+dmgrUsername+" -password "+dmgrPassword+" -timeout 600"; //TODO: getBaseDir() kan slettes etter at BPM8.5 går i prod
		try {
			SshUtil.executeSshCommand(sshUser, cmd);
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
	public static boolean checkDiskSpace(SshUser sshUser, int minimumFreeSpaceInMegaBytes) {
		String dir = "/opt/";
		String cmd = "df -m " + dir;
		logger.info("Checking disk that there is more than " + minimumFreeSpaceInMegaBytes + "MB free space in " + dir);
		String commandOutput = SshUtil.executeSshCommand(sshUser, cmd);
		int spaceAvailable = extractSizeFromDfCommandOutput(commandOutput);
		return spaceAvailable > minimumFreeSpaceInMegaBytes;
	}

	private static int extractSizeFromDfCommandOutput(String sizeString){
		StringBuilder output = new StringBuilder(sizeString.split("\n")[2].trim());
		int index;
		while ((index = output.indexOf(" ")) >=0 )
			output.setCharAt(index, '_');
		for (int i=0; i<2; i++){
			output.replace(0, output.indexOf("_"), "");
			while (output.charAt(0) == '_')
				output.deleteCharAt(0);
		}
		int sizeOfAvailableSpace = Integer.parseInt(output.substring(0,output.indexOf("_")));
		return sizeOfAvailableSpace;
	}
}
