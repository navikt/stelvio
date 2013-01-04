package no.stelvio.common.cache.support;

import no.stelvio.common.cache.CacheAnnotationsWithoutAutoProxyTest;
import no.stelvio.common.cache.TestCache;

/**
 * Class that implements the same interface as {@link DefaultTestCache} but is not declared as a chacheable resource using
 * caching annotations.
 * 
 * Used to assert that even though the class matches a pointcut for which caching interceptors are applied, this class will not
 * be subject to caching.
 * 
 * @see CacheAnnotationsWithoutAutoProxyTest#cacheIsNotUsedWhenAnnotationsAreNotApplied()
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public class NoAnnotationsTestCache implements TestCache {
	private static final String INITIAL_STRING = "This is a cacheable string";
	private String cachedString = INITIAL_STRING;
	private int cnt;

	/**
	 * {@inheritDoc}
	 */
	public String getStringCached() {
		return cachedString;
	}

	/**
	 * {@inheritDoc}
	 */
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
