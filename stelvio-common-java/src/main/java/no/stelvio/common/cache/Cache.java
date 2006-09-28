package no.stelvio.common.cache;

/**
 * Implementors define a caching algorithm. All implementors
 * <b>must</b> be threadsafe.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: Cache.java 2565 2005-10-19 15:19:43Z psa2920 $
 */
public interface Cache {
	
	/**
	 * Get an item from the cache
	 * 
	 * @param key the key of the item to get.
	 * @return the cached object or <tt>null</tt>.
	 */
	Object get(Object key);

	/**
	 * Add an item to the cache.
	 * 
	 * @param key the key of the item to add.
	 * @param value the item to add.
	 */
	void put(Object key, Object value);
	
	/**
	 * Remove an item from the cache
	 * 
	 *@param key the key of the item to remove.
	 */
	void remove(Object key);
	
	/**
	 * Clear the cache.
	 */
	void clear();
	
	/**
	 * Clean up.
	 */
	void destroy();
	
	/**
	 * If this is a clustered cache, lock the item.
	 * 
	 * @param key the item to lock.
	 */
	void lock(Object key);
	
	/**
	 * If this is a clustered cache, unlock the item.
	 * 
	 * @param key the item to unlock.
	 */
	void unlock(Object key);
	
	/**
	 * Generate a timestamp.
	 * 
	 * @return the timestamp.
	 */
	long nextTimestamp();
	
	/**
	 * Get a reasonable "lock timeout".
	 * 
	 * @return the "lock timeout".
	 */
	int getTimeout();
}
