package no.stelvio.common.error;

import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo error handler needs locale from context, have a prototype target source get it so we dont need to call static method here
 */
public abstract class ExceptionHandlerFacadeTest {
    @Test
    public void afterHandlingRethrow() throws Throwable {
        try {
            exceptionHandlerFacade().rethrow(new IllegalArgumentException());
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Should happen
        }
    }

    @Ignore(value="do later") @Test
    public void testThatUnknownErrorFromAnywhereIsHandled() {
        fail("Implement tests for the important components in error handling to live through for example the db going down");
    }

    protected abstract ExceptionHandlerFacade exceptionHandlerFacade();
}
