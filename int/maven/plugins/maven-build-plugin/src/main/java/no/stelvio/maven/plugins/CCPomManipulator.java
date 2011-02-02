/**
 * 
 */
package no.stelvio.maven.plugins;

import java.io.File;
import java.util.ArrayList;

import no.stelvio.maven.build.plugin.utils.ApplicationNameResolve;
import no.stelvio.maven.build.plugin.utils.CCCQRequest;
import no.stelvio.maven.build.plugin.utils.PomSearchUtil;

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
	 * @parameter expression="${scriptFolder}"
	 */
	private String scriptFolder;
	
	/**
	 * Action to perform: setTask, checkout, checkin
	 * @parameter expression="${action}"
	 * @required
	 */
	private String action;
	
	/**
	 * Version to be released
	 * @parameter expression="${versionNumber}"
	 * @required
	 */
	private String release_version;
	
	/**
	 * Whether this goal should be done
	 * @parameter expression="${perform_increaseSS}" default-value=true
	 */
	private boolean perform;


	
	private ArrayList<File> poms;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (!perform){
			this.getLog().warn("Skipping set task, check in/out pom.xml files");
			return;
		}
		
		String workDir = this.ccProjectDir+this.build+this.devStream+"/"+ApplicationNameResolve.ApplicationFromProject(build)+"/layers";
		poms = PomSearchUtil.SearchForPoms(workDir);
		boolean fail = false;
		if (action.equalsIgnoreCase("setTask")){
			String build_tag = this.build + " version: " + this.release_version;
			fail = this.setCQTask("Edit poms in Dev stream for build: "+build_tag, "activity to check out all pom.xml files") != 0;
			if (fail) throw new MojoExecutionException("Unable to create a CQ task.");
		}else if (action.equalsIgnoreCase("checkout")){
			fail = this.doCheckOut() != 0;
			if (fail) throw new MojoExecutionException("Unable to perform checkout. Some files might have been checked out.");
		}else if (action.equalsIgnoreCase("checkin")){
			fail = this.doCheckIn() != 0;
			if (fail) throw new MojoExecutionException("Unable to perform checkin. Some files might have been checked in.");
		}
	}
	
	private int setCQTask(String headline, String description) throws MojoFailureException{
		this.getLog().info("-----------------------------------------------");
		this.getLog().info("--- Creating a task to perform check in/out ---");
		this.getLog().info("-----------------------------------------------");
		if (!this.scriptFolder.endsWith("/")) this.scriptFolder += "/";
		
//		return CCCQRequest.setActivity(
//				this.ccProjectDir+this.build+this.devStream, 
//				CCCQRequest.createActivity(
//						this.scriptFolder, headline, description)
//				);
		return 0;
	}
	
	private int doCheckOut() throws MojoFailureException{
		this.getLog().info("-----------------------------");
		this.getLog().info("--- Cheking out pom files ---");
		this.getLog().info("-----------------------------");
		int result = 0;
		for (File pom : poms){
			//result += CCCQRequest.checkOutFile(pom);
		}
		return result;
	}
	
	private int doCheckIn() throws MojoFailureException{
		this.getLog().info("-----------------------------");
		this.getLog().info("--- Checking in pom files ---");
		this.getLog().info("-----------------------------");
		int result = 0;
		for (File pom : poms){
			//result += CCCQRequest.checkInFile(pom);
		}
		return result;
	}

}
