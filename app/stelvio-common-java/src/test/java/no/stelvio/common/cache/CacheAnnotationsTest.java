package no.stelvio.common.cache;

import org.apache.log4j.Logger;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Simple test for caching using JDK 1.5 annotations
 * 
 * @author person4f9bc5bd17cc, Accenture
 */
public class CacheAnnotationsTest extends AbstractDependencyInjectionSpringContextTests {
	private Logger log = Logger.getLogger(CacheAnnotationsTest.class);
	private final String initialString = "This is a cacheable string";
	
	/** Implementation class to test */
	private CacheTest cacheTest;
	
	/**
	 * @return the location of the spring configuration xml-file.
	 */
	@Override
	protected String[] getConfigLocations() {
		return new String[] {
				"common-java_test_cache_beans.xml"
		};
	}

	/**
	 * Initialize components prior to running tests.
	 */
	public void onSetUp() throws Exception {
		cacheTest = (CacheTest) applicationContext.getBean("cacheTest");
	}
	
	/**
	 * Test-method that test caching using JDK 1.5 annotations
	 */
	public void testAnnotations() {
		log.debug("DEBUG: Entering testAnnotations()...");
		
		// Retrieve String for the first time
		String cachedString = cacheTest.getStringCached();
		assertTrue("Retrieved string is not equal to inital String.", cachedString.equals(initialString));
		log.debug("DEBUG: String="+cachedString);
		
		// Retrieve cached String
		cachedString = cacheTest.getStringCached();
		log.debug("DEBUG: String="+cachedString);
		assertTrue("Cached string is not equal to inital String.", cachedString.equals(initialString));
		
		// Update data without flushing cache
		cacheTest.updateStringNoFlush();
		cachedString = cacheTest.getStringCached();
		log.debug("DEBUG: String="+cachedString);
		assertTrue("String not retrieved from cache.", cachedString.equals(initialString));
		
		// Update data and flush cache
		cacheTest.updateStringAndFlush();
		cachedString = cacheTest.getStringCached();
		log.debug("DEBUG: String="+cachedString);
		assertFalse("String retrieved from cache. Cache was not flushed.", cachedString.equals(initialString));
	}
}