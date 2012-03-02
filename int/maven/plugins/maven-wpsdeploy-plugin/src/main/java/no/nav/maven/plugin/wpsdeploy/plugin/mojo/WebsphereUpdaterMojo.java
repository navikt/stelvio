package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;
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
	 * @parameter expression="${logging.level}" default-value="false"
	 */
	private String logLevel;
	protected String targetDirectory;
	protected String deployableArtifactsHome;
	protected String environmentFile;
	protected String moduleConfigHome;

	protected abstract void applyToWebSphere(final Commandline wsadminCommandline) throws MojoExecutionException, MojoFailureException;

	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
		
		targetDirectory = baseDirectory + "/target/";
		deployableArtifactsHome = targetDirectory + "/EARFilesToDeploy";
		moduleConfigHome = baseDirectory + busConfigurationExtractDirectory + "/moduleconfig";
		environmentFile = baseDirectory + busConfigurationExtractDirectory + "/environments/" + environment + ".properties";

		/* Given that the variable wid.runtime is set correctly in settings.xml */
		Commandline wsadminCommandLine = new Commandline();
		wsadminCommandLine.setExecutable(widRuntime + "/bin/wsadmin.sh");

		Commandline.Argument arg1 = new Commandline.Argument();
		arg1.setLine("-host " + dmgrHostname);
		wsadminCommandLine.addArg(arg1);

		Commandline.Argument arg2 = new Commandline.Argument();
		arg2.setLine("-port " + dmgrSOAPPort);
		wsadminCommandLine.addArg(arg2);

		Commandline.Argument arg3 = new Commandline.Argument();
		arg3.setLine("-user " + dmgrUsername);
		wsadminCommandLine.addArg(arg3);

		Commandline.Argument arg4 = new Commandline.Argument();
		arg4.setLine("-password " + dmgrPassword);
		wsadminCommandLine.addArg(arg4);
		
		Commandline.Argument arg5 = new Commandline.Argument();
		arg5.setLine("-f " + targetDirectory + "/scripts/Executor.py");
		wsadminCommandLine.addArg(arg5);
		
		
		if (logLevel != null){
			Commandline.Argument arg6 = new Commandline.Argument();
			arg6.setLine("-Dlogging.level " + logLevel);
			wsadminCommandLine.addArg(arg6);
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