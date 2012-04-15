package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that contacts the deployment manager to deploy resources for artifacts.
 * 
 * @goal deploy-resources
 * @requiresDependencyResolution
 */
public class DeployResourcesMojo extends WebsphereUpdaterMojo {

	public final void applyToWebSphere(final Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {

		if (!isConfigurationLoaded()) {
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}
		
		Commandline.Argument arg = new Commandline.Argument();
		String app_props = busConfigurationDirectory + "/app_props/" + environment + "/";
		arg.setLine("CreateApplicationArtifacts.py " + environment + " " + app_props);
		wsadminCommandLine.addArg(arg);
		executeCommand(wsadminCommandLine);

	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Deploy artifact resources to WPS";
	}
}