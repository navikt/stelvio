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
 * Goal that contacts the deployment manager via plink to remotely execute a backup command.
 * 
 * @author test@example.com
 * 
 * @goal backup-config
 * @requiresDependencyResolution
 */
public class BackupWebsphereConfigMojo extends AbstractMojo {

	
	public void execute() throws MojoExecutionException, MojoFailureException {
		applyToWebSphere(null);
	}
	
	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		backupConfig(commandLine);
	}
	
	private final void backupConfig(final Commandline commandLine) {
				
		final CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
		final CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
		
		try {
			Commandline.Argument arg = new Commandline.Argument();

			System.out.print("Enter password for wasadm: ");
			String pwd = PwdConsole.getPassword();

			if(Os.isFamily("windows") == true) {
				arg.setLine("C:/apps/SSH/plink.exe -pw " + pwd + " wasadm@" + "e11apvl029.utv.internsone.local" + " /opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/backupConfig.sh /opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/WebSphereConfig_`date +%d%m%Y%H%M%S`.zip -nostop");
	 		} else {
	 			arg.setLine("ssh test@example.com '/opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/backupConfig.sh /opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/WebSphereConfig_`date +%d%m%Y%H%M%S`.zip -nostop'");
	 		}	
			
			Commandline commLine = new Commandline(); 
			commLine.addArg(arg);
			String hiddenPasswordOutput = commLine.toString().replace(pwd, "*******");
			getLog().info("Executing the following command: " + hiddenPasswordOutput);
			CommandLineUtils.executeCommandLine(commLine, stdout, stderr);
			getLog().info(stdout.getOutput());
		} catch (CommandLineException e) {
			throw new RuntimeException("An error occured executing: " + commandLine, e);
		}
	}
}	