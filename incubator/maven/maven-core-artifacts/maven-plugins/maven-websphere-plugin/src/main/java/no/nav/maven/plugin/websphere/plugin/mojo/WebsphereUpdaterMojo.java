package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;
import java.util.Set;

import no.nav.maven.commons.configuration.ArtifactConfiguration;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
* Abstract class using the template pattern for child mojos.
* 
* @author test@example.com 
*/
public abstract class WebsphereUpdaterMojo extends WebsphereMojo {
    
	/**
	 * @parameter expression="${wid.home}"
	 * @required
	 */
	protected String widHome;
	
	/**
	 * @parameter expression="${username}"
	 * @required
	 */
	protected String deploymentManagerUser;
	
	/**
	 * @parameter expression="${password}"
	 * @required
	 */
	protected String deploymentManagerPassword;

	/**
	 * @parameter expression="${host}"
	 * @required
	 */
	protected String deploymentManagerHost;
	
	/**
	 * @parameter expression="${port}"
	 * @required
	 */
	protected String deploymentManagerPort;

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
	
	protected  String scriptsHome;
	protected  String deployableArtifactsHome;
	protected  String resourcePropertiesHome;

	
	protected abstract void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException;

	protected final void reportResult(CommandLineUtils.StringStreamConsumer stdout, CommandLineUtils.StringStreamConsumer stderr) {
		
		
		if(stdout != null) {
			getLog().info(stdout.getOutput());
		}
		
		/*
		if(stderr != null) {
			getLog().error(stdout.getOutput());
		}
		*/	
	}
	
	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
		scriptsHome = baseDirectory + "/" + scriptDirectory;
		deployableArtifactsHome = baseDirectory + "/target";
		resourcePropertiesHome = scriptsHome + "/app_props/" + environment;
		
		if(ArtifactConfiguration.isConfigurationLoaded() == false) {
			throw new RuntimeException("The artifact configuration is not loaded");
		}
		
		Commandline commandLine = new Commandline();
		if(Os.isFamily("windows") == true) {
			commandLine.setExecutable(widHome + "/pf/wps01/bin/wsadmin.bat");
		} else {
			commandLine.setExecutable(widHome + "/pf/wps01/bin/wsadmin.sh");
		}	
		
		Commandline.Argument arg1 = new Commandline.Argument();
		arg1.setLine("-host " + deploymentManagerHost);
		commandLine.addArg(arg1);
		Commandline.Argument arg2 = new Commandline.Argument();
		arg2.setLine("-port " + deploymentManagerPort);
		commandLine.addArg(arg2);
		Commandline.Argument arg3 = new Commandline.Argument();
		arg3.setLine("-user " + deploymentManagerUser);
		commandLine.addArg(arg3);
		Commandline.Argument arg4 = new Commandline.Argument();
		arg4.setLine("-password " + deploymentManagerPassword);
		commandLine.addArg(arg4);	
		
		applyToWebSphere(commandLine);
	}
}	