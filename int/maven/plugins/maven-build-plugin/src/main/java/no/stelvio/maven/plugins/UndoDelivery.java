/**
 * 
 */
package no.stelvio.maven.plugins;

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
	 * Stream name - BUILD_TEST
	 * 
	 * @parameter expression="${stream}"
	 * @required
	 */
	private String stream;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getLog().info("---------------------");
		this.getLog().info("--- Undo delivery ---");
		this.getLog().info("---------------------");
		Commandline undeliver = new Commandline();
		// must stand in the right place
		undeliver.setWorkingDirectory("D:/cc/"+this.stream+"_Dev");
		Commandline.Argument arg = new Commandline.Argument();
		String command = "cleartool -cancel -force";
		arg.setLine(command);
		undeliver.addArg(arg);
		if (CommandLineUtil.executeCommand(undeliver) != 0) 
			throw new MojoExecutionException("Unable to perform delivery");
	}

}
