package no.nav.maven.plugin.websphere.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that calls another script.
 * 
 * @author test@example.com
 * 
 * @goal update-bus-configuration-version
 * @requiresDependencyResolution
 */


public class UpdateBusConfigurationVersion extends WebsphereUpdaterMojo {

	/**
	 * @parameter expression="${bus-configuration-version}"
	 * @required
	 */
	protected String busConfigurationVersion;

	@Override
	public void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		addOrUpdateWebsphereVariable(commandLine);
	}
	
	private final void addOrUpdateWebsphereVariable(final Commandline commandLine) {
		// Set bus configuration version		
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/setEnvironmentVariable.py" + " BUS_CONFIGURATION_VERSION " + busConfigurationVersion + " " + scriptsHome + " " + environment);
		commandLine.addArg(arg);
		executeCommand(commandLine);	
	}
	
	@Override
	protected String getGoalPrettyPrint() {
		return "Update BUS Configuration version WebSphere variables";
	}

}
