package no.stelvio.common.error.support;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.core.Ordered;

/**
 * Advice that handles exceptions by rethrowing them. Is setup with Spring's <code>ThrowsAdviceInterceptor</code>.
 * 
 * @author personf8e9850ed756, Accenture
 */
public class RethrowExceptionHandlingAdvice implements ThrowsAdvice, Ordered {

	private int order = Integer.MAX_VALUE;

	/**
	 * Will be called when an exception occurs and rethrows it.
	 * 
	 * @param throwable
	 *            the exception to handle
	 * @throws Throwable
	 *             after handling the exception it is rethrown.
	 */
	public void afterThrowing(Throwable throwable) throws Throwable {
		throw throwable;
	}

	/**
	 * Returns the priority of the advice, that is, in which order this advice should be run in relation to other advice. The
	 * default value is set to Integer.MAX_VALUE (the lowest priority).
	 * 
	 * @return the priority of the advice in relation to other advice.
	 */
	@Override
	public int getOrder() {
		return order;
	}

	/**
	 * Sets the priority of the advice.
	 * 
	 * @param order
	 *            the priority of the advice.
	 * @see #getOrder()
	 */
	public void setOrder(int order) {
		this.order = order;
	}
}
