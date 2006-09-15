package no.nav.common.framework.service;

import no.nav.common.framework.error.ApplicationException;
import no.nav.common.framework.error.ErrorCode;

/**
 * Thrown to indicate that execution of a <i>Service</i> failed.
 * 
 * @author person7553f5959484
 * @version $Revision: 2297 $ $Author: psa2920 $ $Date: 2005-05-31 12:06:20 +0200 (Tue, 31 May 2005) $
 */
public class ServiceFailedException extends ApplicationException {

	/** Flag indicating that current transaction should be marked for rollback */
	private boolean rollbackOnly = false;

	/**
	 * Constructs a new ServiceFailedException with the specified error code.
	 * 
	 * @param code the error code to be used when handling the exception.
	 */
	public ServiceFailedException(ErrorCode code) {
		super(code);
	}

	/**
	 * Constructs a new ServiceFailedException with the specified error code 
	 * and argument.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param argument detail to be included in the error message.
	 */
	public ServiceFailedException(ErrorCode code, Object argument) {
		super(code, argument);
	}

	/**
	 * Constructs a new ServiceFailedException with the specified error code 
	 * and list of arguments.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param arguments list of details to be included in the error message.
	 */
	public ServiceFailedException(ErrorCode code, Object[] arguments) {
		super(code, arguments);
	}

	/**
	 * Constructs a new ServiceFailedException with the specified error code 
	 * and cause.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param cause the cause of this exception.
	 */
	public ServiceFailedException(ErrorCode code, Throwable cause) {
		super(code, cause);
	}

	/**
	 * Constructs a new ServiceFailedException with the specified error code, 
	 * cause and argument.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param cause the cause of this exception.
	 * @param argument detail to be included in the error message.
	 */
	public ServiceFailedException(ErrorCode code, Throwable cause, Object argument) {
		super(code, cause, argument);
	}

	/**
	 * Constructs a new ServiceFailedException with the specified error code, 
	 * cause and list of arguments.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param cause the cause of this exception.
	 * @param arguments list of details to be included in the error message.
	 */
	public ServiceFailedException(ErrorCode code, Throwable cause, Object[] arguments) {
		super(code, cause, arguments);
	}

	/**
	 * Constructs a new copy of the specified ServiceFailedException.
	 * 
	 * @param other the original exception.
	 */
	protected ServiceFailedException(ServiceFailedException other) {
		super(other);
		this.rollbackOnly = other.isRollbackOnly();
	}

	/**
	 * Test if the transaction has been marked for rollback only. An enterprise 
	 * bean can use this operation, for example, to test after an exception has 
	 * been caught, wheter it is fruitless to continue computation on behalf of 
	 * the current transaction. Only enterprise beans with container-managed 
	 * transactions are allowed to use this method.
	 * 
	 * @return true if the current transaction is marked for rollback, false otherwise.
	 */
	public boolean isRollbackOnly() {
		return rollbackOnly;
	}

	/**
	 * Mark transaction for rollback.
	 */
	public void setRollbackOnly() {
		this.rollbackOnly = true;
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
	 * @see no.nav.common.framework.error.LoggableException#copy()
	 */
	public Object copy() {
		return new ServiceFailedException(this);
	}
}