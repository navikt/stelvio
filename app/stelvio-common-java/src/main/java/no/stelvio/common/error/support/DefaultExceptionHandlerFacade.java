package no.stelvio.common.error.support;

import no.stelvio.common.error.ExceptionHandlerFacade;
import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;
import no.stelvio.common.error.strategy.support.FallbackExceptionHandlerStrategy;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo should check that all exception classes specified in the database can be instantiated, maybe have a set anyway  
 */
public class DefaultExceptionHandlerFacade implements ExceptionHandlerFacade {
    // TODO javadoc... default is FallBack...
    private ExceptionHandlerStrategy fallbackStrategy = new FallbackExceptionHandlerStrategy();
    private ExceptionHandlerStrategy defaultStrategy;

    public <T extends Throwable> void handleException(T e) throws Throwable {
        T t;

        try {
            t = defaultStrategy.handleException(e);
        } catch (Throwable throwable) {
            // TODO is it possible to catch T?
            t = (T) fallbackStrategy.handleException(throwable);
        }

        throw t;
    }

    public void setFallbackStrategy(ExceptionHandlerStrategy fallbackStrategy) {
        this.fallbackStrategy = fallbackStrategy;
    }

    public void setDefaultStrategy(ExceptionHandlerStrategy defaultStrategy) {
        this.defaultStrategy = defaultStrategy;
    }
}
