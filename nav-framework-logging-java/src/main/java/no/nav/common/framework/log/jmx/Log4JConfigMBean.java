package no.nav.common.framework.log.jmx;

/**
 * MBean implementation which manages Log4J configuration.
 * 
 * @author person356941106810, Accenture
 * @version $Id: Log4JConfigMBean.java 2194 2005-04-06 09:43:22Z psa2920 $
 */
public interface Log4JConfigMBean {
	
	/**
	 * Returns the currently loaded log4.config
	 * @return the config
	 */
	String getLogConfig();
	
	/**
	 * Sets a new log4j configuration.
	 * @param newConfig the config as a string
	 */
	void setLogConfig(String newConfig);
	
	/**
	 * Reloads the config file. This will override the config stored in memory.
	 * @exception Exception an error occurred.
	 */
	void load() throws Exception;
	
	/**
	 * Saves the currently loaded configuration to file.
	 * This will overwrite the config currently stored on disk.
	 * @exception Exception an error occurred.
	 */
	void save() throws Exception;
	
}
