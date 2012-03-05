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

	public final void applyToWebSphere(final Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {

		if (!isConfigurationLoaded()) {
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}

		String[] orgArgs = wsadminCommandLine.getArguments();
		
		deployResources(wsadminCommandLine);

		for (DeployArtifact da : artifacts) {

			if (da.getVersion() == null || da.getVersion().startsWith("$")) {
				getLog().info("Skipping " + da.getGroupId() + ":" + da.getArtifactId() + ", no version specified.");
				continue;
			}

			Commandline cmdline = new Commandline();
			cmdline.setExecutable(wsadminCommandLine.getExecutable());
			cmdline.addArguments(orgArgs);
			addOrUpdateWebsphereVariable(cmdline, " " + da.getVariableName() + " ", da.getVersion());
		}
		
		Commandline cmdlineBusConfig = new Commandline();
		cmdlineBusConfig.setExecutable(wsadminCommandLine.getExecutable());
		cmdlineBusConfig.addArguments(orgArgs);
		addOrUpdateWebsphereVariable(cmdlineBusConfig, " BUS_CONFIGURATION_VERSION ", busConfigurationVersion);

	}

	private final void deployResources(final Commandline wsadminCommandLine) {
		Commandline.Argument arg = new Commandline.Argument();
		String app_props = busConfigurationDirectory + "/app_props/" + environment + "/";
		arg.setLine("CreateApplicationArtifacts.py " + deployableArtifactsHome + " " + environment + " " + app_props);
		wsadminCommandLine.addArg(arg);
		executeCommand(wsadminCommandLine);
	}

	/**
	 * Adds or updates a WebSphere variable
	 */
	private final void addOrUpdateWebsphereVariable(final Commandline wsadminCommandLine, final String propertyType, final String propertyValue) {
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("SetEnvironmentVariableDmgr.py" + propertyType + propertyValue);
		wsadminCommandLine.addArg(arg);
		executeCommand(wsadminCommandLine);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Deploy artifact resources to WPS";
	}
}