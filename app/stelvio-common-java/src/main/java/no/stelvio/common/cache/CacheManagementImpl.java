/**
 * 
 */
package no.stelvio.common.cache;

import net.sf.ehcache.CacheManager;

import org.apache.log4j.Logger;

/**
 * CacheManagement component for stelvio.
 * 
 * This class makes cache-administration accessible as a
 * JMX-component using Spring configuration.
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class CacheManagementImpl implements CacheManagement {
	private Logger log = Logger.getLogger(CacheManagementImpl.class);
	private CacheManager cacheManager;
	
	/**
	 * {@inheritDoc CacheManagement#clearAll()}
	 */
	public void flushCache() {
		if (log.isDebugEnabled()) {
			log.debug("Flushing all elements in "+getCacheManager().getCacheNames().length+" configured cache(s).");
		}
		
		cacheManager.clearAll();
	}

	/**
	 * {@inheritDoc CacheManagement#getCacheManager()}
	 */
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	/**
	 * 
	 * @param cacheManager to set.
	 */
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
}