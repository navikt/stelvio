package no.nav.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * Goal which deletes all files from the specified locations in the specified domain
 * 
 * @goal cleanFiles
 * 
 * @author utvikler
 *
 */
public class CleanFilesMojo extends AbstractDeviceMgmtMojo {
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		// TODO Auto-generated method stub
		getLog().info("Executing CleanFilesMojo");
		getLog().info("DataPower deviceUrl = " + getDeviceUrl());
		getLog().info("DataPower domain = " + getDomain());
	}
}
