package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.MySocketTimeoutException;

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
		
		int attempt = 0;
		int maxattempt = 5;
		while (true) {
			++attempt;
			try{
				executeCommand(commandLine);
				break;
			} catch (MySocketTimeoutException e) {
				if (attempt < maxattempt){
					getLog().info("Caught exception, retrying ... " + "[" + attempt + "/" + maxattempt + "]");
				} else {
					throw new RuntimeException("Exiting after retrying "+maxattempt+" times!");
				}
			}
		}
		getLog().info("Cluster started!");
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Synchronize WPS nodes";
	}
}