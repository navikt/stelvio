/**
 * 
 */
package no.stelvio.maven.plugins;

import no.stelvio.maven.build.plugin.utils.CleartoolCommandLine;
import no.stelvio.maven.build.plugin.utils.CommandLineUtil;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

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
	 * @parameter expression="${project}"
	 * @required
	 */
	private String project;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getLog().info("---------------------");
		this.getLog().info("--- Undo delivery ---");
		this.getLog().info("---------------------");
		String workingDir = "D:/cc/"+this.project+"_Dev";
		String subcommand = "deliver -cancel -force";
		if (CleartoolCommandLine.runClearToolCommand(workingDir, subcommand) != 0) 
			throw new MojoExecutionException("Unable to undo delivery");
	}

}
