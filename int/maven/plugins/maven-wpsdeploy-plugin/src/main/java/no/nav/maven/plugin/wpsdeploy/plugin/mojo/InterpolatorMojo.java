package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.FileNotFoundException;
import java.io.IOException;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.ConfigurationException;
import no.nav.maven.plugin.wpsdeploy.plugin.utils.MojoLauncher;
import no.nav.maven.plugin.wpsdeploy.plugin.utils.PropertyUtils;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * @author test@example.com
 * 
 * Executes the 'generate' goal on the maven-propertiesgenerator-plugin
 * 
 * @goal interpolate-config
 * @requiresDependencyResolution
 */
public class InterpolatorMojo extends WebsphereUpdaterMojo {

	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		
		String tmpEnvironmentFile = tmpBusConfigurationExtractDirectory + "/environments/" + environment + ".properties";
		
		try {
			PropertyUtils pf = new PropertyUtils(tmpEnvironmentFile, project);
			if(pf.getProperty("envClass") == null){
				throw new ConfigurationException("The envClass property can't be null!");
			}
			pf.exposeProperty("envClass", false);
			
		} catch (FileNotFoundException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (IOException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		}
		
		MojoLauncher.executePropertiesGeneratorMojo(project, session, pluginManager);
	}

	protected String getGoalPrettyPrint() {
		return "Interpolate configuration";
	}
}
