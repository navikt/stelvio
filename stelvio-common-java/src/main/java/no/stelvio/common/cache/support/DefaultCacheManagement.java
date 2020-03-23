package no.stelvio.common.cache.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.CacheManager;

import no.stelvio.common.cache.CacheManagement;

/**
 * CacheManagement component for stelvio.
 * 
 * This class makes cache-administration accessible as a
 * JMX-component using Spring configuration.
 * 
 * @version $id$
 */
public class DefaultCacheManagement implements CacheManagement {
	private Logger log = LoggerFactory.getLogger(DefaultCacheManagement.class);
	private CacheManager cacheManager;

	@Override
	public void flushCache() {
		if (log.isDebugEnabled()) {
			log.debug("Flushing all elements in " + getCacheManager().getCacheNames().length + " configured cache(s).");
		}
		
		cacheManager.clearAll();
	}

	@Override
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