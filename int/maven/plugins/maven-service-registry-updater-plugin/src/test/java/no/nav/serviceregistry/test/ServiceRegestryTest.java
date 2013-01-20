package no.nav.serviceregistry.test;

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
	static final String	BRUKERPROFIL_WSDL = getResource("/nav-fim-brukerprofil-tjenestespesifikasjon-0.0.1-alpha002/Brukerprofil.wsdl");

	@Test
	public void testAddServiceInstanceStringStringString() throws JAXBException, MalformedURLException {
		String resultString = TestUtils.fileToString(getResource("/serviceregistry-testAddServiceInstanceStringStringString/serviceregistry-simple.result.xml"));
		ServiceRegistry serviceRegistry = new ServiceRegistry();
		serviceRegistry.addServiceInstance("popp", new URL("http://tset.com/"), BRUKERPROFIL_WSDL);
		assertEquals(resultString, serviceRegistry.toXml());
	}

	@Test
	public void testReadServiceRegistry() throws FileNotFoundException, JAXBException {
		String sr = getResource("/serviceregistry-testReadServiceRegistry/serviceregistry-simple.xml");
		String resultString = TestUtils.fileToString(getResource("/serviceregistry-testReadServiceRegistry/serviceregistry-simple.result.xml"));
		ServiceRegistry readSR = ServiceRegistryUtils.readServiceRegistryFromFile(sr);
		assertEquals(resultString, readSR.toXml());
	}

}
