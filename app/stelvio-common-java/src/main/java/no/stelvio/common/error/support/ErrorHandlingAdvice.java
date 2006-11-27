package no.stelvio.common.error.support;

import org.springframework.aop.ThrowsAdvice;

import no.stelvio.common.error.ExceptionHandlerFacade;

/**
 * Advice that handles exceptions. Is setup with Spring's <code>ThrowsAdviceInterceptor</code>.
 *
 * @author personf8e9850ed756
 * @todo better javadoc
 */
public class ErrorHandlingAdvice implements ThrowsAdvice {
    private ExceptionHandlerFacade exceptionHandlerFacade;

    /**
     * Will be called when an exception occurs.
     *
     * @param throwable the exception to handle
     * @throws Throwable after handling the exception it is rethrown. 
     */
    public void afterThrowing(Throwable throwable)
            throws Throwable {
        exceptionHandlerFacade.handleException(throwable);
    }

    public void setExceptionHandlerFacade(ExceptionHandlerFacade exceptionHandlerFacade) {
        this.exceptionHandlerFacade = exceptionHandlerFacade;
    }
}
