package no.nav.serviceregistry.util;

import static no.nav.serviceregistry.util.StringUtils.empty;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import no.nav.aura.appconfig.Application;
import no.nav.aura.envconfig.client.ApplicationInfo;
import no.nav.aura.envconfig.client.rest.ServiceGatewayRestClient;
import no.nav.serviceregistry.exception.ApplicationConfigException;

import org.apache.maven.plugin.MojoExecutionException;

public class AppConfigUtils {

	public static Application readAppConfig(File appConfig) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Application.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Application app = (Application) unmarshaller.unmarshal(appConfig);
		return app;
	}
	
	public static Set<String> parseApplicationsString(String appString) throws MojoExecutionException {
		if (empty(appString)){
			throw new ApplicationConfigException("You need to give the -freshInstall option or give applications(\"tys,pen,gosys\") as input to the -apps input!");
		}
		Set<String> apps = new HashSet<String>();
		for (String applicationString : appString.split(",")) {
			String application = applicationString.split(":")[0].trim().toLowerCase();
			if (empty(application)) {
				throw new ApplicationConfigException("Something is wrong with the 'apps'-string. Verify that it's on the correct format. " + apps);
			}
			apps.add(application);
		}
		return apps;
	}
	
	public static Application unmarshalAppConfig(String appConfigXml) throws MojoExecutionException {
		File appConfig = new File(appConfigXml);
		try {
			return AppConfigUtils.readAppConfig(appConfig);
		} catch (JAXBException e) {
			throw new ApplicationConfigException("app-config.xml in "+appConfigXml+" could not be read", e);
		}
	}
	
	public static Set<ApplicationInfo> getInfoFromEnvconfig(String environment, String baseUrl) throws MojoExecutionException {
		ServiceGatewayRestClient client = new ServiceGatewayRestClient(baseUrl);
		return client.getApplicationInfo(environment);
	}
}
