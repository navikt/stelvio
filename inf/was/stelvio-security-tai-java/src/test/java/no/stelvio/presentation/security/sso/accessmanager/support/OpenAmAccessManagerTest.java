package no.stelvio.presentation.security.sso.accessmanager.support;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import no.stelvio.presentation.security.sso.accessmanager.PrincipalNotValidException;
import no.stelvio.presentation.security.sso.accessmanager.StelvioPrincipal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class OpenAmAccessManagerTest {

    protected static Properties props = null;
    
    @Before
    public void setup() throws IOException {
        props = new Properties();
        String filename = "stelvio-tai-ldap-groups.properties";
        URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
        props.load(url.openStream());        
    }
    
    @Test
    public void testPrincipalRepresentationWithValidAuthMethod() throws PrincipalNotValidException, IOException {
        OpenAMRestAPIImpl openAMRestAPIMock = Mockito.mock(OpenAMRestAPIImpl.class);
        String openAMResponse = "{\"token\":{\"tokenId\":\"12345678901-4\"},\"roles\":[],\"attributes\":[{\"values\":[\"12345678901\"],\"name\":\"uid\"},{\"values\":[\"4\"],\"name\":\"SecurityLevel\"},{\"values\":[\"NAV-OneDayPw\"],\"name\":\"AuthMethod\"},{\"values\":[\"somevalue\"],\"name\":\"AuthType\"}]}";
        Mockito.when(openAMRestAPIMock.invokeOpenAmRestApi(Mockito.anyString(), Mockito.anyString())).thenReturn(openAMResponse);

        OpenAmAccessManager openAmAccessManager = new OpenAmAccessManager();
        openAmAccessManager.setOpenAMRestAPI(openAMRestAPIMock);

        openAmAccessManager.setGroupMap(props);

        String openamSession = "someopenamsession";
        openAmAccessManager.setPrincipalRepresentation(openamSession);

        StelvioPrincipal principal = openAmAccessManager.getPrincipal();
        assertEquals("12345678901", principal.getAuthorizedAs());
        assertEquals(openamSession, principal.getSsoToken());
        assertEquals("12345678901", principal.getUserId());
        assertEquals("0000-GA-PENSJON_HOY", principal.getGroupIds().iterator().next());
    }
    
    @Test
    public void testPrincipalRepresentationWithValidAuthTypeFederation() throws PrincipalNotValidException, IOException {
        OpenAMRestAPIImpl openAMRestAPIMock = Mockito.mock(OpenAMRestAPIImpl.class);
        String openAMResponse = "{\"token\":{\"tokenId\":\"12345678901-4\"},\"roles\":[],\"attributes\":[{\"values\":[\"12345678901\"],\"name\":\"uid\"},{\"values\":[\"4\"],\"name\":\"SecurityLevel\"},{\"values\":[\"somevalue\"],\"name\":\"AuthMethod\"},{\"values\":[\"Federation\"],\"name\":\"AuthType\"}]}";
        Mockito.when(openAMRestAPIMock.invokeOpenAmRestApi(Mockito.anyString(), Mockito.anyString())).thenReturn(openAMResponse);

        OpenAmAccessManager openAmAccessManager = new OpenAmAccessManager();
        openAmAccessManager.setOpenAMRestAPI(openAMRestAPIMock);

        openAmAccessManager.setGroupMap(props);

        String openamSession = "someopenamsession";
        openAmAccessManager.setPrincipalRepresentation(openamSession);

        StelvioPrincipal principal = openAmAccessManager.getPrincipal();
        assertEquals("12345678901", principal.getAuthorizedAs());
        assertEquals(openamSession, principal.getSsoToken());
        assertEquals("12345678901", principal.getUserId());
        assertEquals("0000-GA-PENSJON_HOY", principal.getGroupIds().iterator().next());
    }
    
    @Test
    public void testPrincipalRepresentationWithValidAuthTypeLDAP() throws PrincipalNotValidException, IOException {
        OpenAMRestAPIImpl openAMRestAPIMock = Mockito.mock(OpenAMRestAPIImpl.class);
        String openAMResponse = "{\"token\":{\"tokenId\":\"12345678901-4\"},\"roles\":[],\"attributes\":[{\"values\":[\"12345678901\"],\"name\":\"uid\"},{\"values\":[\"4\"],\"name\":\"SecurityLevel\"},{\"values\":[\"somevalue\"],\"name\":\"AuthMethod\"},{\"values\":[\"LDAP\"],\"name\":\"AuthType\"}]}";
        Mockito.when(openAMRestAPIMock.invokeOpenAmRestApi(Mockito.anyString(), Mockito.anyString())).thenReturn(openAMResponse);

        OpenAmAccessManager openAmAccessManager = new OpenAmAccessManager();
        openAmAccessManager.setOpenAMRestAPI(openAMRestAPIMock);

        openAmAccessManager.setGroupMap(props);

        String openamSession = "someopenamsession";
        openAmAccessManager.setPrincipalRepresentation(openamSession);

        StelvioPrincipal principal = openAmAccessManager.getPrincipal();
        assertEquals("12345678901", principal.getAuthorizedAs());
        assertEquals(openamSession, principal.getSsoToken());
        assertEquals("12345678901", principal.getUserId());
        assertEquals("0000-GA-PENSJON_HOY", principal.getGroupIds().iterator().next());
    }
    
    @Test(expected=PrincipalNotValidException.class)
    public void testPrincipalRepresentationWithInvalidAuthTypeAndAuthMethod() throws PrincipalNotValidException, IOException {
        OpenAMRestAPIImpl openAMRestAPIMock = Mockito.mock(OpenAMRestAPIImpl.class);
        String openAMResponse = "{\"token\":{\"tokenId\":\"12345678901-4\"},\"roles\":[],\"attributes\":[{\"values\":[\"12345678901\"],\"name\":\"uid\"},{\"values\":[\"4\"],\"name\":\"SecurityLevel\"},{\"values\":[\"somevalue\"],\"name\":\"AuthMethod\"},{\"values\":[\"somevalue\"],\"name\":\"AuthType\"}]}";
        Mockito.when(openAMRestAPIMock.invokeOpenAmRestApi(Mockito.anyString(), Mockito.anyString())).thenReturn(openAMResponse);

        OpenAmAccessManager openAmAccessManager = new OpenAmAccessManager();
        openAmAccessManager.setOpenAMRestAPI(openAMRestAPIMock);

        String openamSession = "someopenamsession";
        openAmAccessManager.setPrincipalRepresentation(openamSession);
    }
    
    @Test(expected=PrincipalNotValidException.class)
    public void testPrincipalRepresentationWithNullAuthTypeAndAuthMethod() throws PrincipalNotValidException, IOException {
        OpenAMRestAPIImpl openAMRestAPIMock = Mockito.mock(OpenAMRestAPIImpl.class);
        String openAMResponse = "{\"token\":{\"tokenId\":\"12345678901-4\"},\"roles\":[],\"attributes\":[{\"values\":[\"12345678901\"],\"name\":\"uid\"},{\"values\":[\"4\"],\"name\":\"SecurityLevel\"}]}";
        Mockito.when(openAMRestAPIMock.invokeOpenAmRestApi(Mockito.anyString(), Mockito.anyString())).thenReturn(openAMResponse);

        OpenAmAccessManager openAmAccessManager = new OpenAmAccessManager();
        openAmAccessManager.setOpenAMRestAPI(openAMRestAPIMock);

        String openamSession = "someopenamsession";
        openAmAccessManager.setPrincipalRepresentation(openamSession);
    }
}
