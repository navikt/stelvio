package no.stelvio.common.error.support;

import no.stelvio.common.error.ExceptionWrapper;

/**
 * Simple wrapper for unit testing.
 * 
 * @version $Id$
 */
public class SimpleExceptionWrapper implements ExceptionWrapper {
	/**
	 * {@inheritDoc}
	 */
	public Throwable getCause(Throwable t) {
		return t.getCause();
	}
}
