package no.stelvio.common.context;

/**
 * Has information that should be accessible anywhere in the system for a single request. Implementations decide how to
 * get to/store the information necessary for providing this functionality.
 *
 * @author personf8e9850ed756, Accenture
 */
public interface RequestContext {
	/**
	 * Get the id of the process that is currently executing. The id should be informative
	 * and describe the name of the process, e.g. "Find Users" and "Save Changes".
	 *
	 * @return The id of the current process
	 */
	String getProcessId();

	/**
	 * Get the id of the screen that started the process that is currently executing. The id
	 * should be informative and describe the name of the screen, e.g. "TaskList" and "Customer".
	 *
	 * @return The id of the current screen, null if not user initiated
	 */
	String getScreenId();

	/**
	 * Get the id of the module that started the process that is currently executing. The id
	 * should be informative and describe the name of the screen, e.g. "TaskList" and "Customer".
	 * <p/>
	 * <i>Note! &nbsp; More than one module may be initiated by a screen.</i>
	 *
	 * @return The id of the current module, null if not user initiated
	 */
	String getModuleId();

	/**
	 * Get the unique id of the transaction that is currently executing. The id is typically
	 * a unique number or string, and not a descriptive name. Current user may initiate the same
	 * process more than one time, but the transaction id will typically be unique per execution.
	 *
	 * @return The id of the current transaction.
	 */
	String getTransactionId();
}
