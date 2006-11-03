package no.stelvio.common.error.system;

import no.stelvio.common.error.ErrorCode;
import no.stelvio.common.error.SystemException;

/**
 * Thrown to indicate that communication with <b>Predator</b> failed.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2389 $ $Author: jrd2920 $ $Date: 2005-06-29 08:50:15 +0200 (Wed, 29 Jun 2005) $
 */
public class PredatorException extends SystemException {

	/**
	 * Constructs a new PredatorException with the specified error code.
	 * 
	 * @param code the error code to be used when handling the exception.
	 */
	public PredatorException(ErrorCode code) {
		super();
	}

	/**
	 * Constructs a new PredatorException with the specified error code 
	 * and cause.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param cause the cause of this exception.
	 */
	public PredatorException(ErrorCode code, Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new PredatorException with the specified error code 
	 * and cause.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param argument the cause of this exception.
	 */
	public PredatorException(ErrorCode code, Object argument) {
		super(argument);
	}

	/**
	 * Constructs a new copy of the specified exception.
	 * 
	 * @param other the original exception.
	 */
	protected PredatorException(PredatorException other) {
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
		return new PredatorException(this);
	}
}
