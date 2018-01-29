package no.stelvio.presentation.security.eai.headers.config.support;

import java.util.Properties;

import no.stelvio.presentation.security.eai.headers.config.ConfigEntry;
import no.stelvio.presentation.security.eai.headers.config.EaiHeaderConfig;

/**
 * DefaultEaiHeaderConfig.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 * @see EaiHeaderConfig
 * @see ConfigEntry
 * @see Properties
 * 
 */
public class DefaultEaiHeaderConfig implements EaiHeaderConfig {

	private Properties config;

	/**
	 * Set configuration.
	 * 
	 * @param config configuration
	 */
	public void setConfig(Properties config) {
		this.config = config;
	}

	/**
	 * Get configuration.
	 * 
	 * @return configuration
	 */
	public Properties getConfig() {
		return this.config;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getHeaderName(ConfigEntry entry) {
		return config.getProperty(entry.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsConfigEntry(ConfigEntry entry) {
		return config.containsKey(entry.getName());
	}
}
