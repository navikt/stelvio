package no.stelvio.common.cache;

import net.sf.ehcache.CacheManager;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Simple test for caching using JDK 1.5 annotations.
 * Should test that management interface is in place with an http get or something
 * 
 * @author person4f9bc5bd17cc, Accenture 
 */
public class CacheAnnotationsTest extends AbstractDependencyInjectionSpringContextTests {
	private static final String INITIAL_STRING = "This is a cacheable string";

	/** Implementation class to test. */
	private TestCache testCache;

	/**
	 * @return the location of the spring configuration xml-file.
	 */
	@Override
	protected String[] getConfigLocations() {
		return new String[] { "common-java_test_cache_beans_cache_annotations.xml" };
	}

	/**
	 * Initialize components prior to running tests.
	 * 
	 * @throws Exception exception
	 */
	public void onSetUp() throws Exception {
		testCache = (TestCache) applicationContext.getBean("testCache");
	}

	/**
	 * Test-method that test caching using JDK 1.5 annotations.
	 */
	public void testAnnotations() {
		// Retrieve String for the first time
		String cachedString = testCache.getStringCached();
		assertTrue("Retrieved string is not equal to inital String.", cachedString.equals(INITIAL_STRING));

		// Retrieve cached String
		cachedString = testCache.getStringCached();
		assertTrue("Cached string is not equal to inital String.", cachedString.equals(INITIAL_STRING));

		// Update data without flushing cache
		testCache.updateStringNoFlush();
		cachedString = testCache.getStringCached();
		assertTrue("String not retrieved from cache.", cachedString.equals(INITIAL_STRING));

		// Update data and flush cache
		testCache.updateStringAndFlush();
		cachedString = testCache.getStringCached();
		assertFalse("String retrieved from cache. Cache was not flushed.", cachedString.equals(INITIAL_STRING));
	}
}
