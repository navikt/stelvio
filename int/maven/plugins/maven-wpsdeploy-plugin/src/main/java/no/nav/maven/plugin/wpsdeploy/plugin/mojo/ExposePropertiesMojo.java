package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.IOException;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.PropertyUtils;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that loads the environment configuration
 * 
 * @goal expose-properties
 * @requiresDependencyResolution
 */
public class ExposePropertiesMojo extends WebsphereUpdaterMojo {

	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		String path = deployProperties;
		getLog().info("Loading deploy properties from "+ path);
		try {
			PropertyUtils pf = new PropertyUtils(project);
			pf.loadFile(path);

			pf.exposeProperty("envClass", false, true);
			pf.exposeProperty("envName", false, true);
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
