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
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public interface CacheManagement {
	/**
	 * Flushes all elements in all configured caches.
	 */
	public void flushCache();
	
	/**
	 * @return the Ehcache CacheManager
	 */
	public CacheManager getCacheManager();
}