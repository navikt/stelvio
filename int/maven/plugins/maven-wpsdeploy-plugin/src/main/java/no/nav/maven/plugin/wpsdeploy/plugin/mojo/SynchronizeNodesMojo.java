package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.MySOAPException;

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

	public final void applyToWebSphere(final Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {

		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}

		synchronizeNodes(wsadminCommandLine);
	}

	private final void synchronizeNodes(final Commandline wsadminCommandLine) {
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("ClusterSync.py");

		wsadminCommandLine.addArg(arg);

		int attempt = 0;
		int maxattempt = 5;
		while (true) {
			++attempt;
			try{
				executeCommand(wsadminCommandLine);
				break;
			} catch (MySOAPException e) {
				if (attempt <= maxattempt){
					getLog().info("Caught exception, retrying ... " + "[" + attempt + "/" + maxattempt + "]");
				} else {
					throw new RuntimeException("Exiting after retrying "+maxattempt+" times!");
				}
			}
		}
		getLog().info("Cluster synchronized!");
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Synchronize WPS nodes";
	}
}
