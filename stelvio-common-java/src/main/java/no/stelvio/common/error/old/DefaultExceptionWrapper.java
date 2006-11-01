package no.stelvio.common.error.old;

/**
 * Default wrapper for unit testing.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: DefaultExceptionWrapper.java 2266 2005-05-24 10:22:09Z psa2920 $
 * @deprecated Check the new implementation
 */
public class DefaultExceptionWrapper implements ExceptionWrapper {

	/** 
	 * {@inheritDoc}
	 * @see ExceptionWrapper#getCause(java.lang.Throwable)
	 */
	public Throwable getCause(Throwable t) {
		return t.getCause();
	}
}
