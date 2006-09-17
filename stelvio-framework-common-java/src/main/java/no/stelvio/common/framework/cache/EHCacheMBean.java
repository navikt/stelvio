package no.stelvio.common.framework.cache;

/**
 * JMX interface that defines the management operations on the system cache services. 
 * Implementations are Standard MBeans. 
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: EHCacheMBean.java 2152 2005-03-31 11:41:18Z psa2920 $
 */
public interface EHCacheMBean {
	
	/**
	 * Gets the name of the cache.
	 * 
	 * @return the cache's name.
	 */
	String getName();
	
	/**
	 * Gets maximum number of elements to be hold in cache.
	 * 
	 * @return the maximum number of elements.
	 */
	int getMaxElements();
	
	/**
	 * Gets the number of elements currently in cache.
	 * 
	 * @return the current number of elements.
	 */
	int getNumElements();
	
	/**
	 * Gets the total size of the elements currently in cache in bytes. 
	 * 
	 * @return the byte size.
	 */
	long getMemorySize();
	
	/**
	 * Gets the number of times a requested item was found in the cache.
	 * 
	 * @return the hit count.
	 */
	int getHitCount();
	
	/**
	 * Gets the number of times a requested element was found but was expired.
	 * 
	 * @return the expired miss count.
	 */
	int getMissCountExpired();
	
	/**
	 * Gets the number of times a requested element was not found in the cache. Miss count is excluded.
	 * 
	 * @return the not found miss count.
	 */
	int getMissCountNotFound();
	
	/**
	 * Gets the maximum number of seconds an element in cache can be idle.
	 * 
	 * @return the max idle seconds.
	 */
	long getTimeToIdleSeconds();
	
	/**
	 * Gets the maximum number of seconds an element in cache can live.
	 * 
	 * @return the max seconds.
	 */
	long getTimeToLiveSeconds();
	
	/**
	 * Gets if the elements in cache live forever or not.
	 * 
	 * @return if the element is eternal.
	 */
	boolean isEternal();
	
	/**
	 * Clears the elements currently in cache.
	 */
	void reset();

}
