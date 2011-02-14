package no.stelvio.maven.plugins;

import java.io.IOException;
import java.util.Properties;

import no.stelvio.maven.build.plugin.utils.ApplicationNameResolve;
import no.stelvio.maven.build.plugin.utils.PropertiesFile;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * @goal prepare-config-verify
 * @author utvikler
 */
public class ConfigVerificationPreparation extends AbstractMojo {

	/**
	 * Project name - BUILD_TEST
	 * 
	 * @parameter expression="${build}"
	 * @required
	 */
	private String build;
	
	/**
	 * @parameter expression="${project}"
	 * @required
	 */
	private MavenProject project;
	
	/**
	 * Folder where all CC streams are located
	 * 
	 * @parameter expression="${ccProjectDir}"
	 * @required
	 */
	private String ccProjectDir;
	
	private String application;
	private String applicationVersion;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getProperties();
		project.getProperties().put("app", application.toLowerCase());
		project.getProperties().put("version", applicationVersion.toLowerCase());
		this.getLog().info("-------------------------");
    	this.getLog().info("--- Exposed to maven: ---");
    	this.getLog().info("app: "+ application);
    	this.getLog().info("version: "+ applicationVersion);
    	this.getLog().info("-------------------------");
	}
	
	private void getProperties(){
		this.getLog().info("--------------------------------------------");
    	this.getLog().info("--- Reading verion from build.properties ---");
    	this.getLog().info("--------------------------------------------");
		Properties props;
		try {
			props = PropertiesFile.getProperties(this.ccProjectDir, this.build);
			application = ApplicationNameResolve.ApplicationFromProject(build);
			applicationVersion = props.getProperty("RELEASE"); 
		} catch (IOException e) {
			getLog().error(e.getLocalizedMessage());
		}
	}

}
