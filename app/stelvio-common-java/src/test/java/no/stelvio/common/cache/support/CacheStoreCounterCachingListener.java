package no.stelvio.common.cache.support;

import java.io.Serializable;

import net.sf.ehcache.statistics.CacheUsageListener;
import net.sf.ehcache.statistics.sampled.NullSampledCacheStatistics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A CacheStoreCounterCachingListener.
 * 
 * @author MA
 */
public class CacheStoreCounterCachingListener extends NullSampledCacheStatistics {

	private static final Log LOG = LogFactory.getLog(CacheStoreCounterCachingListener.class);

	private int onCachingCounter;
	private String name;
	
	// Det er lagt til String name på denne klassen for å ha et utgangspunkt for hashCode() og equals()
	public CacheStoreCounterCachingListener(){
		this.name = "Ost OG kjeks";
	}
	
	public CacheStoreCounterCachingListener(String name){
		this.name = name;
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

	public void notifyCacheElementPut() {
		System.out.println(" HOI HOI.notifyCacheElementPut");
		onCachingCounter++;	
		LOG.debug("Cache hit, incrementing onChacingCounter");	
	}
	
	public void notifyRemoveAll() {
		System.out.println("Hoisann! notifyRemoveAll");
		LOG.debug("Cache hit, incrementing onChacingCounter");
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheStoreCounterCachingListener other = (CacheStoreCounterCachingListener) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
