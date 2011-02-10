package no.stelvio.maven.plugins;

import no.stelvio.maven.build.plugin.utils.ApplicationNameResolve;
import no.stelvio.maven.build.plugin.utils.CleartoolCommandLine;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which delivers candidates from DEV to INT
 *
 * @goal editWarnUnreserved
 * 
 * @author test@example.com
 */
public class WarnUnreserved extends AbstractMojo{
	

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
	public void execute() throws MojoExecutionException, MojoFailureException {
		boolean fail = false;
		if (action.equalsIgnoreCase("remove")) {
			if (perform_removeTrg) fail = remove() != 0;
			else this.getLog().warn("Skipping remove WARN_UNRESERVED trigger");
			if (fail) this.getLog().warn("Cleartool could not find any triggers to remove. Proceding with build.");
		}
		else if (action.equalsIgnoreCase("add")) {
			if (perform_addTrg)	fail = add() != 0;
			else this.getLog().warn("Skipping add WARN_UNRESERVED trigger");
			if (fail) this.getLog().warn("Cleartool detected already existing trigger. Proceding with build.");
		}		
	}
	
	/**
	 * Remove trigger
	 * @throws MojoFailureException
	 */
	private int remove() throws MojoFailureException{
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
	private int add() throws MojoFailureException{
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

}
