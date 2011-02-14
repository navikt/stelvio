/**
 * 
 */
package no.stelvio.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import no.stelvio.maven.build.plugin.utils.ApplicationNameResolve;
import no.stelvio.maven.build.plugin.utils.CCCQRequest;
import no.stelvio.maven.build.plugin.utils.PomSearchUtil;
import no.stelvio.maven.build.plugin.utils.PropertiesFile;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * This mojo perfroms check in/out operations from ClearQuest for pom.xml files
 * 
 * @goal prepare-pom-update
 * @author test@example.com
 */
public class CCPomManipulator extends AbstractMojo {
	
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
	 * Development stream tag
	 * 
	 * @parameter expression="${devStream}" default-value="_Dev"
	 */
	private String devStream;
	
	/**
	 * Action to perform: setTask, checkout, checkin
	 * @parameter expression="${action}"
	 * @required
	 */
	private String action;
	
	/**
	 * Whether this goal should be done
	 * @parameter expression="${perform_increaseSS}" default-value=true
	 */
	private boolean perform;

	public static final String TASK_HEADLINE = "Edit poms in Dev stream after build: "; 
	public static final String TASK_DESCRIPTION = "\"activity to check out all pom.xml files\"";
	
	private ArrayList<File> poms;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (!perform){
			this.getLog().warn("Skipping set task, check in/out pom.xml files");
			return;
		}
		
		String workDir = this.ccProjectDir+this.build+this.devStream+"/"+ApplicationNameResolve.ApplicationFromProject(build.toUpperCase())+"/layers";
		poms = PomSearchUtil.SearchForPoms(workDir);
		boolean fail = false;
		if (action.equalsIgnoreCase("setTask")){
			String build_tag = this.build + "_" + getReleaseVersion();
			fail = this.setCQTask("\"" + TASK_HEADLINE+build_tag+"\"", TASK_DESCRIPTION) != 0;
			if (fail) throw new MojoExecutionException("Unable to create a CQ task.");
		}else if (action.equalsIgnoreCase("checkout")){
			fail = this.doCheckOut() != 0;
			if (fail) throw new MojoExecutionException("Unable to perform checkout. Some files might have been checked out.");
		}else if (action.equalsIgnoreCase("checkin")){
			fail = this.doCheckIn() != 0 && this.clearView() != 0;
			if (fail) throw new MojoExecutionException("Unable to perform checkin. Some files might have been checked in.");
		}
	}
	
	private String getReleaseVersion() {
		try {
			return PropertiesFile.getProperties(this.ccProjectDir, this.build).getProperty("RELEASE");
		} catch (IOException e) {
			getLog().error(e.getLocalizedMessage());
		}
		return null;
	}
	
	private int setCQTask(String headline, String description) throws MojoFailureException{
		this.getLog().info("-----------------------------------------------");
		this.getLog().info("--- Creating a task to perform check in/out ---");
		this.getLog().info("-----------------------------------------------");
		
		return CCCQRequest.setActivity(
				this.ccProjectDir+this.build+this.devStream, 
				CCCQRequest.createActivity(headline, description));
		//return 0;
	}
	
	private int clearView() throws MojoFailureException{
		this.getLog().info("--------------------------------------");
		this.getLog().info("--- Clear Dev view from activities ---");
		this.getLog().info("--------------------------------------");
		return CCCQRequest.unsetActivity(this.ccProjectDir+this.build+this.devStream);
	}
	
	private int doCheckOut() throws MojoFailureException{
		this.getLog().info("-----------------------------");
		this.getLog().info("--- Cheking out pom files ---");
		this.getLog().info("-----------------------------");
		int result = 0;
		getLog().info("Total amount of pom files: "+poms.size());
		for (File pom : poms) getLog().info(pom.getAbsolutePath());
		for (File pom : poms) result += CCCQRequest.checkOutFile(pom);
		return result;
	}
	
	private int doCheckIn() throws MojoFailureException{
		this.getLog().info("-----------------------------");
		this.getLog().info("--- Checking in pom files ---");
		this.getLog().info("-----------------------------");
		int result = 0;
		getLog().info("Total amount of pom files: "+poms.size());
		for (File pom : poms) getLog().info(pom.getAbsolutePath());
		for (File pom : poms) result += CCCQRequest.checkInFile(pom);
		return result;
	}

}
