package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.IOException;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.SshUtil;

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

	//TODO: må fjernes etter at 7.4 har gått prod. Den mer generelle -Dskip=backup-config skal brukes i stede
	/**
	 * @parameter expression="${config.backup.skip}"
	 */
	private boolean skipBackup;
	
	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having extracted the bus-configuration. Skipping ...");
			return;
		}
	
		try {
			if (!checkDiskSpace())
				throw new MojoFailureException("Not enought space on dmgr available! Cannot continue.");
			else getLog().info("There is enough disk space. Proceeding with deployment.");
			if (skipBackup) getLog().info("Skipping backup of WebSohere configuration"); //TODO: må fjernes etter at 7.4 har gått prod. Den mer generelle -Dskip=backup-config skal brukes i stede
			else SshUtil.backupConfig(dmgrHostname, linuxUser, linuxPassword);
			
		} catch (IOException e) {
			throw new MojoFailureException(e.getMessage());
		}
	}
	
	private boolean checkDiskSpace() throws IOException{
		return SshUtil.checkDiskSpace(dmgrHostname, linuxUser, linuxPassword);
	}
	
	protected String getGoalPrettyPrint() {
		return "Backup Websphere configuration";
	}


}