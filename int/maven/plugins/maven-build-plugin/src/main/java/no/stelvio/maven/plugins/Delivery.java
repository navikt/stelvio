package no.stelvio.maven.plugins;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import no.stelvio.maven.build.plugin.utils.CCCQRequest;
import no.stelvio.maven.build.plugin.utils.CleartoolCommandLine;
import no.stelvio.maven.build.plugin.utils.PropertiesFile;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which delivers candidates from DEV to INT
 *
 * @goal deliverCandidates
 * 
 * @author test@example.com
 */
public class Delivery extends AbstractMojo{

	/**
	 * Project name - BUILD_TEST
	 * 
	 * @parameter expression="${build}"
	 * @required
	 */
	private String build;
	
	/**
	 * Step of delivery: I or II
	 * 
	 * @parameter expression="${step}" 
	 * @required
	 */
	private int step;

	/**
	 * Folder where all CC streams are located
	 * 
	 * @parameter expression="${ccProjectDir}"
	 * @required
	 */
	private String ccProjectDir;
	
	/**
	 * Activities to be included in the build.
	 * If "all" - everything will be included. Otherwise just those activities
	 * that are defined in this string
	 * <p>N.B. if this string contains unrelated activities, build fails</p>
	 * 
	 * @parameter expression="${activities}" default-value="all"
	 */
	private String activityList;
	
	/**
	 * Development stream tag
	 * 
	 * @parameter expression="${devStream}" default-value="_Dev"
	 */
	private String devStream;
	
	/**
	 * Integration stream tag
	 * 
	 * @parameter expression="${intStream}" default-value="_int"
	 */
	private String intStream;
	
	/**
	 * Whether this goal should be done
	 * @parameter expression="${perform_delivery_1}" default-value=true
	 */
	private boolean perform_delivery_1;
	
	/**
	 * Whether this goal should be done
	 * @parameter expression="${perform_delivery_2}" default-value=true
	 */
	private boolean perform_delivery_2;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		boolean fail = false;
		
		if (step == 1){
			if (perform_delivery_1)	fail = deliverI() != 0;
			else this.getLog().warn("Skipping delivery I"); //$NON-NLS-1$
			// TODO consider to run undo delivery here if failed
			// and maybe rerun this goal
			/** 
			 * AGREED TO RELEASE WITHOUT RERUN, BUT 
			 * MONITOR BEHAVIOUR AND FIX LATER IF NEEDED
			 */
		}else if (step == 2){
			if (perform_delivery_2)	fail = deliverII() != 0;
			else this.getLog().warn("Skipping delivery II"); //$NON-NLS-1$
		}
		if (fail) throw new MojoExecutionException("Unable to perform delivery"); //$NON-NLS-1$
	}
	
	/**
	 * Deliver I
	 * @throws MojoFailureException
	 * @throws  
	 */
	private int deliverI() throws MojoFailureException {
		this.getLog().info("-----------------------------"); //$NON-NLS-1$
		this.getLog().info("--- Performing delivery I ---"); //$NON-NLS-1$
		this.getLog().info("-----------------------------"); //$NON-NLS-1$
		String workingDir = this.ccProjectDir+this.build+this.devStream;
		if (activityList.equalsIgnoreCase("none") || activityList == null || activityList.equalsIgnoreCase("")) activityList = null; //$NON-NLS-1$ //$NON-NLS-2$
		ArrayList<String> activities = (ArrayList<String>) CCCQRequest.getActivitiesToDeliver(this.ccProjectDir, this.build+this.devStream, activityList);
		if (activities == null || activities.isEmpty())	{
			getLog().info("Nothing to deliver"); //$NON-NLS-1$
			return 1;
		}
		StringBuffer subcommand = new StringBuffer(Messages.getString("Delivery.9") + this.build + this.intStream+" -force " + "-act " + this.getActIDString(activities)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		try {
			this.saveActivitiesToFile(this.getActIDString(activities));
		} catch (IOException e) {
			getLog().error(e.getLocalizedMessage());
			throw new MojoFailureException("Could not create build.properties file"); //$NON-NLS-1$
		}
		return CleartoolCommandLine.runClearToolCommand(workingDir, subcommand.toString());
		//return 0;
	}
	
	private void saveActivitiesToFile(String activities) throws IOException{ 
		Properties properties = new Properties();
		properties.setProperty(Messages.getString("Delivery.13"), activities); //$NON-NLS-1$
		PropertiesFile.setProperties(this.ccProjectDir, this.build, properties);
	}
	
	/**
	 * This method extracts IDs from array and puts them in a comma separated string
	 * @param activities - list with IDs
	 * @return string NAV00xxxxxx,NAV00xxxxxx,...
	 */
	private String getActIDString(ArrayList<String> activities){
		StringBuffer result = new StringBuffer();
		for (int i=0; i<activities.size();i++){
			result.append(activities.get(i)); 
			if (i<activities.size()-1) result.append(',');
		}
		return result.toString();
	}
	
	/**
	 * Deliver II
	 * @throws MojoFailureException
	 */
	private int deliverII() throws MojoFailureException{
		this.getLog().info("------------------------------"); //$NON-NLS-1$
		this.getLog().info("--- Performing delivery II ---"); //$NON-NLS-1$
		this.getLog().info("------------------------------"); //$NON-NLS-1$
		String workingDir = this.ccProjectDir+this.build+this.devStream;
		String subcommand = "deliver -complete -force"; //$NON-NLS-1$
		return CleartoolCommandLine.runClearToolCommand(workingDir, subcommand);
		//return 0;
	}

}
