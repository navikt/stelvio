package no.stelvio.common.event.support;

import no.stelvio.common.event.EventListener;
import no.stelvio.common.event.EventListenerTest;

/**
 * Unit test for {@link LoggingEventListener}.
 *
 * @author personf8e9850ed756
 */
public class LoggingEventListenerTest extends EventListenerTest {
    protected EventListener createEventListener() {
        return new LoggingEventListener();
    }
}
