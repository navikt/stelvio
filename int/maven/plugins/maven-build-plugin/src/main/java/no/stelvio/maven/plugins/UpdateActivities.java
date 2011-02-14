package no.stelvio.maven.plugins;

import java.io.IOException;
import java.util.Properties;

import no.stelvio.maven.build.plugin.utils.CCCQRequest;
import no.stelvio.maven.build.plugin.utils.PropertiesFile;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which updates CQ activities
 *
 * @goal updateActivities
 * 
 * @author test@example.com
 */
public class UpdateActivities extends AbstractMojo{

	
	private String release_version;
	private String activities;
	
	/**
	 * Folder where all CC streams are located
	 * 
	 * @parameter expression="${ccProjectDir}"
	 * @required
	 */
	private String ccProjectDir;
	
	/**
	 * Project name - BUILD_TEST
	 * 
	 * @parameter expression="${build}"
	 * @required
	 */
	private String build;
	
	/**
	 * Whether this goal should be done
	 * @parameter expression="${update_activities}" default-value=true
	 */
	private boolean update_activities;
	
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (!update_activities) {
			this.getLog().warn("Skipping activities update");
			return;
		}
		this.getLog().info("---------------------------");
    	this.getLog().info("--- Updating activities ---");
    	this.getLog().info("---------------------------");
    	this.getProperties();    	
    	if (CCCQRequest.updateActivity(activities, build, release_version) != 0)
    		throw new MojoExecutionException("Unable to update activities");    	
	}
	
	/**
	 * This method retrieves activties' ids and released version from build.properties
	 */
	private void getProperties(){
		Properties props;
		try {
			props = PropertiesFile.getProperties(this.ccProjectDir, this.build);
			activities = props.getProperty("TO_DELIVER");
			release_version = props.getProperty("RELEASE"); 
		} catch (IOException e) {
			getLog().error(e.getLocalizedMessage());
		}
	}

}
