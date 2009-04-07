package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.BufferedReader;
import java.io.IOException;

import no.nav.maven.plugin.websphere.plugin.utils.PwdConsole;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.components.interactivity.DefaultPrompter;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;


/**
 * Goal that contacts the deployment manager via plink to remotely execute a backup command. For windows (typically developer machine)
 * it will prompt for password for the user. For unix variants ssh trust is assumed
 * 
 * @author test@example.com
 * 
 * @goal backup-config
 * @requiresDependencyResolution
 */
public class BackupWebsphereConfigMojo extends RemoteCommandExecutorMojo {

	protected void executeRemoteCommand() throws MojoExecutionException, MojoFailureException {
		backupConfig();
	}
	
	private final void backupConfig() {
				
		final CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
		final CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
		
		Commandline commLine = new Commandline();
		try {
			Commandline.Argument arg = new Commandline.Argument();

			/* TODO: Put these hardcoded values in settings.xml for new WID Image */
			String pwd = null;
			if(Os.isFamily("windows") == true) {
				System.out.print("Enter password for wasadm: ");
				pwd = PwdConsole.getPassword();
				arg.setLine("C:/apps/SSH/plink.exe -pw " + pwd + " wasadm@" + deploymentManagerHost + " /opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/backupConfig.sh /opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/WebSphereConfig_`date +%d%m%Y%H%M%S`.zip -nostop");
	 		} else {
	 			arg.setLine("ssh "  + deploymentManagerHost + " '/opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/backupConfig.sh /opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/WebSphereConfig_`date +%d%m%Y%H%M%S`.zip -nostop'");
	 		}	
			
			commLine.addArg(arg);
			String hiddenPasswordOutput = commLine.toString().replace(pwd, "*******");
			getLog().info("Executing the following command: " + hiddenPasswordOutput);
			CommandLineUtils.executeCommandLine(commLine, stdout, stderr);
			getLog().info(stdout.getOutput());
		} catch (CommandLineException e) {
			throw new RuntimeException("An error occured executing: " + commLine, e);
		}
	}

	protected String getGoalPrettyPrint() {
		return "Backup Websphere configuration";
	}
}	