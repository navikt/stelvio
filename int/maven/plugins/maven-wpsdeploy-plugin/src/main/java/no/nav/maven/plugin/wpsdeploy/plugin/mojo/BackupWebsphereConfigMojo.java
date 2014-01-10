package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.SshCommands;
import no.nav.maven.plugin.wpsdeploy.plugin.utils.SshUser;

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

	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		SshUser sshUser = new SshUser(dmgrHostname, linuxUser, linuxPassword);

		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having extracted the bus-configuration. Skipping ...");
			return;
		}
		if (!SshCommands.checkDiskSpace(sshUser)) throw new MojoFailureException("Not enought space on dmgr available! Cannot continue.");
		else getLog().info("There is enough disk space. Proceeding with deployment.");

		SshCommands.backupConfig(sshUser);
		getLog().info("The backup of config went OK. Proceeding with deployment.");
	}

	protected String getGoalPrettyPrint() {
		return "Backup Websphere configuration";
	}

}
