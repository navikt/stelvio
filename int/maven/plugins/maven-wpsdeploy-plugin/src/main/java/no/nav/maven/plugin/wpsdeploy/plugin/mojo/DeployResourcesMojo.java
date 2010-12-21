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
	
	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		
		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}
		
		String[] orgArgs = commandLine.getArguments();
		
		//Create new Commandline objects
		Commandline cmdline2 = new Commandline();
		Commandline cmdline3 = new Commandline();
		
		cmdline2.setExecutable(commandLine.getExecutable());
		cmdline2.addArguments(orgArgs);

		cmdline3.setExecutable(commandLine.getExecutable());
		cmdline3.addArguments(orgArgs);
		
		deployResources(commandLine);
		
		// reintroduce this when we know what the different module poms are called. /Johnny
//		addOrUpdateWebsphereVariable(cmdline2, " ESB_RELEASE_VERSION ", esbReleaseVersion);
		addOrUpdateWebsphereVariable(cmdline3, " BUS_CONFIGURATION_VERSION ", busConfigurationVersion);
	}

	private final void deployResources(final Commandline commandLine) {
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/CreateApplicationArtifacts.py " + deployableArtifactsHome + " "
				+ environment + " " + scriptsHome);
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