package no.stelvio.common.error.resolver;

import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public abstract class ErrorDefinitionResolverTest {
    @Test(expected = ErrorDefinitionNotFoundException.class)
    public void throwsExceptionIfErrorDontExist() {
        errorDefinitionResolver().resolve(new Error("test"));
    }

    @Test
    public void getErrorThrowsExceptionIfNoLocaleExistsOrGiven() {
        fail("implement this");
//            exceptionHandlerFacade().getError(IllegalArgumentException.class);
//            fail("Exception should have been thrown");
    }

    protected abstract ErrorDefinitionResolver errorDefinitionResolver();
}
