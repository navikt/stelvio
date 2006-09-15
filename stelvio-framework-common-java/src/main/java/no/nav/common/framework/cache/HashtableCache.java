package no.nav.common.framework.cache;

import java.util.Hashtable;
import java.util.Map;

/**
 * A lightweight implementation of the <tt>Cache</tt> interface.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: HashtableCache.java 2581 2005-10-26 05:38:13Z skb2930 $
 */
public class HashtableCache implements Cache {

	private Map cache = new Hashtable();
	/** Constant for 60 seconds. */
	public static final int ONE_MINUTE = 60000;

	/**
	 * {@inheritDoc}
	 */
	public Object get(Object key) {
		return cache.get(key);
	}

	/** 
	 * {@inheritDoc}
	 */
	public void put(Object key, Object value) {
		cache.put(key, value);
	}

	/** 
	 * {@inheritDoc}
	 */
	public void remove(Object key) {
		cache.remove(key);
	}

	/** 
	 * {@inheritDoc}
	 */
	public void clear() {
		cache.clear();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void destroy() {
	}

	/** 
	 * {@inheritDoc}
	 */
	public void lock(Object key) {
		// local cache, so we use synchronization
	}

	/** 
	 * {@inheritDoc}
	 */
	public void unlock(Object key) {
		// local cache, so we use synchronization
	}

	/** 
	 * {@inheritDoc}
	 */
	public long nextTimestamp() {
		return Timestamper.next();
	}

	/** 
	 * {@inheritDoc}
	 */
	public int getTimeout() {
		return (int) Timestamper.ONE_MS * ONE_MINUTE;
	}

	/**
	 * Returns the size of the cache, that is, how many elements are cached.
	 *
	 * @return the size of the cache, that is, how many elements are cached.
	 */
	public int size() {
		return cache.size();
	}
}
