package no.stelvio.common.event.support;

import no.stelvio.common.event.ApplicationEventListener;
import no.stelvio.common.event.ApplicationEventListenerTest;

/**
 * Unit test for {@link LoggingApplicationEventListener}.
 *
 * @author personf8e9850ed756
 */
public class LoggingApplicationEventListenerTest extends ApplicationEventListenerTest {
    protected ApplicationEventListener createEventListener() {
        return new LoggingApplicationEventListener();
    }
}
