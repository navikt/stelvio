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
	protected String externalEndpointsXml;
	protected String externalTilkoblingslisteXml;
	protected String templatesPath;
	protected String blaGroupsPath;
	protected String moduleConfigPath;
	protected String deployInfoPropertiesPath;
	protected String applicationPropertiesPath;	
	protected String activationspecificationsPath;
	protected String authorizationConsXmlPath;
	protected String policySetBindings;
	
	protected abstract void applyToWebSphere(final Commandline wsadminCommandline) throws MojoExecutionException, MojoFailureException;
	
	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
		/* ESB Configuration parts */
		String authorizationConfiguration = "esb-authorization-configuration";
		String enviromentConfiguration = "esb-enviroment-configuration";
		String nonenviromentConfiguration = "esb-nonenviroment-configuration";
		
		configurationParts.add(authorizationConfiguration);
		configurationParts.add(enviromentConfiguration);
		configurationParts.add(nonenviromentConfiguration);

		jythonScriptsPath = targetDirectory + "/scripts";
		deployDependencies = targetDirectory + "/EarFilesToDeploy.csv";
		
		/* tmp bus configuration dirs */
		deployProperties = tmpInterpolationStageOne + "/" + nonenviromentConfiguration +"/properties/deployInfo.properties";

		/* dirs after interpolation (a plugin in the pom inserts passwords and copys the config to a new dir) */
		String authorizationConfigurationPath = busConfigPath + "/" + authorizationConfiguration;
		String enviromentConfigurationPath = busConfigPath + "/" + enviromentConfiguration;
		String nonenviromentConfigurationPath = busConfigPath + "/" + nonenviromentConfiguration;
		
		templatesPath = enviromentConfigurationPath + "/templates";
		mainPropertiesFilepath = targetDirectory + "/main.properties";
		environmentPropertiesTree = enviromentConfigurationPath + "/properties-tree";
		externalEndpointsXml = nonenviromentConfigurationPath +"/samhandlers/external_endpoints.xml";
		externalTilkoblingslisteXml = nonenviromentConfigurationPath +"/samhandlers/external_tilkoblingsListe.xml";
		nonenvironmentProperties = nonenviromentConfigurationPath + "/properties";
		deployInfoPropertiesPath = nonenvironmentProperties + "/deployInfo.properties";
		applicationPropertiesPath = enviromentConfigurationPath + "/app_props/" + envName;
		blaGroupsPath = nonenviromentConfigurationPath + "/BLA-groups";
		moduleConfigPath = enviromentConfigurationPath + "/moduleconfig";
		activationspecificationsPath = nonenviromentConfigurationPath + "/activationspecifications/maxconcurrency.xml";
		authorizationConsXmlPath = authorizationConfigurationPath;
		policySetBindings = nonenviromentConfigurationPath + "/policySetBindings/policySetBindings.xml";
		
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