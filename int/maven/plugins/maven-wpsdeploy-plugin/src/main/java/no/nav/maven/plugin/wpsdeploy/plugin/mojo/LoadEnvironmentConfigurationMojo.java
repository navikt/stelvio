package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;
import java.io.IOException;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.PropertyUtils;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that loads the environment configuration
 * 
 * @goal load-configuration
 * @requiresDependencyResolution
 */
public class LoadEnvironmentConfigurationMojo extends WebsphereUpdaterMojo {

	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		try {
			File tmpEnvironmentPath = new File(environmentPropertiesPath);
			PropertyUtils pf = new PropertyUtils(project);
			for(String propFile : tmpEnvironmentPath.list()){
				pf.loadFile(tmpEnvironmentPath.getAbsolutePath() + "/" + propFile);
			}

			pf.exposeProperty("envClass", false);
			pf.exposeProperty("dmgrUsername", false);
			pf.exposeProperty("dmgrPassword", true);
			pf.exposeProperty("dmgrHostname", false);
			pf.exposeProperty("dmgrSOAPPort", false);
			pf.exposeProperty("linuxUser", false);
			pf.exposeProperty("linuxPassword", true);
		} catch (IOException e) {
			throw new MojoFailureException(e.getMessage() + "\n[INFO]\n[INFO] Tip: Make sure you have extracted the bus configuration.");
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Load environment configuration";
	}

}
