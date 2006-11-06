package no.stelvio.common.error.strategy.support;

import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;

/**
 * Will multicast to others {@link ExceptionHandlerStrategy} to do the real work.
 *
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo maybe output from one should go into input to the next? maybe not call it multicast then --> chain 
 */
public class ExceptionHandlerStrategyMulticaster implements ExceptionHandlerStrategy {
}
