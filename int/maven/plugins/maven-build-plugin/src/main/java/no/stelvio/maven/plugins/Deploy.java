package no.stelvio.maven.plugins;

import no.stelvio.maven.build.plugin.utils.ApplicationNameResolve;
import no.stelvio.maven.build.plugin.utils.CommandLineUtil;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;


/**
 * Goal which runs deploy script and updates maven repo
 *
 * @goal updateRepo
 * 
 * @author test@example.com
 */
public class Deploy extends AbstractMojo{
	
	private final String BAT_FILE = "D:\\scripts\\mvn_job_exec.bat";
	
	/**
	 * Project name - BUILD_TEST
	 * 
	 * @parameter expression="${project}"
	 * @required
	 */
	private String project;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getLog().info("---------------------------------");
		this.getLog().info("--- Updating maven repository ---");
		this.getLog().info("---------------------------------");
		Commandline cmd = new Commandline();
		cmd.setWorkingDirectory("D:\\cc\\"+this.project+"_int\\"+ApplicationNameResolve.ApplicationFromProject(project)+"\\layers");
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine(BAT_FILE + " mvn deploy -Dmaven.test.skip ");
		cmd.addArg(arg);
		if (CommandLineUtil.executeCommand(cmd) != 0) throw new MojoExecutionException("Unable to update maven repository.");
	}

}
