package no.stelvio.common.context;

/**
 * Helper class to manage a thread-bound instance of the <code>RequestContext</code> class.
 *
 * @author personf8e9850ed756
 * @see RequestContext
 */
public final class RequestContextHolder  {
    /** Holds an instance of the <code>RequestContext</code> class. */
	private static final ThreadLocal<RequestContext> requestContextHolder = new InheritableThreadLocal<RequestContext>();

    /** Should not be instantiated. */
    private RequestContextHolder() {
    }

    /**
	 * Bind the given <code>RequestContext</code> to the current thread.
     *
	 * @param context the <code>RequestContext</code> to expose
	 */
	public static void setRequestContext(RequestContext context) {
		requestContextHolder.set(context);
	}

	/**
	 * Return the <code>RequestContext</code> currently bound to the thread.
     *
	 * @return the <code>RequestContext</code> currently bound to the thread, or <code>null</code>.
	 */
	public static RequestContext getRequestContext() {
		return requestContextHolder.get();
	}

	/**
	 * Return the <code>RequestContext</code> currently bound to the thread.
     *
	 * @return the <code>RequestContext</code> currently bound to the thread.
	 * @throws IllegalStateException if no <code>RequestContext</code> is bound to the current thread.
	 */
	public static RequestContext currentRequestContext() throws IllegalStateException {
		RequestContext context = requestContextHolder.get();

        if (context == null) {
			throw new IllegalStateException("No thread-bound request: use RequestContextFilter");
		}

        return context;
	}
}
