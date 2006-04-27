package no.trygdeetaten.common.framework.context;

/**
 * Defines a "pluggable" Context module. 
 * <p/>
 * This is used to split between Log4jContextImpl and DefaultContext.
 * <p/>
 * Choice is made in no.trygdeetaten.common.framework.context.AbstractContext
 *
 * @author person7553f5959484
 * @version $Revision: 1975 $ $Date: 2005-02-16 16:57:06 +0100 (Wed, 16 Feb 2005) $
 */
public interface Context {

	/**
	 * Put a context value as identified with the key parameter 
	 * into the current thread's context.
	 * 
	 * @param key key to identify the value
	 * @param value object to store
	 */
	void put(String key, Object value);
	
	/**
	 * Get the context value identified by the key parameter 
	 * from the current thread's context.
	 * 
	 * @param key key to identify the value
	 * @return the value stored
	 */
	Object get(String key);
	
	/**
	 * Remove all of the context values.
	 */
	void remove();
	
	/**
	 * Import a new context
	 * 
	 * @param o the context to import
	 */
	void importContext(Object o);
	
	/**
	 * Export the context
	 * 
	 * @return the exported context
	 */
	Object exportContext();
}