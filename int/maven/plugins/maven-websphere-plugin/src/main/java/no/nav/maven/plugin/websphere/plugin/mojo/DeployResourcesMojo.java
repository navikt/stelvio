package no.nav.maven.plugin.websphere.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that contacts the deployment manager to deploy resources for artifacts.
 * 
 * @author test@example.com
 * 
 * @goal deploy-resources
 * @requiresDependencyResolution
 */
public class DeployResourcesMojo extends WebsphereUpdaterMojo {

	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		deployResources(commandLine);
	}

	private final void deployResources(final Commandline commandLine) {
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/CreateApplicationArtifacts.py " + deployableArtifactsHome + " "
				+ environment + " " + scriptsHome);
		commandLine.addArg(arg);
		executeCommand(commandLine);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Deploy artifact resources to WPS";
	}
}