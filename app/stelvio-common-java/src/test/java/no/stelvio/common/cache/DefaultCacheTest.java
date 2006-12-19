package no.stelvio.common.cache;


import org.apache.log4j.Logger;
import org.springmodules.cache.annotations.CacheFlush;
import org.springmodules.cache.annotations.Cacheable;

/**
 * Implements services for accessing data related to CacheTest. 
 *  
 * @author person4f9bc5bd17cc, Accenture
 */
public class DefaultCacheTest implements CacheTest {
	private Logger log = Logger.getLogger(CacheManagementTest.class);
	private final String initialString = "This is a cacheable string";
	private String cachedString = new String(initialString);
	private int cnt = 0;
	
	/**
	 * {@inheritDoc CacheTest#getStringCached()}
	 */
	@Cacheable(modelId = "non-persistent")	
	public String getStringCached() {
		log.debug("DEBUG: Entering getStringCached()...");
		return cachedString;
	}

	/**
	 * {@inheritDoc CacheTest#updateStringAndFlush()}
	 */
	@CacheFlush(modelId = "flushingModel")
	public void updateStringAndFlush() {
		log.debug("DEBUG: Entering updateStringAndFlush()...");
		cachedString = initialString+(++cnt);
		
	}

	/**
	 * {@inheritDoc CacheTest#updateStringNoFlush()}
	 */
	public void updateStringNoFlush() {
		log.debug("DEBUG: Entering updateStringNoFlush()...");
		cachedString = initialString+(++cnt);
	}
}