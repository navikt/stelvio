package no.stelvio.common.event.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import no.stelvio.common.error.strategy.support.LoggerExceptionHandlerStrategyTest;
import no.stelvio.common.event.ApplicationEvent;
import no.stelvio.common.event.ApplicationEventListener;
import no.stelvio.common.event.ApplicationEventListenerTest;

/**
 * Unit test for {@link LoggingApplicationEventListener}.
 *
 * @author personf8e9850ed756
 */
public class LoggingApplicationEventListenerTest extends ApplicationEventListenerTest {
    private LoggingApplicationEventListener lael;
    private Mockery context;
    private Log log;

    @Test
    public void eventShouldBeLogged() {
        context.expects(new InAnyOrder() {{
            one (log).info(with(an(ApplicationEvent.class)));
        }});

        lael.onApplicationEvent(new ApplicationEvent(this) {});
        context.assertIsSatisfied();
    }

    @Before
    public void setupForTest() {
        context = new Mockery();
        log = context.mock(Log.class);

        System.setProperty("org.apache.commons.logging.LogFactory",
                "no.stelvio.common.error.strategy.support.LoggerExceptionHandlerStrategyTest$TestLogFactory");
        LogFactory.releaseAll();
        ((LoggerExceptionHandlerStrategyTest.TestLogFactory) LogFactory.getFactory()).setLog(log);

        // Must create this after setting up the logging, as otherwise this won't get our mock log implementation
        lael = new LoggingApplicationEventListener();
    }

    protected ApplicationEventListener createEventListener() {
        return lael;
    }
}
