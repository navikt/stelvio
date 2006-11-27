package no.stelvio.common.error.strategy.support;

import java.util.Set;

import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;

/**
 * Will multicast to others {@link ExceptionHandlerStrategy} to do the real work.
 *
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo maybe output from one should go into input to the next? maybe not call it multicast then --> chain 
 */
public class ExceptionHandlerStrategyMulticaster implements ExceptionHandlerStrategy {
    // TODO have the strategies implement Ordered from Spring so they will be ordered correctly in the chain
    private Set<ExceptionHandlerStrategy> strategies;

    public <T extends Throwable> T handleException(T e) throws T {
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
