/**
 * 
 */
package no.stelvio.maven.plugins;

import no.stelvio.maven.build.plugin.utils.CleartoolCommandLine;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal to perform undoDelivery operation on a given stream
 * 
 * @goal undoDelivery
 * 
 * @author test@example.com
 */
public class UndoDelivery extends AbstractMojo{
	
	/**
	 * Project name - BUILD_TEST
	 * 
	 * @parameter expression="${build}"
	 * @required
	 */
	private String build;
	
	/**
	 * Folder where all CC streams are located
	 * 
	 * @parameter expression="${ccProjectDir}"
	 * @required
	 */
	private String ccProjectDir;
	
	/**
	 * Development stream tag
	 * 
	 * @parameter expression="${devStream}" default-value="_Dev"
	 */
	private String devStream;
	
	/**
	 * Whether this goal should be done
	 * @parameter expression="${perform_undeliver}" default-value=true
	 */
	private boolean perform;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (!perform) {
			this.getLog().warn("Skipping undo delivery");
			return;
		}
		this.getLog().info("---------------------");
		this.getLog().info("--- Undo delivery ---");
		this.getLog().info("---------------------");
		String workingDir = this.ccProjectDir+this.build+this.devStream;
		String subcommand = "deliver -cancel -force";
//		if (CleartoolCommandLine.runClearToolCommand(workingDir, subcommand) != 0) 
//			throw new MojoExecutionException("Unable to undo delivery");
	}

}
