package no.trygdeetaten.common.framework.error;

/**
 * Default wrapper for unit testing.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: DefaultExceptionWrapper.java 2266 2005-05-24 10:22:09Z psa2920 $
 */
public class DefaultExceptionWrapper implements ExceptionWrapper {

	/** 
	 * {@inheritDoc}
	 * @see no.trygdeetaten.common.framework.error.ExceptionWrapper#getCause(java.lang.Throwable)
	 */
	public Throwable getCause(Throwable t) {
		return t.getCause();
	}
}
