package no.stelvio.presentation.constants;

import java.util.Map;

/**
 * Wrapper class for handling constants in the presentation layer. Provides
 * functionality to extract constants from a given class as a map.
 * 
 * @version $Id$
 *
 */
public class ConstantsWrapper {
	/**
	 * Returns the constants from a given class as a Map with the constant field values as 
	 * values and their names as the key.
	 * @param clazz the class to fetch constants from
	 * @return a Map initialized with constants from the given class
	 */
	public Map getConstants(Class clazz) {
		return new ConstantsHolder(clazz).values();
	}

	/**
	 * The <code>org.springframework.core.Constants</code> class can be used to parse 
	 * other classes containing constant definitions of public static final members. 
	 * This inner class is a subclass of the Constants class in the springframework, and 
	 * provides additional functionality to access the constant field values as a Map. 
	 * This functionality is convinient in order to access constants
	 * from xhtml pages.	
	 */
	private class ConstantsHolder extends org.springframework.core.Constants {
		
		/**
		 * Create a new Constants converter class wrapping the given class.
		 * 
		 * @param clazz the class to be analyzed
		 */
		public ConstantsHolder(Class clazz) {
			super(clazz);
		}
		
		/**
		 * Returns all the constants in a Map. The key is the name of the constant and
		 * the value is the constant fields value.
		 * 
		 * @return Map containing all the constants of the analyzed class
		 */
		public Map values() {
			return super.getFieldCache();
		}
	}
}
