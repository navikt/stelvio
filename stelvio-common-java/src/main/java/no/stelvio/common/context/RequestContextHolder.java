package no.stelvio.common.context;

import no.stelvio.common.util.RedeployableThreadLocalSubstitute;

/**
 * Helper class to manage a thread-bound instance of the <code>RequestContext</code> class.
 * 
 * @author personf8e9850ed756
 * @see RequestContext
 */
public final class RequestContextHolder {
	/**
	 * Holds an instance of the <code>RequestContext</code> class.
	 * 
	 * Use RedeployableThreadLocalSubstitute to avoid memory leak when application is stopped.
	 */
	//private static final ThreadLocal<RequestContext> REQUEST_CONTEXT_HOLDER = new InheritableThreadLocal<RequestContext>();
	private static final RedeployableThreadLocalSubstitute<RequestContext> REQUEST_CONTEXT_HOLDER = 
			new RedeployableThreadLocalSubstitute<RequestContext>();

	/** Should not be instantiated. */
	private RequestContextHolder() {
	}

	/**
	 * Return the <code>RequestContext</code> currently bound to the thread.
	 * 
	 * @return the <code>RequestContext</code> currently bound to the thread.
	 * @throws IllegalStateException
	 *             if no <code>RequestContext</code> is bound to the current thread.
	 */
	public static RequestContext currentRequestContext() throws IllegalStateException {
		RequestContext context = REQUEST_CONTEXT_HOLDER.get();

		if (context == null) {
			throw new IllegalStateException("No thread-bound request context found: "
					+ "Make sure filters for binding the request is setup properly for the web application "
					+ "in front and the proper request context interceptors are setup for the layers below.");
		}

		return context;
	}

	/**
	 * Returns true if <code>RequestContext</code> is set.
	 * 
	 * @return set - true <code>RequestContext</code> is set
	 */
	public static boolean isRequestContextSet() {
		return REQUEST_CONTEXT_HOLDER.isValueSet();
	}
}
