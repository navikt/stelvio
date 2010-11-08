package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that contacts the deployment manager to synchronize all nodes in the
 * cluster.
 * 
 * @author test@example.com
 * 
 * @goal synchronize-nodes
 * @requiresDependencyResolution
 */
public class SynchronizeNodesMojo extends WebsphereUpdaterMojo {

	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		
		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}
		
		synchronizeNodes(commandLine);
	}

	private final void synchronizeNodes(final Commandline commandLine) {
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/ClusterStartStop.py " + scriptsHome + " synch");
		
		commandLine.addArg(arg);
		
		// Handling SOAPException, SocketTimeoutException (retval 105), retrying five times.
		int attempt = 0;
		int maxattempt = 5;
		
		while (attempt <= maxattempt){
			int retval = executeCommand(commandLine);
			getLog().info("[RETVAL = " + retval + "]");
			
			if (retval != 105){
				break;
			}
			
			if (attempt != maxattempt) getLog().info("Caught exception, retrying ... " + "[" + ++attempt + "/" + maxattempt + "]" );
			else {
				getLog().info("Could not perform the operation. Continuing ...");
				break;
			}
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Synchronize WPS nodes";
	}
}