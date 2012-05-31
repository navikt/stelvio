/**
 * 
 */
package no.stelvio.common.cache;

import java.io.Serializable;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

import org.apache.log4j.Logger;
//import org.springmodules.cache.CachingModel;
//import org.springmodules.cache.interceptor.caching.CachingListener;

/**
 * Custom listener-class that listens to changes in the cache.
 * 
 * @author Eirik, Accenture
 */
public class CustomCachingListener /* implements CacheEventListener */ {
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
//	public void onCaching(Serializable key, Object object, CachingModel model) {
//		log.debug("DEBUG FROM LISTENER: Entering CustomCachingListener.onCaching()");
//
//		log.debug("DEBUG FROM LISTENER: \n\n");
//		log.debug("DEBUG FROM LISTENER: Listener - key: " + key);
//		log.debug("DEBUG FROM LISTENER: Listener - object: " + object);
//		log.debug("DEBUG FROM LISTENER: Listener - model: " + model);
//		log.debug("DEBUG FROM LISTENER: \n\n");
//	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void notifyElementEvicted(Ehcache arg0, Element arg1) {
		// TODO Auto-generated method stub
		
	}

	public void notifyElementExpired(Ehcache arg0, Element arg1) {
		// TODO Auto-generated method stub
		
	}

	public void notifyElementPut(Ehcache arg0, Element arg1)
			throws CacheException {
		log.debug("DEBUG FROM LISTENER: Entering CustomCachingListener.onCaching()");

		log.debug("DEBUG FROM LISTENER: \n\n");
		//log.debug("DEBUG FROM LISTENER: Listener - key: " + key);
		//log.debug("DEBUG FROM LISTENER: Listener - object: " + object);
		//log.debug("DEBUG FROM LISTENER: Listener - model: " + model);
		log.debug("DEBUG FROM LISTENER: \n\n");		
	}

	public void notifyElementRemoved(Ehcache arg0, Element arg1)
			throws CacheException {
		// TODO Auto-generated method stub
		
	}

	public void notifyElementUpdated(Ehcache arg0, Element arg1)
			throws CacheException {
		// TODO Auto-generated method stub
		
	}

	public void notifyRemoveAll(Ehcache arg0) {
		// TODO Auto-generated method stub
		
	}
}