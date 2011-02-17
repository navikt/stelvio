/**
 * 
 */
package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.IOException;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.SshUtil;

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
public class BounceDeploymentManager extends WebsphereUpdaterMojo {

	@Override
	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having extracted the bus-configuration. Skipping ...");
			return;
		}
		try {
			if (SshUtil.bounceDeploymentManager(dmgrHostname, linuxUser, linuxPassword, dmgrUsername, dmgrPassword)) {
				getLog().info(dmgrHostname+" bounced successfully!");
			}
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage());
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Bounce deployment manager";
	}

}
