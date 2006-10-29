package no.stelvio.common.error.system;

import no.stelvio.common.error.old.ErrorCode;
import no.stelvio.common.error.SystemException;

/**
 * Thrown to indicate that communication with <b>ACF2</b> failed.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2132 $ $Author: psa2920 $ $Date: 2005-03-16 13:48:43 +0100 (Wed, 16 Mar 2005) $
 */
public class ACF2Exception extends SystemException {

	private static final String ACF2 = "ACF2";

	/**
	 * Constructs a new ACF2Exception with the specified error code.
	 * 
	 * @param code the error code to be used when handling the exception.
	 */
	public ACF2Exception(ErrorCode code) {
		super(code, ACF2);
	}

	/**
	 * Constructs a new ACF2Exception with the specified error code 
	 * and cause.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param cause the cause of this exception.
	 */
	public ACF2Exception(ErrorCode code, Throwable cause) {
		super(code, cause, ACF2);
	}

	/**
	 * Constructs a new copy of the specified exception.
	 * 
	 * @param other the original exception.
	 */
	protected ACF2Exception(ACF2Exception other) {
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
		return new ACF2Exception(this);
	}
}
