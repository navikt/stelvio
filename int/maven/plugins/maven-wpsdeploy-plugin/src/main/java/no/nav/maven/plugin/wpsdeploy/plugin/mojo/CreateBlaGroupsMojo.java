package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that sets environment variables on DMGR
 * 
 * 
 * @goal create-BLA-groups
 * @requiresDependencyResolution
 */
public class CreateBlaGroupsMojo extends WebsphereUpdaterMojo {

	public final void applyToWebSphere(final Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		if (!isConfigurationLoaded()) {
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}
				
		Commandline.Argument arg = new Commandline.Argument();;
		arg.setLine("CreateBLAGroups.py " + blaGroupsPath);
		wsadminCommandLine.addArg(arg);
		executeCommand(wsadminCommandLine);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Create all the BLA groups";
	}
}