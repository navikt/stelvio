package no.stelvio.common.error.support;

import no.stelvio.common.error.ExceptionHandlerFacade;
import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;
import no.stelvio.common.error.strategy.support.FallbackExceptionHandlerStrategy;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
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
	        // Original exception
            t = fallbackStrategy.handleException(e);
	        // Just print out new exception (database down, etc)
            fallbackStrategy.handleException(throwable);
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
