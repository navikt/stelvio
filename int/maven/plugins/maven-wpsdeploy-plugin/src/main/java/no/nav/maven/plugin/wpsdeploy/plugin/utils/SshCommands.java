package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.NonZeroSshExitCode;
import no.nav.maven.plugin.wpsdeploy.plugin.models.SshUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SshCommands {
	private static Integer SERVER_ALREADY_STARTED_EXIT_CODE = 255;
	private static Integer SERVER_ALREADY_STOPPED_EXIT_CODE = 246;
	private static String BASE_PATH = "/opt/IBM/BPM/profiles/Dmgr01/bin/";

	private final static Logger logger = LoggerFactory.getLogger(SshCommands.class);


	/**
	 * @throws no.nav.maven.plugin.wpsdeploy.plugin.exceptions.NonZeroSshExitCode
	 */
	public static void backupConfig(SshUser sshUser) {
		SshClient sshClient = new SshClient(sshUser);
		String cmd = "sudo " + BASE_PATH + "backupConfig.sh " + BASE_PATH + "ConfigBackup_`date +%Y.%m.%d-%H.%M.%S`.zip -nostop";
		sshClient.execute(cmd);
	}

	/**
	 * @throws no.nav.maven.plugin.wpsdeploy.plugin.exceptions.NonZeroSshExitCode
	 */
	public static void bounceDeploymentManager(SshUser sshUser, String dmgrUsername, String dmgrPassword) {
		stopDeploymentManager(sshUser, dmgrUsername, dmgrPassword);
		startDeploymentManager(sshUser);
	}

	private static void startDeploymentManager(SshUser sshUser) {
		SshClient sshClient = new SshClient(sshUser);
		logger.info("Starting the deployment manager...");
		String cmd = "sudo service dmgr start";
		try {
			sshClient.execute(cmd);
		} catch (NonZeroSshExitCode e){
			if (e.getExitCode() == SERVER_ALREADY_STARTED_EXIT_CODE) {
				logger.info("Server was already running! (exitcode " + SERVER_ALREADY_STARTED_EXIT_CODE + ")");
			} else {
				throw e;
			}
		}
	}

	private static void stopDeploymentManager(SshUser sshUser, String dmgrUsername, String dmgrPassword) {
		SshClient sshClient = new SshClient(sshUser);
		logger.info("Stopping the deployment manager...");
		String cmd = "sudo service dmgr stop";
		try {
			sshClient.execute(cmd);
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
		SshClient sshClient = new SshClient(sshUser);
		String dir = "/opt/";
		String cmd = "df -mP " + dir;
		logger.info("Checking disk that there is more than " + minimumFreeSpaceInMegaBytes + "MB free space in " + dir);
		String commandOutput = sshClient.execute(cmd);
		int spaceAvailable = extractSizeFromDfCommandOutput(commandOutput);
		return spaceAvailable > minimumFreeSpaceInMegaBytes;
	}

	private static int extractSizeFromDfCommandOutput(String sizeString) {
		String[] lineArray = sizeString.trim().split("\n");
		String secondLine = lineArray[1];
		String[] whiteSpaceSplit = secondLine.split("\\s+");
		String availableSpace = whiteSpaceSplit[3];

		return Integer.parseInt(availableSpace);
	}
}
