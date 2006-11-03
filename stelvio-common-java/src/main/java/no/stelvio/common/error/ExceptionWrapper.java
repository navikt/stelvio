package no.stelvio.common.error;

/**
 * Interface for platform specific exception wrapping.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: ExceptionWrapper.java 2265 2005-05-24 08:08:38Z psa2920 $
 * @todo do we need this?
 * @todo this could be used to wrap "native" exceptions like IllegalArgumentException, trying to be more like a mixin,
 * @todo that is toString would produce the same result (+ some extra info, which are in our exceptions).
 * @todo Use Throwable.setStackTrace 
 */
public interface ExceptionWrapper {
	
	/**
	 * Extracts the wrapped cause from the vendor specific 
	 * exception.
	 * 
	 * @param t the vendor specific Throwable.
	 * @return the non-vendor specific original cause.
	 */
	Throwable getCause(Throwable t);

}
