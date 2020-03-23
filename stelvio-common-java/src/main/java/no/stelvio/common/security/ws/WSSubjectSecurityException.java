package no.stelvio.common.security.ws;

import no.stelvio.common.error.SystemUnrecoverableException;

/**
 * Thrown to indicate that an error has occurred when invoking the com.ibm.websphere.security.auth.WSSubject class from a
 * Stelvio component.
 * 
 */
public class WSSubjectSecurityException extends SystemUnrecoverableException {

	/**
	 * Constructs a <code>WSSubjectSecurityException</code> with message and cause.
	 * 
	 * @param message
	 *            - the exception message.
	 * @param cause
	 *            - the throwable that caused the exception to be raised.
	 */
	public WSSubjectSecurityException(String message, Throwable cause) {
		super(message, cause);
	}
}
