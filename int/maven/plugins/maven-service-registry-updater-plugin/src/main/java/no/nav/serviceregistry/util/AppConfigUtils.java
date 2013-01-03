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
		System.out.println("I metoden");
//		JAXBContext context = JAXBContext.newInstance("no.nav.aura.appconfig");
		JAXBContext context = JAXBContext.newInstance(Application.class);
		System.out.println("lager context, " + context);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		System.out.println("Skal unmarshalle, " + unmarshaller);
		Application app = (Application) unmarshaller.unmarshal(appConfig);
		System.out.println("har lest");
		return app;
	}
}
