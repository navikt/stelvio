package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.MySOAPException;

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
		arg.setLine("ClusterStartStop.py start");
		commandLine.addArg(arg);

		int attempt = 1;
		int maxattempt = 5;
		while (true) {
			++attempt;
			try{
				executeCommand(commandLine);
				break;
			} catch (MySOAPException e) {
				if (attempt < maxattempt){
					getLog().info("Caught exception, retrying ... " + "[" + attempt + "/" + maxattempt + "]");
					continue;
				} else {
					throw new RuntimeException("Exiting after retrying "+maxattempt+" times!");
				}
			}
		}
		getLog().info("Cluster started!");
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Start WPS";
	}
}