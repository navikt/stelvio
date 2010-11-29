package no.stelvio.common.cache;

/**
 * Defines an interface for accessing data related to TestCache.
 * 
 * @author person4f9bc5bd17cc, Accenture
 */
public interface TestCache {
	/**
	 * @return the cached String.
	 */
	String getStringCached();
	
	/**
	 * Update the cached string and flush cache.
	 */
	void updateStringAndFlush();
	
	/**
	 * Update the cached string without flushing the cache.
	 */
	void updateStringNoFlush();
}
