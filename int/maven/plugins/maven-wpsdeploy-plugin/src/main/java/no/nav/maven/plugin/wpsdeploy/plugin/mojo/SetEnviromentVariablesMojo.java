package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.util.ArrayList;
import java.util.List;

import no.nav.maven.plugin.wpsdeploy.plugin.models.DeployArtifact;
import no.nav.maven.plugin.wpsdeploy.plugin.utils.ArgumentUtil;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that sets environment variables on DMGR
 * 
 * 
 * @goal set-environment-variables
 * @requiresDependencyResolution
 */
public class SetEnviromentVariablesMojo extends WebsphereUpdaterMojo {

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

		
		List<String> environmentVariables = new ArrayList<String>();
		for (DeployArtifact da : artifacts) {
			if (da.getVersion() == null || da.getVersion().startsWith("$")) {
				getLog().info("Skipping " + da.getGroupId() + ":" + da.getArtifactId() + ", no version specified.");
				continue;
			}

			environmentVariables.add(da.getVariableName() + "=" + da.getVersion());
		}
		environmentVariables.add("BUS_CONFIGURATION_VERSION="+busConfigurationVersion);
				
		Commandline.Argument arg = new Commandline.Argument();
		String environmentVariablesCSV = ArgumentUtil.listToDelimitedString(environmentVariables, ",");
		arg.setLine("SetEnvironmentVariableDmgr.py " + environmentVariablesCSV);
		wsadminCommandLine.addArg(arg);
		executeCommand(wsadminCommandLine);

	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Set enviroment variables on DMGR";
	}
}