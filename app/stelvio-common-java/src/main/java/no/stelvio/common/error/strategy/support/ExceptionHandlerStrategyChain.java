package no.stelvio.common.error.strategy.support;

import java.util.Set;

import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;

/**
 * Will chain the handling of the exception through a set of {@link ExceptionHandlerStrategy}s that does the real work.
 *
 * @author personf8e9850ed756
 */
public class ExceptionHandlerStrategyChain implements ExceptionHandlerStrategy {
    // TODO have the strategies implement Ordered from Spring so they will be ordered correctly in the chain
    private Set<ExceptionHandlerStrategy> strategies;

    public <T extends Throwable> T handleException(T e) {
        T throwable = e;

        for (ExceptionHandlerStrategy strategy : strategies) {
            throwable = strategy.handleException(throwable);
        }

        return throwable;
    }

    public void setStrategies(Set<ExceptionHandlerStrategy> strategies) {
        this.strategies = strategies;
    }
}
