package no.nav.maven.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import no.nav.aura.service.discovery.EnvConfigResourceDiscovery;
import no.nav.aura.service.discovery.constants.EnvConfigQueryParameters;
import no.nav.aura.service.discovery.domain.EnvConfigResource;
import no.nav.datapower.config.freemarker.Freemarker;
import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.DPPropertiesUtils;
import no.nav.datapower.util.PropertiesBuilder;

import org.apache.maven.artifact.InvalidRepositoryException;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectUtils;

import freemarker.template.TemplateException;

/**
 * Goal which generates a DataPower configuration.
 * 
 * @goal generateServiceGW
 * @phase compile
 * 
 * @author person4fdbf4cece95, Accenture
 */
public class GenerateServiceGWConfigMojo extends AbstractMojo {

	private static final String MAIN_TEMPLATE = "main.ftl";

	/**
	 * The maven project
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;
	
	/**
	 * Artifact repository factory component.
	 * 
	 * @component
	 * @readonly
	 * @required
	 */
	private ArtifactRepositoryFactory artifactRepositoryFactory;

	/**
	 * @parameter expression="${session}"
	 * @readonly
	 * @required
	 */
	private MavenSession mavenSession;

	/**
	 * The remote repositories used as specified in your POM.
	 * 
	 * @parameter expression="${project.repositories}"
	 * @readonly
	 * @required
	 */
	private List repositories;
	

	 /**
     * The endpoint URL to environment configuration
     * 
     * @parameter expression="${envConfigBaseUrlString}"
     * @required
     * @readonly
     */
	
    private String envConfigBaseUrlString;
    /**
     * The name of the environment where to discover web services
     * 
     * @parameter expression="${env}"
     * @required
     * @readonly
     */
    private String env;

    /**
     * The domain of the environment where to discover web services
     * 
     * Example: devillo.no, test.local, oera.t
     * 
     * @parameter expression="${envDomain}"
     * @required
     * @readonly
     */
    private String envDomain;

    /**
     * The username to authenticate to environment configuration
     * 
     * @parameter expression="${username}"
     * @required
     * @readonly
     */
    private String username;

    /**
     * The password to authenticate to environment configuration
     * 
     * 
     * @parameter expression="${password}"
     * @required
     * @readonly
     */
    private String password;
	
    /**
     * secgw or partner-gw
     * 
     * @parameter expression="${gateway-name}"
     * @readonly
     * @required
     */
    private String gatewayName;

    /**
     * @parameter expression="${envclass}"
     * @required
     */
    protected String envclass;
	

	// Properties for template interpolation
	private Properties properties;

	private List remoteRepos;
	
	
	// Exported properties
    public static final String PROPERTY_KEY_LOGGINGHOST = "loggingRemoteHost";
    public static final String PROPERTY_KEY_LOGGINGURI = "loggingRemoteURI";

	/**
	 * The mojo method doing the actual work when goal is invoked
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		getLog().info("Generating Datapower config for " + gatewayName);
		
		File tempDir = new File(project.getBasedir(), "target/temp");
		tempDir.mkdir();
		File configFile = new File(tempDir, "configuration.xml");		
		File certFile = new File(project.getBasedir(), "target/dependency/" + gatewayName + "-environment-configuration-jar/" + envclass + "/trustCerts.properties");
		properties = loadProperties(certFile.getPath());
		
		getLog().info("Adding certificates from partnertrust file: " + certFile);
		
		addTrustCerts(properties);

		try {
			getEnvConfigProperties(properties);
		} catch (Exception e) {
			throw new MojoExecutionException("[ERROR] Getting values from EnvConfig", e);
		}

		// Instantiate the remote repositories object
		try {
			remoteRepos = ProjectUtils.buildArtifactRepositories(repositories, artifactRepositoryFactory, mavenSession.getContainer());
		} catch (InvalidRepositoryException e) {
			throw new MojoExecutionException("[ERROR] Error building remote repositories", e);
		}

		// Merge templates with properties and output to config file
		try {
			getLog().info("Processing Freemarker-templates");
			processFreemarkerTemplates(configFile);
			getLog().info("Config is generated successfully");
		} catch (IOException e) {
			throw new IllegalStateException("Caught IOException while generating DataPower configuration", e);
		} catch (TemplateException e) {
			throw new IllegalStateException("Caught IllegalStateException while generating DataPower configuration", e);
		}
	}
	
	

	private void getEnvConfigProperties(Properties properties) throws URISyntaxException, MalformedURLException, IOException  {
		URI envConfigBaseUrl = new URI(envConfigBaseUrlString);

		EnvConfigResourceDiscovery discovery = new EnvConfigResourceDiscovery(envConfigBaseUrl, username, password);
		discovery.addQueryParameter(EnvConfigQueryParameters.QUERY_PARAM_ENTRY_ALIAS, "nfs.log");
		discovery.addQueryParameter(EnvConfigQueryParameters.QUERY_PARAM_ENTRY_DOMAIN, envDomain);
		discovery.addQueryParameter(EnvConfigQueryParameters.QUERY_PARAM_ENTRY_ENV_NAME, env);
		
		EnvConfigResource resource = discovery.discover(EnvConfigResourceDiscovery.DISCOVERY_MODE_BESTMATCH);
		URL url = new URL(resource.getPropertyValue("url"));
		properties.put(PROPERTY_KEY_LOGGINGHOST, url.getHost());
		properties.put(PROPERTY_KEY_LOGGINGURI, url.getPath());
	}
	

	/*
	 * Load properties from property file
	 */
	private Properties loadProperties(String propertiesFile) {
		Properties properties = DPPropertiesUtils.load(propertiesFile);
		PropertiesBuilder builder = new PropertiesBuilder();
		builder.putAll(properties);
		builder.interpolate();
		return builder.buildProperties();
	}

	/*
	 * Expand trust certificate property and inject new expanded property TODO
	 * Move out of the generic configuration generator
	 */
	private void addTrustCerts(Properties props) {
		if (props.getProperty("partnerTrustCerts") != null) {
			List<String> trustCertList = DPCollectionUtils.listFromString(props.getProperty("partnerTrustCerts"));
			List<Map<String, String>> trustCertMapList = DPCollectionUtils.newArrayList();
			for (String trustCert : trustCertList) {
				Map<String, String> cert = DPCollectionUtils.newHashMap();
				cert.put("name", trustCert.substring(trustCert.lastIndexOf("/") + 1));
				cert.put("file", trustCert);
				trustCertMapList.add(cert);
			}
			props.put("partnerTrustedCerts", trustCertMapList);
		}
	}

	private void processFreemarkerTemplates(File configFile) throws IOException, TemplateException {
		Freemarker freemarker = new Freemarker(new File(project.getBasedir(), "target/dependency/" + gatewayName + "-nonenvironment-configuration-jar/freemarker-templates"));
		FileWriter writer = new FileWriter(configFile);
		freemarker.processTemplate(MAIN_TEMPLATE, properties, writer);
		writer.flush();
		writer.close();
	}

}