package no.nav.maven.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import no.nav.aura.envconfig.client.rest.ServiceGatewayRestClient;
import no.nav.serviceregistry.ServiceRegistry;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
//import org.codehaus.plexus.archiver.ArchiverException;
//import org.codehaus.plexus.archiver.UnArchiver;

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
//	/**
//	 * @component roleHint="zip"
//	 * @required
//	 * @readonly
//	 */
//	private UnArchiver unArchiver;

	/**
	 * Apps with services to be exposed formatted like "pselv:1.2.3, norg:3.2.1,Joark:2.1.3"
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

	protected HashMap<String, String> applications = new HashMap<String, String>();

	private String currentEndpoint;
	private File currentWsdlDir;

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

		// Parse apps og versjoner (forberede rest-kall mot envconfig)
		parseApplicationsString();
		// fjern versjon
		for (Map.Entry<String, String> appAndVersion : applications.entrySet()) {
			String application = appAndVersion.getKey();
			String version = appAndVersion.getValue();
			
			// Gjor sporring mot envconfig for a finne wsdls
			getInfoFromEnvconfig(application, version);
			
			getLog().debug("Trying to replace app block for application " + application + ", version " + version);
			serviceRegistry.replaceApplicationBlock(currentEndpoint, currentWsdlDir, application);
			System.out.println("BUILD DIR " + buildDirectory);
						
			// Bytt ut med ny og skriv
		}
		try {
			serviceRegistry.writeToFile(new File(serviceRegistryFile));
		} catch (Exception e) {
			throw new MojoExecutionException("An error occured while trying to write service registry to file", e);
		}
//		downloadAndExtractMavenArtifact("nav-fim-brukerprofil-tjenestespesifikasjon", "no.nav.tjenester.fim", "0.0.1-alpha002");
	}

	//fjern versjonsnummer
	private void parseApplicationsString() throws MojoExecutionException {
		// "pselv:1.2.3, norg:3.2.1,Joark:2.1.3" -> [<pselv, 1.2.3>, <norg, 3.2.1>, <joark, 2.1.3>]
		for (String applicationString : apps.split(",")) {

			String application = applicationString.split(":")[0].trim().toLowerCase();
			String version = applicationString.split(":")[1].trim();

			if (application == null || version == null) {
				throw new MojoExecutionException("Something is wrong with the 'apps'-string. Verify that it's on the correct format. " + apps, new NullPointerException());
			}

			getLog().debug("Adding " + application + ", " + version + " to applications");
			applications.put(application, version);
		}
	}

	private void getInfoFromEnvconfig(String app, String version) throws MojoExecutionException {

		//EnvConfigClient client = new EnvConfigClient... (rest-klient api fra testsenteret/plattformtjenester)

		ServiceGatewayRestClient client = new ServiceGatewayRestClient(baseUrl);
		// gjor sporring mot envconfig, returner endpoint og wsdl-artifakt
		currentEndpoint = "https://hostnavn.test.local:9443/"; // = client.getHostname(app, env)
		//groupId = client.getGroupId(app);
		currentWsdlDir = new File(userHome + "\\Kode\\trunk\\stelvio-int-maven\\poms\\maven-service-gw-provisioning-pom", "\\wsdl-" + app); // = undersøkes.
		getLog().debug("Setting endpoint and wsdl-dir to " + currentEndpoint + " and " + currentWsdlDir);
	}
	
	// tar inn mavenkoordinater for appconfig-artifakt, laster det ned, pakker ut, setter wsdl-lokasjon
	private void downloadAndExtractMavenArtifact(String artifactId, String groupId, String version) throws MojoExecutionException {

		Artifact pomArtifact = factory.createArtifact(groupId, artifactId, version, "", "zip");
		try {
			artifactResolver.resolve(pomArtifact, remoteRepositories, localRepository);
			System.out.println("Prøver å finne artifakt!! " + pomArtifact.getArtifactId());
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException("Could not resolve artifact, " + e);
		} catch (ArtifactNotFoundException e) {
			throw new MojoExecutionException("Artifact not found, " + e);
		}
		
//		unArchiver.setDestDirectory(currentWsdlDir);//ikke riktig
//		unArchiver.setSourceFile(pomArtifact.getFile());
//		try {
//			unArchiver.extract();
//		} catch (ArchiverException e) {
//			throw new MojoExecutionException("Could not extract artifact, " + e);
//		} catch (IOException e) {
//			throw new MojoExecutionException("Could not extract artifact, IO: " + e);
//		}

	}

	
	public void setServiceRegistryFile(String serviceRegistryFile) {
		this.serviceRegistryFile = serviceRegistryFile;
	}

}
