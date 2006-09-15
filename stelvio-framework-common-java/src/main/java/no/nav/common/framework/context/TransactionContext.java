package no.nav.common.framework.context;

/**
 * This class is used to store and retrieve information that must
 * be available anywhere in the system during a single request.
 * The population of the different fields in the TransactionContext is left to the
 * outmost component (normally the Presentation Framework).
 *
 * All transaction specific information is stored for the current thread.
 * Again, it is the outmost component responsibility to populate
 * and remove the data from the thread.
 *
 * While working on different tiers, the TransactionContext must be exported 
 * in one tier, and then imported back again on the other tier.
 *
 * @author person7553f5959484, Accenture
 * @version $Id: TransactionContext.java 1979 2005-02-16 16:34:40Z psa2920 $
 */
public final class TransactionContext extends AbstractContext {

	// Internal constants to be used as keys for accessing the heap
	private static final String USER_ID = "user";
	private static final String SCREEN_ID = "screen";
	private static final String MODULE_ID = "module";
	private static final String PROCESS_ID = "process";
	private static final String TRANSACTION_ID = "transaction";
	private static final String STATE = "state";

	/**
	 * Private constructor to ensure class being accessed
	 * in a static way only
	 */
	private TransactionContext() {
		super();
	}

	// ---------- Convenient Getter and Setter methods

	/**
	 * Get the id of the process that is currently executing. The id should be informative
	 * and describe the name of the process, e.g. "Find Users" and "Save Changes".
	 * 
	 * @return The id of the current process
	 */
	public static String getProcessId() {
		return (String) get(PROCESS_ID);
	}

	/**
	 * Get the id of the screen that started the process that is currently executing. The id
	 * should be informative and describe the name of the screen, e.g. "TaskList" and "Customer".
	 * 
	 * @return The id of the current screen, null if not user initiated
	 */
	public static String getScreenId() {
		return (String) get(SCREEN_ID);
	}

	/**
	 * Get the id of the module that started the process that is currently executing. The id
	 * should be informative and describe the name of the screen, e.g. "TaskList" and "Customer".
	 * <p/>
	 * <i>Note! &nbsp; More than one module may be initiated by a screen.</i>
	 * 
	 * @return The id of the current module, null if not user initiated
	 */
	public static String getModuleId() {
		return (String) get(MODULE_ID);
	}

	/**
	 * Get the unique id of the transaction that is currently executing. The id is typically 
	 * a unique number or string, and not a descriptive name. Current user may initiate the same 
	 * process more than one time, but the transaction id will typically be unique per execution. 
	 * 
	 * @return The id of current transaction.
	 */
	public static String getTransactionId() {
		return (String) get(TRANSACTION_ID);
	}

	/**
	 * Get the id of the user or system that started the process that is currently executing.
	 * 
	 * @return The id of the current user/system
	 */
	public static String getUserId() {
		return (String) get(USER_ID);
	}

	/**
	 * Get the state in which the current process is executing.
	 * 
	 * @return The state of the current process.
	 */
	public static String getState() {
		return (String) get(STATE);
	}

	/**
	 * Assign the id of the process that is currently executing. The id should be informative
	 * and describe the name of the process, e.g. "Find Users" and "Save Changes".
	 * 
	 * @param processId The id of the current process
	 */
	public static void setProcessId(String processId) {
		put(PROCESS_ID, processId);
	}

	/**
	 * Assign the id of the screen that started the process that is currently executing. The id
	 * should be informative and describe the name of the screen, e.g. "TaskList" and "Customer".
	 * 
	 * @param screenId The id of the current screen, null if not user initiated
	 */
	public static void setScreenId(String screenId) {
		put(SCREEN_ID, screenId);
	}

	/**
	 * Assign the id of the module that started the process that is currently executing. The id
	 * should be informative and describe the name of the module, e.g. "TaskList" and "Customer".
	 * <p/>
	 * <i>Note! &nbsp; More than one module may be initiated by a screen.</i>
	 * 
	 * @param moduleId The id of the current module, null if not user initiated
	 */
	public static void setModuleId(String moduleId) {
		put(MODULE_ID, moduleId);
	}

	/**
	 * Assign the unique id of the transaction that is currently executing. The id is typically 
	 * a unique number or string, and not a descriptive name. Current user may initiate the same 
	 * process more than one time, but the transaction id will typically be unique per execution. 
	 * 
	 * @param transactionId The id of current transaction.
	 */
	public static void setTransactionId(String transactionId) {
		put(TRANSACTION_ID, transactionId);
	}

	/**
	 * Get the id of the user or system that started the process that is currently executing.
	 * 
	 * @param userId The id of the current user/system
	 */
	public static void setUserId(String userId) {
		put(USER_ID, userId);
	}

	/**
	 * Set the state in which the current process is executing.
	 * 
	 * @param state The state of the current process.
	 */
	public static void setState(String state) {
		put(STATE, state);
	}
}
