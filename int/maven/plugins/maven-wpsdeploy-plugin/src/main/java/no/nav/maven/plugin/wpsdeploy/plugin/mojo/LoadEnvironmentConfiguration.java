package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.FileNotFoundException;
import java.io.IOException;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.MySOAPException;
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

		exposeProperty(pf, "envClass", false);
		exposeProperty(pf, "dmgrUsername", false);
		exposeProperty(pf, "dmgrPassword", true);
		exposeProperty(pf, "dmgrHostname", false);
		exposeProperty(pf, "dmgrSOAPPort", false);
		exposeProperty(pf, "linuxUser", false);
		exposeProperty(pf, "linuxPassword", true);
	}
	
	private void exposeProperty(PropertyUtils pf, String name, boolean password) throws FileNotFoundException, IOException{
		if(pf.getProperty(name) == null){
			throw new MySOAPException("The "+ name +" property can't be \"null\"!");
		} 
		else if (pf.getProperty(name).contains("$")){
			throw new MySOAPException("The "+ name +" property can't contain \"$\"!\nWhen this happens it is most likely that there is a property file that can't be found.");
		}
		pf.exposeProperty(name, pf.getProperty(name), password);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Load environment configuration";
	}

}
