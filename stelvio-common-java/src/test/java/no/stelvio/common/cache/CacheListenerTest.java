package no.stelvio.common.cache;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.RegisteredEventListeners;

import no.stelvio.common.cache.support.DefaultCacheEventListener;

/**
 * The purpose of this test is to verify the configuration of cache interceptors.
 * The test class also uses this interceptor to verify the general cache configuration
 * 
 * @author person983601e0e117 (Accenture)
 * @author person5dc3535ea7f4 (Accenture)
 */
@ContextConfiguration(locations = { "classpath:test-cachelistener-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class CacheListenerTest {

	@Resource(name = "testCache")
	private TestCache testCache;

	@Autowired
	public CacheManager cacheManager;

	private DefaultCacheEventListener counterListener;

	private Cache cacheInstance;

	@Before
	public void setUp() {
		cacheInstance = cacheManager.getCache("sampleCache2");
		counterListener = getCacheEventListener(cacheInstance);
	}

	@After
	public void tearDown(){
		cacheInstance.removeAll();
	}
	/**
	 * Tests that caching is working correctly by altering the return value of the method,
	 * and verify that method still returns the original (cached) value.
	 * Also using a counter interceptor to verify that cache is stored (with notifyCacheElementPut()) 
	 */
	@Test
	@DirtiesContext
	public void cacheIsUsed() {
		String cachedString = testCache.getStringCached();
		assertEquals("This is a cacheable string", cachedString);
		assertEquals("Data was cached an unexpected number of times", 1, counterListener.getOnCachingCounter());

		testCache.updateStringAndFlush();
		cachedString = testCache.getStringCached();
		assertEquals("This is a cacheable string1", cachedString);
		assertEquals("Data was cached an unexpected number of times", 2, counterListener.getOnCachingCounter());

		testCache.updateStringNoFlush();
		cachedString = testCache.getStringCached();
		assertEquals("This is a cacheable string1", cachedString);
		assertEquals("Data was cached an unexpected number of times", 2, counterListener.getOnCachingCounter());
	}

	private DefaultCacheEventListener getCacheEventListener(Cache cacheInstance){
		 RegisteredEventListeners cacheEventNotificationService = cacheInstance.getCacheEventNotificationService();
		 Set<CacheEventListener> cacheEventListeners = cacheEventNotificationService.getCacheEventListeners();

		 for (CacheEventListener cacheEventListener : cacheEventListeners) {
				if (cacheEventListener instanceof DefaultCacheEventListener) {
					return (DefaultCacheEventListener) cacheEventListener;
				}
			}
		return null;
	}

}
