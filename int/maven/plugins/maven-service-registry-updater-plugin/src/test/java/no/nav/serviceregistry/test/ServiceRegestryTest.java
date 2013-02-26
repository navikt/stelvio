package no.nav.serviceregistry.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static no.nav.serviceregistry.test.util.TestUtils.getResource;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import no.nav.serviceregistry.model.ServiceRegistry;
import no.nav.serviceregistry.test.util.TestUtils;
import no.nav.serviceregistry.util.ServiceRegistryUtils;

import org.junit.Test;

public class ServiceRegestryTest {
	static final String	BRUKERPROFIL_WSDL = getResource("/ServiceRegistryTest/nav-fim-brukerprofil-tjenestespesifikasjon-0.0.1-alpha002/Brukerprofil.wsdl");

	@Test
	public void testAddServiceInstanceStringStringString() throws JAXBException, MalformedURLException {
		String resultString = TestUtils.fileToString(getResource("/ServiceRegistryTest/serviceregistry-testAddServiceInstanceStringStringString/serviceregistry-simple.xml"));
		ServiceRegistry serviceRegistry = new ServiceRegistry();
		serviceRegistry.addServiceInstance("popp", new URL("http://tset.com/"), BRUKERPROFIL_WSDL);
		assertEquals(resultString, serviceRegistry.toXml());
	}

	@Test
	public void testReadServiceRegistry() throws FileNotFoundException, JAXBException {
		String sr = getResource("/ServiceRegistryTest/serviceregistry-testReadServiceRegistry/serviceregistry-simple.xml");
		ServiceRegistry readSR = ServiceRegistryUtils.readServiceRegistryFromFile(sr);

		String result = readSR.toXml();
		assertThat(result, containsString("<application>autodeploy-test</application>"));
		assertThat(result, containsString("<application>popp</application>"));
		assertThat(result, containsString("<application>tsys</application>"));
		assertThat(result, containsString("<application>pselv</application>"));
	}

}

