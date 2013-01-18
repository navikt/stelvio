package no.nav.maven.plugins;

import static no.nav.serviceregistry.util.AppConfigUtils.empty;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import no.nav.aura.appconfig.Application;
import no.nav.aura.appconfig.exposed.Service;
import no.nav.aura.envconfig.client.ApplicationInfo;
import no.nav.serviceregistry.ServiceRegistry;
import no.nav.serviceregistry.util.AppConfigUtils;
import no.nav.serviceregistry.util.ExposedService;
import no.nav.serviceregistry.util.MvnArtifact;

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
@SuppressWarnings("deprecation")
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


	/**
	 * This mojo takes an old service registry file as input and creates a java representation of it using JAXB.  
	 * The mojo then retrieves all applications in environment env and retrieves the app config artifact for all applications 
	 * that was given as input to the plugin.  Then the app config file is used to retrieve wsdl files for all exposed services 
	 * of the applications.  This information will be used to replace all information in the original service registry file 
	 * for each application.  
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		Set<String> applicationsFromInput = new HashSet<String>();
		getLog().debug("Retrieving info for environment " + env + " from envConfig!");
		Set<ApplicationInfo> applicationsFromEnvconfig = AppConfigUtils.getInfoFromEnvconfig(env, baseUrl);//gir et set av alle apps i miljoet
		ServiceRegistry serviceRegistry = readServiceRegistryFromFile(buildDirectory);

		if (!empty(apps)) {
			Set<String> parsedApps = AppConfigUtils.parseApplicationsString(apps);
			applicationsFromInput.addAll(parsedApps);
			getLog().debug("Addded applications: " + Arrays.toString(parsedApps.toArray()));
		}
		
		for (ApplicationInfo envConfigApplicationInfo : applicationsFromEnvconfig) {
			String applicationName = envConfigApplicationInfo.getName();

			if (empty(apps) || applicationsFromInput.contains(applicationName)){
				String hostname = envConfigApplicationInfo.getEndpoint();
				if (empty(hostname)) {
					getLog().warn("Maven coordinates needed to locate appConfig for application " + applicationName + " is missing");
					continue; //TODO: avslutte  når dette skjer?
				}
				
				File appConfigExtractDir = downloadAndExtractApplicationApplicationInfo(envConfigApplicationInfo, buildDirectory+ "/appConfDir-" + applicationName);
				
				getLog().debug("Reading app-config.xml for application " + applicationName);
				Application thisApp = AppConfigUtils.unmarshalAppConfig(appConfigExtractDir + "/app-config.xml");
				
				Collection<Service> services = thisApp.getExposedServices();
				Collection<ExposedService> exposedServices = new HashSet<ExposedService>(); 
				for (Service service : services) {
					String serviceName = service.getName();
					String wsdlDownloadDir = buildDirectory + "/wsdl-" + applicationName + "/" + serviceName;
					getLog().debug("Downloading WSDL into: " + wsdlDownloadDir);
					File serviceExtractDir = downloadAndExtractApplicationService(service, wsdlDownloadDir);

					exposedServices.add(new ExposedService(serviceName, service.getPath(), serviceExtractDir));
					getLog().debug("Added service " + serviceName);
				}

				getLog().debug("Replacing all information for application " + envConfigApplicationInfo);
				serviceRegistry.replaceApplicationBlock(applicationName, hostname, exposedServices);
			}
		}
		try {
			serviceRegistry.writeToFile(new File(serviceRegistryFile));
		} catch (Exception e) {
			throw new MojoExecutionException("An error occured while trying to write service registry to file", e);
		}
	}
//TODO: fiks etter at du har fikset serviceRegistry.readServiceRegistry metoden:
	private ServiceRegistry readServiceRegistryFromFile(File serviceRegistryPath) throws MojoExecutionException {
		File buildOutputDirectory = new File(this.buildDirectory, "/classes");
		buildOutputDirectory.mkdir();
		
		ServiceRegistry serviceRegistry = new ServiceRegistry(buildOutputDirectory);
		getLog().debug("Trying to read original service registry file...");
		try {
			serviceRegistry = serviceRegistry.readServiceRegistry(serviceRegistryFile);
		} catch (FileNotFoundException e1) {
			//TODO: legg inn support for ingen fil!
			throw new MojoExecutionException("Original service registry file not found", e1);
		} catch (JAXBException e1) {
			throw new MojoExecutionException("XML processing went wrong", e1);
		}
		getLog().debug("Original service registry file read!");
		return serviceRegistry;
	}
	
	private File downloadAndExtractApplicationApplicationInfo(ApplicationInfo appInfo, String extractTo) throws MojoExecutionException{
		MvnArtifact artifact = new MvnArtifact(appInfo, "jar");
		return downloadAndExtract(artifact, extractTo);
	}
	
	private File downloadAndExtractApplicationService(Service service, String extractTo) throws MojoExecutionException{
		MvnArtifact artifact = new MvnArtifact(service, "zip");
		return downloadAndExtract(artifact, extractTo);
	}
	
	public File downloadMavenArtifact(MvnArtifact mvnArtifact) throws MojoExecutionException {

		Artifact pomArtifact = factory.createArtifact(mvnArtifact.getGroupId(), mvnArtifact.getArtifactId(), mvnArtifact.getVersion(), "", mvnArtifact.getType());
		getLog().debug("Resolving artifact: "+ mvnArtifact);
		try {
			artifactResolver.resolve(pomArtifact, remoteRepositories, localRepository);
			return pomArtifact.getFile();
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException("Could not resolve artifact, " + e);
		} catch (ArtifactNotFoundException e) {
			throw new MojoExecutionException("Artifact not found, " + e);
		}
	}
	
	private File downloadAndExtract(MvnArtifact artifact, String extractTo) throws MojoExecutionException{
		File zip = downloadMavenArtifact(artifact);
		File extractToFile = new File(extractTo);
		extractToFile.mkdirs();
		extractArtifact(zip, extractToFile);
		return extractToFile;
	}
	
	private void extractArtifact(File source, File destination) throws MojoExecutionException {

		unArchiver.setDestDirectory(destination);
		unArchiver.setSourceFile(source);
		try {
			unArchiver.extract();
		} catch (ArchiverException e) {
			throw new MojoExecutionException("Could not extract artifact: " + source + "\n" + e);
		} catch (IOException e) {
			throw new MojoExecutionException("Could not extract artifact(IO error): " + source + "\n" + e);
		}
	}
	
	public void setServiceRegistryFile(String serviceRegistryFile) {
		this.serviceRegistryFile = serviceRegistryFile;
	}
}
