package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that calls another script.
 * 
 * @author test@example.com
 * 
 * @goal modify-max-failed-deliveries
 * @requiresDependencyResolution
 */
public class ModifyMaxFailedDeliveriesMojo extends WebsphereUpdaterMojo {

	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		modifyMaxFailedDeliveries(commandLine);
	}

	private final void modifyMaxFailedDeliveries(final Commandline commandLine) {
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/ModifyMaxFailedDeliveries.py");
		commandLine.addArg(arg);
		executeCommand(commandLine);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Modify max failed deliveries";
	}
}