package no.stelvio.common.security;

import no.stelvio.common.security.support.SimpleSecurityContext;
import no.stelvio.common.util.RedeployableThreadLocalSubstitute;

/**
 * Helper class to manage a thread-bound instance of the <code>SecurityContext</code> class.
 * 
 * @author persondab2f89862d3
 * @see SimpleSecurityContext
 */
public final class SecurityContextHolder {
	/**
	 * Holds an instance of the <code>SecurityContext</code> class.
	 * 
	 * Use RedeployableThreadLocalSubstitute to avoid memory leak when application is stopped.
	 */
	// private static final ThreadLocal<SecurityContext> SECURITY_CONTEXT_HOLDER = new
	// InheritableThreadLocal<SecurityContext>();
	private static final RedeployableThreadLocalSubstitute<SecurityContext> SECURITY_CONTEXT_HOLDER = 
		new RedeployableThreadLocalSubstitute<SecurityContext>();

	/** Should not be instantiated. */
	private SecurityContextHolder() {
	}

	/**
	 * Bind the given <code>SecurityContext</code> to the current thread.
	 * 
	 * To be used reflectively if it is absolutely neccessary to explicitly set the SecurityContext.
	 * 
	 * @param context
	 *            the <code>SecurityContext</code> to expose.
	 */
	@SuppressWarnings("unused")
	private static void setSecurityContext(SecurityContext context) {
		SECURITY_CONTEXT_HOLDER.set(context);
	}

	/**
	 * Return the <code>SecurityContext</code> currently bound to the thread.
	 * 
	 * @return the <code>SecurityContext</code> currently bound to the thread.
	 * @throws IllegalStateException
	 *             if no <code>SecurityContext</code> is bound to the current thread.
	 */
	public static SecurityContext currentSecurityContext() throws IllegalStateException {
		SecurityContext context = SECURITY_CONTEXT_HOLDER.get();

		if (context == null) {
			throw new IllegalStateException("No thread-bound security context found: "
					+ "Make sure filters for binding the SecurityContext is setup properly for the web application "
					+ "in front and the proper request context interceptors are setup for the layers below.");
		}

		return context;
	}

	/**
	 * Returns true if <code>SecurityContext</code> is set.
	 * 
	 * @return set - true <code>SecurityContext</code> is set
	 */
	public static boolean isSecurityContextSet() {
		return SECURITY_CONTEXT_HOLDER.isValueSet();
	}

	/**
	 * Reset the <code>SecurityContext</code> for the current thread.
	 * 
	 * To be used reflectively if it is absolutely neccessary to explicitly reset the SecurityContext.
	 */
	private static void resetSecurityContext() {
		SECURITY_CONTEXT_HOLDER.remove();
	}
}