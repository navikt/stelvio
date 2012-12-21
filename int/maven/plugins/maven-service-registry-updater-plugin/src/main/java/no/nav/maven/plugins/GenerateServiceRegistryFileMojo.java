package no.nav.maven.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import no.nav.aura.appconfig.Application;
import no.nav.aura.appconfig.exposed.ExposedService;
import no.nav.aura.appconfig.exposed.ExposedWebservice;
import no.nav.aura.envconfig.client.ApplicationInfo;
import no.nav.aura.envconfig.client.rest.ServiceGatewayRestClient;
import no.nav.serviceregistry.ServiceRegistry;
import no.nav.serviceregistry.util.AppConfigUtils;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;

/**
 * Goal which generates a service registry file. Based on service registry plugin by Øystein Gisnås.
 * 
 * @goal generate-file
 * @phase compile
 * 
 * @author person4fdbf4cece95, Accenture
 * @author Johnny Horvi, Accenture
 * @author Øystein Gisnås, Accenture
 */
public class GenerateServiceRegistryFileMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project.build.outputDirectory}"
	 * @required
	 */
	protected File buildDirectory;
	/**
	 * @parameter expression="${user.home}"
	 * @required
	 */
	protected File userHome;
	/**
	 * The maven project
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;
    /**
     * Used to look up Artifacts in the remote repository.
     * 
     * @parameter expression=
     *  "${component.org.apache.maven.artifact.factory.ArtifactFactory}"
     * @required
     * @readonly
     */
    protected ArtifactFactory factory;

    /**
     * Used to look up Artifacts in the remote repository.
     * 
     * @parameter expression=
     *  "${component.org.apache.maven.artifact.resolver.ArtifactResolver}"
     * @required
     * @readonly
     */
    protected ArtifactResolver artifactResolver;

    /**
     * List of Remote Repositories used by the resolver
     * 
     * @parameter expression="${project.remoteArtifactRepositories}"
     * @readonly
     * @required
     */
    protected List remoteRepositories;

    /**
     * Location of the local repository.
     * 
     * @parameter expression="${localRepository}"
     * @readonly
     * @required
     */
    protected ArtifactRepository localRepository;
	/**
	 * @component roleHint="jar"
	 * @required
	 * @readonly
	 */
	private UnArchiver unArchiver;

	/**
	 * Apps with services to be exposed formatted like "pselv, norg,Joark"
	 * 
	 * @parameter
	 * @required
	 */
	protected String apps;

	/**
	 * Environment
	 * 
	 * @parameter expression="${env}"
	 * @required
	 */
	protected String env;

	/**
	 * Path to service-registry.xml
	 * 
	 * @parameter
	 * @required
	 */
	protected String serviceRegistryFile;

	/**
	 * Base url for envconfig
	 * 
	 * @parameter
	 * @required
	 */
	protected String baseUrl;

	protected Set<String> applications = new HashSet<String>();
	protected Set<ApplicationInfo> applicationsFromEnvconfig = new HashSet<ApplicationInfo>();

	private String hostname;
	private String appConfArtifactID;
	private String appConfGroupId;
	private String appConfigVersion;
	private File appConfig;
	private File wsdlDir;

	public void execute() throws MojoExecutionException, MojoFailureException {

		// unmarshal gammel fil!
		ServiceRegistry serviceRegistry = new ServiceRegistry();
		getLog().debug("Trying to read service registry file...");
		try {
			serviceRegistry = serviceRegistry.readServiceRegistry(serviceRegistryFile);
		} catch (FileNotFoundException e1) {
			throw new MojoExecutionException("Original service registry file not found", e1);
		} catch (JAXBException e1) {
			throw new MojoExecutionException("XML processing went wrong", e1);
		}
		getLog().debug("Service registry file read! (or sort of read...)");

		parseApplicationsString();
		getInfoFromEnvconfig(env, baseUrl);//gir et set av alle apps i miljøet
		
		for (ApplicationInfo application : applicationsFromEnvconfig) {
			
			System.out.println("Application " + application.getName());
			
			if (applications.contains(application.getName())){
				hostname = application.getEndpoint();
				appConfArtifactID = application.getAppConfigArtifactId();
				appConfGroupId = application.getAppConfigGroupId();
				appConfigVersion = application.getVersion();
				if (hostname == null || appConfArtifactID == null || appConfGroupId == null || appConfigVersion == null) {
					getLog().warn("Maven coordinates needed to locate appConfig for application " + application.getName() + " is missing");
					break;
					//skal det være null eller ""?
				}
				//extract appConfig-artifact
				File appConfigJar = downloadMavenArtifact(appConfArtifactID, appConfGroupId, appConfigVersion, "jar");
				File extractTo = new File(project.getBasedir(), "/appConfDir");
				extractTo.mkdir();
				extractArtifact(appConfigJar, extractTo);
				appConfig = new File(extractTo, "/app-config.xml");//stier må fikses
				System.out.println("Try to read, " + appConfig);
				AppConfigUtils util = new AppConfigUtils();
				Application thisApp = null;
				try {
					thisApp = util.readAppConfig(appConfig);
				} catch (JAXBException e) {
					throw new MojoExecutionException("app-config.xml could not be read", e);
				}
				Collection<ExposedService> services = thisApp.getExposedServices();
				
				wsdlDir = new File(project.getBasedir(), "/wsdl-" + application.getName());
				wsdlDir.mkdir();
				for (ExposedService exposedService : services) {
					String path = "getFromAppConfig";
					String wsdlArtifactId = "getFromAppConfig";
					String wsdlGroupId = "getFromAppConfig";
					String wsdlVersion = "getFromAppConfig";
					if (path == null || wsdlArtifactId == null || wsdlGroupId == null || wsdlVersion == null) {
						getLog().warn("Maven coordinates needed to locate wsdl artifact for service " + "exposedService.getName()" + " is missing");
						break;
						//skal det være null eller ""?						
					}
					File serviceJar = downloadMavenArtifact(wsdlArtifactId, wsdlGroupId, wsdlVersion, "jar");
					File extractServiceTo = new File(wsdlDir, "/" + "exposedService.getName()");
					extractArtifact(serviceJar, extractServiceTo);
					
					//finn mavenkoordinater, last ned og pakk ut, send lokasjon tilbake
				}

				getLog().debug("Trying to replace app block for application " + application);
				//endre metoden til å ta inn endpoint, wsdldir, app og path
				serviceRegistry.replaceApplicationBlock(hostname, wsdlDir, application.getName());
				
				
			}						
			// Bytt ut med ny og skriv
		}
		try {
			serviceRegistry.writeToFile(new File(serviceRegistryFile));
		} catch (Exception e) {
			throw new MojoExecutionException("An error occured while trying to write service registry to file", e);
		}
//		downloadAndExtractMavenArtifact("nav-fim-brukerprofil-tjenestespesifikasjon", "no.nav.tjenester.fim", "0.0.1-alpha002");
	}

	private void parseApplicationsString() throws MojoExecutionException {
		
		for (String applicationString : apps.split(",")) {

			String application = applicationString.trim().toLowerCase();

			if (application == null) {
				throw new MojoExecutionException("Something is wrong with the 'apps'-string. Verify that it's on the correct format. " + apps, new NullPointerException());
			}

			getLog().debug("Adding " + application + " to applications");
			applications.add(application);
		}
	}

	private void getInfoFromEnvconfig(String environment, String baseUrl) throws MojoExecutionException {

		ServiceGatewayRestClient client = new ServiceGatewayRestClient(baseUrl);
		applicationsFromEnvconfig = client.getApplicationInfo(environment);
		getLog().debug("Info for environment " + environment + " retrieved from envConfig");		
		
		
		// gjor sporring mot envconfig, returner endpoint og wsdl-artifakt
//		hostname = "https://hostnavn.test.local:9443/"; // = client.getHostname(app, env)
//		wsdlDir = new File(userHome + "\\Kode\\trunk\\stelvio-int-maven\\poms\\maven-service-gw-provisioning-pom", "\\wsdl-app"); // = undersøkes.
		
	}
	
	// tar inn mavenkoordinater for appconfig-artifakt, laster det ned, pakker ut, setter wsdl-lokasjon
	private File downloadMavenArtifact(String artifactId, String groupId, String version, String type) throws MojoExecutionException {

		Artifact pomArtifact = factory.createArtifact(groupId, artifactId, version, "", type);
		try {
			artifactResolver.resolve(pomArtifact, remoteRepositories, localRepository);
			System.out.println("Prøver å finne artifakt!! " + pomArtifact.getArtifactId());
			return pomArtifact.getFile();
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException("Could not resolve artifact, " + e);
		} catch (ArtifactNotFoundException e) {
			throw new MojoExecutionException("Artifact not found, " + e);
		}
	}
	
	private void extractArtifact(File source, File destination) throws MojoExecutionException {
		unArchiver.setDestDirectory(destination);
		unArchiver.setSourceFile(source);
		try {
			unArchiver.extract();
		} catch (ArchiverException e) {
			throw new MojoExecutionException("Could not extract artifact, " + e);
		} catch (IOException e) {
			throw new MojoExecutionException("Could not extract artifact, IO: " + e);
		}
	}

	
	public void setServiceRegistryFile(String serviceRegistryFile) {
		this.serviceRegistryFile = serviceRegistryFile;
	}

}
