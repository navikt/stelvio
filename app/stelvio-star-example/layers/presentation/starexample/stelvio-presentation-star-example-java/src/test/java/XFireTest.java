import junit.framework.TestCase;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Simple integration test showing how to setup XFire in Spring.
 *
 * @author personf8e9850ed756
 * @todo enable this again?
 */
//public class XFireTest extends AbstractDependencyInjectionSpringContextTests {
public class XFireTest extends TestCase {
    /**
     * Tests that calling a simple quote web service works.
     *
     * @throws Exception if something went wrong retrieving the Spring context.
     */
    public void testCallsWebService() throws Exception {
        //StockQuote stockQuote = (StockQuote) getContext(contextKey()).getBean("testWebService");
        //String returnValue = stockQuote.GetQuote("ACN");

        //assertNotNull("Should return the quote", returnValue);
        //assertFalse("Web service should not return exception", "exception".equals(returnValue));
    }

    /**
     * Sets the Spring configuration to use. In addition sets up the HTTP proxy to use.
     */
    protected String[] getConfigLocations() {
        System.setProperty("http.proxyHost", "webproxy.trygdeetaten.no");
        System.setProperty("http.proxyPort", "8088");

        return new String[] {"xfire-test.context.xml"};
    }

    /**
     * The web service interface.
     */
    public interface StockQuote {
        String GetQuote(String symbol);
    }
}
