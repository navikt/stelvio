package no.stelvio.maven.plugins;

import no.stelvio.maven.build.plugin.utils.CleartoolCommandLine;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which makes baselines.
 *
 * @goal baseline
 * 
 * @author test@example.com
 */
public class Baseline extends AbstractMojo{

	/**
	 * Project name - BUILD_TEST
	 * 
	 * @parameter expression="${project}"
	 * @required
	 */
	private String project;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getLog().info("-------------------------");
		this.getLog().info("--- Creating baseline ---");
		this.getLog().info("-------------------------");
		
		String workingDir = "D:/cc/"+this.project+"_int";
		String subcommand = "-mkbl -force";
		if (CleartoolCommandLine.runClearToolCommand(workingDir, subcommand) != 0) 
			throw new MojoExecutionException("Unable to create baseline");
	}

}
