package no.nav.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which imports a DataPower configuration into a specified device.
 *
 * @goal importFiles
 * 
 */
public class ImportFilesMojo extends AbstractDataPowerMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		// TODO Auto-generated method stub
		getLog().info("Executing ImportFilesMojo, domain = " + getDomain());
	}
}
