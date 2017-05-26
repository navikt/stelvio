package no.stelvio.presentation.security.logout;

import org.junit.Test;

import static org.junit.Assert.assertEquals;



public class OpenAMLogoutServiceTest {

    @Test
    public void withoutOpenAM() throws Exception {

        OpenAMLogoutService openAMLogoutService = new OpenAMLogoutService();
        openAMLogoutService.setLogoutFromOpenAM(false);
        openAMLogoutService.setPointOfContactHostAddress("http://testaddress");
        openAMLogoutService.setLogoutAction("testaction");
        openAMLogoutService.afterPropertiesSet();
        String result = openAMLogoutService.buildRedirectUrl("http://testurl");

        assertEquals("testaction?logoutExitPage=http://testurl", result);
    }

    @Test
    public void withOpenAM() throws Exception {

        OpenAMLogoutService openAMLogoutService = new OpenAMLogoutService();
        openAMLogoutService.setLogoutFromOpenAM(true);
        openAMLogoutService.setPointOfContactHostAddress("http://testaddress");
        openAMLogoutService.setLogoutAction("testaction");
        openAMLogoutService.afterPropertiesSet();
        String result = openAMLogoutService.buildRedirectUrl("http://testurl");

        assertEquals("testaction?logoutExitPage=http://testaddress/esso/logout", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingProperty() throws Exception {
        OpenAMLogoutService openAMLogoutService = new OpenAMLogoutService();
        openAMLogoutService.afterPropertiesSet();
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongPropertyValue() throws Exception {
        OpenAMLogoutService openAMLogoutService = new OpenAMLogoutService();
        openAMLogoutService.setPointOfContactHostAddress("addressWithoutHttpFirst");
        openAMLogoutService.setLogoutAction("testaction");
        openAMLogoutService.afterPropertiesSet();
    }

    @Test
    public void removeUnwantedSlash() throws Exception {
        OpenAMLogoutService openAMLogoutService = new OpenAMLogoutService();
        openAMLogoutService.setLogoutFromOpenAM(true);
        openAMLogoutService.setPointOfContactHostAddress("http://testaddress/");
        openAMLogoutService.setLogoutAction("testaction");
        openAMLogoutService.afterPropertiesSet();
        String result = openAMLogoutService.buildRedirectUrl("http://testurl");

        assertEquals("testaction?logoutExitPage=http://testaddress/esso/logout", result);
    }
}
