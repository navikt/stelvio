package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.FileNotFoundException;
import java.io.IOException;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.PropertyUtils;
import no.nav.maven.plugin.wpsdeploy.plugin.utils.PwdConsole;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that contacts the deployment manager via plink to remotely execute a backup command. For windows (typically developer machine) it will prompt for password for the user. For unix variants ssh
 * trust is assumed
 * 
 * @author test@example.com
 * 
 * @goal backup-config
 * @requiresDependencyResolution
 */
public class BackupWebsphereConfigMojo extends WebsphereUpdaterMojo {

	
	@Override
	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		if (interactiveMode)
			backupConfig();
	}

	private final void backupConfig() throws MojoFailureException {
		
		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having extracted the bus-configuration. Skipping ...");
			return;
		}
		
		Commandline commLine = new Commandline();
		Commandline.Argument arg = new Commandline.Argument();

		/* TODO: Put these hardcoded values somewhere else */
		String pwd = null;
		if (Os.isFamily("windows") == true) {
			System.out.print("Enter password for wasadm: ");
			pwd = PwdConsole.getPassword();
			arg.setLine("C:/apps/SSH/plink.exe -pw " + pwd + " wasadm@" + dmgrHostname
					+ " /opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/backupConfig.sh /opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/WebSphereConfig_`date +%d%m%Y%H%M%S`.zip -nostop");
		} else {
			arg.setLine("ssh wasadm@" + dmgrHostname
					+ " '/opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/backupConfig.sh /opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/WebSphereConfig_`date +%d%m%Y%H%M%S`.zip -nostop'");
		}

		commLine.addArg(arg);

		executeCommand(commLine);
	}

	protected String getGoalPrettyPrint() {
		return "Backup Websphere configuration";
	}


}