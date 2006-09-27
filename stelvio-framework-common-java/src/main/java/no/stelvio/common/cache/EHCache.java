package no.stelvio.common.cache;

import java.io.IOException;
import java.io.Serializable;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import no.stelvio.common.FrameworkError;
import no.stelvio.common.config.ConfigurationException;
import no.stelvio.common.jmx.JMXUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * EHCache plugin for stelvio.
 * 
 * <p/>
 * 
 * This class is almost a replica of {@link net.sf.hibernate.cache.EhCache}
 * from Hibernate 2.1.6.
 * 
 * <p/>
 * 
 * EHCache uses a {@link net.sf.ehcache.store.MemoryStore} and a
 * {@link net.sf.ehcache.store.DiskStore}. The {@link net.sf.ehcache.store.DiskStore}
 * requires that both keys and values be {@link Serializable}. For this reason
 * this plugin throws Exceptions when either of these are not castable to {@link Serializable}.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: EHCache.java 2786 2006-02-28 13:24:08Z skb2930 $
 */
public class EHCache
	implements EHCacheMBean, no.stelvio.common.cache.Cache {

	private final Log log = LogFactory.getLog(this.getClass());
	private Cache cache;

	/**
	 * Creates a new pluggable cache based on a cache configuration filename and cache name.
	 * 
	 * @param configuration the filepath to the configuration file, must be available on the classpath.
	 * @param category the name of the cache. This cache must have already been configured.
	 */
	public EHCache(String configuration, String category) {

		try {
			CacheManager manager =
				CacheManager.create(
					Thread
						.currentThread()
						.getContextClassLoader()
						.getResourceAsStream(
						configuration));
			cache = manager.getCache(category);

			if (null == cache) {
				if (log.isWarnEnabled()) {
					log.warn(
						"Could not find configuration for "
							+ category
							+ ". Configuring using the defaultCache settings.");
				}

				manager.addCache(category);
				cache = manager.getCache(category);
			}

			if (log.isDebugEnabled()) {
				log.debug(
					"EHCache initialized using configuration '"
						+ configuration
						+ "' and category '"
						+ category
						+ "'");
			}
		} catch (CacheException e) {
			throw new ConfigurationException(
				FrameworkError.CACHE_CONFIGURATION_ERROR,
				e,
				new String[] { configuration, category });
		}

		// Find the JMX server where to register this cache instance
		MBeanServer server = JMXUtils.getMBeanServer(null);
		ObjectName mbeanName = null;

		try {
			mbeanName = new ObjectName("Cache:name=" + getName());
		} catch (MalformedObjectNameException e) {
			throw new ConfigurationException(
				FrameworkError.CACHE_CONFIGURATION_ERROR,
				e,
				"Cache:name=" + getName());
		}

		if (!server.isRegistered(mbeanName)) {
			if (log.isDebugEnabled()) {
				log.debug("Registering MBean with name:" + mbeanName);
			}

			try {
				server.registerMBean(this, mbeanName);
			} catch (InstanceAlreadyExistsException iae) {
				// Defect #2984 As we have a cluster, we cannot guarantee that the MBean is not registered between the check
				// and our registering, so we only log this
				if (log.isDebugEnabled()) {
					log.debug("Failed to register service for management", iae);
				}
			} catch (MBeanRegistrationException e) {
				throw new ConfigurationException(
					FrameworkError.CACHE_CONFIGURATION_ERROR,
					e,
					mbeanName.toString());
			} catch (NotCompliantMBeanException e) {
				throw new ConfigurationException(
					FrameworkError.CACHE_CONFIGURATION_ERROR,
					e,
					mbeanName.toString());
			}
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	public Object get(Object key) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("key: " + key);
			}

			if (null == key) {
				return null;
			} else {
				Element element = cache.get((Serializable) key);

				if (log.isDebugEnabled()) {
					log.debug("Element for " + key + " is " + element);
				}

				if (null == element) {
					return null;
				} else {
					return element.getValue();
				}
			}
		} catch (CacheException e) {
			throw new ConfigurationException(
				FrameworkError.CACHE_GET_ERROR,
				e,
				new Object[] { key, cache.getName()});
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	public void put(Object key, Object value) {
		try {
			Element element =
				new Element((Serializable) key, (Serializable) value);
			cache.put(element);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException(
				FrameworkError.CACHE_PUT_ERROR,
				e,
				new Object[] { key, cache.getName()});
		}
	}

	/** 
	 * Removes the element which matches the key.
	 * <p>
	 * If no element matches, nothing is removed and no Exception is thrown.
	 * 
	 * {@inheritDoc}
	 */
	public void remove(Object key) {
		try {
			cache.remove((Serializable) key);
		} catch (ClassCastException e) {
			throw new ConfigurationException(
				FrameworkError.CACHE_REMOVE_ERROR,
				e,
				new Object[] { key, cache.getName()});
		}
	}

	/** 
	 * Remove all elements in the cache, but leave the cache in a useable state.
	 * 
	 * {@inheritDoc}
	 */
	public void clear() {
		try {
			cache.removeAll();
		} catch (IOException e) {
			throw new ConfigurationException(
				FrameworkError.CACHE_CLEAR_ERROR,
				e,
				cache.getName());
		}
	}

	/** 
	 * Remove the cache and make it unuseable.
	 * 
	 * {@inheritDoc}
	 */
	public void destroy() {
		try {
			CacheManager.getInstance().removeCache(cache.getName());
		} catch (CacheException e) {
			throw new ConfigurationException(
				FrameworkError.CACHE_DESTROY_ERROR,
				e,
				cache.getName());
		}
	}

	/** 
	 * Calls to this method should perform their own synchronization.
	 * It is provided for distributed caches. Because EHCache is not distributed this method does nothing.
	 * 
	 * {@inheritDoc}
	 */
	public void lock(Object key) {
	}

	/** 
	 * Calls to this method should perform their own synchronization.
	 * It is provided for distributed caches. Because EHCache is not distributed this method does nothing.
	 * 
	 * {@inheritDoc}
	 */
	public void unlock(Object key) {
	}

	/** 
	 * {@inheritDoc}
	 */
	public long nextTimestamp() {
		return Timestamper.next();
	}

	/** 
	 * Returns the lock timeout for this cache.
	 * 
	 * {@inheritDoc}
	 */
	public int getTimeout() {
		// 60 second lock timeout
		return Timestamper.ONE_MS * 60000;
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getName() {
		return cache.getName();
	}

	/** 
	 * {@inheritDoc}
	 */
	public int getMaxElements() {
		return cache.getMaxElementsInMemory();
	}

	/** 
	 * {@inheritDoc}
	 */
	public int getNumElements() {
		return cache.getKeysNoDuplicateCheck().size();
	}

	/** 
	 * {@inheritDoc}
	 */
	public long getMemorySize() {
		try {
			return cache.calculateInMemorySize();
		} catch (IllegalStateException e) {
			return -1;
		} catch (CacheException e) {
			return -1;
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	public int getHitCount() {
		return cache.getHitCount();
	}

	/** 
	 * {@inheritDoc}
	 */
	public int getMissCountExpired() {
		return cache.getMissCountExpired();
	}

	/** 
	 * {@inheritDoc}
	 */
	public int getMissCountNotFound() {
		return cache.getMissCountNotFound() - cache.getMissCountExpired();
	}

	/** 
	 * {@inheritDoc}
	 */
	public long getTimeToIdleSeconds() {
		return cache.getTimeToIdleSeconds();
	}

	/** 
	 * {@inheritDoc}
	 */
	public long getTimeToLiveSeconds() {
		return cache.getTimeToLiveSeconds();
	}

	/** 
	 * {@inheritDoc}
	 */
	public boolean isEternal() {
		return cache.isEternal();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void reset() {
		try {
			cache.removeAll();
			if (log.isInfoEnabled()) {
				log.info("Successfully reset cache " + getName());
			}
		} catch (IllegalStateException e) {
			if (log.isWarnEnabled()) {
				log.warn("Failed to reset cache " + getName(), e);
			}
		} catch (IOException e) {
			if (log.isWarnEnabled()) {
				log.warn("Failed to reset cache " + getName(), e);
			}
		}
	}
}
