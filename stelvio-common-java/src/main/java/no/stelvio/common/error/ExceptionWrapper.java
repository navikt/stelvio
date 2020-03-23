package no.stelvio.common.error;

/**
 * Interface for platform specific exception wrapping.
 * do we need this?
 * this could be used to wrap "native" exceptions like IllegalArgumentException, trying to be more like a mixin,
 * that is toString would produce the same result (+ some extra info, which are in our exceptions).
 * Use Throwable.setStackTrace 
 * 
 * @version $Id: ExceptionWrapper.java 2265 2005-05-24 08:08:38Z psa2920 $
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
