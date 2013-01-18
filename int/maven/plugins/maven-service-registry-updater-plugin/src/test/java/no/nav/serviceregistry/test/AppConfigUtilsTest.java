package no.nav.serviceregistry.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBException;

import no.nav.aura.appconfig.Application;
import no.nav.serviceregistry.util.AppConfigUtils;

import org.junit.Test;

public class AppConfigUtilsTest {

	@Test
	public void testReadAppConfig() throws JAXBException {
		URL appConfigUrl = this.getClass().getResource("/app-config.xml");
		AppConfigUtils acu = new AppConfigUtils();
		Application app = acu.readAppConfig(new File(appConfigUrl.getFile()));
		assertEquals("autodeploy-test", app.getName());
	}
}
