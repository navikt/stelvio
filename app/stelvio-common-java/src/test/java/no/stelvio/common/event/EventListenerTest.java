package no.stelvio.common.event;

import junit.framework.TestCase;

/**
 * Unit test for {@link EventListener}.
 * @author personf8e9850ed756
 */
public abstract class EventListenerTest extends TestCase {
    private EventListener eventListener;

    public void testApplicationEventAreMandatory() {
        try {
            eventListener.onApplicationEvent(null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // should happen
        }
    }

    protected abstract EventListener createEventListener();

    protected void setUp() throws Exception {
        eventListener = createEventListener();
    }
}
