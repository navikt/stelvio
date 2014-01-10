/**
 *
 */
package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.SshCommands;
import no.nav.maven.plugin.wpsdeploy.plugin.models.SshUser;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 *
 * @author test@example.com
 *
 * @goal bounce-dmgr
 * @requiresDependencyResolution
 */
public class BounceDeploymentManagerMojo extends WebsphereUpdaterMojo {

	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		SshUser sshUser = new SshUser(dmgrHostname, linuxUser, linuxPassword);
		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having extracted the bus-configuration. Skipping ...");
			return;
		}
		getLog().info("Bouncing deployment manager");
		SshCommands.bounceDeploymentManager(sshUser, dmgrUsername, dmgrPassword);
		getLog().info(dmgrHostname+" bounced successfully!");
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Bounce deployment manager";
	}

}
