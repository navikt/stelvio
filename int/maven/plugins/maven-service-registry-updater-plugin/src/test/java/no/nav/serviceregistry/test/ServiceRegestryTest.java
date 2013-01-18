package no.nav.serviceregistry.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import no.nav.serviceregistry.ServiceRegistry;

import org.junit.Test;

public class ServiceRegestryTest {

	private enum Values {
		BRUKERPROFIL_WSDL("/nav-fim-brukerprofil-tjenestespesifikasjon-0.0.1-alpha002/Brukerprofil.wsdl"),
		BRUKER_PROFIL_SERVICEREGISTRY_XML("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<ns2:serviceRegistry xmlns:ns2=\"http://www.stelvio.no/ServiceRegistry\">\n    <service xmlns:ns3=\"http://nav.no/tjeneste/virksomhet/brukerprofil/v1/\" name=\"ns3:Brukerprofil\">\n        <application>popp</application>\n        <serviceVersion bindingName=\"ns3:BrukerprofilBinding\">\n            <operation action=\"http://nav.no/tjeneste/virksomhet/brukerprofil/v1/BrukerprofilPortType/hentKontaktinformasjonOgPreferanser\" name=\"hentKontaktinformasjonOgPreferanser\"/>\n            <serviceInstance endpoint=\"http://tset.com/\" wsdlAddress=\"http://tset.com/?wsdl\"/>\n        </serviceVersion>\n    </service>\n</ns2:serviceRegistry>\n"),
		SERVICE_REGISTRY_XML("/serviceregistry-simple.xml"),
		STD_SERVICE_REGISTRY_XML("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<ns2:serviceRegistry xmlns:ns2=\"http://www.stelvio.no/ServiceRegistry\">\n    <service xmlns:ns3=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding\" name=\"ns3:OpptjeningWSEXP_OpptjeningHttpService\">\n        <application>popp</application>\n        <serviceVersion bindingName=\"ns3:OpptjeningWSEXP_OpptjeningHttpBinding\">\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/hentBeholdningListe\" name=\"hentBeholdningListe\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/hentInntektListe\" name=\"hentInntektListe\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/hentOpptjeningsgrunnlag\" name=\"hentOpptjeningsgrunnlag\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/hentPensjonspoengListe\" name=\"hentPensjonspoengListe\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/hentRestpensjonListe\" name=\"hentRestpensjonListe\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/lagreDagpenger\" name=\"lagreDagpenger\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/lagreForstegangstjeneste\" name=\"lagreForstegangstjeneste\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/lagreInntekt\" name=\"lagreInntekt\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/lagreOmsorgBolk\" name=\"lagreOmsorgBolk\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/overforOmsorgBolk\" name=\"overforOmsorgBolk\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/slettInntekt\" name=\"slettInntekt\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/slettDagpenger\" name=\"slettDagpenger\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/slettForstegangstjeneste\" name=\"slettForstegangstjeneste\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/slettOmsorg\" name=\"slettOmsorg\"/>\n            <operation action=\"http://nav.no/virksomhet/tjenester/opptjening/v1/Binding/hentOpptjeningsgrunnlagHistorikk\" name=\"hentOpptjeningsgrunnlagHistorikk\"/>\n            <serviceInstance endpoint=\"https://hostnavn.test.local:9443/moduleNameWeb/sca/exportName\" wsdlAddress=\"https://hostnavn.test.local:9443/moduleNameWeb/sca/exportName?wsdl\"/>\n        </serviceVersion>\n    </service>\n    <service xmlns:ns3=\"http://nav.no/tjeneste/virksomhet/behandleBrukerprofil/v1/\" name=\"ns3:BehandleBrukerprofil\">\n        <application>pselv</application>\n        <serviceVersion bindingName=\"ns3:BehandleBrukerprofilBinding\">\n            <operation action=\"http://nav.no/tjeneste/virksomhet/behandleBrukerprofil/v1/BehandleBrukerprofilPortType/oppdaterKontaktinformasjonOgPreferanser\" name=\"oppdaterKontaktinformasjonOgPreferanser\"/>\n            <serviceInstance endpoint=\"https://hostnavn.test.local:9443/moduleNameWeb/sca/exportName\" wsdlAddress=\"https://hostnavn.test.local:9443/moduleNameWeb/sca/exportName?wsdl\"/>\n        </serviceVersion>\n    </service>\n    <service xmlns:ns3=\"http://nav.no/tjeneste/virksomhet/brukerprofil/v1/\" name=\"ns3:Brukerprofil\">\n        <application>tsys</application>\n        <serviceVersion bindingName=\"ns3:BrukerprofilBinding\">\n            <operation action=\"http://nav.no/tjeneste/virksomhet/brukerprofil/v1/BrukerprofilPortType/hentKontaktinformasjonOgPreferanser\" name=\"hentKontaktinformasjonOgPreferanser\"/>\n            <serviceInstance endpoint=\"https://hostname.no:443//path/til/tsys\" wsdlAddress=\"https://hostname.no:443//path/til/tsys?wsdl\"/>\n        </serviceVersion>\n    </service>\n    <service xmlns:ns3=\"http://nav.no/tjeneste/virksomhet/brukerprofil/v1/\" name=\"ns3:Brukerprofil\">\n        <application>autodeploy-test</application>\n        <serviceVersion bindingName=\"ns3:BrukerprofilBinding\">\n            <operation action=\"http://nav.no/tjeneste/virksomhet/brukerprofil/v1/BrukerprofilPortType/hentKontaktinformasjonOgPreferanser\" name=\"hentKontaktinformasjonOgPreferanser\"/>\n            <serviceInstance endpoint=\"https://e34apsl00120.devillo.no:443/test/brukerprofil\" wsdlAddress=\"https://e34apsl00120.devillo.no:443/test/brukerprofil?wsdl\"/>\n        </serviceVersion>\n    </service>\n</ns2:serviceRegistry>\n");
		private String value;
		private Values(String value) { this.value = value;}
		protected String getValue() { return value; }
	}
	
	private String getResource(Values f){
		return this.getClass().getResource(f.value).getFile();
	}
	
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
	public void testAddServiceInstanceStringStringString() throws JAXBException {
		ServiceRegistry sr = new ServiceRegistry();
		sr.addServiceInstance("popp", "http://tset.com/", getResource(Values.BRUKERPROFIL_WSDL));
		assertEquals(Values.BRUKER_PROFIL_SERVICEREGISTRY_XML.getValue(), sr.toXml());
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
		ServiceRegistry readSR = new ServiceRegistry();
		readSR.readServiceRegistry(getResource(Values.SERVICE_REGISTRY_XML));
		assertEquals(Values.STD_SERVICE_REGISTRY_XML.getValue(), readSR.toXml());
	}

}
