package no.stelvio.common.service;

import no.stelvio.common.error.SystemException;

/**
 * Thrown to indicate that a particular <i>Service</i> could not be found.
 * 
 * @author person7553f5959484
 * @version $Revision: 2570 $ $Author: psa2920 $ $Date: 2005-10-19 18:07:03 +0200 (Wed, 19 Oct 2005) $
 */
public final class ServiceNotFoundException extends SystemException {

	/**
	 * Constructs a new ServiceNotFoundException with the specified error code.
	 *
     */
	public ServiceNotFoundException() {
		super();
	}

	/**
	 * Constructs a new ServiceNotFoundException with the specified error code and cause.
	 * 
	 * @param cause the cause of this exception.
	 */
	public ServiceNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new ServiceNotFoundException with the specified error code and cause.
	 * 
	 * @param cause the cause of this exception.
     * @param argument detail to be included in the error message.
     */
	public ServiceNotFoundException(Throwable cause, Object argument) {
		super(cause, argument);
	}
}