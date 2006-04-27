package no.trygdeetaten.common.framework.service;

import no.trygdeetaten.common.framework.error.ErrorCode;
import no.trygdeetaten.common.framework.error.SystemException;

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
	 * @param code the error code to be used when handling the exception.
	 */
	public ServiceNotFoundException(ErrorCode code) {
		super(code);
	}

	/**
	 * Constructs a new ServiceNotFoundException with the specified error code and cause.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param cause the cause of this exception.
	 */
	public ServiceNotFoundException(ErrorCode code, Throwable cause) {
		super(code, cause);
	}

	/**
	 * Constructs a new ServiceNotFoundException with the specified error code and cause.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param cause the cause of this exception.
	 * @param argument detail to be included in the error message.
	 */
	public ServiceNotFoundException(ErrorCode code, Throwable cause, Object argument) {
		super(code, cause, argument);
	}
}