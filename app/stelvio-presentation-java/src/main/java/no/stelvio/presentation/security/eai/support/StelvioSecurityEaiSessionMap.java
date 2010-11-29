package no.stelvio.presentation.security.eai.support;

import java.util.HashMap;

import no.stelvio.presentation.security.session.StelvioSecuritySessionMap;

/**
 * A package private map for security related attributes which should be stored in the HttpSession.
 * 
 * @author persondab2f89862d3, Accenture
 */
class StelvioSecurityEaiSessionMap implements StelvioSecuritySessionMap {

	public static final String SESSIONMAP_KEY = "no.stelvio.presentation.security.eai.support.StelvioSecurityEaiSessionMap";

	/**
	 * The keys of the attributes stored in the StelvioSecurityEaiSessionMap.
	 */
	public enum Keys {
		AUTHORIZATION_TYPE, AUTHORIZED_AS, POST_AUTHENTICATION_URL, ALLOW_REAUTHENTICATION_AS_SELF;

		/**
		 * Get name.
		 * 
		 * @return name
		 */
		public String getName() {
			return name();
		}
	}

	private HashMap<String, Object> map;

	/**
	 * Constructor which creates a new HashMap.
	 */
	public StelvioSecurityEaiSessionMap() {
		this.map = new HashMap<String, Object>();
	}

	/**
	 * Returns the value to which the specified key is mapped in this security hash map, or null if the map contains no mapping
	 * for this key.
	 * 
	 * @param key
	 *            the key whose associated value is to be returned.
	 * @return the value to which this map maps the specified key, or null if the map contains no mapping for this key.
	 */
	public Object get(String key) {
		// String strKey = key.getName();
		Object value = map.get(key);
		return value;
	}

	/**
	 * Associates the specified value with the specified key in this map. If the map previously contained a mapping for this
	 * key, the old value is replaced
	 * 
	 * @param key
	 *            key with which the specified value is to be associated.
	 * @param value
	 *            value to be associated with the specified key.
	 */
	public void put(String key, Object value) {
		// String strKey = key.getName();
		map.put(key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {

		return "StelvioSecurityEaiSessionMap[" + map.toString() + "]";
	}
}
