package no.stelvio.maven.plugins;

import java.util.ArrayList;

import no.stelvio.maven.build.plugin.utils.CCCQRequest;
import no.stelvio.maven.build.plugin.utils.CleartoolCommandLine;
import no.stelvio.maven.build.plugin.utils.CommandLineUtil;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

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
	 * @parameter expression="${project}"
	 * @required
	 */
	private String project;
	
	/**
	 * Step of delivery: I or II
	 * 
	 * @parameter expression="${step}" 
	 * @required
	 */
	private int step;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		boolean fail = false;
		
		if (step == 1){
			fail = deliverI() != 0;
			// TODO consider to run undo delivery here if failed
			// and maybe rerun this goal
			/** 
			 * AGREED TO RELEASE WITHOUT RERUN, BUT 
			 * MONITOR BEHAVIOUR AND FIX LATER IF NEEDED
			 */
		}else if (step == 2){
			fail = deliverII() != 0;
		}
		if (fail) throw new MojoExecutionException("Unable to perform delivery");
	}
	
	/**
	 * Deliver I
	 * @throws MojoFailureException
	 */
	private int deliverI() throws MojoFailureException{
		this.getLog().info("-----------------------------");
		this.getLog().info("--- Performing delivery I ---");
		this.getLog().info("-----------------------------");
		String workingDir = "D:/cc/"+this.project+"_Dev";
		ArrayList<String> activities = (ArrayList<String>) CCCQRequest.getActivitiesToDeliver(this.project);
		StringBuffer subcommand = new StringBuffer("deliver -to srvmooseadmin_" + this.project + "_int"+" -force -act " + this.getActIDString(activities));
		return CleartoolCommandLine.runClearToolCommand(workingDir, subcommand.toString());
	}
	
	private String getActIDString(ArrayList<String> activities){
		StringBuffer result = new StringBuffer();
		for (int i=0; i<activities.size();i++){
			result.append(activities.get(i)); 
			if (i<activities.size()) result.append(',');
		}
		return result.toString();
	}
	
	/**
	 * Deliver II
	 * @throws MojoFailureException
	 */
	private int deliverII() throws MojoFailureException{
		this.getLog().info("------------------------------");
		this.getLog().info("--- Performing delivery II ---");
		this.getLog().info("------------------------------");
		String workingDir = "D:/cc/"+this.project+"_Dev";
		String subcommand = "deliver -complete -force";
		return CleartoolCommandLine.runClearToolCommand(workingDir, subcommand);
	}

}
