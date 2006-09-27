package no.stelvio.common.config;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * JMX interface that defines the management operations on
 * the system configuration services. Implementations are 
 * the Standard MBeans. 
 * 
 * @author person7553f5959484
 * @version $Revision: 2720 $ $Author: skb2930 $ $Date: 2005-12-22 14:39:55 +0100 (Thu, 22 Dec 2005) $
 */
public interface ConfigMBean {

	/**
	 * Sets the new XML definition for this config instance. A call to this method will reload the bean factory with the new XML
	 * definition.
	 *
	 * @param xml the new xml
	 */
	void update(String xml);

	/**
	 * Saves the currently loaded configuration to the file, overwriting the previously saved configuration.
	 */
	void save();

	/**
	 * Adds a config instance as a listener for changes in the configuration.
	 *
	 * @param listener the Config instance to add
	 */
	void addListener(Config listener);

	/**
	 * Gets the filename of the configuration file.
	 * 
	 * @return the config filename.
	 */
	String getFileName();

	/**
	 * Gets the XML currently loaded into memory as a string.
	 * @return xml in memory.
	 */
	String getXML();

	/**
	 * Sets the XML currently loaded into memory.
	 *
	 * @param xml in memory.
	 */
	void setXML(String xml);

	/**
	 * Gets the BeanFactory associated with this Config instance.
	 *
	 * @return the BeanFactory instance
	 */
	ConfigurableListableBeanFactory getBeanFactory();

	/**
	 * Sets the BeanFactory to be associated with this Config instance.
	 *
	 * @param factory the BeanFactory instance.
	 */
	void setBeanFactory(ConfigurableListableBeanFactory factory);
}
