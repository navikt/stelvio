package no.nav.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which imports a DataPower configuration into a specified device.
 *
 * @goal importConfig
 * 
 */
public class ImportConfigMojo extends AbstractDataPowerMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		// TODO Auto-generated method stub
		getLog().info("Executing ImportConfigMojo, domain = " + getDomain());
	}
}
