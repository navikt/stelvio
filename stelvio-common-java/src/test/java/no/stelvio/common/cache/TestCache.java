package no.stelvio.common.cache;

/**
 * Defines an interface for accessing data related to TestCache.
 * 
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
