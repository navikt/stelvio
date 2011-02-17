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
public class LoadEnvironmentConfiguration extends WebsphereUpdaterMojo {

	@Override
	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		try {
			exposeEnvironmentProperties();
		} catch (IOException e) {
			throw new MojoFailureException(e.getMessage() + "\n[INFO]\n[INFO] Tip: Make sure you have extracted the bus configuration.");
		}
	}

	// Get the properties from the environment file
	protected void exposeEnvironmentProperties() throws FileNotFoundException, IOException {
		PropertyUtils pf = new PropertyUtils(environmentFile, project);

		pf.exposeProperty("envClass", pf.getProperty("envClass"), false);
		pf.exposeProperty("dmgrUsername", pf.getProperty("dmgrUsername"), false);
		pf.exposeProperty("dmgrPassword", pf.getProperty("dmgrPassword"), true);
		pf.exposeProperty("dmgrHostname", pf.getProperty("dmgrHostname"), false);
		pf.exposeProperty("dmgrSOAPPort", pf.getProperty("dmgrSOAPPort"), false);
		pf.exposeProperty("linuxUser",pf.getProperty("linuxUser"), false);
		pf.exposeProperty("linuxPassword",pf.getProperty("linuxPassword"), true);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Load environment configuration";
	}

}
