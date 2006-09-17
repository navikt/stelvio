package no.stelvio.common.framework.cache;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;

import junit.framework.TestCase;

import no.stelvio.common.framework.cache.EHCache;
import no.stelvio.common.framework.cache.Timestamper;
import no.stelvio.common.framework.jmx.JMXUtils;

/**
 * EHCache Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: EHCacheTest.java 2155 2005-03-31 14:46:49Z psa2920 $
 */
public class EHCacheTest extends TestCase {

	private static final String CACHENAME = "TestCache";
	private static final String CACHENAME2 = "TestCache2";
	private static final String FILENAME = "test-ehcache.xml";

	private static final String A = "A";
	private static final String B = "B";
	private static final String C = "C";
	private static final String D = "D";

	private EHCache cache = null;

	/**
	 * Constructor for EHCacheTest.
	 * @param arg0
	 */
	public EHCacheTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();

		if (null != cache) {
			cache.clear();
			cache = null;
		}

	}

	public void testEHCache() throws MalformedObjectNameException {

		assertEquals("MBeanServers available before construction", 0, MBeanServerFactory.findMBeanServer(null).size());
		cache = new EHCache(FILENAME, CACHENAME);
		assertEquals("MBeanServers not available after construction", 1, MBeanServerFactory.findMBeanServer(null).size());

		MBeanServer server = JMXUtils.getMBeanServer(null);

		assertEquals("Cache not registered in MBeanServer", 1, JMXUtils.getMBeans(server, "Cache:name=TestCache,*").length);

	}

	public void testGetPutRemoveClear() {

		cache = new EHCache(FILENAME, CACHENAME);

		assertNull("A should not be in cache yet", cache.get(A));

		cache.put(A, A);
		assertNotNull("put/get: A should be in cache now", cache.get(A));

		cache.remove(A);
		assertNull("remove: A should no longer be in cache", cache.get(A));

		cache.put(A, A);
		cache.put(B, B);
		cache.put(C, C);
		cache.put(D, D);
		assertNull("put: A should no longer be in cache", cache.get(A));

		cache.clear();

		assertNull("clear: B should no longer be in cache", cache.get(B));
		assertNull("clear: C should no longer be in cache", cache.get(C));
		assertNull("clear: D should no longer be in cache", cache.get(D));

		//		cache.put(A, A);
		//		cache.destroy();
		//
		//		try {
		//			cache.put(B, B);
		//			fail("Status of EHCache should have been disposed");
		//		} catch (Throwable t) {
		//			assertTrue("EHCache should have thrown IllegalStateException", t instanceof IllegalStateException);
		//		}

	}

	public void testLockAndUnlock() {
		cache = new EHCache(FILENAME, CACHENAME);
		cache.lock(new Object());
		cache.unlock(new Object());
		assertTrue("There is really nothing to test here", true);
	}

	public void testGetTimeoutAndNextTimestamp() {

		cache = new EHCache(FILENAME, CACHENAME);
		assertEquals("Timeout is not 60 seconds", 60 * 1000 * Timestamper.ONE_MS, cache.getTimeout());
		final long t1 = cache.nextTimestamp();
		final long t2 = cache.nextTimestamp();
		assertTrue("Next timestamp has not increased", t1 < t2);

	}

	public void testGetName() {
		cache = new EHCache(FILENAME, CACHENAME);
		assertEquals("Cache name is wrong", "TestCache", cache.getName());
	}

	public void testGetMaxElements() {
		cache = new EHCache(FILENAME, CACHENAME);
		assertEquals("Max elements is wrong", 3, cache.getMaxElements());
	}

	public void testGetNumElements() {
		cache = new EHCache(FILENAME, CACHENAME);
		assertEquals("Number of elements is wrong", 0, cache.getNumElements());

		cache.put(A, A);
		assertEquals("Number of elements is wrong", 1, cache.getNumElements());

		cache.put(B, B);
		assertEquals("Number of elements is wrong", 2, cache.getNumElements());

		cache.put(C, C);
		assertEquals("Number of elements is wrong", 3, cache.getNumElements());

		cache.put(D, D);
		assertEquals("Number of elements is wrong", 3, cache.getNumElements());
	}

	public void testGetMemorySize() {

		cache = new EHCache(FILENAME, CACHENAME);
		long s0 = cache.getMemorySize();
		assertEquals("Memory size is wrong", 0, s0);

		cache.put(A, A);
		long s1 = cache.getMemorySize();
		assertTrue("Memory size is wrong", s0 < s1);

		cache.put(B, B);
		long s2 = cache.getMemorySize();
		assertEquals("Memory size is wrong", s1 * 2, s2);

		cache.put(C, C);
		long s3 = cache.getMemorySize();
		assertEquals("Memory size is wrong", s1 * 3, s3);

		cache.put(D, D);
		long s4 = cache.getMemorySize();
		assertEquals("Memory size is wrong", s1 * 3, s4);

	}

	public void testGetHitCountMissCountExpiredAndMissCountNotFound() {

		cache = new EHCache(FILENAME, CACHENAME2);

		assertNull("A should not be in cache", cache.get(A));
		assertNull("B should not be in cache", cache.get(B));
		assertNull("C should not be in cache", cache.get(C));
		assertNull("D should not be in cache", cache.get(D));

		assertEquals("HitCount is wrong", 0, cache.getHitCount());
		assertEquals("Miss count expired is wrong", 0, cache.getMissCountExpired());
		assertEquals("Miss count not found is wrong", 4, cache.getMissCountNotFound());

		cache.put(A, A);

		assertNotNull("A should be in cache", cache.get(A));
		assertNull("B should not be in cache", cache.get(B));

		assertEquals("HitCount is wrong", 1, cache.getHitCount());
		assertEquals("Miss count expired is wrong", 0, cache.getMissCountExpired());
		assertEquals("Miss count not found is wrong", 5, cache.getMissCountNotFound());

		assertNotNull("A should still be in cache", cache.get(A));
		assertEquals("HitCount is wrong", 2, cache.getHitCount());

		sleep(750); // -- sleep for 3/4 second

		assertNotNull("A should still be in cache", cache.get(A));
		// Less than one second idle time and less than two seconds total time now
		assertEquals("HitCount is wrong", 3, cache.getHitCount());

		sleep(750); // -- sleep again for 3/4 second, more than 1,5 secs since we added A

		assertNotNull("A should still be in cache", cache.get(A));
		// Still less than one second idle time and two seconds total time
		assertEquals("HitCount is wrong", 4, cache.getHitCount());

		sleep(750); // -- sleep again for 3/4 second, more than 2,25 secs since we added A
		assertNull("A should not be in cache anymore", cache.get(A));
		// Still less than one second idle time but two seconds total time has expired
		assertEquals("HitCount is wrong", 4, cache.getHitCount());
		assertEquals("Miss count expired is wrong", 1, cache.getMissCountExpired());
		assertEquals("Miss count not found is wrong", 5, cache.getMissCountNotFound());

		cache.put(B, B);
		assertNotNull("B should be in cache", cache.get(B));
		assertEquals("HitCount is wrong", 5, cache.getHitCount());

		sleep(1500); // -- for 1,5 seconds to check idle time

		assertNull("B should not be in cache anymore", cache.get(B));

		assertEquals("HitCount is wrong", 5, cache.getHitCount());
		assertEquals("Miss count expired is wrong", 2, cache.getMissCountExpired());
		assertEquals("Miss count not found is wrong", 5, cache.getMissCountNotFound());
	}

	public void testGetTimeToIdleSeconds() {
		cache = new EHCache(FILENAME, CACHENAME);
		assertEquals("Time to idle seconds is wrong", 30, cache.getTimeToIdleSeconds());
	}

	public void testGetTimeToLiveSeconds() {
		cache = new EHCache(FILENAME, CACHENAME);
		assertEquals("Time to live seconds is wrong", 60, cache.getTimeToLiveSeconds());
	}

	public void testIsEternal() {
		cache = new EHCache(FILENAME, CACHENAME);
		assertFalse("Cache should not be eternal", cache.isEternal());
	}

	public void testReset() {

		cache = new EHCache(FILENAME, CACHENAME);

		assertNull("A should not be in cache yet", cache.get(A));
		assertNull("B should not be in cache yet", cache.get(B));
		assertNull("C should not be in cache yet", cache.get(C));

		cache.put(A, A);
		cache.put(B, B);
		cache.put(C, C);

		assertNotNull("A should now be in cache", cache.get(A));
		assertNotNull("B should now be in cache", cache.get(B));
		assertNotNull("C should now be in cache", cache.get(C));

		cache.clear();

		assertNull("A should no longer be in cache", cache.get(A));
		assertNull("B should no longer be in cache", cache.get(B));
		assertNull("C should no longer be in cache", cache.get(C));
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
}
