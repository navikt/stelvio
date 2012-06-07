package no.stelvio.common.cache;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import no.stelvio.common.cache.support.CacheStoreCounterCachingListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The purpose of this test is to verify that it is possible to configure
 * caching using the AOP-schema instead of Spring autoproxy. As the AOP-schema
 * is the way AOP is configured in Stelvio no configuration should use autoproxy
 * <p>
 * From the Spring reference documentation:<br/>
 * <em>
 * The <aop:config> style of configuration makes heavy use of Spring's auto-proxying mechanism. 
 * This can cause issues (such as advice not being woven) if you are already using explicit auto-proxying 
 * via the use of BeanNameAutoProxyCreator or suchlike. 
 * The recommended usage pattern is to use either just the <aop:config>  style, 
 * or just the AutoProxyCreator style. 
 * (http://maven.adeo.no/apidoc/spring-api/reference/aop.html#aop-schema)
 * </em>
 * <p>
 * 
 * @author person983601e0e117 (Accenture)
 * @author person5dc3535ea7f4 (Accenture)
 */
@ContextConfiguration(locations = { "classpath:test-cachewithoutautoproxy-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class CacheAnnotationsWithoutAutoProxyTest {

	@Resource(name = "testCache")
	private TestCache testCache;
	
	private ApplicationContext ctx;

	@Autowired
	public CacheManager cacheManager;
	
	@Autowired
	private CacheStoreCounterCachingListener counter;

	private Cache cacheInstance;

	@Before
	public void setUp() {
		cacheInstance = cacheManager.getCache("sampleCache2");
		cacheInstance.registerCacheUsageListener(counter);
	}
	
	@After
	public void tearDown(){
		cacheInstance.removeAll();
	}
	/**
	 * Test when cache is used.
	 */
	@Test
	public void cacheIsUsed() {

		String cachedString = testCache.getStringCached();
		assertEquals("This is a cacheable string", cachedString);
		assertEquals("Data was cached an unexpected number of times", 1, counter.getOnCachingCounter());

		testCache.updateStringAndFlush();
		cachedString = testCache.getStringCached();
		assertEquals("This is a cacheable string1", cachedString);
		assertEquals("Data was cached an unexpected number of times", 2, counter.getOnCachingCounter());

		testCache.updateStringNoFlush();
		cachedString = testCache.getStringCached();
		assertEquals("This is a cacheable string1", cachedString);
		assertEquals("Data was cached an unexpected number of times", 2, counter.getOnCachingCounter());
	}

	/**
	 * Tests that cache is not used when annotations are not applied to a class
	 * that matches the pointcut for which cache-interceptors are configured.
	 */
	@Test
	@Ignore
	public void cacheIsNotUsedWhenAnnotationsAreNotApplied() {
		testCache = (TestCache) ctx.getBean("testNoCache");
		CacheStoreCounterCachingListener counter = (CacheStoreCounterCachingListener) ctx.getBean("cacheStoreCounter");
		String cachedString = testCache.getStringCached();
		assertEquals("This is a cacheable string", cachedString);
		assertEquals("Cache should not have been used", 0, counter.getOnCachingCounter());

		// testCache.updateStringAndFlush();
		cachedString = testCache.getStringCached();
		assertEquals("This is a cacheable string1", cachedString);
		assertEquals("Cache should not have been used", 0, counter.getOnCachingCounter());
		testCache.updateStringNoFlush();
		cachedString = testCache.getStringCached();
		assertEquals("This is a cacheable string2", cachedString);
		assertEquals("Cache should not have been used", 0, counter.getOnCachingCounter());
	}

}
