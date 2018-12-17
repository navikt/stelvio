package no.stelvio.presentation.security.eai.headers.config.support;

import no.stelvio.presentation.security.eai.headers.config.ConfigEntry;

/**
 * StanzaEntryEnum.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 * @see ConfigEntry
 * 
 */
public enum StanzaEntryEnum implements ConfigEntry {

	// PAC headers
	/** PAC header. */
	EAI_PAC_HEADER, 
	/** PAC svc header. */
	EAI_PAC_SVC_HEADER,
	
	// User identity headers
	/** UserId header. */
	EAI_USER_ID_HEADER, 
	/** Authentication level header. */
	EAI_AUTH_LEVEL_HEADER, 
	/** xattrs header. */
	EAI_XATTRS_HEADER,
	
	// Common headers
	/** Redir url header. */
	EAI_REDIR_URL_HEADER;

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
