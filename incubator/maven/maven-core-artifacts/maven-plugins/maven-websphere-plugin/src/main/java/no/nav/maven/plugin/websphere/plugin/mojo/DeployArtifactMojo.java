package no.nav.maven.plugin.websphere.plugin.mojo;

import java.util.List;

import no.nav.pensjonsprogrammet.wpsconfiguration.ActivationspecificationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.ActivationspecificationsType;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;


/**
 * Goal that contacts local scripts to deploy all applications to target deployment manager
 * 
 * @author test@example.com
 * 
 * @goal deploy-artifact
 * @requiresDependencyResolution
 */
public class DeployArtifactMojo extends WebsphereUpdaterMojo {
    	
	/**
	 * @parameter expression="${environment}"
	 * @required
	 */
	private String environment;
	
	
	public final void applyToWebSphere() throws MojoExecutionException, MojoFailureException {
		deployArtifacts();
	}
	
	private final void deployArtifacts() {
		
		String scriptsHome = baseDirectory + "/" + scriptDirectory;
		String deployableArtifactsHome = baseDirectory + "/target";
		
		Commandline commandLine = new Commandline();
		if(Os.isFamily("windows") == true) {
			commandLine.setExecutable(widHome + "/pf/wps01/bin/wsadmin.bat");
		} else {
			commandLine.setExecutable(widHome + "/pf/wps01/bin/wsadmin.sh");
		}	

		final CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
		final CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
		
		try {
			Commandline.Argument arg1 = new Commandline.Argument();
			arg1.setLine("-host " + deploymentManagerHost);
			commandLine.addArg(arg1);
			Commandline.Argument arg2 = new Commandline.Argument();
			arg2.setLine("-port " + deploymentManagerPort);
			commandLine.addArg(arg2);
			Commandline.Argument arg3 = new Commandline.Argument();
			arg3.setLine("-user " + deploymentManagerUser);
			commandLine.addArg(arg3);
			Commandline.Argument arg4 = new Commandline.Argument();
			arg4.setLine("-password " + deploymentManagerPassword);
			commandLine.addArg(arg4);
			Commandline.Argument arg5 = new Commandline.Argument();
			arg5.setLine("-f " + baseDirectory + "/" + scriptDirectory + "/scripts/Applications.py applications " + environment + " " + scriptsHome + " " + deployableArtifactsHome);
			commandLine.addArg(arg5);
			getLog().info("Executing the following command: " + commandLine.toString());
			CommandLineUtils.executeCommandLine(commandLine, stdout, stderr);
		} catch (CommandLineException e) {
			throw new RuntimeException("An error occured executing: " + commandLine, e);
		}
	}
}	