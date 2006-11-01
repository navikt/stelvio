package no.stelvio.common.error;

import com.agical.rmock.extension.junit.RMockTestCase;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo error handler needs locale from context, have a prototype target source get it so we dont need to call static method here
 */
public abstract class ErrorHandlerTest extends RMockTestCase {
    public void testAfterHandlingRethrow() {
        try {
            getErrorHandler().handleError(new IllegalArgumentException());
            fail("Exception should have been thrown");
        } catch (Exception e) {
            // Should happen
        }
    }

    public void testGetErrorThrowsExceptionIfNoLocaleExistsOrGiven() {
        try {
            getErrorHandler().getError(IllegalArgumentException.class);
            fail("Exception should have been thrown");
        } catch (Exception e) {
            // Should happen, TODO: better exception
        }
    }

    public abstract ErrorHandler getErrorHandler();
}
