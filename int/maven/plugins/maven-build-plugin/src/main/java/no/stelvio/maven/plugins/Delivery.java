package no.stelvio.maven.plugins;

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
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		boolean fail = false;
		this.getLog().info("---------------------------");
		this.getLog().info("--- Performing delivery ---");
		this.getLog().info("---------------------------");
		Commandline deliver = new Commandline();
		// must stand in the right place
		deliver.setWorkingDirectory("D:/cc/"+this.stream+"_Dev");
		Commandline.Argument arg = new Commandline.Argument();
		String command = "cleartool deliver -preview";
		arg.setLine(command);
		deliver.addArg(arg);
		fail = CommandLineUtil.executeCommand(deliver) != 0;
		if (fail) throw new MojoExecutionException("Unable to perform delivery");
		// TODO to think: cleartool deliver -cancel -> undo delivery?
	}

}
