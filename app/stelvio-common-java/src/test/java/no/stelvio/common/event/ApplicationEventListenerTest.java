package no.stelvio.common.event;

import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link ApplicationEventListener}.
 * @author personf8e9850ed756
 */
public abstract class ApplicationEventListenerTest {
    private ApplicationEventListener applicationEventListener;

    @Test
    public void applicationEventAreMandatory() {
        try {
            applicationEventListener.onApplicationEvent(null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // should happen
        }
    }

    protected abstract ApplicationEventListener createEventListener();

    @Before
    public void setupApplicationEventListener() throws Exception {
        applicationEventListener = createEventListener();
    }
}
