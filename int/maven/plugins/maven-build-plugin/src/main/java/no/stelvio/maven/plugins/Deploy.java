package no.stelvio.maven.plugins;

import no.stelvio.maven.build.plugin.utils.ApplicationNameResolve;
import no.stelvio.maven.build.plugin.utils.MavenCommandLine;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Goal which runs deploy script and updates maven repo
 *
 * @goal updateRepo
 * 
 * @author test@example.com
 */
public class Deploy extends AbstractMojo{
	
	/**
	 * Project name - BUILD_TEST
	 * 
	 * @parameter expression="${build}"
	 * @required
	 */
	private String build;
	
	/**
	 * Action to be done: install, deploy
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
	 * @parameter expression="${perform_install}" default-value=true
	 */
	private boolean perform_install;
	
	/**
	 * Whether this goal should be done
	 * @parameter expression="${perform_deploy}" default-value=true
	 */
	private boolean perform_deploy;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		boolean fail = false;
		if (this.action.equalsIgnoreCase("install")){
			if (perform_install) fail = doInstall() != 0;
			else this.getLog().warn("Skipping install");
		}else if (this.action.equalsIgnoreCase("deploy")){
			if (perform_deploy)	fail = doDeploy() != 0;
			else this.getLog().warn("Skipping deploy");
		}
		if (fail) throw new MojoExecutionException("Unable to update maven repository.");
	}
	
	/**
	 * This method runs mvn clean install in layers folder in Int stream
	 * @return 0 if everything is OK
	 * @throws MojoFailureException
	 */
	private int doInstall() throws MojoFailureException{
		this.getLog().info("--------------------------------------");
		this.getLog().info("--- Installing to local repository ---");
		this.getLog().info("--------------------------------------");
		String workDir = this.ccProjectDir+this.build+this.intStream+"/"+ApplicationNameResolve.ApplicationFromProject(build)+"/layers";
		//return MavenCommandLine.PerformMavenCommand(workDir, "clean install");
		return 0;
	}
	
	/**
	 * This method runs mvn clean install in layers folder in Int stream
	 * @return 0 if everything is OK
	 * @throws MojoFailureException
	 */
	private int doDeploy() throws MojoFailureException{
		this.getLog().info("---------------------------------------");
		this.getLog().info("--- Installing to remote repository ---");
		this.getLog().info("---------------------------------------");
		String workDir = this.ccProjectDir+this.build+this.intStream+"/"+ApplicationNameResolve.ApplicationFromProject(build)+"/layers";
		//return MavenCommandLine.PerformMavenCommand(workDir, "deploy -Dmaven.test.skip");
		return 0;
	}
	

}
