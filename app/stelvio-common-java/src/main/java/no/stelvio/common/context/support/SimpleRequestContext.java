package no.stelvio.common.context.support;

import no.stelvio.common.context.RequestContext;

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
 * @author personf8e9850ed756, Accenture
 * @version $Id: RequestContext.java 1979 2005-02-16 16:34:40Z psa2920 $
 * @todo make this into a "standalone class", and have a RequestContextHolder that holds an instance of this in the
 * thread context. Or even better, have the possibility to have multiple context objects in the thread context, see
 * the *ContextHolder classes in Spring. By having this, we could have the Log4J's MDC values by it self, not inside
 * this class.
 */
public final class SimpleRequestContext extends AbstractContext implements RequestContext {
	private static final String SCREEN_ID = "screen";
	private static final String MODULE_ID = "module";
	private static final String PROCESS_ID = "process";
	private static final String TRANSACTION_ID = "transaction";
	
	/**
	 * Initializes the instance with the context specified.
	 *
	 * @param screenId the current screen id.
	 * @param moduleId the current module id.
	 * @param processId the current process id.
	 * @param transactionId the current transaction id.
	 */
	public SimpleRequestContext(String screenId, String moduleId, String processId, String transactionId) {
		put(SCREEN_ID, screenId);
		put(MODULE_ID, moduleId);
		put(PROCESS_ID, processId);
		put(TRANSACTION_ID, transactionId);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getProcessId() {
		return (String) get(PROCESS_ID);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getScreenId() {
		return (String) get(SCREEN_ID);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getModuleId() {
		return (String) get(MODULE_ID);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTransactionId() {
		return (String) get(TRANSACTION_ID);
	}

	/**
	 * Implementation of the Builder pattern to more easily build a <code>RequestContext</code>, especially when having
	 * to create a new version based on another.
	 */
	public static class Builder {
		private String moduleId;
		private String screenId;
		private String processId;
		private String transactionId;

		public Builder() {
		}

		public Builder(RequestContext requestContext) {
			moduleId = requestContext.getModuleId();
			screenId = requestContext.getScreenId();
			processId = requestContext.getProcessId();
			transactionId = requestContext.getTransactionId();
		}

		public Builder moduleId(String moduleId) {
			this.moduleId = moduleId;

			return this;
		}

		public Builder screenId(String screenId) {
			this.screenId = screenId;

			return this;
		}

		public Builder processId(String processId) {
			this.processId = processId;

			return this;
		}

		public Builder transactionId(String transactionId) {
			this.transactionId = transactionId;

			return this;
		}

		public SimpleRequestContext build() {
			return new SimpleRequestContext(screenId, moduleId, processId, transactionId);
		}
	}
}
