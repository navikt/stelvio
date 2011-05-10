package no.stelvio.maven.plugins;

import no.stelvio.maven.build.plugin.utils.ApplicationNameResolve;
import no.stelvio.maven.build.plugin.utils.CleartoolCommandLine;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which delivers candidates from DEV to INT
 *
 * @goal editTriggers
 * 
 * @author test@example.com
 */
public class EditTriggers extends AbstractMojo{
	

	/**
	 * Project name - BUILD_TEST
	 * 
	 * @parameter expression="${build}"
	 * @required
	 */
	private String build;
	
	/**
	 * Action to perform with the trigger {remove,add}
	 * 
	 * @parameter expression="${action}" 
	 * @required
	 */
	private String action;
	
	/**
	 * Folder where all CC streams are located
	 * 
	 * @parameter expression="${ccProjectDir}"
	 * @required
	 */
	private String ccProjectDir;
	
	/**
	 * Integration stream tag
	 * 
	 * @parameter expression="${intStream}" default-value="_int"
	 */
	private String intStream;
	
	/**
	 * Whether this goal should be done
	 * @parameter expression="${perform_removeTrg}" default-value=true
	 */
	private boolean perform_removeTrg;
	
	/**
	 * Whether this goal should be done
	 * @parameter expression="${perform_addTrg}" default-value=true
	 */
	private boolean perform_addTrg;
	
	@Override
	public void execute() throws MojoExecutionException {
			if (action.equalsIgnoreCase("remove")) {
				if (perform_removeTrg)
					try {
						remove();
					} catch (MojoFailureException e) {
						this.getLog().warn("Cleartool could not find any triggers to remove. Proceding with build.");
					}
				else this.getLog().warn("Skipping remove triggers");
				
			}
			else if (action.equalsIgnoreCase("add")) {
				if (perform_addTrg)
					try {
						add();
					} catch (MojoFailureException e) {
						this.getLog().warn("Cleartool detected already existing trigger. Proceding with build.");
					}
				else this.getLog().warn("Skipping add WARN_UNRESERVED trigger");
			}	
	}
	
	private int remove() throws MojoFailureException{
		try {
			this.removeWARN_UNRESERVED();
		} catch (Exception e) {
			this.getLog().warn("Cleartool could not find any WARN_UNRESERVED to remove. Proceding with build.");
		}
		return this.removePLUGIN_FILES();
	}
	
	private int add() throws MojoFailureException{
		try {
			this.addWARN_UNRESERVED();
		} catch (Exception e) {
			this.getLog().warn("Cleartool detected already existing WARN_UNRESERVED trigger. Proceding with build.");
		}
		return this.addPLUGIN_FILES();
	}
	
	/**
	 * Remove trigger
	 * @throws MojoFailureException
	 */
	private int removeWARN_UNRESERVED() throws MojoFailureException{
		this.getLog().info("----------------------------------------");
		this.getLog().info("--- Removing WARN_UNRESERVED trigger ---");
		this.getLog().info("----------------------------------------");
		String workingDir = this.ccProjectDir+this.build+this.intStream;
		String subcommand = "rmtype -ignore trtype:WARN_UNRESERVED@\\" + ApplicationNameResolve.ApplicationFromProject(this.build);
		return CleartoolCommandLine.runClearToolCommand(workingDir, subcommand);
		//return 0;
	}
	
	/**
	 * Add trigger
	 * @throws MojoFailureException
	 */
	private int addWARN_UNRESERVED() throws MojoFailureException{
		this.getLog().info("--------------------------------------");
		this.getLog().info("--- Adding WARN_UNRESERVED trigger ---");
		this.getLog().info("--------------------------------------");
		String workingDir = this.ccProjectDir+this.build+this.intStream;
		String subcommand = "mktrtype -nc -element -all -preop checkout -execwin \"ccperl " +
				"\\\\e11apfw001\\ccstg_d\\triggers\\warn_unreserved.pl\" WARN_UNRESERVED@\\" + 
				ApplicationNameResolve.ApplicationFromProject(this.build);
		return CleartoolCommandLine.runClearToolCommand(workingDir, subcommand);
		//return 0;
	}
	
	/**
	 * Remove trigger
	 * @throws MojoFailureException
	 */
	private int removePLUGIN_FILES() throws MojoFailureException{
		this.getLog().info("-------------------------------------");
		this.getLog().info("--- Removing PLUGIN_FILES trigger ---");
		this.getLog().info("-------------------------------------");
		String workingDir = this.ccProjectDir+this.build+this.intStream;
		String subcommand = "rmtype -ignore trtype:Plugin_Files@\\" + ApplicationNameResolve.ApplicationFromProject(this.build);
		return CleartoolCommandLine.runClearToolCommand(workingDir, subcommand);
		//return 0;
	}
	
	/**
	 * Add trigger
	 * @throws MojoFailureException
	 */
	private int addPLUGIN_FILES() throws MojoFailureException{
		this.getLog().info("-----------------------------------");
		this.getLog().info("--- Adding PLUGIN_FILES trigger ---");
		this.getLog().info("-----------------------------------");
		String workingDir = this.ccProjectDir+this.build+this.intStream;
		String subcommand = "mktrtype -nc -element -all -preop checkout -execwin \"ccperl " +
				"\\\\e11apfw001\\ccstg_d\\triggers\\moose_deployment_files.pl\" Plugin_Files@\\" + 
				ApplicationNameResolve.ApplicationFromProject(this.build);
		return CleartoolCommandLine.runClearToolCommand(workingDir, subcommand);
		//return 0;
	}

}
