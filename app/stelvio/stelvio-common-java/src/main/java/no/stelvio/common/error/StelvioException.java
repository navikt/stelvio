package no.stelvio.common.error;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo should this be package private? Is this needed outside the package?
 */
public interface StelvioException extends LoggableException {
    /**
	 * Get the getTemplateArguments identifying details about the error.
	 *
	 * @return The getTemplateArguments detailing this exception.
	 */
	Object [] getTemplateArguments();

    /**
	 * Get the unique id of this exception.
     *
     * @return The unique id of this exception
     */
    long getErrorId();

    /**
	 * Get the id of the user or system that executed
     * the code when the exception occured.
     *
     * @return The id of the current user/system
     */
    String getUserId();

    /**
	 * Get the id of the screen that started the process
     * that executed the code that failed.
     *
     * @return The id of the current screen, null if not user initiated
     */
    String getScreenId();

    /**
	 * Get the id of the process that was executing
     * the code when the exception occured.
     *
     * @return The id of the current process
     */
    String getProcessId();

    /**
	 * Get the unique id of the transaction that was executing
     * the code when the exception occured.
     *
     * @return The unique id of the current transaction
     */
    String getTransactionId();
}
