package no.stelvio.common.error;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.util.SequenceNumberGenerator;

/**
 * Thrown to indicate that a recoverable exception in the application logic
 * occured. <p/> This is the base exception that all application specific
 * exceptions must extend.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: ApplicationException.java 2839 2006-04-25 10:23:10Z psa2920 $
 * @todo change javadoc to say something about only use it when the client MUST catch it else use SystemException.
 */
public abstract class ApplicationException extends Exception implements LoggableException {

	// Initialize upon construction
	private final String userId;

	private final String screenId;

	private final String processId;

	private final String transactionId;

	private final String errorId;

    private final Object[] arguments;

	// The exception is not logged by default
	private boolean isLogged = false;

	/**
	 * Constructs a new ApplicationException with the specified error code.
	 *
     */
	public ApplicationException() {
		this(null, null);
	}

	/**
	 * Constructs a new ApplicationException with the specified error code and
	 * argument.
	 * 
	 * @param argument
     */
	public ApplicationException(Object argument) {
		this(null, new Object[] { argument });
	}

	/**
	 * Constructs a new ApplicationException with the specified error code and
	 * list of arguments.
	 * 
	 * @param arguments
     */
	public ApplicationException(Object[] arguments) {
		this(null, arguments);
	}

	/**
	 * Constructs a new ApplicationException with the specified error code and
	 * cause.
	 * 
	 * @param cause
     */
	public ApplicationException(Throwable cause) {
		this(cause, null);
	}

	/**
	 * Constructs a new ApplicationException with the specified error code,
	 * cause and argument.
	 * 
	 * @param cause
	 *            the cause of this exception.
     * @param argument
     */
	public ApplicationException(Throwable cause, Object argument) {
		this(cause, new Object[] { argument });
	}

	/**
	 * Constructs a new ApplicationException with the specified error code,
	 * cause and list of arguments.
	 * 
	 * @param cause
	 *            the cause of this exception.
     * @param arguments
     */
	public ApplicationException(Throwable cause, Object[] arguments) {
		super(cause);

        if (null == arguments) {
			this.arguments = null;
		} else {
			this.arguments = new Object[arguments.length];
			System.arraycopy(arguments, 0, this.arguments, 0, arguments.length);
		}

		this.userId = RequestContext.getUserId();
		this.screenId = RequestContext.getScreenId();
		this.processId = RequestContext.getProcessId();
		this.transactionId = RequestContext.getTransactionId();
		this.errorId = String.valueOf(SequenceNumberGenerator.getNextId("ErrorId"));
	}

	/**
	 * Constructs a new copy of the specified SystemException.
	 * 
	 * @param other
	 *            the original exception.
	 */
	protected ApplicationException(ApplicationException other) {
		super();
        if (null == other.getArguments()) {
			this.arguments = null;
		} else {
			Object[] tmp = new Object[other.getArguments().length];
			System.arraycopy(other.getArguments(), 0, tmp, 0, other.getArguments().length);
			this.arguments = tmp;
		}
		this.userId = other.userId;
		this.screenId = other.screenId;
		this.processId = other.processId;
		this.transactionId = other.transactionId;
		this.errorId = other.errorId;
		this.isLogged = other.isLogged;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see no.stelvio.common.error.LoggableException#isLogged()
	 */
	public final boolean isLogged() {
		return isLogged;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see no.stelvio.common.error.LoggableException#setLogged()
	 */
	public final void setLogged() {
		isLogged = true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see no.stelvio.common.error.LoggableException#getErrorId()
	 */
	public final String getErrorId() {
		return errorId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see no.stelvio.common.error.LoggableException#getUserId()
	 */
	public final String getUserId() {
		return userId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see no.stelvio.common.error.LoggableException#getProcessId()
	 */
	public final String getProcessId() {
		return processId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see no.stelvio.common.error.LoggableException#getTransactionId()
	 */
	public final String getTransactionId() {
		return transactionId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see no.stelvio.common.error.LoggableException#getScreenId()
	 */
	public final String getScreenId() {
		return screenId;
	}

    /**
	 * {@inheritDoc}
	 * 
	 * @see no.stelvio.common.error.LoggableException#getArguments()
	 */
	public Object[] getArguments() {
		if (null == arguments) {
			return null;
		} else {
			Object[] array = new Object[arguments.length];
			System.arraycopy(arguments, 0, array, 0, arguments.length);
			return array;
		}
	}

	/**
	 * Gets a String representation of the error code.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Throwable#getMessage()
     * @todo not correct in new version
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
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(getClass().getName());
		sb.append(": ErrId=").append(this.errorId);
		sb.append(",Screen=").append(this.screenId);
		sb.append(",Process=").append(this.processId);
		sb.append(",TxId=").append(this.transactionId);
		sb.append(",User=").append(this.userId);
		if (null != this.arguments && 0 < this.arguments.length) {
			sb.append(",Args=[");
			for (int arg = 0; arg < this.arguments.length; arg++) {
				sb.append(this.arguments[arg]);
				if (arg < this.arguments.length - 1) {
					sb.append(",");
				}
			}
			sb.append("]");
		}
		return sb.toString();
	}
}