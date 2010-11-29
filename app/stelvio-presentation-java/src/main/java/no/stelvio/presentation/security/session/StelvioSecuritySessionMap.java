package no.stelvio.presentation.security.session;

/**
 * 
 * Just a marker interface for package private implementations.
 * 
 * @author persondab2f89862d3
 */
public interface StelvioSecuritySessionMap {
	/** 
	 * Get object identified by key from the map.
	 * 
	 *  @param key the key
	 *  @return object
	 */
	Object get(String key); 	
}
