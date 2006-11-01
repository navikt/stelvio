package no.stelvio.common.error.old;

/**
 * Interface for platform specific exception wrapping.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: ExceptionWrapper.java 2265 2005-05-24 08:08:38Z psa2920 $
 * @deprecated Check the new implementation
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
