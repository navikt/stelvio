/**
 * 
 */
package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * @author utvikler
 *
 * @goal apply-usernametokendetails
 * @requiresDependencyResolution
 */
public class ApplyUsernameTokenDetailsMojo extends WebsphereUpdaterMojo {

	/* (non-Javadoc)
	 * @see no.nav.maven.plugin.wpsdeploy.plugin.mojo.WebsphereUpdaterMojo#applyToWebSphere(org.codehaus.plexus.util.cli.Commandline)
	 */
	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("ModifyUserNameTokenGenerator.py " + policySetBindings);
		wsadminCommandLine.addArg(arg);
		executeCommand(wsadminCommandLine);
	}
		

	/* (non-Javadoc)
	 * @see no.nav.maven.plugin.wpsdeploy.plugin.mojo.WebsphereMojo#getGoalPrettyPrint()
	 */
	@Override
	protected String getGoalPrettyPrint() {
		return "Apply username token details";
	}

}
