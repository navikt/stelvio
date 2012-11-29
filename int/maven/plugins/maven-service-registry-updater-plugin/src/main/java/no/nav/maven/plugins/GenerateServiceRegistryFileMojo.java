package no.nav.maven.plugins;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import no.nav.serviceregistry.ServiceRegistry;
import no.nav.serviceregistry.ServiceInstance;

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
	 * @parameter expression="${oldServiceRegistryFile}"
	 * @required
	 */
	protected File oldServiceRegistryFile;

	protected HashMap<String, String> applications = new HashMap<String, String>();

	private String currentEndpoint;
	private File currentWsdlDir;

	protected File serviceReg;

	public void execute() throws MojoExecutionException, MojoFailureException {

		ServiceRegistry serviceRegistry = new ServiceRegistry();

		try {
			// les inn gammel service registry-fil (serviceRegistry) (unmarshal)
			buildDirectory.mkdir();
			serviceReg = new File(buildDirectory, "service-registry.xml");
		} catch (Exception e) {
			throw new MojoExecutionException("An error occured while trying to write service registry to file", e);
		}

		// Parse apps og versjoner (forberede rest-kall mot envconfig)
		parseApplicationsString();

		for (Map.Entry<String, String> appAndVersion : applications.entrySet()) {
			String application = appAndVersion.getKey();
			String version = appAndVersion.getValue();
			
			// Gjor sporring mot envconfig for a finne wsdls
			getInfoFromEnvconfig(application, version);
						
//			serviceRegistry.replaceApplicationBlock(currentEndpoint, currentWsdlDir, application);
			
			serviceRegistry.addServiceInstance(application, currentEndpoint, currentWsdlDir.toString());
			
			// for alle wsdler i pathToWSDL
			
			// Generer filblokk i minne
			
			// Finn applikasjonsblokk i gammel service-registry.xml, og slett 
			
			// Bytt ut med ny og skriv
			
			try {
				serviceRegistry.writeToFile(new File(buildDirectory, "service-registry.xml"));
			} catch (Exception e) {
				throw new MojoExecutionException("An error occured while trying to write service registry to file", e);
			}
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
		currentEndpoint = ""; // = client.getHostname(app, env)
		//groupId = client.getGroupId(app);
		currentWsdlDir = new File(buildDirectory, "/wsdl-" + app); // = undersøkes.
	}

}
