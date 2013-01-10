package no.nav.maven.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import no.nav.aura.appconfig.Application;
import no.nav.aura.appconfig.exposed.Service;
import no.nav.aura.envconfig.client.ApplicationInfo;
import no.nav.aura.envconfig.client.rest.ServiceGatewayRestClient;
import no.nav.serviceregistry.ServiceRegistry;
import no.nav.serviceregistry.util.AppConfigUtils;
import no.nav.serviceregistry.util.ExposedService;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
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
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	protected File buildDirectory;
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
	private UnArchiver unArchiver;//vurder aa endre til role,rolehint siden denne syntaksen er deprecated

	/**
	 * Apps with services to be exposed formatted like "pselv, norg,Joark"
	 * 
	 * @parameter
	 * 
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

	protected Set<String> applicationsFromInput = new HashSet<String>();
	protected Set<ApplicationInfo> applicationsFromEnvconfig = new HashSet<ApplicationInfo>();

	/**
	 * This mojo takes an old service registry file as input and creates a java representation of it using JAXB.  
	 * The mojo then retrieves all applications in environment env and retrieves the app config artifact for all applications 
	 * that was given as input to the plugin.  Then the app config file is used to retrieve wsdl files for all exposed services 
	 * of the applications.  This information will be used to replace all information in the original service registry file 
	 * for each application.  
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		File buildOutputDirectory = new File(buildDirectory, "/classes");
		buildOutputDirectory.mkdir();
		
		ServiceRegistry serviceRegistry = new ServiceRegistry(buildOutputDirectory);
		getLog().debug("Trying to read original service registry file...");
		try {
			serviceRegistry = serviceRegistry.readServiceRegistry(serviceRegistryFile);
		} catch (FileNotFoundException e1) {
			//legg inn support for ingen fil!
			throw new MojoExecutionException("Original service registry file not found", e1);
		} catch (JAXBException e1) {
			throw new MojoExecutionException("XML processing went wrong", e1);
		}
		getLog().debug("Original service registry file read!");

		if (apps != null) {
			parseApplicationsString();
		}
		getInfoFromEnvconfig(env, baseUrl);//gir et set av alle apps i miljoet
		
		for (ApplicationInfo application : applicationsFromEnvconfig) {
			String applicationName = application.getName();

			//sjekk om apps er null eller ""!
			if (apps == null || applicationsFromInput.contains(applicationName)){
				getLog().debug("Retrieving maven coordinates for appConfig artifact for application " + applicationName);
				String hostname = application.getEndpoint();
				String appConfArtifactID = application.getAppConfigArtifactId();
				String appConfGroupId = application.getAppConfigGroupId();
				String appConfigVersion = application.getVersion();
				if (hostname == null || appConfArtifactID == null || appConfGroupId == null || appConfigVersion == null) {
					getLog().warn("Maven coordinates needed to locate appConfig for application " + applicationName + " is missing");
					continue;
					//skal det være null eller ""?
				}
				
				//extract appConfig-artifact
				File appConfigJar = downloadMavenArtifact(appConfArtifactID, appConfGroupId, appConfigVersion, "jar");
				File extractAppConfigTo = new File(buildDirectory, "/appConfDir-" + applicationName);
				extractAppConfigTo.mkdir();
				extractArtifact(appConfigJar, extractAppConfigTo);
				
				//Unmarshal app-config.xml
				File appConfig = new File(extractAppConfigTo, "/app-config.xml");
				AppConfigUtils util = new AppConfigUtils();
				Application thisApp = null;
				getLog().debug("Reading app-config.xml for application " + applicationName);
				try {
					thisApp = util.readAppConfig(appConfig);
				} catch (JAXBException e) {
					throw new MojoExecutionException("app-config.xml for application " + applicationName + " could not be read", e);
				}
				
				Collection<Service> services = thisApp.getExposedServices();
				File wsdlDir = new File(buildDirectory, "/wsdl-" + applicationName);
				wsdlDir.mkdir();
				Collection<ExposedService> exposedServices = new HashSet<ExposedService>(); 
				for (Service service : services) {
					String serviceName = service.getName();
					String path = service.getPath();
					getLog().debug("Retrieving maven coordinates for artifact containing wsdl files for service " + serviceName);
					String wsdlArtifactId = service.getWsdlArtifactId();
					String wsdlGroupId = service.getWsdlGroupId();
					String wsdlVersion = service.getWsdlVersion();
					if (path == null || wsdlArtifactId == null || wsdlGroupId == null || wsdlVersion == null) {
						getLog().warn("Maven coordinates needed to locate wsdl artifact for service " + serviceName + " is missing");
						//skal hele jobben feile i dette tilfellet?
						continue;
						//skal det være null eller ""?						
					}
					File serviceZip = downloadMavenArtifact(wsdlArtifactId, wsdlGroupId, wsdlVersion, "zip");
					File extractServiceTo = new File(wsdlDir, "/" + serviceName);
					extractServiceTo.mkdir();
					extractArtifact(serviceZip, extractServiceTo);

					exposedServices.add(new ExposedService(serviceName, path, extractServiceTo));
					getLog().debug("Added service " + serviceName);
				}

				getLog().debug("Replacing all information for application " + application);
				serviceRegistry.replaceApplicationBlock(applicationName, hostname, exposedServices);
			}
		}
		try {
			serviceRegistry.writeToFile(new File(serviceRegistryFile));
		} catch (Exception e) {
			throw new MojoExecutionException("An error occured while trying to write service registry to file", e);
		}
	}

	private void parseApplicationsString() throws MojoExecutionException {
		
		for (String applicationString : apps.split(",")) {
			String application = applicationString.trim().toLowerCase();
			if (application == null) {
				throw new MojoExecutionException("Something is wrong with the 'apps'-string. Verify that it's on the correct format. " + apps, new NullPointerException());
			}
			getLog().debug("Adding " + application + " to applications");
			applicationsFromInput.add(application);
		}
	}

	private void getInfoFromEnvconfig(String environment, String baseUrl) throws MojoExecutionException {

		ServiceGatewayRestClient client = new ServiceGatewayRestClient(baseUrl);
		applicationsFromEnvconfig = client.getApplicationInfo(environment);
		getLog().debug("Info for environment " + environment + " retrieved from envConfig");		
	}
	
	private File downloadMavenArtifact(String artifactId, String groupId, String version, String type) throws MojoExecutionException {

		Artifact pomArtifact = factory.createArtifact(groupId, artifactId, version, "", type);
		getLog().debug("Resolving artifact, artifactId " + artifactId + ", groupId " + groupId + ", version " + version);
		try {
			artifactResolver.resolve(pomArtifact, remoteRepositories, localRepository);
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
