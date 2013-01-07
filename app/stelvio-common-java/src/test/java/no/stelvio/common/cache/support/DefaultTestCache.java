package no.stelvio.common.cache.support;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;

import no.stelvio.common.cache.TestCache;

/**
 * Implements services for accessing data related to TestCache.
 * 
 * @author person4f9bc5bd17cc, Accenture
 */
public class DefaultTestCache implements TestCache {
	private static final String INITIAL_STRING = "This is a cacheable string";
	private String cachedString = INITIAL_STRING;
	private int cnt;

	/**
	 * {@inheritDoc}
	 */	
	@Cacheable(cacheName = "sampleCache2")
	public String getStringCached() {
		return cachedString;
	}

	/**
	 * {@inheritDoc}
	 */
	@TriggersRemove(cacheName = "sampleCache2", removeAll=true)
	public void updateStringAndFlush() {
		cnt++;
		cachedString = INITIAL_STRING + cnt;
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateStringNoFlush() {
		cnt++;
		cachedString = INITIAL_STRING + cnt;
	}
}