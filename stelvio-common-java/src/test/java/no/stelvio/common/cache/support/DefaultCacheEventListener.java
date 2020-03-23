package no.stelvio.common.cache.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

/**
 * 
 */
public class DefaultCacheEventListener implements CacheEventListener{

	private static final Log LOG = LogFactory.getLog(DefaultCacheEventListener.class);

	private int onCachingCounter;
	
	/**
	 * Get onChachingCounter.
	 * 
	 * @return onChachingCounter
	 */
	public int getOnCachingCounter() {
		return onCachingCounter;
	}

	/**
	 * Reset onChachingCounter.
	 */
	public void resetOnCachingCounter() {
		this.onCachingCounter = 0;
	}

	public void notifyElementPut(Ehcache arg0, Element arg1) throws CacheException {
		onCachingCounter++;	
		LOG.debug("Cache hit, incrementing onChacingCounter");		
	}
	
	public Object clone(){
		throw new UnsupportedOperationException();
	}
	
	public void dispose(){}

	public void notifyElementEvicted(Ehcache arg0, Element arg1) {
		// No action required
	}

	public void notifyElementExpired(Ehcache arg0, Element arg1) {
		// No action required
	}

	public void notifyElementRemoved(Ehcache arg0, Element arg1) throws CacheException {
		// No action required
	}

	public void notifyElementUpdated(Ehcache arg0, Element arg1) throws CacheException {
		// No action required
	}

	public void notifyRemoveAll(Ehcache arg0) {
		// No action required
	}
	
}
