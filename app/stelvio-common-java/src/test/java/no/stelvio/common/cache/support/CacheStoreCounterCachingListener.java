package no.stelvio.common.cache.support;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.cache.CachingModel;
import org.springmodules.cache.interceptor.caching.CachingListener;

/**
 * A CacheStoreCounterCachingListener.
 * 
 * @author MA
 */
public class CacheStoreCounterCachingListener implements CachingListener {

	private static final Log LOG = LogFactory.getLog(CacheStoreCounterCachingListener.class);

	private int onCachingCounter;

	/**
	 * {@inheritDoc}
	 */
	public void onCaching(Serializable key, Object obj, CachingModel model) {
		LOG.debug("Cache hit, incrementing onChacingCounter");
		onCachingCounter++;
	}

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

}
