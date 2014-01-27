package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.ConfigurationException;
import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.HttpCode404Exception;
import no.nav.maven.plugin.wpsdeploy.plugin.utils.FasitUtil;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Goal that finds and parses the ${fileName}.xml to accumulate a String of rolemappings,
 * and executes the Jython script.
 *
 * @goal apply-fasit
 * @requiresDependencyResolution
 */
public class ApplyFasitMojo extends WebsphereUpdaterMojo {
	/**
	 * @parameter expression="${fasitUsername}"
	 */
	private String fasitUsername;

	/**
	 * @parameter expression="${fasitPassword}"
	 */
	private String fasitPassword;

	/**
	 * @parameter expression="${environment}"
	 */
	private String environmentLowerOrUpperCase;

	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		String deployInfoPropertiesPath = this.deployProperties;
		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream(deployInfoPropertiesPath));
			HashMap<String, String> dmgrResources = FasitUtil.getDmgrResources(environmentLowerOrUpperCase, fasitUsername, fasitPassword);
			HashMap<String, String> wsadminUser = FasitUtil.getWsadminUser(environmentLowerOrUpperCase, fasitUsername, fasitPassword);

			for (Map.Entry<String, String> entry : dmgrResources.entrySet()) {
				prop.put(entry.getKey(), entry.getValue());
			}
			for (Map.Entry<String, String> entry : wsadminUser.entrySet()) {
				prop.put(entry.getKey(), entry.getValue());
			}

			prop.store(new FileOutputStream(deployInfoPropertiesPath), null);
		} catch (IOException e) {
			throw new ConfigurationException("Could not read the properties file: " + deployInfoPropertiesPath);
		} catch (HttpCode404Exception e){
			getLog().info("Could not find the environment "+ environmentLowerOrUpperCase +" in Fasit. Continuing with the vairables unchanged from envConfig.");
		}
	}

	protected String getGoalPrettyPrint() {
		return "Apply fasit by downloading and replacing the vairables from the REST api";
	}

}
