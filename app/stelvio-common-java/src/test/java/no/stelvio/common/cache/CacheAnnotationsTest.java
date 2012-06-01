package no.stelvio.common.cache;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sf.ehcache.CacheManager;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Simple test for caching using JDK 1.5 annotations.
 * Should test that management interface is in place with an http get or something
 * 
 * @author person4f9bc5bd17cc, Accenture 
 * @author person5dc3535ea7f4, Accenture
 */
@ContextConfiguration(locations = { "classpath:common-java_test_cache_beans_cache_annotations.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class CacheAnnotationsTest {
	private static final String INITIAL_STRING = "This is a cacheable string";

	@Autowired
	private TestCache testCache;
	
	@Autowired
	private CacheManager cacheManager;
	
	@After
	public void after(){
		cacheManager.clearAll();
	}
	
	/**
	 * Test-method that test caching using JDK 1.5 annotations.
	 */
	@Test
	@DirtiesContext
	public void shuldReturnCorrectStringsWhenChaching() {
		
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
