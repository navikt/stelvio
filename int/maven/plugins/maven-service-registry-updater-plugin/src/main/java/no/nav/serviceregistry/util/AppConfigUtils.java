package no.nav.serviceregistry.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import no.nav.aura.appconfig.Application;
import no.nav.aura.envconfig.client.ApplicationInfo;
import no.nav.aura.envconfig.client.rest.ServiceGatewayRestClient;
import no.nav.serviceregistry.exception.ServiceRegistryException;
import no.nav.serviceregistry.model.ServiceRegistry;

import org.apache.maven.plugin.MojoExecutionException;

public class AppConfigUtils {

	public static Application readAppConfig(File appConfig) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Application.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Application app = (Application) unmarshaller.unmarshal(appConfig);
		return app;
	}
	
	public static Set<String> parseApplicationsString(String appString) throws MojoExecutionException {
		Set<String> apps = new HashSet<String>();
		for (String applicationString : appString.split(",")) {
			String application = applicationString.trim().toLowerCase();
			if (application == null) {
				throw new MojoExecutionException("Something is wrong with the 'apps'-string. Verify that it's on the correct format. " + apps, new NullPointerException());
			}
			apps.add(application);
		}
		return apps;
	}
	
	public static boolean empty(String s){
		if(s == null || "".equals(s)){
			return true;
		}
		return false;
	}
	
	public static Application unmarshalAppConfig(String appConfigXml) throws MojoExecutionException {
		File appConfig = new File(appConfigXml);
		try {
			return AppConfigUtils.readAppConfig(appConfig);
		} catch (JAXBException e) {
			throw new MojoExecutionException("app-config.xml in "+appConfigXml+" could not be read", e);
		}
	}
	
	public static Set<ApplicationInfo> getInfoFromEnvconfig(String environment, String baseUrl) throws MojoExecutionException {
		ServiceGatewayRestClient client = new ServiceGatewayRestClient(baseUrl);
		return client.getApplicationInfo(environment);
	}
	
	public static ServiceRegistry readServiceRegistryFromFile(File serviceRegistryPath, String serviceRegistryFile) throws MojoExecutionException {
		File buildOutputDirectory = new File(serviceRegistryPath, "/classes");
		buildOutputDirectory.mkdir();
		
		ServiceRegistry serviceRegistry = new ServiceRegistry();
		try {
			serviceRegistry.readServiceRegistry(serviceRegistryFile);
		} catch (FileNotFoundException e1) {
			//TODO: legg inn support for ingen fil!
			throw new ServiceRegistryException("Original service registry file not found", e1);
		} catch (JAXBException e1) {
			throw new ServiceRegistryException("XML processing went wrong", e1);
		}
		return serviceRegistry;
	}
}
