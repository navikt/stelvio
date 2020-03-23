package no.stelvio.presentation.security.eai.headers.config;

/**
 * EaiHeaderConfig.
 * 
 * @version $Id$
 * @see ConfigEntry
 *
 */
public interface EaiHeaderConfig {
	
	/**
	 * Get header name.
	 * 
	 * @param propertyName proeprty name
	 * @return header name
	 */
	String getHeaderName(ConfigEntry propertyName);
	
	/**
	 * Contains config entry.
	 * 
	 * @param propertyName property name
	 * @return true if propertyName is contained
	 */
	boolean containsConfigEntry(ConfigEntry propertyName);
}
