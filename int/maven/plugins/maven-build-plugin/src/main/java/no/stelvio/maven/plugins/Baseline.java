package no.stelvio.maven.plugins;

import no.stelvio.maven.build.plugin.utils.ApplicationNameResolve;
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
	 * Integration stream tag
	 * 
	 * @parameter expression="${intStream}" default-value="_int"
	 */
	private String intStream;
	
	/**
	 * Version to be released
	 * @parameter expression="${versionNumber}"
	 * @required
	 */
	private String release_version;
	
	/**
	 * Whether this goal should be done
	 * @parameter expression="${perform_baseline}" default-value=true
	 */
	private boolean perform;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {		
		if (!perform) {
			this.getLog().warn("Skipping baseline");
			return;
		}
		this.getLog().info("-------------------------");
		this.getLog().info("--- Creating baseline ---");
		this.getLog().info("-------------------------");
		
		String workingDir = this.ccProjectDir+this.build+this.intStream;
		String build_tag = this.build + " version: " + this.release_version;
		String subcommand = "mkbl -c \"Baseline for " + build_tag + "\" -component " +
				"component:" + ApplicationNameResolve.ApplicationFromProject(build) + "_Composite@\\NAV_PVOB" + 
				"-identical -full " + build_tag;
//		if (CleartoolCommandLine.runClearToolCommand(workingDir, subcommand) != 0) 
//			throw new MojoExecutionException("Unable to create baseline");
	}

}
