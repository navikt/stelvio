package no.stelvio.presentation.security.sso.accessmanager.support;

import no.stelvio.presentation.security.sso.accessmanager.PrincipalNotValidException;
import no.stelvio.presentation.security.sso.accessmanager.StelvioPrincipal;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OpenAmAccessManagerTest {

    @Test
    public void testPrincipalRepresentation() throws PrincipalNotValidException, IOException {
        OpenAMRestAPIImpl openAMRestAPIMock = Mockito.mock(OpenAMRestAPIImpl.class);
        String openAMResponse = "{\"token\":{\"tokenId\":\"12345678901-4\"},\"roles\":[],\"attributes\":[{\"values\":[\"12345678901\"],\"name\":\"uid\"},{\"values\":[\"4\"],\"name\":\"SecurityLevel\"}]}";
        Mockito.when(openAMRestAPIMock.invokeOpenAmRestApi(Mockito.anyString(), Mockito.anyString())).thenReturn(openAMResponse);

        OpenAmAccessManager openAmAccessManager = new OpenAmAccessManager();
        openAmAccessManager.setOpenAMRestAPI(openAMRestAPIMock);

        Properties props = new Properties();
        String filename = "stelvio-tai-ldap-groups.properties";
        URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
        props.load(url.openStream());
        openAmAccessManager.setGroupMap(props);

        openAmAccessManager.setPrincipalRepresentation(new String("{\"token\":{\"tokenId\":\"12345678901-4\"},\"roles\":[],\"attributes\":[{\"values\":[\"12345678901\"],\"name\":\"uid\"},{\"values\":[\"4\"],\"name\":\"SecurityLevel\"}]}"));

        StelvioPrincipal principal = openAmAccessManager.getPrincipal();
        assertEquals("12345678901", principal.getAuthorizedAs());
        assertEquals(openAMResponse, principal.getSsoToken());
        assertEquals("12345678901", principal.getUserId());
        assertEquals("0000-GA-PENSJON_HOY", principal.getGroupIds().iterator().next());
    }
}
