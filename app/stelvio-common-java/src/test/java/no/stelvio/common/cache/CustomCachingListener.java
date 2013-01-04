/**
 * 
 */
package no.stelvio.common.cache;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springmodules.cache.CachingModel;
import org.springmodules.cache.interceptor.caching.CachingListener;

/**
 * Custom listener-class that listens to changes in the cache.
 * 
 * @author Eirik, Accenture
 */
public class CustomCachingListener implements CachingListener {
	private Logger log = Logger.getLogger(CustomCachingListener.class);

	/**
	 * Creates a new instance of CustomCachingListener.
	 */
	public CustomCachingListener() {
		log.debug("DEBUG FROM LISTENER: Entering constructor for CustomCachingListener...");
	}

	/**
	 * {@inheritDoc}
	 */
	public void onCaching(Serializable key, Object object, CachingModel model) {
		log.debug("DEBUG FROM LISTENER: Entering CustomCachingListener.onCaching()");

		log.debug("DEBUG FROM LISTENER: \n\n");
		log.debug("DEBUG FROM LISTENER: Listener - key: " + key);
		log.debug("DEBUG FROM LISTENER: Listener - object: " + object);
		log.debug("DEBUG FROM LISTENER: Listener - model: " + model);
		log.debug("DEBUG FROM LISTENER: \n\n");
	}
}