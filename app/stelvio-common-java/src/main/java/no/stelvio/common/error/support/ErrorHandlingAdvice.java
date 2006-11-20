package no.stelvio.common.error.support;

import java.lang.reflect.Method;

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
     * @param methodCalled TODO should we use this?
     * @param methodArgs TODO should we use this?
     * @param target TODO should we use this? the object on which the method was called.
     * @param throwable the exception to handle
     * @throws Throwable after handling the exception it is rethrown. 
     */
    public void afterThrowing(Method methodCalled, Object[] methodArgs, Object target, Throwable throwable)
            throws Throwable {
        exceptionHandlerFacade.handleException(throwable);
    }

    public void setExceptionHandlerFacade(ExceptionHandlerFacade exceptionHandlerFacade) {
        this.exceptionHandlerFacade = exceptionHandlerFacade;
    }
}
