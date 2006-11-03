package no.stelvio.common.error.system;

import no.stelvio.common.error.ErrorCode;
import no.stelvio.common.error.SystemException;

/**
 * Thrown to indicate that communication with <b>TSS</b> failed.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2434 $ $Author: jrd2920 $ $Date: 2005-08-29 08:43:39 +0200 (Mon, 29 Aug 2005) $
 */
public class TSSException extends SystemException {
	/**
	 * Constructs a new TSSException with the specified error code.
	 * 
	 * @param code the error code to be used when handling the exception.
	 */
	public TSSException(ErrorCode code) {
		super();
	}

	/**
	 * Constructs a new TSSException with the specified error code 
	 * and cause.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param cause the cause of this exception.
	 */
	public TSSException(ErrorCode code, Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new copy of the specified exception.
	 * 
	 * @param other the original exception.
	 */
	protected TSSException(TSSException other) {
		super(other);
	}
	
	/**
	 * Constructs a new TSSException with the specified error code and argument.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param argument detail to be included in the error message.
	 */
	public TSSException(ErrorCode code, Object argument) {
		super(argument);
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
		return new TSSException(this);
	}
}
