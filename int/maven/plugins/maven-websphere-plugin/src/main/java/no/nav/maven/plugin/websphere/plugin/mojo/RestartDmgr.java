package no.nav.maven.plugin.websphere.plugin.mojo;

import no.nav.maven.plugin.websphere.plugin.utils.PwdConsole;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that restarts the deployment manager for the given environment
 * 
 * @author test@example.com
 * 
 * @goal restart-dmgr
 * @requiresDependencyResolution
 */
public class RestartDmgr extends RemoteCommandExecutorMojo {

	private final String stopDmgr = "stopManager.sh";

	private final String startDmgr = "startManager.sh";

	protected void executeRemoteCommand() throws MojoExecutionException, MojoFailureException {
		
		backupConfig(stopDmgr);
		backupConfig(startDmgr);
		
	}

	private final void backupConfig(String script) {

		Commandline commLine = new Commandline();
		Commandline.Argument arg = new Commandline.Argument();

		String pwd = null;
		if (Os.isFamily("windows") == true) {
			System.out.print("Enter password for wasadm: ");
			pwd = PwdConsole.getPassword();
			arg.setLine("C:/apps/SSH/plink.exe -pw "
							+ pwd
							+ " wasadm@"
							+ deploymentManagerHost
							+ " /opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/"
							+ script );
		} else {
			arg.setLine("ssh wasadm@"
							+ deploymentManagerHost
							+ " '/opt/IBM/WebSphere/ProcServer/profiles/Dmgr01/bin/" 
							+ script );
		}

		commLine.addArg(arg);

		executeCommand(commLine);
	}

	protected String getGoalPrettyPrint() {
		return "Restart DMGR";
	}
}