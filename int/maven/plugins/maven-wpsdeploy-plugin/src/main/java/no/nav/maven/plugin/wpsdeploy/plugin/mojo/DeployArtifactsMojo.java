package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that contacts local scripts to deploy all applications to target
 * deployment manager
 * 
 * @author test@example.com
 * 
 * @goal deploy-artifacts
 * @requiresDependencyResolution
 */
public class DeployArtifactsMojo extends WebsphereUpdaterMojo {

	public final void applyToWebSphere(final Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}
		

		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("ApplicationManagement.py");
		wsadminCommandLine.addArg(arg);
		executeCommand(wsadminCommandLine);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Deploy artifacts to WPS";
	}
}