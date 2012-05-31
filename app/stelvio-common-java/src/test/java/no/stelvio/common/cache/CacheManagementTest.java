package no.stelvio.common.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Simple test for CacheManagement.
 * 
 * @author person4f9bc5bd17cc, Accenture
 */
public class CacheManagementTest extends AbstractDependencyInjectionSpringContextTests {
	private Logger log = Logger.getLogger(CacheManagementTest.class);

	private final String initialString = "This is a cacheable string";
	private final String cacheName = "CacheManagementTestCache";
	private String cacheableString = initialString;
	private final String cacheableStringKey = "cacheableString";

	/** Implementation class to test. */
	private CacheManagement cacheManagement;
	
	/**
	 * Get configuration locations.
	 * 
	 * @return the location of the spring configuration xml-file.
	 */
	@Override
	protected String[] getConfigLocations() {
		return new String[] { "common-java_test_cache_beans.xml" };
	}

	/**
	 * Initialize components prior to running tests.
	 * 
	 * @throws Exception
	 *             exception
	 */
	public void onSetUp() throws Exception {
		cacheManagement = (CacheManagement) applicationContext.getBean("cacheManagement");
		assertNotNull("Couldn't initiate CacheManagement using standard POJO implementation.", cacheManagement);

		cacheManagement = (CacheManagement) applicationContext.getBean("cacheManagementProxy");
		assertNotNull("Couldn't initiate CacheManagement using JMX implementation.", cacheManagement);
	}

	/**
	 * Test-method that test functionality in the <code>CacheManagment</code>-class.
	 * 
	 * @throws BeansException
	 *             beans exception
	 * @throws Exception
	 *             exception
	 */
	public void testCacheManagement() throws BeansException, Exception {
		log.debug("DEBUG: Entering testCacheManagement()...");

		// Add cache to CacheManager
		log.debug("DEBUG: Adding new Cache to CacheManager...");
		Cache cache = new Cache(cacheName, 1000, true, true, 100, 150);
		cacheManagement.getCacheManager().addCache(cache);

		log.debug("" + cacheManagement.getCacheManager().getCacheNames().length);

		// Check if cache exists
		log.debug("DEBUG: Checking if Cache exists...");
		assertTrue("Specified cache (" + cacheName + ") doesn't exist.", cacheManagement.getCacheManager().cacheExists(
				cacheName));

		// Check number of caches
		log.debug("DEBUG: Checking number of Caches...");
		assertTrue("There are no caches registered in the cachemanager.",
				cacheManagement.getCacheManager().getCacheNames().length >= 3);

		// Add element to cache
		log.debug("DEBUG: Adding new Element to Cache...");
		Element cachedElement = new Element(cacheableStringKey, cacheableString);
		cacheManagement.getCacheManager().getCache(cacheName).put(cachedElement);

		// Get cached element
		log.debug("DEBUG: Getting cached Element...");
		Element retrievedElement = cacheManagement.getCacheManager().getCache(cacheName).get(cacheableStringKey);
		assertNotNull("There are no cached elements.", retrievedElement);

		// Get element value
		log.debug("DEBUG: Getting Element value...");
		String cachedValue = (String) cachedElement.getObjectValue();
		assertNotNull("Cached element value is not initialized.", cachedValue);

		// Clear all cache elements
		log.debug("DEBUG: Clearing all Cache Elements...");
		cacheManagement.flushCache();
		Element retrievedNullElement = cacheManagement.getCacheManager().getCache(cacheName).get(cacheableStringKey);
		assertNull("Element was not cleared from cache.", retrievedNullElement);

		// Remove all Caches
		log.debug("DEBUG: Removing all Caches...");
		cacheManagement.getCacheManager().removalAll();
		Cache removedCache = cacheManagement.getCacheManager().getCache(cacheName);
		assertNull("Cache was not removed.", removedCache);
	}

	/**
	 * Cleans up after tests are complete.
	 */
	@Override
	public void onTearDown() {
		cacheManagement.getCacheManager().shutdown();
	}
}