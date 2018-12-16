package no.stelvio.consumer.ws;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.ibm.ws.webservices.multiprotocol.AgnosticService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ServiceUserFallbackConsumerFacadeBaseTest {

    private ServiceUserFallbackConsumerFacadeBase cfb;

    @Before
    public void initFacade() {
        cfb = new ServiceUserFallbackConsumerFacadeBase(DummyServiceLocator.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionIfUseUNTFlagIsSetButNoCredentialsAreGiven() {
        cfb.setUseUsernameToken(true);

        cfb.isUseUsernameToken();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionIfUseUNTFlagIsNotSetAndWSSubjectIsExternalUserButNoCredentialsAreGiven() {
        ServiceUserFallbackConsumerFacadeBase cfbSpy = spy(cfb);
        doReturn(true).when(cfbSpy).isEksternbruker();

        cfbSpy.isUseUsernameToken();
    }

    @Test
    public void returnFalseIfUseUNTFlagIsNotAndSubjectIsNotExternalUser() {
        cfb.setUseUsernameToken(false);
        ServiceUserFallbackConsumerFacadeBase cfbSpy = spy(cfb);
        doReturn(false).when(cfbSpy).isEksternbruker();

        assertThat(cfbSpy.isUseUsernameToken(), is(false));
    }

    @Test
    public void returnTrueIffUseUNTFlagIsSetAndCredentialsAreGiven() {
        cfb.setUseUsernameToken(true);
        cfb.setServiceUsername("srvStelvio");
        cfb.setServicePassword("srvStelvioPw");

        assertThat(cfb.isUseUsernameToken(), is(true));
    }

    private static class DummyServiceLocator extends AgnosticService {
        // Mockito does not handle AgnosticService class
    }
}