package no.nav.maven.plugin.websphere.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that contacts the deployment manager to start MECluster,SupportCluster
 * and WPSCluster.
 * 
 * @author test@example.com
 * 
 * @goal start-clusters
 * @requiresDependencyResolution
 */
public class StartClustersMojo extends WebsphereUpdaterMojo {

	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		startClusters(commandLine);
	}

	private final void startClusters(final Commandline commandLine) {
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/ClusterStartStop.py " + scriptsHome + " start");
		commandLine.addArg(arg);
		executeCommand(commandLine);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Start WPS";
	}
}