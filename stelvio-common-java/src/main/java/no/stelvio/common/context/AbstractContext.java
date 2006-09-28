package no.stelvio.common.context;

/**
 * This class is used to store and retrieve information that must
 * be available anywhere in the system during a single request.
 * <p/>
 * AbstractContext is a wrapper that tries to dynamically load a log4j class to
 * determine if log4j is available in the VM. If it is the case,
 * a log4j delegate is built and used. In the contrary, a default
 * delegate is used. This class cannot directly reference log4j
 * classes otherwise the JVM will try to load it and make it fail.
 * <p/>
 *
 * @author person7553f5959484
 * @version $Revision: 2829 $ $Author: skb2930 $ $Date: 2006-03-05 11:18:52 +0100 (Sun, 05 Mar 2006) $
 */
abstract class AbstractContext {

	protected static Class contextClass = null;

	// We don't directly reference the class so that this class can be loaded
	// without the JVM to try to load any log4j classes

	private static final String LOG4J_PLUGIN_CLASS_NAME = "no.stelvio.common.context.Log4jContextImpl";

	private static final String LOG4J_DETECTOR_CLASS_NAME = "org.apache.log4j.Logger";

	private static Context context = null;

	static {
		init();
	}

	/**
	 * Default Constructor is only visible for sub classes
	 */
	protected AbstractContext() {
	}

	/**
	 * Initializes the AbstractContext by detecting
	 * if Log4j is present in the classpath, and loads
	 * the Log4jContextImpl if it is and the
	 * DefaultContextImpl if it isn't. 
	 */
	private static void init() {

		// Determine the context to use
		contextClass = DefaultContextImpl.class;
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();

			// try to load the class...
			cl.loadClass(LOG4J_DETECTOR_CLASS_NAME);

			// if we arrive here, it means that we can use log4j in this VM (or CL scope)
			contextClass = cl.loadClass(LOG4J_PLUGIN_CLASS_NAME);
		} catch (ClassNotFoundException cnfe) {
			System.err.println("***** log4j is not present on the classpath *****");
		}

		// Intantiate the context
		try {
			context = (Context) contextClass.newInstance();
		} catch (Exception ie) {
			ie.printStackTrace(System.err);
			context = new DefaultContextImpl();
		}
	}

	/** 
	 * Put a context value as identified with the key parameter 
	 * into the current thread's context.
	 * 
	 * @param key key to identify the value
	 * @param value object to store
	 * 
	 * @see no.stelvio.common.context.Context#put(java.lang.String, java.lang.Object)
	 */
	protected static final void put(String key, Object value) {
		context.put(key, value);
	}

	/**
	 * Get the context value identified by the key parameter 
	 * from the current thread's context.
	 * 
	 * @param key key to identify the value
	 * @return the value stored
	 * 
	 * @see no.stelvio.common.context.Context#get(java.lang.String)
	 */
	protected static final Object get(String key) {
		return context.get(key);
	}

	/**
	 * Remove all of the context values.
	 * 
	 * @see no.stelvio.common.context.Context#remove()
	 */
	public static final void remove() {
		context.remove();
	}

	/**
	 * Export the context
	 * 
	 * @return the exported context
	 * 
	 * @see no.stelvio.common.context.Context#exportContext()
	 */
	public static final Object exportContext() {
		return context.exportContext();
	}

	/**
	 * Import a new context
	 * 
	 * @param o the context to import
	 * @see no.stelvio.common.context.Context#importContext(java.lang.Object)
	 */
	public static final void importContext(Object o) {
		context.importContext(o);
	}
}