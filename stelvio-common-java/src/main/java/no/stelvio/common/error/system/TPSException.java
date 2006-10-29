package no.stelvio.common.error.system;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.error.old.ErrorCode;
import no.stelvio.common.error.SystemException;

/**
 * Thrown to indicate that communication with <b>TPS</b> failed.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2762 $ $Author: skb2930 $ $Date: 2006-02-07 15:24:54 +0100 (Tue, 07 Feb 2006) $
 */
public class TPSException extends SystemException {
	private static final String TPS = "TPS";
	private String identifier;

	/** Should not be possible to instansiate directly, only through factory methods. */
	private TPSException() {
		super(ErrorCode.UNSPECIFIED_ERROR);
	}

	/**
	 * Constructs a new TPSException with the specified error code, cause, argument and identifier. Should only be used by the
	 * factory methods, therefore it is made private.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param cause the cause of this exception.
	 * @param argument the argument to the text string to write out for this exception.
	 * @param identifier the identifier for the request that caused this exception.
	 */
	private TPSException(ErrorCode code, Throwable cause, final String argument, final String identifier) {
		super(code, cause, argument);
		this.identifier = identifier;
	}

	/** Constructs a new TPSException to be used when TPS is not answering. */
	public static TPSException systemNotAnswering() {
		return new TPSException(FrameworkError.SYSTEM_NOT_ANSWERING, null, TPS, null);
	}

	/**
	 * Constructs a new TPSException to be used when TPS in unavailable.
	 *
	 * @param cause the cause of this exception.
	 * @param identifier the identifier for the request that caused this exception.
	 */
	public static TPSException systemUnavailableError(final Throwable cause, final String identifier) {
		return new TPSException(FrameworkError.SYSTEM_UNAVAILABLE_ERROR, cause, null, identifier);
	}

	/**
	 * Constructs a new TPSException to be used when TPS failed with return code 08.
	 *
	 * @param errorMessage the error message from TPS.
	 * @param identifier the identifier for the request that caused this exception.
	 */
	public static TPSException failedWithReturnCode08(final String errorMessage, final String identifier) {
		return new TPSException(FrameworkError.TPS_ERROR_08, null, errorMessage, identifier);

	}

	/**
	 * Constructs a new TPSException to be used when TPS failed with return code 09.
	 *
	 * @param errorMessage the error message from TPS.
	 * @param identifier the identifier for the request that caused this exception.
	 */
	public static TPSException failedWithReturnCode09(final String errorMessage, final String identifier) {
		return new TPSException(FrameworkError.TPS_ERROR_09, null, errorMessage, identifier);

	}

	/**
	 * Constructs a new TPSException to be used when TPS failed with return code 12.
	 *
	 * @param errorMessage the error message from TPS.
	 * @param identifier the identifier for the request that caused this exception.
	 */
	public static TPSException failedWithReturnCode12(final String errorMessage, final String identifier) {
		return new TPSException(FrameworkError.TPS_ERROR_12, null, errorMessage, identifier);

	}

	/**
	 * Constructs a new copy of the specified exception.
	 * 
	 * @param other the original exception.
	 */
	protected TPSException(TPSException other) {
		super(other);
		identifier = other.identifier;
	}

	/**
	 * Checks whether this instance is an error thrown because a system is not answering.
	 *
	 * @return true if this instance is an error thrown because a system is not answering, false otherwise.
	 */
	public boolean isSystemNotAnsweringError() {
		return FrameworkError.SYSTEM_NOT_ANSWERING.getCode() == getErrorCode();
	}

	/**
	 * Checks whether this instance is an error thrown because a system is unavailable.
	 *
	 * @return true if this instance is an error thrown because a system is unavailable, false otherwise.
	 */
	public boolean isSystemUnavailableError() {
		return FrameworkError.SYSTEM_UNAVAILABLE_ERROR.getCode() == getErrorCode();
	}

	/**
	 * Checks whether this instance is an error thrown because the return code from TPS is 08.
	 *
	 * @return true if this instance is an error thrown because the return code from TPS is 08, false otherwise.
	 */
	public boolean isReturnCode08Error() {
		return FrameworkError.TPS_ERROR_08.getCode() == getErrorCode();
	}

	/**
	 * Checks whether this instance is an error thrown because the return code from TPS is 09.
	 *
	 * @return true if this instance is an error thrown because the return code from TPS is 09, false otherwise.
	 */
	public boolean isReturnCode09Error() {
		return FrameworkError.TPS_ERROR_09.getCode() == getErrorCode();
	}

	/**
	 * Checks whether this instance is an error thrown because the return code from TPS is 12.
	 *
	 * @return true if this instance is an error thrown because the return code from TPS is 12, false otherwise.
	 */
	public boolean isReturnCode12Error() {
		return FrameworkError.TPS_ERROR_12.getCode() == getErrorCode();
	}

	/**
	 * Creates an exact copy of this instance. 
	 * <p/>
	 * Note that the elements of the copy's arguments are the same elements as in the original.
	 * <p/>
	 * Also notice that the cause will not be copied.
	 * 
	 * {@inheritDoc}
	 */
	public Object copy() {
		return new TPSException(this);
	}

	/**
	 * Returns the identifier for the request that caused this exception.
	 *
	 * @return the identifier for the request that caused this exception.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Appending the identifier to the output from the super class.
	 */
	public String toString() {
		final String superToString = super.toString();

		return superToString + ",Identifier=" + identifier;
	}
}
