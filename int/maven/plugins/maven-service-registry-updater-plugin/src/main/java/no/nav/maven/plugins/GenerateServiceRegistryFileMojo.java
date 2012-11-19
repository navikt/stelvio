package no.nav.maven.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
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
	 * @parameter
	 * @required
	 */
	protected String oldServiceReg;

	protected HashMap<String, String> applications = new HashMap<String, String>();
	
	private String endpoint;
	private String pathToWSDL;
	
	protected File serviceReg;

	public void execute() throws MojoExecutionException, MojoFailureException {
		
		ServiceRegistry serviceRegistry = new ServiceRegistry();
		
		// lag ny service-registry-fil
		try {
			buildDirectory.mkdir();
			serviceReg = new File(buildDirectory,
			"service-registry.xml");
		} catch (Exception e) {
			throw new MojoExecutionException(
					"An error occured while trying to write service registry to file",
					e);
		}
		
		// Parse apps og versjoner (forberede rest-kall mot envconfig)
		parseApplicationsString();
		
		// For hver app:  Gjor rest-sporring for endpoint og wsdl
		Iterator it = applications.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, String> appAndVersion = (Map.Entry<String, String>) it.next();
			
			// Gjor sporring mot envconfig for a finne wsdls
			getInfoFromEnvconfig(appAndVersion.getKey(), appAndVersion.getValue());
			
			// Generer filblokk
			try {
				serviceRegistry.writeToFile(new File(buildDirectory,
				"service-registry.xml"));
			} catch (Exception e) {
				throw new MojoExecutionException(
						"An error occured while trying to write service registry to file",
						e);
			}

			it.remove();
			
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
		
		// gjor sporring mot envconfig, returner endpoint og wsdl-artifakt
		endpoint = "";
		pathToWSDL = "";
	}

}
