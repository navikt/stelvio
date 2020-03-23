package no.stelvio.presentation.security.eai.headers.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import no.stelvio.presentation.security.eai.headers.EaiHeaders;
import no.stelvio.presentation.security.eai.headers.config.ConfigEntry;
import no.stelvio.presentation.security.eai.headers.config.EaiHeaderConfig;
import no.stelvio.presentation.security.eai.headers.config.support.StanzaEntryEnum;

/**
 * Abstract class for retieval of EaiHeaders.
 * 
 * @version $Id$
 * @see EaiHeaders
 * @see EaiHeaderConfig
 * @see ConfigEntry
 * @see StanzaEntryEnum
 * 
 */
public abstract class AbstractEaiHeaders implements EaiHeaders {

	private Map<String, String> headers;
	private EaiHeaderConfig config;

	/**
	 * Construct a new AbstractEaiHeaders.
	 * 
	 * @param config
	 *            the EaiHeaderConfig.
	 */
	protected AbstractEaiHeaders(EaiHeaderConfig config) {
		this.config = config;
		this.headers = new HashMap<>();
	}

	/**
	 * Returns a Eai header.
	 * 
	 * @param key
	 *            the headers key in the config.
	 * @return the Eai header.
	 */
	protected String getHeader(ConfigEntry key) {
		return headers.get(config.getHeaderName(key));
	}

	@Override
	public Set<String> getHeaderNames() {
		return null;
	}

	@Override
	public Set<String> getHeaderValues() {
		return null;
	}

	@Override
	public Map<String, String> getHeaders() {
		return this.headers;
	}

	/**
	 * Sets the Eai redirect url header.
	 * 
	 * @param headerValue
	 *            the Eai redirect url header to set.
	 */
	public void setEaiRedirUrlHeader(String headerValue) {
		addHeader(StanzaEntryEnum.EAI_REDIR_URL_HEADER, headerValue);
	}

	/**
	 * Returns the Eai header configuration.
	 * 
	 * @return the EaiHeaderConfig.
	 */
	public EaiHeaderConfig getConfig() {
		return config;
	}

	/**
	 * Adds an Eai header.
	 * 
	 * @param entry entry
	 * @param headerValue header value
	 */
	protected void addHeader(ConfigEntry entry, String headerValue) {
		if (config.containsConfigEntry(entry)) {
			headers.put(config.getHeaderName(entry), headerValue);
		} else {
			throw new RuntimeException("Could not add header as it is not specified in the config file.");
		}
	}
}
