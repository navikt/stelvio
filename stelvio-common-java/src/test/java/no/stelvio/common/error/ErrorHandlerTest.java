package no.stelvio.common.error;

import com.agical.rmock.extension.junit.RMockTestCase;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo error handler needs locale from context, have a prototype target source get it so we dont need to call static method here
 */
public abstract class ErrorHandlerTest extends RMockTestCase {
    public void testAfterHandlingRethrow() {
        expectThatExceptionThrown(is.instanceOf(Throwable.class));
    }

    public void testThrowsExceptionIfInputToGetErrorIsNotThrowable() {
        expectThatExceptionThrown(is.);
    }

    public void testGetErrorThrowsExceptionIfNoLocaleGiven() {
        getErrorHandler().getError(IllegalArgumentException.class);
    }

    public abstract ErrorHandler getErrorHandler();
}
