package no.stelvio.common.error.system;

import no.stelvio.common.error.old.ErrorCode;
import no.stelvio.common.error.SystemException;

/**
 * Thrown to indicate that communication with <b>BBM</b> failed.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2516 $ $Author: jla2920 $ $Date: 2005-10-04 22:12:15 +0200 (Tue, 04 Oct 2005) $
 */
public class BBMException extends SystemException {

	/**
	 * Constructs a new BBMException with the specified error code.
	 * 
	 * @param code the error code to be used when handling the exception.
	 */
	public BBMException(ErrorCode code) {
		super(code);
	}

	/**
	 * Constructs a new BBMException with the specified error code and argument.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param argument detail to be included in the error message.
	 */
	public BBMException(ErrorCode code, Object argument) {
		super(code, argument);
	}

	/**
	 * Constructs a new BBMException with the specified error code and list of arguments.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param arguments list of details to be included in the error message.
	 */
	public BBMException(ErrorCode code, Object[] arguments) {
		super(code, null, arguments);
	}

	/**
	 * Constructs a new BBMException with the specified error code 
	 * and cause.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param cause the cause of this exception.
	 */
	public BBMException(ErrorCode code, Throwable cause) {
		super(code, cause);
	}

	/**
	 * Constructs a new copy of the specified exception.
	 * 
	 * @param other the original exception.
	 */
	protected BBMException(BBMException other) {
		super(other);
	}

	/**
	 * Creates an exact copy of this instance. 
	 * <p/>
	 * Note that the elements of the copy's arguments
	 * are the same elements as in the original.
	 * <p/>
	 * Also notice that the cause will not be copied.
	 * 
	 * {@inheritDoc}
	 * @see no.stelvio.common.error.LoggableException#copy()
	 */
	public Object copy() {
		return new BBMException(this);
	}
}
