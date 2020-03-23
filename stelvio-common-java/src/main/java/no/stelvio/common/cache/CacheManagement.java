/**
 * 
 */
package no.stelvio.common.cache;

import net.sf.ehcache.CacheManager;

/**
 * CacheManagement component for stelvio.
 * 
 * Interface that defines the management operations on the system cache services.
 * Implementations are standard POJO's that can be JMX-enabled using Spring configuration.
 * 
 * @version $id$
 */
public interface CacheManagement {
	/**
	 * Flushes all elements in all configured caches.
	 */
	void flushCache();
	
	/**
	 * @return the Ehcache CacheManager
	 */
	CacheManager getCacheManager();
}