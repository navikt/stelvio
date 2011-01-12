package no.stelvio.maven.plugins;

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
	 * Stream name - BUILD_TEST
	 * 
	 * @parameter expression="${stream}"
	 * @required
	 */
	private String stream;
	
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
		}else if (step == 2){
			fail = deliverII() != 0;
		}
		if (fail) throw new MojoExecutionException("Unable to perform delivery");
	}
	
	private int deliverI() throws MojoFailureException{
		this.getLog().info("-----------------------------");
		this.getLog().info("--- Performing delivery I ---");
		this.getLog().info("-----------------------------");
		String workingDir = "D:/cc/"+this.stream+"_Dev";
		String subcommand = "deliver -to " + this.stream + "_int"+" -force";
		return CleartoolCommandLine.runClearToolCommand(workingDir, subcommand);
	}
	
	private int deliverII() throws MojoFailureException{
		this.getLog().info("------------------------------");
		this.getLog().info("--- Performing delivery II ---");
		this.getLog().info("------------------------------");
		String workingDir = "D:/cc/"+this.stream+"_Dev";
		String subcommand = "deliver -complete -force";
		return CleartoolCommandLine.runClearToolCommand(workingDir, subcommand);
	}

}
