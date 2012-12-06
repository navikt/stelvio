package no.nav.maven.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import no.nav.serviceregistry.ServiceRegistry;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

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
//	/**
//	 * @parameter expression="${session}"
//	 * @readonly
//	 * @required
//	 */
//	protected MavenSession session;
//
//	/**
//	 * @parameter expression="${project}"
//	 * @readonly
//	 * @required
//	 */
//	protected MavenProject project;
//
//	/**
//	 * @component
//	 * @required
//	 */
//	protected BuildPluginManager buildPluginManager;
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
	 * Path to old service-registry.xml
	 * 
	 * @parameter
	 * @required
	 */
	protected String oldServiceRegistryFile;

	protected HashMap<String, String> applications = new HashMap<String, String>();

	private String currentEndpoint;
	private File currentWsdlDir;

	protected File serviceReg;

	public void execute() throws MojoExecutionException, MojoFailureException {

		// unmarshal gammel fil!
		ServiceRegistry serviceRegistry = new ServiceRegistry();
		getLog().debug("Trying to read service registry file...");
		try {
			serviceRegistry = serviceRegistry.readServiceRegistry(oldServiceRegistryFile);
		} catch (FileNotFoundException e1) {
			throw new MojoExecutionException("Old service registry file not found", e1);
		} catch (JAXBException e1) {
			throw new MojoExecutionException("XML processing went wrong", e1);
		}
		getLog().debug("Service registry file read! (or sort of read...)");

		// Parse apps og versjoner (forberede rest-kall mot envconfig)
		parseApplicationsString();

		for (Map.Entry<String, String> appAndVersion : applications.entrySet()) {
			String application = appAndVersion.getKey();
			String version = appAndVersion.getValue();
			
			// Gjor sporring mot envconfig for a finne wsdls
			getInfoFromEnvconfig(application, version);
			
			getLog().debug("Trying to replace app block for application " + application + ", version " + version);
			serviceRegistry.replaceApplicationBlock(currentEndpoint, currentWsdlDir, application);
						
			// Bytt ut med ny og skriv
		}
		try {
			serviceRegistry.writeToFile(new File(buildDirectory, "service-registry.xml"));
		} catch (Exception e) {
			throw new MojoExecutionException("An error occured while trying to write service registry to file", e);
		}
	}

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
		// gjor sporring mot envconfig, returner endpoint og wsdl-artifakt
		currentEndpoint = "https://hostnavn.test.local:9443/"; // = client.getHostname(app, env)
		//groupId = client.getGroupId(app);
		currentWsdlDir = new File(userHome + "\\Kode\\trunk\\stelvio-int-maven\\poms\\maven-service-gw-provisioning-pom", "\\wsdl-" + app); // = undersøkes.
		getLog().debug("Setting endpoint and wsdl-dir to " + currentEndpoint + " and " + currentWsdlDir);
	}

	public void setOldServiceRegistryFile(String oldServiceRegistryFile) {
		this.oldServiceRegistryFile = oldServiceRegistryFile;
	}

}
