package no.nav.integration.framework.stubs;

import java.util.Set;

import no.nav.integration.framework.stubs.IntegrationStub;
import no.trygdeetaten.common.framework.config.Config;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceRequest;
import no.trygdeetaten.common.framework.service.ServiceResponse;

import junit.framework.TestCase;

/**
 * testCase for IntegrationStub.
 * @author person1f201b37d484, Accenture
 * @version $Id: IntegrationStubTest.java 2013 2005-03-02 11:50:49Z psa2920 $
 */
public class IntegrationStubTest extends TestCase {

	public final static String SERVICE_NAME = "TrekkModul";
	public final static String TREKK_SYSTEM_SERVICE = "SystemServiceName";
	public final static String TREKKDETALJER_SERVICE = "TrekkdetaljerService";
	public final static String TREKKDETALJER_IN = "TREKKDETALJINPUT";
	public final static String TREKKDETALJER_OUT = "TREKKDETALJOUTPUT";

	public void testIntegrationStub() throws ServiceFailedException {

		IntegrationStub stub = (IntegrationStub) Config.getConfig("enterprise-services.xml").getBean("TrekkModul");

		ServiceRequest req = new ServiceRequest();
		req.setData(TREKK_SYSTEM_SERVICE, TREKKDETALJER_SERVICE);

		ServiceResponse resp = stub.doExecute(req);

		TestT771TrekkdetaljerOutput out = (TestT771TrekkdetaljerOutput) resp.getData(TREKKDETALJER_OUT);
		assertNotNull("Fikk ikke data tilbake", out);
		Set detaljer = out.getTrekkdetaljer();

		assertTrue("Fikk ikke forventet antall rader", detaljer.size() == 2);
	}
}
