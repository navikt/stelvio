package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
* Abstract class using the template pattern for child mojos.
* 
* @author test@example.com 
*/
public abstract class RemoteCommandExecutorMojo extends WebsphereMojo {
    
	/**
	 * @parameter expression="${host}"
	 * @required
	 */
	protected String deploymentManagerHost;
	
	protected abstract void executeRemoteCommand() throws MojoExecutionException, MojoFailureException;
	
	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
		executeRemoteCommand();
	}
}	