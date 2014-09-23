package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import no.nav.maven.plugin.wpsdeploy.plugin.models.SshUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SshCommands {
	private static final String BASE_PATH = "/opt/IBM/BPM/profiles/Dmgr01/bin/";
	private static final String BACKUP_ROOT_DIRECTORY = "/app/backup/";
	private static final String BACKUP_FILE_PREFIX = "DmgrConfigBackup_";
	
	private static final Logger logger = LoggerFactory.getLogger(SshCommands.class);

	/**
	 * @throws no.nav.maven.plugin.wpsdeploy.plugin.exceptions.NonZeroSshExitCode
	 */
	public static void backupConfig(SshUser sshUser, String enviroment) {
		SshClient sshClient = new SshClient(sshUser);
		String cmd = "sudo " + BASE_PATH + "backupConfig.sh " + BACKUP_ROOT_DIRECTORY + enviroment + "/" + BACKUP_FILE_PREFIX +"`date +%Y.%m.%d-%H.%M.%S`.zip -nostop";
		sshClient.execute(cmd);
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
	
	public static void verifyBackupDirectory(SshUser sshUser, String enviroment) {
		SshClient sshClient = new SshClient(sshUser);
		String backupDirectory = BACKUP_ROOT_DIRECTORY + enviroment + "/";
		String cmd = "sudo mkdir -p " + backupDirectory;
		logger.info("Verifying that the backup directory {" + backupDirectory + "} exists");
		sshClient.execute(cmd);
	}
	public static void deleteOldBackups(SshUser sshUser, String enviroment, int numberOfBackupsToKeep) {
		SshClient sshClient = new SshClient(sshUser);
		String backupDirectory = BACKUP_ROOT_DIRECTORY + enviroment + "/";
		String cmd = "sudo ls " + backupDirectory + " | grep " + BACKUP_FILE_PREFIX + " | grep .zip";
		logger.info("Getting a list of old backups");
		try {
			String commandOutput = sshClient.execute(cmd);
			String[] lineArray = commandOutput.trim().split("\n");
			logger.info("There is " + lineArray.length + " old backups in " + backupDirectory);
			for (int i = lineArray.length - 1 - numberOfBackupsToKeep; i >= 0; i--) {
				if(lineArray[i].startsWith(BACKUP_FILE_PREFIX) && lineArray[i].endsWith(".zip")){ // Extra check to verify external input before deleting.
					SshClient sshDeleteClient = new SshClient(sshUser);
					String deleteCmd = "sudo rm " + backupDirectory + lineArray[i];
					logger.info("Deleting old backup " + lineArray[i]);
					sshDeleteClient.execute(deleteCmd);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}
