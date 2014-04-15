package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.SshCommands;
import no.nav.maven.plugin.wpsdeploy.plugin.models.SshUser;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 *
 * @author test@example.com
 *
 * @goal backup-config
 * @requiresDependencyResolution
 */
public class BackupWebsphereConfigMojo extends WebsphereUpdaterMojo {

	/**
	 * @parameter expression="${environment}"
	 */
	private String environmentLowerOrUpperCase;
	
	private static final int NUMBER_OF_OLD_BACKUPS_TO_KEEP = 4;
	private static final int MINIMUM_FREE_SPACE_IN_MEGABYTES = 1024;
	
	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		
		String enviromentLoverCase = environmentLowerOrUpperCase.toLowerCase();
		SshUser sshUser = new SshUser(dmgrHostname, linuxUser, linuxPassword);		

		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having extracted the bus-configuration. Skipping ...");
			return;
		}

		SshCommands.verifyBackupDirectory(sshUser, enviromentLoverCase);
		SshCommands.deleteOldBackups(sshUser, enviromentLoverCase, NUMBER_OF_OLD_BACKUPS_TO_KEEP);
		
		if (!SshCommands.checkDiskSpace(sshUser, MINIMUM_FREE_SPACE_IN_MEGABYTES)) throw new MojoFailureException("Not enought space on dmgr available! Cannot continue.");
		else getLog().info("There is enough disk space. Proceeding with deployment.");

		SshCommands.backupConfig(sshUser,enviromentLoverCase);
		getLog().info("The backup of config went OK. Proceeding with deployment.");
	}

	protected String getGoalPrettyPrint() {
		return "Backup Websphere configuration";
	}

}
