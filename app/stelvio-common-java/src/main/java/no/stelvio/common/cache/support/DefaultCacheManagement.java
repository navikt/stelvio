/**
 * 
 */
package no.stelvio.common.cache.support;

import net.sf.ehcache.CacheManager;
import no.stelvio.common.cache.CacheManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CacheManagement component for stelvio.
 * 
 * This class makes cache-administration accessible as a
 * JMX-component using Spring configuration.
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class DefaultCacheManagement implements CacheManagement {
	private Logger log = LoggerFactory.getLogger(DefaultCacheManagement.class);
	private CacheManager cacheManager;
	
	/**
	 * {@inheritDoc CacheManagement#clearAll()}.
	 */
	public void flushCache() {
		if (log.isDebugEnabled()) {
			log.debug("Flushing all elements in " + getCacheManager().getCacheNames().length + " configured cache(s).");
		}
		
		cacheManager.clearAll();
	}

	/**
	 * {@inheritDoc}
	 */
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	/**
	 * Set cache manager.
	 * 
	 * @param cacheManager to set.
	 */
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
}