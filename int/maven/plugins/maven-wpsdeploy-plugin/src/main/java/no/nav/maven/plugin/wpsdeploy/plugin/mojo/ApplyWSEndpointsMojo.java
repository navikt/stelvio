package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * 
 * @goal apply-wsendpoints
 * @requiresDependencyResolution
 */
public class ApplyWSEndpointsMojo extends WebsphereUpdaterMojo {

	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {

		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}
		
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("ModifySCAImportsBinding.py " + moduleConfigPath);
		wsadminCommandLine.addArg(arg);
		executeCommand(wsadminCommandLine);
	}

	protected String getGoalPrettyPrint() {
		return "Apply webservice endpoints";
	}

}
