package no.stelvio.common.error.logging;

/**
 * Class that throws exception. Can be configured in Spring and thus set up
 * to be intercepted through Spring AOP
 *
 */
public class ExceptionThrower {

	/**
	 * Throw exception.
	 * 
	 * @param <T> exception type
	 * @param e exception
	 * @throws T exception
	 */
	public <T extends Throwable> void throwException(T e) throws T {
		throw e;
	}
	
}
