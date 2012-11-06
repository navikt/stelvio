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
	 * @parameter expression="${esb-authorization-configuration}"
	 * @required
	 */
	protected String authorizationConfigurationVersion;

	/**
	 * @parameter expression="${esb-enviroment-configuration}"
	 * @required
	 */
	protected String environmentConfigurationVersion;

	/**
	 * @parameter expression="${esb-nonenviroment-configuration}"
	 * @required
	 */
	protected String nonenvironmentConfigurationVersion;
	
	/**
	 * @parameter expression="${envClass}"
	 */
	protected String envClass;
	
	/**
	 * @parameter expression="${envName}"
	 */
	protected String envName;
	
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
	
	/**
	 * @parameter expression="${tmpExtractDir}"
	 */
	protected String tmpExtractPath;
	
	/**
	 * @parameter expression="${tmpInterpolationStageOne}"
	 */
	protected String tmpInterpolationStageOne;

	/**
	 * @parameter expression="${busConfigDir}"
	 */
	protected String busConfigPath;
	
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

	/* tmp */
	protected String deployProperties;
	
	/* after interpolation */
	protected String environmentPropertiesTree;
	protected String mainPropertiesFilepath;
	protected String nonenvironmentProperties;
	protected String templatesPath;
	protected String blaGroupsPath;
	protected String applicationEndpoints;
	protected String deployInfoPropertiesPath;
	protected String applicationPropertiesPath;	
	protected String activationspecificationsPath;
	protected String authorizationConsXmlPath;
	protected String policySetBindings;
	
	protected abstract void applyToWebSphere(final Commandline wsadminCommandline) throws MojoExecutionException, MojoFailureException;
	
	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
		/* ESB Configuration parts */
		String authorizationConfiguration = "esb-authorization-configuration";
		String environmentConfiguration = "esb-enviroment-configuration";
		String nonenvironmentConfiguration = "esb-nonenviroment-configuration";
		
		configurationParts.add(authorizationConfiguration);
		configurationParts.add(environmentConfiguration);
		configurationParts.add(nonenvironmentConfiguration);

		jythonScriptsPath = targetDirectory + "/scripts";
		deployDependencies = targetDirectory + "/EarFilesToDeploy.csv";
		
		/* tmp bus configuration dirs */
		deployProperties = tmpInterpolationStageOne + "/" + nonenvironmentConfiguration +"/properties/deployInfo.properties";

		/* dirs after interpolation (a plugin in the pom inserts passwords and copys the config to a new dir) */
		String authorizationConfigurationPath = busConfigPath + "/" + authorizationConfiguration;
		String environmentConfigurationPath = busConfigPath + "/" + environmentConfiguration;
		String nonenvironmentConfigurationPath = busConfigPath + "/" + nonenvironmentConfiguration;
		
		templatesPath = nonenvironmentConfigurationPath + "/templates";
		mainPropertiesFilepath = targetDirectory + "/main.properties";
		environmentPropertiesTree = environmentConfigurationPath + "/properties-tree";
		nonenvironmentProperties = nonenvironmentConfigurationPath + "/properties";
		deployInfoPropertiesPath = nonenvironmentProperties + "/deployInfo.properties";
		applicationPropertiesPath = environmentConfigurationPath + "/app_props/" + envName;
		blaGroupsPath = nonenvironmentConfigurationPath + "/BLA-groups";
		applicationEndpoints = nonenvironmentConfigurationPath + "/applicationEndpoints";
		activationspecificationsPath = nonenvironmentConfigurationPath + "/activationspecifications/maxconcurrency.xml";
		authorizationConsXmlPath = authorizationConfigurationPath;
		policySetBindings = nonenvironmentConfigurationPath + "/policySetBindings/policySetBindings.xml";
		
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

	//Todo: replace the invocations of this method with the configurationRequierdToProceed method
	protected boolean isConfigurationLoaded(){
		return envClass != null;
	}
	
	protected void configurationRequierdToProceed() {
		if ((envClass == null) || (envName == null)){
			throw new IllegalStateException("You can't run this step before having loaded the environment configuration!\nenvClass("+envClass+")\nenvName("+envName+")");
		}
	}

}