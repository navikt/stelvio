package no.stelvio.presentation.security.sso.accessmanager.support;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Properties;

import no.stelvio.presentation.security.sso.ConfigPropertyKeys;
import no.stelvio.presentation.security.sso.ibm.StelvioTaiPropertiesConfig;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class OpenAMRestAPIImplTest {

	@Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(16384));
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	private static final String OPENAM_URL = "http://localhost:16384";
	
	@Before
    public void setup() throws IOException {
    }
	
	@Test
	public void testOKresponse() throws InterruptedException {
		stubFor(get(urlPathMatching("/openam*"))
    			.willReturn(aResponse()
    					.withStatus(200)
    					.withHeader("Content-Type", "application/json")
    					.withBodyFile("openam_ok.json")));
		
		OpenAMRestAPIImpl openam = new OpenAMRestAPIImpl();
		StelvioTaiPropertiesConfig config = new StelvioTaiPropertiesConfig();
		Properties common = config.loadPropertiesFromFile("test-" + config.getCommonPropertiesFileName());
		String result = openam.invokeOpenAmRestApi("session", OPENAM_URL + common.getProperty(ConfigPropertyKeys.OPENAM_QUERY_TEMPLATE));
		assertNotNull(result);
		// not responsibility of OpenAMRestAPIImpl to check JSON content so no further asserts
	}
	
	@Test
	public void testFailureResponse() throws InterruptedException {
		stubFor(get(urlPathMatching("/openam*"))
    			.willReturn(aResponse()
    					.withStatus(500)));
		
		thrown.expect(java.lang.RuntimeException.class);
		thrown.expectMessage("500");
		
		OpenAMRestAPIImpl openam = new OpenAMRestAPIImpl();
		StelvioTaiPropertiesConfig config = new StelvioTaiPropertiesConfig();
		Properties common = config.loadPropertiesFromFile("test-" + config.getCommonPropertiesFileName());
		openam.invokeOpenAmRestApi("session", OPENAM_URL + common.getProperty(ConfigPropertyKeys.OPENAM_QUERY_TEMPLATE));
	}
	
	@Test
	public void testMalformedURL() throws InterruptedException {
		thrown.expect(java.lang.RuntimeException.class);
		thrown.expectCause(IsInstanceOf.<Throwable>instanceOf(java.net.MalformedURLException.class));
		
		OpenAMRestAPIImpl openam = new OpenAMRestAPIImpl();
		openam.invokeOpenAmRestApi("session", "NOT_AN_URL");
	}

}
