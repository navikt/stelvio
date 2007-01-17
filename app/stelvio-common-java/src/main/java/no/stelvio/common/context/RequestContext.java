package no.stelvio.common.context;

/**
 * This class is used to store and retrieve information that must
 * be available anywhere in the system during a single request.
 * The population of the different fields in the RequestContext is left to the
 * outmost component (normally the Presentation Framework).
 *
 * All transaction specific information is stored for the current thread.
 * Again, it is the outmost component responsibility to populate
 * and remove the data from the thread.
 *
 * While working on different tiers, the RequestContext must be exported 
 * in one tier, and then imported back again on the other tier.
 *
 * @author person7553f5959484, Accenture
 * @version $Id: RequestContext.java 1979 2005-02-16 16:34:40Z psa2920 $
 * @todo make this into a "standalone class", and have a RequestContextHolder that holds an instance of this in the
 * thread context. Or even better, have the possibility to have multiple context objects in the thread context, see
 * the *ContextHolder classes in Spring. By having this, we could have the Log4J's MDC values by it self, not inside
 * this class.
 * @todo make a RequestContextBuilder -> easier to copy the old object when inserting just one new property
 */
public final class RequestContext extends AbstractContext {

	// Internal constants to be used as keys for accessing the heap
	private static final String USER_ID = "user";
	private static final String SCREEN_ID = "screen";
	private static final String MODULE_ID = "module";
	private static final String PROCESS_ID = "process";
	private static final String TRANSACTION_ID = "transaction";
	private static final String LOCALE = "locale";

	/**
	 * Initializes the instance with the context specified.
	 *
	 * @param userId the current user id.
	 * @param screenId the current screen id.
	 * @param moduleId the current module id.
	 * @param processId the current process id.
	 * @param transactionId the current transaction id.
	 */
	public RequestContext(String userId, String screenId, String moduleId, String processId, String transactionId) {
		put(USER_ID, userId);
		put(SCREEN_ID, screenId);
		put(MODULE_ID, moduleId);
		put(PROCESS_ID, processId);
		put(TRANSACTION_ID, transactionId);
	}

	// ---------- Convenient Getter and Setter methods

	/**
	 * Get the id of the process that is currently executing. The id should be informative
	 * and describe the name of the process, e.g. "Find Users" and "Save Changes".
	 * 
	 * @return The id of the current process
	 */
	public String getProcessId() {
		return (String) get(PROCESS_ID);
	}

	/**
	 * Get the id of the screen that started the process that is currently executing. The id
	 * should be informative and describe the name of the screen, e.g. "TaskList" and "Customer".
	 * 
	 * @return The id of the current screen, null if not user initiated
	 */
	public String getScreenId() {
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
	public String getModuleId() {
		return (String) get(MODULE_ID);
	}

	/**
	 * Get the unique id of the transaction that is currently executing. The id is typically 
	 * a unique number or string, and not a descriptive name. Current user may initiate the same 
	 * process more than one time, but the transaction id will typically be unique per execution. 
	 * 
	 * @return The id of current transaction.
	 */
	public String getTransactionId() {
		return (String) get(TRANSACTION_ID);
	}

	/**
	 * Get the id of the user or system that started the process that is currently executing.
	 * 
	 * @return The id of the current user/system
	 */
public String getUserId() {
		return (String) get(USER_ID);
	}
}
