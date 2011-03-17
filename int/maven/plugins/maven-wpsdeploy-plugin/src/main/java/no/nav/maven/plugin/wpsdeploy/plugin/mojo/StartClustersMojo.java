package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that contacts the deployment manager to start MECluster,SupportCluster
 * and WPSCluster.
 * 
 * @goal start-clusters
 * @requiresDependencyResolution
 */
public class StartClustersMojo extends WebsphereUpdaterMojo {

	/**
	 * @parameter expression="${start-clusters.skip}"
	 */
	protected boolean startClustersSkip;

	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {

		if (!isConfigurationLoaded()) {
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}

		if (!startClustersSkip) {
			startClusters(commandLine);
		} else {
			getLog().info("Skipping cluster start-up.");
		}
	}

	private final void startClusters(final Commandline commandLine) {

		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/ClusterStartStop.py " + scriptsHome + " start");
		commandLine.addArg(arg);

		// Handling SOAPException, SocketTimeoutException (retval 105), retrying
		// five times.
		int attempt = 0;
		int maxattempt = 5;

		while (attempt <= maxattempt) {
			int retval = executeCommand(commandLine);
			getLog().info("[RETVAL = " + retval + "]");

			if (retval != 105) {
				break;
			}

			if (attempt != maxattempt)
				getLog().info("Caught exception, retrying ... " + "[" + ++attempt + "/" + maxattempt + "]");
			else {
				getLog().info("Could not perform the operation. Continuing ...");
				break;
			}
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Start WPS";
	}
}