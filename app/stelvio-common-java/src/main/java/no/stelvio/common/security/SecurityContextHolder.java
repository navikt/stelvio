package no.stelvio.common.security;

import java.util.ArrayList;

/**
* Helper class to manage a thread-bound instance of the <code>SecurityContext</code> class.
*
* @author persondab2f89862d3
* @see SecurityContext
*/
public class SecurityContextHolder {

	    /**
	     * Holds an instance of the <code>SecurityContext</code> class.
	     * TODO check if InheritableThreadLocal is safe to use; something about JVM incompatibilities
	     * (see Acegi's SecurityContextHolder)
	     */
		private static final ThreadLocal<SecurityContext> securityContextHolder = new InheritableThreadLocal<SecurityContext>();

	    /** Should not be instantiated. */
	    private SecurityContextHolder() {
	    }

	    /**
		 * Bind the given <code>SecurityContext</code> to the current thread.
	     *
		 * @param context the <code>SecurityContext</code> to expose.
		 */
		public static void setSecurityContext(SecurityContext context) {
			securityContextHolder.set(context);
		}

		/**
		 * Return the <code>SecurityContext</code> currently bound to the thread.
	     *
		 * @return the <code>SecurityContext</code> currently bound to the thread.
		 * @throws IllegalStateException if no <code>SecurityContext</code> is bound to the current thread.
		 */
		public static SecurityContext currentSecurityContext() throws IllegalStateException {
			SecurityContext context = securityContextHolder.get();

	        if (context == null) {
		        
	        	context = new SecurityContext("Temporary", new ArrayList<String>());
	        	/*throw new IllegalStateException("No thread-bound security context found: " +
				        "Make sure filters for binding the SecurityContext is setup properly for the web application " +
				        "in front and the proper request context interceptors are setup for the layers below.");*/
			}
	        return context;
		}

		/**
		 * Reset the <code>SecurityContext</code> for the current thread.
		 */
		public static void resetSecurityContext() {
			securityContextHolder.set(null);
		}
	

	
}
