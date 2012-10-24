package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * @author test@example.com
 * 
 * Goal that finds and parses the ${fileName}.xml to accumulate a String of rolemappings, 
 * and executes the Jython script. 
 * 
 * @goal apply-rolemapping
 * @requiresDependencyResolution
 */
public class ApplyRoleMappingMojo extends WebsphereUpdaterMojo {

	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {

		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}
		
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("RoleMapping.py " + authorizationConsXmlPath + "/" + envClass.toUpperCase() + ".xml");
		wsadminCommandLine.addArg(arg);

		executeCommand(wsadminCommandLine);
	}

	protected String getGoalPrettyPrint() {
		return "Apply user/group mapping and RunAs";
	}

}
