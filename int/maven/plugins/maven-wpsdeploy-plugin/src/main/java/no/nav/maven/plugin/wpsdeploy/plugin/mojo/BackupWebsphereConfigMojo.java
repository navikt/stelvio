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

	
	@Override
	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having extracted the bus-configuration. Skipping ...");
			return;
		}
	
		try {
			SshUtil.backupConfig(dmgrHostname, linuxUser, linuxPassword);
		} catch (IOException e) {
			throw new MojoFailureException(e.getMessage());
		}
	}
	
	protected String getGoalPrettyPrint() {
		return "Backup Websphere configuration";
	}


}