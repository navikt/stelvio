package no.stelvio.common.cache;

import static org.junit.Assert.assertEquals;

import no.stelvio.common.cache.support.CacheStoreCounterCachingListener;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The purpose of this test is to verify that it is possible to configure caching using the AOP-schema instead of Spring
 * autoproxy. As the AOP-schema is the way AOP is configured in Stelvio no configuration should use autoproxy
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
 * 
 */
public class CacheAnnotationsWithoutAutoProxyTest {

	private TestCache testCache;
	private ApplicationContext ctx;

	/**
	 * Set up a context.
	 */
	@Before
	public void setUp() {

		ctx = new ClassPathXmlApplicationContext("test-cachewithoutautoproxy-context.xml");
	}

	/**
	 * Test when cache is used.
	 */
	@Test
	public void cacheIsUsed() {
		testCache = (TestCache) ctx.getBean("testCache");
		CacheStoreCounterCachingListener counter = (CacheStoreCounterCachingListener) ctx.getBean("cacheStoreCounter");
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
	 * Tests that cache is not used when annotations are not applied to a class that matches the pointcut for which
	 * cache-interceptors are configured.
	 * 
	 */
	@Test
	public void cacheIsNotUsedWhenAnnotationsAreNotApplied() {
		testCache = (TestCache) ctx.getBean("testNoCache");
		CacheStoreCounterCachingListener counter = (CacheStoreCounterCachingListener) ctx.getBean("cacheStoreCounter");
		String cachedString = testCache.getStringCached();
		assertEquals("This is a cacheable string", cachedString);
		assertEquals("Cache should not have been used", 0, counter.getOnCachingCounter());
		testCache.updateStringAndFlush();
		cachedString = testCache.getStringCached();
		assertEquals("This is a cacheable string1", cachedString);
		assertEquals("Cache should not have been used", 0, counter.getOnCachingCounter());
		testCache.updateStringNoFlush();
		cachedString = testCache.getStringCached();
		assertEquals("This is a cacheable string2", cachedString);
		assertEquals("Cache should not have been used", 0, counter.getOnCachingCounter());
	}

}
