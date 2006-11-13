package no.stelvio.common.event;

import junit.framework.TestCase;

/**
 * Unit test for {@link ApplicationEvent}:
 *
 * @author personf8e9850ed756
 */
public abstract class ApplicationEventTest extends TestCase {
    private ApplicationEvent event;

    public void testConstructorArgumentAreMandatory() {
        try {
            new ApplicationEvent(null) {};
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // should happen
        }
    }

    public void testObjectOnWhichTheEventInitiallyOccurredIsSaved() {
        assertEquals("Should hold this instance", event.getSource(), this);
    }

    protected abstract ApplicationEvent createApplicationEvent();

    protected void setUp() throws Exception {
        event = createApplicationEvent();
    }

    protected ApplicationEvent getEvent() {
        return event;
    }
}
