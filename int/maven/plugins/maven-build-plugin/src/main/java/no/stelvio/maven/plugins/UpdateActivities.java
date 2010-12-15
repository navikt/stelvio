package no.stelvio.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which updates CQ activities
 *
 * @goal updateActivity
 * 
 * @author test@example.com
 */
public class UpdateActivities extends AbstractMojo{

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getLog().info("---------------------------");
    	this.getLog().info("--- Updating activities ---");
    	this.getLog().info("---------------------------");
		this.getLog().info("Updating activities");
	}

}
