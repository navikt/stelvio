package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
	 * @parameter expression="${esb-authorization-configuration}"
	 * @required
	 */
	protected String authorizationConfigurationVersion;

	/**
	 * @parameter expression="${esb-enviroment-configuration}"
	 * @required
	 */
	protected String enviromentConfigurationVersion;

	/**
	 * @parameter expression="${esb-nonenviroment-configuration}"
	 * @required
	 */
	protected String nonenviromentConfigurationVersion;
	
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
	
	/* 
	 * path/dir variables:
	 * path = full path
	 * dir = a single directory without path
	 */

	/* general */
	protected List<String> configurationParts = new ArrayList<String>();
	protected String jythonScriptsPath;
	protected String pathMappingFilePath;
	protected String deployDependencies;
	protected String busConfigurationExtractPath;

	/* tmp */
	protected String tmpTemplatesPath;
	protected String tmpEnvironmentPropertiesPath;
	protected String tmpApplicationPropertiesPath;
	
	/* after interpolation */
	protected String blaGroupsPath;
	protected String moduleConfigPath;
	protected String environmentPropertiesPath;
	protected String applicationPropertiesPath;	
	protected String activationspecificationsPath;
	protected String authorizationConsXmlPath;
	
	protected abstract void applyToWebSphere(final Commandline wsadminCommandline) throws MojoExecutionException, MojoFailureException;
	
	@SuppressWarnings("unused")
	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
		
		/* ESB Configuration parts */
		String authorizationConfiguration = "esb-authorization-configuration";
		String enviromentConfiguration = "esb-enviroment-configuration";
		String nonenviromentConfiguration = "esb-nonenviroment-configuration";
		
		configurationParts.add(authorizationConfiguration);
		configurationParts.add(enviromentConfiguration);
		configurationParts.add(nonenviromentConfiguration);

		String busConfigurationPath = targetDirectory + "/bus-config";
		busConfigurationExtractPath = targetDirectory + "/tmp";
		jythonScriptsPath = targetDirectory + "/scripts";
		deployDependencies = targetDirectory + "/EarFilesToDeploy.csv";
		
		/* tmp bus configuration dirs */
		String tmpAuthorizationConfigurationPath = busConfigurationExtractPath + "/" + authorizationConfiguration;
		String tmpEnviromentConfigurationPath = busConfigurationExtractPath + "/" + enviromentConfiguration;
		String tmpNonenviromentConfigurationPath = busConfigurationExtractPath + "/" + enviromentConfiguration;
				
		tmpTemplatesPath = tmpEnviromentConfigurationPath + "/templates";
		tmpEnvironmentPropertiesPath = tmpEnviromentConfigurationPath + "/properties/" + environment;
		tmpApplicationPropertiesPath = tmpEnviromentConfigurationPath + "/app_props";
		
		/* dirs after interpolation (a plugin in the pom inserts passwords and copys the config to a new dir)*/
		String authorizationConfigurationPath = busConfigurationPath + "/" + authorizationConfiguration;
		String enviromentConfigurationPath = busConfigurationPath + "/" + enviromentConfiguration;
		String nonenviromentConfigurationPath = busConfigurationPath + "/" + nonenviromentConfiguration;
		
		environmentPropertiesPath = enviromentConfigurationPath + "/properties/" + environment;
		applicationPropertiesPath = enviromentConfigurationPath + "/app_props/" + environment;
		blaGroupsPath = nonenviromentConfigurationPath + "/BLA-groups";
		moduleConfigPath = enviromentConfigurationPath + "/moduleconfig";
		activationspecificationsPath = nonenviromentConfigurationPath + "/activationspecifications/maxconcurrency.xml";
		authorizationConsXmlPath = authorizationConfigurationPath + "/" + envClass + ".xml";
		
		/* Given that the variable wid.runtime is set correctly in settings.xml */
		Commandline wsadminCommandLine = new Commandline();
		wsadminCommandLine.setExecutable(widRuntime + "/bin/wsadmin.sh");
		ArrayList<String> args = new ArrayList<String>();

		args.add("-host " + dmgrHostname);
		args.add("-port " + dmgrSOAPPort);
		args.add("-user " + dmgrUsername);
		args.add("-password " + dmgrPassword);
		
		String javaoption = "-Dpython.path="+ jythonScriptsPath;
		
		if (logLevel != null){
			javaoption += " -Dlogging.level="+ logLevel;
		}
		
		args.add("-javaoption '"+ javaoption +"'");
		
		args.add("-f " + jythonScriptsPath + "/Executor.py");

		for(String arg : args){
			Commandline.Argument cmdArg = new Commandline.Argument();
			cmdArg.setLine(arg);
			wsadminCommandLine.addArg(cmdArg);
		}
		
		applyToWebSphere(wsadminCommandLine);
	}

	protected boolean isConfigurationLoaded(){
		return envClass != null;
	}

}