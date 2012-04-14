package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.FileNotFoundException;
import java.io.IOException;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.PropertyUtils;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that loads the environment configuration
 * 
 * @author test@example.com
 * 
 * @goal load-configuration
 * @requiresDependencyResolution
 */
public class LoadEnvironmentConfigurationMojo extends WebsphereUpdaterMojo {

	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		try {
			exposeEnvironmentProperties();
		} catch (IOException e) {
			throw new MojoFailureException(e.getMessage() + "\n[INFO]\n[INFO] Tip: Make sure you have extracted the bus configuration.");
		}
	}

	// Get the properties from the environment file
	protected void exposeEnvironmentProperties() throws FileNotFoundException, IOException {
		
		String tmpEnvironmentFile = busConfigurationDirectory + "/environments/" + environment + ".properties";

		PropertyUtils pf = new PropertyUtils(tmpEnvironmentFile, project);

		pf.exposeProperty("envClass", false);
		pf.exposeProperty("dmgrUsername", false);
		pf.exposeProperty("dmgrPassword", true);
		pf.exposeProperty("dmgrHostname", false);
		pf.exposeProperty("dmgrSOAPPort", false);
		pf.exposeProperty("linuxUser", false);
		pf.exposeProperty("linuxPassword", true);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Load environment configuration";
	}

}
