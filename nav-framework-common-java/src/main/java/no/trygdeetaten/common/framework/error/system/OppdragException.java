package no.trygdeetaten.common.framework.error.system;

import no.trygdeetaten.common.framework.error.ErrorCode;
import no.trygdeetaten.common.framework.error.SystemException;

/**
 * Thrown to indicate that communication with <b>OPPDRAG</b> failed.
 * 
 * @author persone5d69f3729a8, Accenture
 * @version $Id: OppdragException.java 2567 2005-10-19 15:27:11Z psa2920 $
 */
public class OppdragException extends SystemException {

	/** Name of the enterprise system. */
	public static final String OPPDRAG = "Oppdrag";

	/**
	 * Constructs a new OppdragException with the specified error code.
	 * 
	 * @param code the error code to be used when handling the exception.
	 */
	public OppdragException(ErrorCode code) {
		super(code, OPPDRAG);
	}

	/**
	 * Constructs a new OppdragException with the specified error code 
	 * and cause.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param cause the cause of this exception.
	 */
	public OppdragException(ErrorCode code, Throwable cause) {
		super(code, cause, OPPDRAG);
	}

	/**
	 * Constructs a new OppdragException with the specified error code 
	 * and list of arguments.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param arguments list of details to be included in the error message.
	 */
	public OppdragException(ErrorCode code, Object[] arguments) {
		super(code, arguments);
	}

	/**
	 * Constructs a new copy of the specified exception.
	 * 
	 * @param other the original exception.
	 */
	protected OppdragException(OppdragException other) {
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
	 * @see no.trygdeetaten.common.framework.error.LoggableException#copy()
	 */
	public Object copy() {
		return new OppdragException(this);
	}

}
