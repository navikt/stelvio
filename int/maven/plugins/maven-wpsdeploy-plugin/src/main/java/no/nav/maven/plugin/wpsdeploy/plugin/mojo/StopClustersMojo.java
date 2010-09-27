package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that contacts the deployment manager to stop MECluster,SupportCluster
 * and WPSCluster.
 * 
 * @author test@example.com
 * 
 * @goal stop-clusters
 * @requiresDependencyResolution
 */
public class StopClustersMojo extends WebsphereUpdaterMojo {

	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}
		
		stopClusters(commandLine);
	}

	private final void stopClusters(final Commandline commandLine) {
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/ClusterStartStop.py " + scriptsHome + " stop");
		commandLine.addArg(arg);
		executeCommand(commandLine);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Stop WPS";
	}
}