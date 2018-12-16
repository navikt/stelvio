package no.stelvio.common.error.strategy.support;

import org.springframework.core.Ordered;

import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;

/**
 * Abstract class subclassed by all <code>{@link ExceptionHandlerStrategy}</code> that should support being
 * executed in an order dictated by a developer. Especially useful when strategy is configured as part of 
 * a {@link ExceptionHandlerStrategyChain}.
 * 
 * @author person983601e0e117 (Accenture)
 *
 */
public abstract class AbstractOrderedExceptionHandlerStrategy implements
		Ordered, ExceptionHandlerStrategy {

	private int order = Integer.MAX_VALUE;
	
	/**
	 * Gets the order.
	 * 
	 * @return the order
	 */
	@Override
	public int getOrder() {
		return order;
	}

	/**
	 * Sets the order.
	 * 
	 * @param order the order
	 */
	public void setOrder(int order) {
		this.order = order;
	}

}
