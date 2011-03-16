package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.FileNotFoundException;
import java.io.IOException;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.PropertyUtils;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
* Abstract class using the template pattern for child mojos.
* 
* @author test@example.com 
*/
public abstract class RemoteCommandExecutorMojo extends WebsphereMojo {
    
	/**
	 * @parameter expression="${environment}"
	 * @required
	 */
	protected String environment;
	
	protected abstract void executeRemoteCommand() throws MojoExecutionException, MojoFailureException;
	
	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
		
		String tmpEnvironmentFile = baseDirectory + tmpBusConfigurationExtractDirectory + "/environments/" + environment + ".properties";
		
		try {
			PropertyUtils pf = new PropertyUtils(tmpEnvironmentFile, project);
			pf.exposeProperty("envClass", pf.getProperty("envClass"), false);
			
		} catch (FileNotFoundException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		} catch (IOException e) {
			throw new MojoExecutionException("[ERROR]: " + e);
		}
		
		executeRemoteCommand();
	}
}	