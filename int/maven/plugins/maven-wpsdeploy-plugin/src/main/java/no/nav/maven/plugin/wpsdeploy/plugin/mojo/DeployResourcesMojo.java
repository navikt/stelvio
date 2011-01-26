package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

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

	/**
	 * @parameter
	 * @required
	 */
	private DeployArtifact[] artifacts;

	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {

		if (!isConfigurationLoaded()) {
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}

		deployResources(commandLine);

		String[] orgArgs = commandLine.getArguments();

		for (DeployArtifact da : artifacts) {

			if (da.getVersion() == null || da.getVersion().startsWith("$")) {
				getLog().info("Skipping " + da.getGroupId() + ":" + da.getArtifactId() + ", no version specified.");
				continue;
			}

			Commandline cmdline = new Commandline();
			cmdline.setExecutable(commandLine.getExecutable());
			cmdline.addArguments(orgArgs);
			addOrUpdateWebsphereVariable(cmdline, " " + da.getVariableName() + " ", da.getVersion());
		}

	}

	private final void deployResources(final Commandline commandLine) {
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/CreateApplicationArtifacts.py " + deployableArtifactsHome + " " + environment + " " + scriptsHome);
		commandLine.addArg(arg);
		executeCommand(commandLine);
	}

	/**
	 * Adds or updates a WebSphere variable
	 */
	private final void addOrUpdateWebsphereVariable(final Commandline commandLine, final String propertyType, final String propertyValue) {
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/setEnvironmentVariableOnDmgr.py" + propertyType + propertyValue + " " + scriptsHome);
		commandLine.addArg(arg);
		executeCommand(commandLine);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Deploy artifact resources to WPS";
	}
}