package no.stelvio.common.event;

import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Unit test for {@link ApplicationEventListener}.
 * @author personf8e9850ed756
 */
public abstract class ApplicationEventListenerTest {
    @Test
    public void applicationEventAreMandatory() {
        try {
            createEventListener().onApplicationEvent(null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // should happen
        }
    }

    protected abstract ApplicationEventListener createEventListener();
}
