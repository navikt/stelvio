package no.stelvio.common.error;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.util.SequenceNumberGenerator;

/**
 * Thrown to indicate that an unrecoverable system exception has occured. <p/>
 * Applications will typically not handle recovery from system exceptions.
 * 
 * @author person7553f5959484
 * @version $Revision: 2837 $ $Author: psa2920 $ $Date: 2006-01-09 16:12:14
 *          +0100 (ma, 09 jan 2006) $
 */
public class SystemException extends RuntimeException implements
		LoggableException {

	// Initialize upon construction
	private final String userId;

	private final String screenId;

	private final String processId;

	private final String transactionId;

	private final String errorId;

	private final int errorCode;

	private Object[] arguments;

	// The exception is not logged by default
	private boolean isLogged = false;

	/**
	 * Constructs a new SystemException with the specified error code.
	 * 
	 * @param code
	 *            the error code to be used when handling the exception.
	 */
	public SystemException(ErrorCode code) {
		this(code, null, null);
	}

	/**
	 * Constructs a new SystemException with the specified error code and
	 * argument.
	 * 
	 * @param code
	 *            the error code to be used when handling the exception.
	 * @param argument
	 *            detail to be included in the error message.
	 */
	public SystemException(ErrorCode code, Object argument) {
		this(code, null, new Object[] { argument });
	}

	/**
	 * Constructs a new SystemException with the specified error code and list
	 * of arguments.
	 * 
	 * @param code
	 *            the error code to be used when handling the exception.
	 * @param arguments
	 *            list of details to be included in the error message.
	 */
	public SystemException(ErrorCode code, Object[] arguments) {
		this(code, null, arguments);
	}

	/**
	 * Constructs a new SystemException with the specified error code and cause.
	 * 
	 * @param code
	 *            the error code to be used when handling the exception.
	 * @param cause
	 *            the cause of this exception.
	 */
	public SystemException(ErrorCode code, Throwable cause) {
		this(code, cause, null);
	}

	/**
	 * Constructs a new SystemException with the specified error code, cause and
	 * argument.
	 * 
	 * @param code
	 *            the error code to be used when handling the exception.
	 * @param cause
	 *            the cause of this exception.
	 * @param argument
	 *            detail to be included in the error message.
	 */
	public SystemException(ErrorCode code, Throwable cause, Object argument) {
		this(code, cause, new Object[] { argument });
	}

	/**
	 * Constructs a new SystemException with the specified error code, cause and
	 * list of arguments.
	 * 
	 * @param code
	 *            the error code to be used when handling the exception.
	 * @param cause
	 *            the cause of this exception.
	 * @param arguments
	 *            list of details to be included in the error message.
	 */
	public SystemException(ErrorCode code, Throwable cause, Object[] arguments) {
		super(cause);

		errorCode = code.getCode();
		userId = RequestContext.getUserId();
		screenId = RequestContext.getScreenId();
		processId = RequestContext.getProcessId();
		transactionId = RequestContext.getTransactionId();
		errorId = String.valueOf(SequenceNumberGenerator.getNextId("ErrorId"));

		copyArguments(arguments);
	}

	/**
	 * Constructs a new copy of the specified SystemException.
	 * 
	 * @param other
	 *            the original exception.
	 */
	protected SystemException(SystemException other) {

		errorCode = other.errorCode;
		userId = other.userId;
		screenId = other.screenId;
		processId = other.processId;
		transactionId = other.transactionId;
		errorId = other.errorId;
		isLogged = other.isLogged;

		copyArguments(other.arguments);
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean isLogged() {
		return isLogged;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void setLogged() {
		isLogged = true;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String getErrorId() {
		return errorId;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String getUserId() {
		return userId;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String getProcessId() {
		return processId;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String getTransactionId() {
		return transactionId;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String getScreenId() {
		return screenId;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] getArguments() {
		Object[] array = new Object[arguments.length];
		System.arraycopy(arguments, 0, array, 0, arguments.length);

		return array;
	}

	/**
	 * Gets a String representation of the error code.
	 * 
	 * {@inheritDoc}
	 */
	public String getMessage() {
		return String.valueOf(errorCode);
	}

	/**
	 * Returns a short description of this exception instance, containing the
	 * following:
	 * <ul>
	 * <li>The name of the actual class of this object
	 * <li>The result of the {@link #getErrorCode} method for this object
	 * <li>The arguments this object was created with
	 * </ul>
	 * 
	 * {@inheritDoc}
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(getClass().getName());
		sb.append(": Code=").append(errorCode);
		sb.append(",ErrId=").append(errorId);
		sb.append(",Screen=").append(screenId);
		sb.append(",Process=").append(processId);
		sb.append(",TxId=").append(transactionId);
		sb.append(",User=").append(userId);

		if (null != arguments && 0 < arguments.length) {
			sb.append(",Args=[");

			for (int idx = 0; idx < arguments.length; idx++) {
				sb.append(arguments[idx]);

				if (idx < arguments.length - 1) {
					sb.append(',');
				}
			}

			sb.append(']');
		}

		return sb.toString();
	}

	/**
	 * Creates an exact copy of this instance. <p/> Note that the elements of
	 * the copy's arguments are the same elements as in the original. <p/> Also
	 * notice that the cause will not be copied.
	 * 
	 * {@inheritDoc}
	 */
	public Object copy() {
		return new SystemException(this);
	}

	/**
	 * Copies the arguments array.
	 * 
	 * @param arguments
	 *            the array of arguments to be copied.
	 */
	private void copyArguments(final Object[] arguments) {
		if (null == arguments) {
			// So NullPointerException is avoided
			this.arguments = new Object[] {};
		} else {
			this.arguments = new Object[arguments.length];
			System.arraycopy(arguments, 0, this.arguments, 0, arguments.length);
		}
	}
}
