package no.nav.maven.plugin.websphere.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that calls another script.
 * 
 * @author test@example.com
 * 
 * @goal update-esb-release-version
 * @requiresDependencyResolution
 */

public class UpdateESBReleaseVersion extends WebsphereUpdaterMojo {

	/**
	 * @parameter expression="${esb-release-version}"
	 * @required
	 */
	protected String esbReleaseVersion;
	
	@Override
	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		// Set esb release version
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/setEnvironmentVariable.py" + " ESB_RELEASE_VERSION " + esbReleaseVersion + " " + scriptsHome);
		commandLine.addArg(arg);
		executeCommand(commandLine);
	}

	@Override
	protected String getGoalPrettyPrint() {
		// TODO Auto-generated method stub
		return "Update ESB Release version WebSphere variables";
	}

}
