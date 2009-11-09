package no.nav.maven.plugin.websphere.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that calls another script.
 * 
 * @author test@example.com
 * 
 * @goal add-or-update-websphere-variable
 * @requiresDependencyResolution
 */


public class AddOrUpdateWebsphereVariable extends WebsphereUpdaterMojo {

	/**
	 * @parameter expression="${bus-configuration-version}"
	 * @required
	 */
	protected String busConfigurationVersion;

	/**
	 * @parameter expression="${esb-release-version}"
	 * @required
	 */
	protected String esbReleaseVersion;
	
	@Override
	public void applyToWebSphere(Commandline commandLine)
			throws MojoExecutionException, MojoFailureException {
		addOrUpdateWebsphereVariable(commandLine);

	}
	
	private final void addOrUpdateWebsphereVariable(final Commandline commandLine) {
		// Set bus configuration version		
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/setEnvironmentVariable.py" + " BUS_CONFIGURATION_VERSION " + busConfigurationVersion + " " + scriptsHome);
		commandLine.addArg(arg);
		executeCommand(commandLine);
		
		commandLine.clearArgs();
		
		// Set esb release version
		arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/setEnvironmentVariable.py" + " ESB_RELEASE_VERSION" + esbReleaseVersion);
		commandLine.addArg(arg);
		executeCommand(commandLine);
	}
	
	@Override
	protected String getGoalPrettyPrint() {
		return "Add or update WebSphere variables";
	}

}
