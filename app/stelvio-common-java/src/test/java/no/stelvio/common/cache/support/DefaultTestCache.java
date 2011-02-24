package no.stelvio.common.cache.support;

import no.stelvio.common.cache.TestCache;
import org.springmodules.cache.annotations.CacheFlush;
import org.springmodules.cache.annotations.Cacheable;

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
	@Cacheable(modelId = "non-persistent")
	public String getStringCached() {
		return cachedString;
	}

	/**
	 * {@inheritDoc}
	 */
	@CacheFlush(modelId = "flushingModel")
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