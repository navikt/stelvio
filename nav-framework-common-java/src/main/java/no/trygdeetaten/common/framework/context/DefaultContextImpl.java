package no.trygdeetaten.common.framework.context;

import java.util.Hashtable;

/**
 * Context implementation that uses the InheritableThreadLocal class
 * for storing key-value pairs.
 * <p/>
 *
 * @author person7553f5959484
 * @version $Revision: 2001 $ $Date: 2005-03-01 14:49:06 +0100 (Tue, 01 Mar 2005) $
 */
public class DefaultContextImpl implements Context {

	// The capasity of the user context
	// --> Update this calue when you add/remove a field
	protected static final int CAPACITY = 5;

	/**
	 * The heap is a threadlocal. We are interested in reducing the
	 * number of threadlocals used, because of the implementation of
	 * threadlocal in Sun's JVM, which uses a map with a capacity
	 * of four. Adding more threadlocals implies resizing the
	 * capacity.
	 */
	private final ThreadLocalMap heap = new ThreadLocalMap();

	/**
	 * Convenient method to ensure that we always work on an existing
	 * heap and not null
	 * 
	 * @return the heap represented as a Hashtable
	 */
	private Hashtable getHeap() {
		Hashtable hashtable = (Hashtable) heap.get();
		if (hashtable == null) {
			hashtable = new Hashtable(CAPACITY);
			heap.set(hashtable);
		}
		return hashtable;
	}

	/**
	 * Put a context value as identified with the key parameter 
	 * into the current thread's context.
	 * 
	 * @param key key to identify the value
	 * @param value object to store
	 * 
	 * @see no.trygdeetaten.common.framework.context.Context#put(java.lang.String, java.lang.Object)
	 */
	public void put(String key, Object value) {
		if (null != key && null != value) {
			getHeap().put(key, value);
		}
	}

	/**
	 * Get the context value identified by the key parameter 
	 * from the current thread's context.
	 * 
	 * @param key key to identify the value
	 * @return the value stored
	 * 
	 * @see no.trygdeetaten.common.framework.context.Context#get(java.lang.String)
	 */
	public Object get(String key) {
		if (null == key) {
			return null;
		} else {
			return getHeap().get(key);
		}
	}

	/**
	 * Export the context
	 * 
	 * @return the exported context
	 * 
	 * @see no.trygdeetaten.common.framework.context.Context#exportContext()
	 */
	public Object exportContext() {
		return getHeap();
	}

	/**
	 * Remove all of the context values.
	 * 
	 * @see no.trygdeetaten.common.framework.context.Context#remove()
	 */
	public void remove() {
		heap.set(new Hashtable(CAPACITY));
	}

	/**
	 * Import a new context
	 * 
	 * @param o the context to import
	 * 
	 * @see no.trygdeetaten.common.framework.context.Context#importContext(java.lang.Object)
	 */
	public void importContext(Object o) {
		if (null == o) {
			// Do nothing
			return;
		} else if (o instanceof Hashtable) {
			// Casted external format to Hashtable,
			// Now, clear the existing context and
			// put all objects in the external hashtable 
			// into the existing context
			heap.set((Hashtable) o);
		} else {
			throw new ClassCastException(
				"Can not import object of type "
					+ o.getClass().getName()
					+ " into Context. Object must be of type "
					+ "java.util.Hashtable");
		}
	}
}