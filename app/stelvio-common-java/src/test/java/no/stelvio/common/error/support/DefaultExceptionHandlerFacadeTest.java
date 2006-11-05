package no.stelvio.common.error.support;

import no.stelvio.common.error.ExceptionHandlerFacade;
import no.stelvio.common.error.ExceptionHandlerFacadeTest;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class DefaultExceptionHandlerFacadeTest extends ExceptionHandlerFacadeTest {
    public ExceptionHandlerFacade getExceptionHandlerFacade() {
        return new DefaultExceptionHandlerFacade();
    }
}
