package no.stelvio.common.error.support;

import no.stelvio.common.error.ExceptionWrapper;

/**
 * Simple wrapper for unit testing.
 * 
 * @author person7553f5959484, Accenture
 * @author personf8e9850ed756, Accenture
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
