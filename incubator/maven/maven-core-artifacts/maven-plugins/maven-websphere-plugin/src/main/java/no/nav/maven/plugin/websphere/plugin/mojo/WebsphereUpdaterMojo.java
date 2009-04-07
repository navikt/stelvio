package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;
import java.util.Set;

import no.nav.maven.commons.configuration.ArtifactConfiguration;
import no.nav.maven.plugin.websphere.plugin.utils.MojoExecutor;

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
	 * 
	 */
	protected String deploymentManagerUser;
	
	/**
	 * @parameter expression="${password}"
	 *
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
	
	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
		scriptsHome = baseDirectory + "/" + scriptDirectory;
		deployableArtifactsHome = baseDirectory + "/target";
		resourcePropertiesHome = scriptsHome + "/app_props/" + environment;
		
		if(ArtifactConfiguration.isConfigurationLoaded() == false) {
			getLog().warn("The deployment does not contain dependency to a wps configuration");
		}
		
		if((new File(baseDirectory,scriptDirectory)).exists() == false ) {
			executeLoadWebsphereConfigurationMojo();
			executePropertiesGeneratorMojo();
		}
		
		/* TODO: Put these hardcoded values in settings.xml for new WID Image */
		Commandline commandLine = new Commandline();
		if(Os.isFamily("windows") == true) {
			commandLine.setExecutable(widHome + "/pf/wps01/bin/wsadmin.bat");
		} else {
			commandLine.setExecutable(widHome + "/pf/wps/bin/wsadmin.sh");
		}	
		
		Commandline.Argument arg1 = new Commandline.Argument();
		arg1.setLine("-host " + deploymentManagerHost);
		commandLine.addArg(arg1);
		Commandline.Argument arg2 = new Commandline.Argument();
		arg2.setLine("-port " + deploymentManagerPort);
		commandLine.addArg(arg2);
		if(deploymentManagerUser != null) {
			Commandline.Argument arg3 = new Commandline.Argument();
			arg3.setLine("-user " + deploymentManagerUser);
			commandLine.addArg(arg3);
		}
		
		if(deploymentManagerPassword != null) {
	 		Commandline.Argument arg4 = new Commandline.Argument();
			arg4.setLine("-password " + deploymentManagerPassword);
			commandLine.addArg(arg4);	
		}
		applyToWebSphere(commandLine);
	}

	/* This is the experimental execute-a-goal-programatically code :) */
	private final void executePropertiesGeneratorMojo()  throws MojoExecutionException, MojoFailureException {
		MojoExecutor.executeMojo(
			MojoExecutor.plugin(
        		   MojoExecutor.groupId("nav.maven.plugins"),
        		   MojoExecutor.artifactId("maven-propertiesgenerator-plugin"),
        		   MojoExecutor.version("1.4")
           ),
           MojoExecutor.goal("generate"),
           MojoExecutor.configuration(
        		   MojoExecutor.element(MojoExecutor.name("templateDir"), "${basedir}/src/main/scripts/templates"),
        		   MojoExecutor.element(MojoExecutor.name("environmentName"), "${environment}"),
        		   MojoExecutor.element(MojoExecutor.name("outputDir"), "${basedir}/src/main/scripts/app_props"),
        		   MojoExecutor.element(MojoExecutor.name("environmentDir"), "${basedir}/src/main/scripts/environments")
           ),
           MojoExecutor.executionEnvironment(
                   project,
                   session,
                   pluginManager
           )
       );
	}
	
	/* This is the experimental execute-a-goal-programatically code :) */
	private final void executeLoadWebsphereConfigurationMojo()  throws MojoExecutionException, MojoFailureException {
		MojoExecutor.executeMojo(
			MojoExecutor.plugin(
        		   MojoExecutor.groupId("no.nav.maven.plugins"),
        		   MojoExecutor.artifactId("maven-websphere-plugin"),
        		   null
           ),
           MojoExecutor.goal("load-runtime-configuration"),
           null,
           MojoExecutor.executionEnvironment(
                   project,
                   session,
                   pluginManager
           )
       );
	}
}	