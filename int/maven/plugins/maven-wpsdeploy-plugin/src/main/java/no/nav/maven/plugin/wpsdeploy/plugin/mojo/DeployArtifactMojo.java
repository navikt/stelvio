package no.nav.maven.plugin.websphere.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that contacts local scripts to deploy all applications to target
 * deployment manager
 * 
 * @author test@example.com
 * 
 * @goal deploy-artifact
 * @requiresDependencyResolution
 */
public class DeployArtifactMojo extends WebsphereUpdaterMojo {

	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		deployArtifacts(commandLine);
	}

	private final void deployArtifacts(final Commandline commandLine) {
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/Applications.py " + scriptsHome + " " + deployableArtifactsHome);
		commandLine.addArg(arg);
		executeCommand(commandLine);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Deploy artifacts to WPS";
	}
}