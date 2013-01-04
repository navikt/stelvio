package no.nav.serviceregistry.util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import no.nav.aura.appconfig.Application;

public class AppConfigUtils {
	
	public AppConfigUtils() {
		
	}

	public Application readAppConfig(File appConfig) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Application.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Application app = (Application) unmarshaller.unmarshal(appConfig);
		return app;
	}
}
