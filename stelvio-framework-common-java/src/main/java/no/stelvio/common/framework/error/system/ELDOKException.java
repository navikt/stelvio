package no.stelvio.common.framework.error.system;

import no.stelvio.common.framework.error.ErrorCode;
import no.stelvio.common.framework.error.SystemException;

/**
 * Thrown to indicate that communication with <b>ELDOK</b> failed.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1512 $ $Author: psa2920 $ $Date: 2004-11-09 14:47:15 +0100 (Tue, 09 Nov 2004) $
 */
public class ELDOKException extends SystemException {

	/**
	 * Constructs a new ELDOKException with the specified error code.
	 * 
	 * @param code the error code to be used when handling the exception.
	 */
	public ELDOKException(ErrorCode code) {
		super(code);
	}

	/**
	 * Constructs a new ELDOKException with the specified error code 
	 * and cause.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param cause the cause of this exception.
	 */
	public ELDOKException(ErrorCode code, Throwable cause) {
		super(code, cause);
	}

	/**
	 * Constructs a new copy of the specified exception.
	 * 
	 * @param other the original exception.
	 */
	protected ELDOKException(ELDOKException other) {
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
	 * @see no.stelvio.common.framework.error.LoggableException#copy()
	 */
	public Object copy() {
		return new ELDOKException(this);
	}
}
