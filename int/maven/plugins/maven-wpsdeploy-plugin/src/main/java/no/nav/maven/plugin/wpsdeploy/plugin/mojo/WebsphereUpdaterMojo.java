package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Abstract class using the template pattern for child mojos.
 * 
 * @author test@example.com
 */
public abstract class WebsphereUpdaterMojo extends WebsphereMojo {

	/**
	 * @parameter expression="${project}"
	 * @readonly
	 * @required
	 */
	protected MavenProject project;
	
	/**
	 * @parameter expression="${wid.runtime}"
	 * @required
	 */
	protected String widRuntime;

	/**
	 * @parameter expression="${project.artifacts}"
	 * @required
	 */
	protected Set<Artifact> artifacts;

	/**
	 * @parameter expression="${project.basedir}"
	 * @required
	 */
	protected File baseDirectory;

	/**
	 * @parameter expression="${project.build.scriptSourceDirectory}"
	 * @required
	 */
	protected String scriptDirectory;

	/**
	 * @parameter expression="${environment}"
	 * @required
	 */
	protected String environment;

	/**
	 * @parameter expression="${bus-configuration-version}"
	 * @required
	 */
	protected String busConfigurationVersion;
	
	/**
	 * @parameter expression="${envClass}"
	 */
	protected String envClass;
	
	/**
	 * @parameter expression="${dmgrUsername}"
	 */
	protected String dmgrUsername;
	
	/**
	 * @parameter expression="${dmgrPassword}"
	 */
	protected String dmgrPassword;
	
	/**
	 * @parameter expression="${dmgrHostname}"
	 */
	protected String dmgrHostname;
	
	/**
	 * @parameter expression="${dmgrSOAPPort}"
	 */
	protected String dmgrSOAPPort;
	
	/**
	 * @parameter expression="${linuxUser}"
	 */
	protected String linuxUser;
	
	/**
	 * @parameter expression="${linuxPassword}"
	 */
	protected String linuxPassword;
	
	/**
	 * @parameter expression="${logging.level}"
	 */
	private String logLevel;
	protected String targetDirectory;
	protected String deployableArtifactsHome;
	protected String environmentFile;
	protected String moduleConfigHome;
	protected String busConfigurationDirectory;

	protected abstract void applyToWebSphere(final Commandline wsadminCommandline) throws MojoExecutionException, MojoFailureException;
	
	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
		
		targetDirectory = baseDirectory + "/target";
		busConfigurationDirectory = baseDirectory + busConfigurationExtractDirectory;
		deployableArtifactsHome = targetDirectory + "/EARFilesToDeploy";
		moduleConfigHome = busConfigurationDirectory + "/moduleconfig";
		environmentFile = busConfigurationDirectory + "/environments/" + environment + ".properties";
		
		/* Given that the variable wid.runtime is set correctly in settings.xml */
		Commandline wsadminCommandLine = new Commandline();
		wsadminCommandLine.setExecutable(widRuntime + "/bin/wsadmin.sh");
		ArrayList<String> args = new ArrayList<String>();

		args.add("-host " + dmgrHostname);
		args.add("-port " + dmgrSOAPPort);
		args.add("-user " + dmgrUsername);
		args.add("-password " + dmgrPassword);
		
		if (logLevel != null){
			args.add("-javaoption -Dlogging.level=" + logLevel);
		}
		
		args.add("-f " + targetDirectory + "/scripts/Executor.py");

		for(String arg : args){
			Commandline.Argument cmdArg = new Commandline.Argument();
			cmdArg.setLine(arg);
			wsadminCommandLine.addArg(cmdArg);
		}
		
		applyToWebSphere(wsadminCommandLine);
	}

	protected File getConfigurationFile(String env, String fileName, String moduleConfigPath) {

		File file = new File(moduleConfigPath + "/" + envClass + "/" + env + "/" + fileName);

		if (file.exists()) {
			return file;
		}

		file = new File(moduleConfigPath + "/" + envClass + "/" + fileName);

		if (file.exists()) {
			return file;
		}

		file = new File(moduleConfigPath + "/" + fileName);

		if (file.exists()) {
			return file;
		}

		return null;
	}

	protected boolean isConfigurationLoaded(){
		return envClass != null;
	}

}