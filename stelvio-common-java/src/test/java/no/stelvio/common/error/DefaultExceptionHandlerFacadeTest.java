package no.stelvio.common.error;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class DefaultExceptionHandlerFacadeTest extends ExceptionHandlerFacadeTest {
    public ExceptionHandlerFacade getExceptionHandlerFacade() {
        return new DefaultExceptionHandlerFacade();
    }
}
