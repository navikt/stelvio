package no.stelvio.provider.error.strategy.support;

import no.stelvio.common.error.FunctionalUnrecoverableException;

/**
 * To be wrapped by AOP in unittest.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public class ExceptionThrower {

	/**
	 * Throws a Throwable.
	 * 
	 * @param t
	 *            the throwable
	 * @return always throws an exception and does not return normally
	 * @throws Throwable the exception
	 */
	public Throwable throwThrowable(Throwable t) throws Throwable {
		throw t;
	}

	/**
	 * Throws a FunctionalUnrecoverableException.
	 * 
	 * @param e
	 *            the exception
	 * @return always throws an exception and does not return normally
	 */
	public FunctionalUnrecoverableException throwFunctionalUnrecoverable(FunctionalUnrecoverableException e) {
		throw e;
	}

	/**
	 * Throws a RuntimeException.
	 * 
	 * @param e
	 *            the exception
	 * @return always throws an exception and does not return normally
	 */
	public RuntimeException throwRuntimeException(RuntimeException e) {
		throw e;
	}

	/**
	 * Generates a NullPointerException.
	 */
	@SuppressWarnings("null")
	public void nullPointerProneMethod() {
		String s = null;
		s.length();
	}
}
