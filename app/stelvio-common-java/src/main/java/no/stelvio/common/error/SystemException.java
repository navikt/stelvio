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
public abstract class SystemException extends RuntimeException implements
		LoggableException {

	// Initialize upon construction
	private final String userId;

	private final String screenId;

	private final String processId;

	private final String transactionId;

	private final String errorId;

    private Object[] arguments;

	// The exception is not logged by default
	private boolean isLogged = false;

	/**
	 * Constructs a new SystemException with the specified error code.
	 *
     */
	public SystemException() {
		this(null, null);
	}

	/**
	 * Constructs a new SystemException with the specified error code and
	 * argument.
	 * 
	 * @param argument
     */
	public SystemException(Object argument) {
		this(null, new Object[] { argument });
	}
	
	/**
	 * Constructs a new SystemException with the specified error code and list
	 * of arguments.
	 * 
	 * @param arguments
     */
	public SystemException(Object[] arguments) {
		this(null, arguments);
	}

	/**
	 * Constructs a new SystemException with the specified error code and cause.
	 * 
	 * @param cause
     */
	public SystemException(Throwable cause) {
		this(cause, null);
	}

	/**
	 * Constructs a new SystemException with the specified error code, cause and
	 * argument.
	 * 
	 * @param cause
	 *            the cause of this exception.
     * @param argument
     */
	public SystemException(Throwable cause, Object argument) {
		this(cause, new Object[] { argument });
	}

	/**
	 * Constructs a new SystemException with the specified error code, cause and
	 * list of arguments.
	 * 
	 * @param cause
	 *            the cause of this exception.
     * @param arguments
     */
	public SystemException(Throwable cause, Object[] arguments) {
		super(cause);

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
	public Object[] getArguments() {
		Object[] array = new Object[arguments.length];
		System.arraycopy(arguments, 0, array, 0, arguments.length);

		return array;
	}

	/**
	 * Gets a String representation of the error code.
	 * 
	 * {@inheritDoc}
     * @todo other message
	 */
	public String getMessage() {
		return "TODO";
	}

	/**
	 * Returns a short description of this exception instance, containing the
	 * following:
	 * <ul>
	 * <li>The name of the actual class of this object
	 * <li>The arguments this object was created with
	 * </ul>
	 * 
	 * {@inheritDoc}
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(getClass().getName());
		sb.append(": ErrId=").append(errorId);
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
