package no.stelvio.common.error;

import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public abstract class ErrorResolverTest {
    @Test(expected = ErrorNotFoundException.class)
    public void throwsExceptionIfErrorDontExist() {
        errorResolver().resolve(new Error("test"));
    }

    @Test
    public void getErrorThrowsExceptionIfNoLocaleExistsOrGiven() {
        fail("implement this");
//            exceptionHandlerFacade().getError(IllegalArgumentException.class);
//            fail("Exception should have been thrown");
    }

    protected abstract ErrorResolver errorResolver();
}
