package no.nav.serviceregistry.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import no.nav.serviceregistry.model.ServiceRegistry;
import no.nav.serviceregistry.util.ServiceRegistryUtils;

import org.junit.Test;

public class ServiceRegestryTest {
	static final String	BRUKERPROFIL_WSDL = ServiceRegestryTest.class.getClass().getResource("/nav-fim-brukerprofil-tjenestespesifikasjon-0.0.1-alpha002/Brukerprofil.wsdl").getFile();
	static final String	BRUKER_PROFIL_SERVICEREGISTRY_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<ns2:serviceRegistry xmlns:ns2=\"http://www.stelvio.no/ServiceRegistry\">\n    <service xmlns:ns3=\"http://nav.no/tjeneste/virksomhet/brukerprofil/v1/\" name=\"ns3:Brukerprofil\">\n        <application>popp</application>\n        <serviceVersion bindingName=\"ns3:BrukerprofilBinding\">\n            <operation action=\"http://nav.no/tjeneste/virksomhet/brukerprofil/v1/BrukerprofilPortType/hentKontaktinformasjonOgPreferanser\" name=\"hentKontaktinformasjonOgPreferanser\"/>\n            <serviceInstance endpoint=\"http://tset.com/\" wsdlAddress=\"http://tset.com/?wsdl\"/>\n        </serviceVersion>\n    </service>\n</ns2:serviceRegistry>\n";
	static final String	SERVICE_REGISTRY_XML = ServiceRegestryTest.class.getClass().getResource("/serviceregistry-simple.xml").getFile();
	static final String	STD_SERVICE_REGISTRY_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<ns2:serviceRegistry xmlns:ns2=\"http://www.stelvio.no/ServiceRegistry\">\n    <service xmlns:ns3=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding\" name=\"ns3:OpptjeningWSEXP_OpptjeningHttpService\">\n        <application>popp</application>\n        <serviceVersion bindingName=\"ns3:OpptjeningWSEXP_OpptjeningHttpBinding\">\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/hentBeholdningListe\" name=\"hentBeholdningListe\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/hentInntektListe\" name=\"hentInntektListe\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/hentOpptjeningsgrunnlag\" name=\"hentOpptjeningsgrunnlag\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/hentPensjonspoengListe\" name=\"hentPensjonspoengListe\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/hentRestpensjonListe\" name=\"hentRestpensjonListe\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/lagreDagpenger\" name=\"lagreDagpenger\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/lagreForstegangstjeneste\" name=\"lagreForstegangstjeneste\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/lagreInntekt\" name=\"lagreInntekt\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/lagreOmsorgBolk\" name=\"lagreOmsorgBolk\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/overforOmsorgBolk\" name=\"overforOmsorgBolk\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/slettInntekt\" name=\"slettInntekt\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/slettDagpenger\" name=\"slettDagpenger\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/slettForstegangstjeneste\" name=\"slettForstegangstjeneste\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/slettOmsorg\" name=\"slettOmsorg\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/hentOpptjeningsgrunnlagHistorikk\" name=\"hentOpptjeningsgrunnlagHistorikk\"/>\n            <serviceInstance endpoint=\"https://hostnavn.test.local:9443/moduleNameWeb/sca/exportName\" wsdlAddress=\"https://hostnavn.test.local:9443/moduleNameWeb/sca/exportName?wsdl\"/>\n        </serviceVersion>\n    </service>\n    <service xmlns:ns3=\"http://nav.no/tjeneste/virksomhet/behandleBrukerprofil/v1/\" name=\"ns3:BehandleBrukerprofil\">\n        <application>pselv</application>\n        <serviceVersion bindingName=\"ns3:BehandleBrukerprofilBinding\">\n            <operation action=\"http://nav.no/tjeneste/virksomhet/behandleBrukerprofil/v1/BehandleBrukerprofilPortType/oppdaterKontaktinformasjonOgPreferanser\" name=\"oppdaterKontaktinformasjonOgPreferanser\"/>\n            <serviceInstance endpoint=\"https://hostnavn.test.local:9443/moduleNameWeb/sca/exportName\" wsdlAddress=\"https://hostnavn.test.local:9443/moduleNameWeb/sca/exportName?wsdl\"/>\n        </serviceVersion>\n    </service>\n    <service xmlns:ns3=\"http://nav.no/tjeneste/virksomhet/brukerprofil/v1/\" name=\"ns3:Brukerprofil\">\n        <application>tsys</application>\n        <serviceVersion bindingName=\"ns3:BrukerprofilBinding\">\n            <operation action=\"http://nav.no/tjeneste/virksomhet/brukerprofil/v1/BrukerprofilPortType/hentKontaktinformasjonOgPreferanser\" name=\"hentKontaktinformasjonOgPreferanser\"/>\n            <serviceInstance endpoint=\"https://hostname.no:443//path/til/tsys\" wsdlAddress=\"https://hostname.no:443//path/til/tsys?wsdl\"/>\n        </serviceVersion>\n    </service>\n    <service xmlns:ns3=\"http://nav.no/tjeneste/virksomhet/brukerprofil/v1/\" name=\"ns3:Brukerprofil\">\n        <application>autodeploy-test</application>\n        <serviceVersion bindingName=\"ns3:BrukerprofilBinding\">\n            <operation action=\"http://nav.no/tjeneste/virksomhet/brukerprofil/v1/BrukerprofilPortType/hentKontaktinformasjonOgPreferanser\" name=\"hentKontaktinformasjonOgPreferanser\"/>\n            <serviceInstance endpoint=\"https://e34apsl00120.devillo.no:443/test/brukerprofil\" wsdlAddress=\"https://e34apsl00120.devillo.no:443/test/brukerprofil?wsdl\"/>\n        </serviceVersion>\n    </service>\n</ns2:serviceRegistry>\n";
	
	@Test
	public void testServiceRegistryFile() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetServices() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSetServices() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReplaceApplicationBlock() {
		//fail("Not yet implemented");
	}

	@Test
	public void testAddServiceInstanceStringStringString() throws JAXBException, MalformedURLException {
		ServiceRegistry sr = new ServiceRegistry();
		sr.addServiceInstance("popp", new URL("http://tset.com/"), BRUKERPROFIL_WSDL);
		assertEquals(BRUKER_PROFIL_SERVICEREGISTRY_XML, sr.toXml());
	}

	@Test
	public void testAddServiceInstanceQNameStringQNameServiceInstanceListOfServiceOperation() {
		//ServiceRegistry serviceRegistry = new ServiceRegistry();
		//serviceRegistry.addServiceInstance(serviceName, applicationName, bindingName, serviceInstance, operations)
		//fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		//fail("Not yet implemented");
	}

	@Test
	public void testToXml() {
		//fail("Not yet implemented");
	}

	@Test
	public void testToDocument() {
		//fail("Not yet implemented");
	}

	@Test
	public void testWriteToFile() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadServiceRegistry() throws FileNotFoundException, JAXBException {
		ServiceRegistry readSR = ServiceRegistryUtils.readServiceRegistryFromFile(SERVICE_REGISTRY_XML);
		assertEquals(STD_SERVICE_REGISTRY_XML, readSR.toXml());
	}

}
