package no.nav.maven.plugins;

import static no.nav.serviceregistry.util.StringUtils.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import no.nav.aura.appconfig.Application;
import no.nav.aura.appconfig.exposed.Service;
import no.nav.aura.envconfig.client.ApplicationInfo;
import no.nav.serviceregistry.exception.ApplicationNotInEnvConfigException;
import no.nav.serviceregistry.exception.MavenArtifactResolevException;
import no.nav.serviceregistry.exception.ServiceRegistryException;
import no.nav.serviceregistry.model.ServiceRegistry;
import no.nav.serviceregistry.util.AppConfigUtils;
import no.nav.serviceregistry.util.MvnArtifact;
import no.nav.serviceregistry.util.ServiceRegistryUtils;
import no.nav.serviceregistry.util.ServiceWrapper;
import no.nav.serviceregistry.util.Testdata;

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
	 * @parameter expression="${apps}"
	 * 
	 */
	protected String applicationsString;

	/**
	 * Boolean paramerter that you set as true if you are installing a new environment, or need a fresh install
	 * 
	 * @parameter default-value="false"
	 */
	protected Boolean isFreshInstall;

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


	protected Testdata testdata;


	/**
	 * This mojo takes an old service registry file as input and creates a java representation of it using JAXB.  
	 * The mojo then retrieves all applications in environment env and retrieves the app config artifact for all applications 
	 * that was given as input to the plugin.  Then the app config file is used to retrieve wsdl files for all exposed services 
	 * of the applications.  This information will be used to replace all information in the original service registry file 
	 * for each application.  
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		testableMojoExecutor(applicationsString, serviceRegistryFile);
	}

	public void testableMojoExecutor(String applicationsString, String serviceRegistryFile) throws MojoExecutionException {
		Set<ApplicationInfo> applicationsFromEnvconfig;
		ServiceRegistry serviceRegistry;
		if (isFreshInstall){
			getLog().debug("Retrieving all applications for environment " + env + " from envConfig...");
			applicationsFromEnvconfig = getAllApplikationsFromEnvConfig();
			serviceRegistry = new ServiceRegistry();
		}
		else{
			getLog().debug("Reading original service registry file...");
			serviceRegistry = ServiceRegistryUtils.readServiceRegistryFromFile(serviceRegistryFile);
			getLog().debug("Retrieving info for "+ applicationsString +" in environment " + env + " from envConfig...");
			Set<String> applicationNames = AppConfigUtils.parseApplicationsString(applicationsString);
			applicationsFromEnvconfig = getFilterdApplikationsFromEnvConfig(applicationNames);
		}

		for (ApplicationInfo envConfigApplicationInfo : applicationsFromEnvconfig) {
			String applicationName = envConfigApplicationInfo.getName();

			String hostname = envConfigApplicationInfo.getEndpoint();
			if (empty(hostname)) throw new ServiceRegistryException("Hostname for " + applicationName + " is missing");

			File appConfigExtractDir = downloadAndExtractApplicationInfo(envConfigApplicationInfo, buildDirectory+ "/appConfDir-" + applicationName);

			getLog().debug("Reading app-config.xml for application " + applicationName);
			Application thisApp = AppConfigUtils.unmarshalAppConfig(appConfigExtractDir + "/app-config.xml");

			Collection<Service> services = thisApp.getExposedServices();
			Collection<ServiceWrapper> exposedServices = getExposedServices(applicationName, services);

			getLog().debug("Replacing all information for application " + applicationName);
			serviceRegistry.replaceApplicationBlock(applicationName, hostname, exposedServices);
		}
		try {
			ServiceRegistryUtils.writeToFile(serviceRegistry, new File(serviceRegistryFile));
		} catch (Exception e) {
			throw new MojoExecutionException("An error occured while trying to write service registry to file", e);
		}
	}

	private Set<ApplicationInfo> getAllApplikationsFromEnvConfig() throws MojoExecutionException {
		if(testdata!=null) return testdata.getEnvConfigApplications();
		return AppConfigUtils.getInfoFromEnvconfig(env, baseUrl);//gir et set av alle apps i miljoet
	}

	private Set<ApplicationInfo> getFilterdApplikationsFromEnvConfig(Set<String> applicationNames) throws MojoExecutionException {
		Set<ApplicationInfo> output = new HashSet<ApplicationInfo>();
		Set<ApplicationInfo> applicationsFromEnvconfig = getAllApplikationsFromEnvConfig();
		for (ApplicationInfo applicationInfo : applicationsFromEnvconfig) {
			for (String applicationName : applicationNames) {
				if(applicationInfo.getName().equals(applicationName)){
					output.add(applicationInfo);
					applicationNames.remove(applicationName);
					break;
				}
			}
		}

		if (applicationNames.size() != 0) throw new ApplicationNotInEnvConfigException("Could not find this/theese application(s) in envConfig"+ Arrays.toString(applicationNames.toArray()));
		getLog().debug("Addded these applications:");
		for (ApplicationInfo applicationInfo : output) {
			getLog().debug(applicationInfo.getName());
		}
		return output;
	}

	private Collection<ServiceWrapper> getExposedServices(String applicationName, Collection<Service> services)	throws MojoExecutionException {
		Collection<ServiceWrapper> exposedServices = new HashSet<ServiceWrapper>(); 
		for (Service service : services) {
			String serviceName = service.getName();
			String wsdlDownloadDir = buildDirectory + "/wsdl-" + applicationName + "/" + serviceName;
			getLog().debug("Downloading WSDL into: " + wsdlDownloadDir);
			File serviceExtractDir = downloadAndExtractService(service, wsdlDownloadDir);

			exposedServices.add(new ServiceWrapper(serviceName, service.getPath(), serviceExtractDir));
			getLog().debug("Added service " + serviceName);
		}
		return exposedServices;
	}

	private File downloadAndExtractApplicationInfo(ApplicationInfo appInfo, String extractTo) throws MojoExecutionException{
		MvnArtifact artifact = new MvnArtifact(appInfo, "jar");
		if(testdata==null){
			return downloadAndExtract(artifact, extractTo);
		}else{
			return testdata.getAppConfigExtractDir();
		}
	}

	private File downloadAndExtractService(Service service, String extractTo) throws MojoExecutionException{
		MvnArtifact artifact = new MvnArtifact(service, "zip");
		if(testdata==null){
			return downloadAndExtract(artifact, extractTo);
		}else{
			return testdata.getServiceExtractDir();
		}
	}

	private File downloadAndExtract(MvnArtifact artifact, String extractTo) throws MojoExecutionException{
		File zip = downloadMavenArtifact(artifact);
		File extractToFile = new File(extractTo);
		extractToFile.mkdirs();
		extractArtifact(zip, extractToFile);
		return extractToFile;
	}

	public File downloadMavenArtifact(MvnArtifact mvnArtifact) {

		Artifact pomArtifact = factory.createArtifact(mvnArtifact.getGroupId(), mvnArtifact.getArtifactId(), mvnArtifact.getVersion(), "", mvnArtifact.getType());
		getLog().debug("Resolving artifact: "+ mvnArtifact);
		try {
			artifactResolver.resolve(pomArtifact, remoteRepositories, localRepository);
			return pomArtifact.getFile();
		} catch (ArtifactResolutionException e) {
			throw new MavenArtifactResolevException("Could not resolve artifact, " + e);
		} catch (ArtifactNotFoundException e) {
			throw new MavenArtifactResolevException("Artifact not found, " + e);
		}
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

	public void setTestdata(Testdata testdata) {
		this.testdata = testdata;
	}

	public void setFreshInstall(boolean freshInstall) {
		this.isFreshInstall = freshInstall;
	}
}
