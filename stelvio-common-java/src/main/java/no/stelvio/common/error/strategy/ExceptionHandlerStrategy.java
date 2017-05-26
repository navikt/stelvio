package no.stelvio.common.error.strategy;

/**
 * refactor common tests inta an abstract test class for the interface.
 * 
 * @author personf8e9850ed756
 */
public interface ExceptionHandlerStrategy {
	/**
	 * Takes an exception and returns an exception.
	 *
	 * @param <T> a Throwable type variable
	 * @param e an exception
	 * @return an exception
	 */
	<T extends Throwable> T handleException(T e);
}
