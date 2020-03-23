package no.stelvio.presentation.security.eai.headers.config.support;

import no.stelvio.presentation.security.eai.headers.config.ConfigEntry;

/**
 * An enum that contains keys for extracting the IBM Webseal EAI extended attribute header names from a configuration map.
 * 
 * @version $Id$
 * @see ConfigEntry
 * 
 */
public enum ExtendedAttributeEnum implements ConfigEntry {

	/** Original user. */
	XATTR_ORIGINAL_USER, 
	/** Authentication level. */
	XATTR_AUTHENTICATION_LEVEL, 
	/** Authorized as. */
	XATTR_AUTHORIZED_AS, 
	/** Authorization type. */
	XATTR_AUTHORIZATION_TYPE, 
	/** Some custom header entry. */
	SOME_CUSTOM_HEADER_ENTRY;

	/**
	 * Get name.
	 * 
	 * @return name
	 */
	@Override
	public String getName() {
		return name();
	}
}
