package no.nav.bpchelper.utils;

/**
 * <p>String values to collect information from environment configuration
 * file. Since this file is reused from FEM Helper tool, can this
 * values be pulled up to a common lib if the code bases are merged,
 * or is changed to use a common library.</p>
 * 
 * @author Andreas Roe
 */
public interface ConnectorProperties {

	/**
	 * Public constant to represent the 'CONNECTOR_HOST' string from
	 * the configuration file and properties related to that
	 */
	public static final String CONNECTOR_HOST = "CONNECTOR_HOST";

	/**
	 * Public constant to represent the 'CONNECTOR_PORT' string from
	 * the configuration file and properties related to that
	 */
	public static final String CONNECTOR_PORT = "CONNECTOR_PORT";

	/**
	 * Public constant to represent the 'CONNECTOR_TYPE' string from
	 * the configuration file and properties related to that
	 */
	public static final String CONNECTOR_TYPE = "CONNECTOR_TYPE";

	/**
	 * Public constant to represent the 'CONNECTOR_SECURITY_ENABLED' string from
	 * the configuration file and properties related to that
	 */
	public static final String CONNECTOR_SECURITY_ENABLED = "CONNECTOR_SECURITY_ENABLED";

	/**
	 * Public constant to represent the 'USERNAME' string from
	 * the configuration file and properties related to that
	 */
	public static final String USERNAME = "USERNAME";

	/**
	 * Public constant to represent the 'PASSWORD' string from
	 * the configuration file and properties related to that
	 */
	public static final String PASSWORD = "PASSWORD";

	/**
	 * Public constant to represent the 'SSL_KEYSTORE' string from
	 * the configuration file and properties related to that
	 */
	public static final String SSL_KEYSTORE = "SSL_KEYSTORE";

	/**
	 * Public constant to represent the 'SSL_KEYSTORE_PASSWORD' string from
	 * the configuration file and properties related to that
	 */
	public static final String SSL_KEYSTORE_PASSWORD = "SSL_KEYSTORE_PASSWORD";

	/**
	 * Public constant to represent the 'SSL_TRUSTSTORE' string from
	 * the configuration file and properties related to that
	 */
	public static final String SSL_TRUSTSTORE = "SSL_TRUSTSTORE";

	/**
	 * Public constant to represent the 'SSL_TRUSTSTORE_PASSWORD' string from
	 * the configuration file and properties related to that
	 */
	public static final String SSL_TRUSTSTORE_PASSWORD = "SSL_TRUSTSTORE_PASSWORD";

}