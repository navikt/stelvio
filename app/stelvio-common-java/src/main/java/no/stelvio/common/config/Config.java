package no.stelvio.common.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.OrderComparator;
import org.springframework.core.io.InputStreamResource;

/**
 * Config is the central class of the system configuration services.
 * <p/>
 * Config is thin wrapper on top of the core package from Spring Framework
 * and supports the following features:
 * <ul>
 * 	<li> Configuration registry based on local xml files improves manageability.
 * 	<li> Configuration lookup and caching improves performance.
 * 	<li> Configuration instances are implemented as JavaBeans to support strong typing.
 * 	<li> Configuration referencing provides Inversion-of-Control support.
 *  <li> Configuration management using JMX. 
 * </ul>
 * 
 * @author person7553f5959484
 * @author person356941106810, Accenture
 * @version $Id: Config.java 2739 2006-01-13 17:56:07Z skb2930 $
 */
public class Config implements ConfigMBean {

	// Spring Framework Java Bean Factory
	private ConfigurableListableBeanFactory beanFactory;

	// Configuration filename
	private String filename;

	// the log
	private Log log = LogFactory.getLog(getClass());

	// the xml that is the basis for the spring bean definition
	private String xml;

	// the MBean server this Config is registered in 
	private MBeanServer server;

	// the ObjectName of this Config
	private ObjectName mbeanName;

	private List configChangeListeners;

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/** PRESENTATION_SERVICES is configured in presentation-services.xml */
	public static final String PRESENTATION_SERVICES = "presentation-services.xml";

	/** APPLICATION_SERVICES is configured in application-services.xml */
	public static final String APPLICATION_SERVICES = "application-services.xml";

	/** ENTERPRISE_SERVICES is configured in enterprise-services.xml */
	public static final String ENTERPRISE_SERVICES = "enterprise-services.xml";

	/** DISTRIBUTED_SERVICES is configured in distributed-services.xml */
	public static final String DISTRIBUTED_SERVICES = "distributed-services.xml";

	/** ERROR_HANDLING is configured in error-handling.xml */
	public static final String ERROR_HANDLING = "error-handling.xml";

	/** PERFORMANCE_MONITORING is configured in performance-monitoring.xml */
	public static final String PERFORMANCE_MONITORING = "performance-monitoring.xml";

	static final String CONFIG_CAN_BE_CHANGED_SYSTEM_PROPERTY =
			"no.stelvio.common.config.ConfigCanBeChanged";

	/**
	 * Used when unit testing FindDispatchAction sub-classes
	 */
	Config() {
	}

	/**
	 * Create a configuration registry based on specified filename.
	 * This constructor should never be used directly. JMX requires that this is public.
	 * 
	 * @param filename path to the xml file where configuration is persisted.
	 * @param server the MBeanServer 
	 * @param mbeanName the ObjectName of the MBean
	 */
	public Config(String filename, MBeanServer server, ObjectName mbeanName) {
		this.filename = filename;
		this.server = server;
		this.mbeanName = mbeanName;

		// use a synchronized list to ensure we don't run into any concurrency problems
		configChangeListeners = new Vector();

		updateInternal(retrieveConfiguration());
		// register the bean after loading in case loading fails
		registerMBean();
	}

	/**
	 * Create a configuration registry based on a existing beanfactory registered in the JMX server.
	 * This constructor should never be used directly. JMX requires that this is public.
	 *
	 * @param server the MBeanServer 
	 * @param mbeanName the ObjectName of the MBean
	 */
	public Config(MBeanServer server, ObjectName mbeanName) {
		this.server = server;
		this.mbeanName = mbeanName;

		retrieveConfigFromServer();
	}

	/**
	 * Retrieves a Config instance which is bound to the given filename. If the Config instance 
	 * does not exist, it will be created.
	 * 
	 * @param filename the filename identifying the Config instance.
	 * @return the Config instance bound to the filename.
	 * @throws ConfigurationException The configuration could not be loaded or the MBean registered.
	 */
	public static Config getConfig(String filename) throws ConfigurationException {
		ObjectName mbeanName = createObjectName(filename);
		MBeanServer server = retrieveMBeanServer();
		Config cfg;

		if (server.isRegistered(mbeanName)) {
			cfg = new Config(server, mbeanName);
		} else {
			// Defect # 2382 - InstanceAlreadyExistsException
			try {
				cfg = new Config(filename, server, mbeanName);
			} catch (ConfigurationException ce) {
				if (ce.getCause() instanceof InstanceAlreadyExistsException) {
					cfg = new Config(server, mbeanName);
				} else {
					throw ce;
				}
			}
		}

		return cfg;
	}

	/**
	 * Gets a bean instance identified by beanName.
	 * 
	 * @param beanName the ID of the bean to retrieve.
	 * @return a bean instance.
	 * @throws ConfigurationException if the requested bean does not exist
	 */
	public Object getBean(String beanName) throws ConfigurationException {
		// Delegate lookup to Spring BeanFactory
		try {
			return beanFactory.getBean(beanName);
		} catch (NoSuchBeanDefinitionException nsbde) {
			throw new ConfigurationException(nsbde, beanName);
		} catch (BeanCreationException bce) {
			throw new ConfigurationException(bce, beanName);
		}
	}

	/**
	 * Saves the currently loaded configuration to the file for this configuration instance, overwriting the previously saved
	 * configuration.
	 */
	public void save() {
		// retrieve the location of the file to persist the bean configuration to
		URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
		FileWriter writer = null;

		try {
			writer = new FileWriter(url.getFile(), false);
			writer.write(xml);
			writer.flush();
		} catch (IOException e) {
			throw new ConfigurationException(e, "Error saving configuration.");
		} finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
					// Cannot do anything here
				}
			}
		}
	}

	/**
	 * Updates the XML definition for this config instance. A call to this method will reload the bean factory with the new XML
	 * definition.
	 *
	 * @param xmlString the new XML definition.
	 */
	public void update(String xmlString) {
		if (!canBeChanged()) {
			throw new ConfigurationException(
                    "Can only change configuration when system property is set");
		}

		updateInternal(xmlString);
	}

	/**
	 * Checks if a system property is set that enables changing the configuration runtime.
	 *
	 * @return true if the system property is true, false otherwise.
	 */
	public static boolean canBeChanged() {
		return Boolean.getBoolean(CONFIG_CAN_BE_CHANGED_SYSTEM_PROPERTY);
	}

	/**
	 * Helper method for retrieving the MBean server.
	 *
	 * @return the MBean server to use.
	 */
	private static MBeanServer retrieveMBeanServer() {
		List servers = MBeanServerFactory.findMBeanServer(null);
		MBeanServer server;

		if (null == servers || 0 == servers.size()) {
			LogFactory.getLog(Config.class).info("No MBeanServer was found, creating new MBeanServer");
			server = MBeanServerFactory.createMBeanServer();
		} else {
			server = (MBeanServer) servers.get(0);
		}

		return server;
	}

	/**
	 * Helper method for creating an <code>ObjectName</code>.
	 *
	 * @param filename the configuration file.
	 * @return an <code>ObjectName</code> identifying the configuration file.
	 */
	private static ObjectName createObjectName(final String filename) {
		ObjectName mbeanName = null;

		// must have classloader ID to separate two applications
		// we do this manually in order to get the same output as Object.toString would give
		String classLoaderId =
				Thread.currentThread().getContextClassLoader().getClass().getName()
				+ "@"
				+ Integer.toHexString(Thread.currentThread().getContextClassLoader().hashCode());
		try {
			mbeanName = new ObjectName("Config:type=Spring,filename=" + filename + ",classloader=" + classLoaderId);
		} catch (MalformedObjectNameException e) {
			throw new ConfigurationException(
                    e,
			        "Config:type=Spring,filename=" + filename + ",classloader=" + classLoaderId);
		}
		return mbeanName;
	}

	/**
	 * Returns the Spring config as a text string. This text string will be used in management operations.
	 * 
	 * @return the file as an XML string.
	 */
	private String retrieveConfiguration() {
		if (log.isInfoEnabled()) {
			log.info("Loading bean config from file: " + filename);
		}

		URL resource = Thread.currentThread().getContextClassLoader().getResource(filename);

		// Log the URL to the file in case of classpath problems
		if (log.isDebugEnabled()) {
			log.debug("URL of bean config file: " + resource);
		}

		if (null == resource) {
			throw new ConfigurationException(filename);
		}

		return configFileContent();
	}

	/**
	 * Helper method for retrieving the content of the configuration file.
	 *
	 * @return the content of the configuration file.
	 */
	private String configFileContent() {
		BufferedReader reader = null;
		StringBuffer newXml = new StringBuffer();

		try {
			reader =
					new BufferedReader(
							new InputStreamReader(
									Thread.currentThread().getContextClassLoader().getResourceAsStream(filename),
							        "8859_1"));

			String line = reader.readLine();

			while (null != line) {
				newXml.append(line).append(LINE_SEPARATOR);
				line = reader.readLine();
			}
		} catch (RuntimeException re) {
			throw new ConfigurationException(re, filename);
		} catch (IOException e) {
			throw new ConfigurationException(e, filename);
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new ConfigurationException(e, filename);
				}
			}
		}

		final String xmlAsString = newXml.toString();

		if (log.isTraceEnabled()) {
			log.trace("Config file content: " + xmlAsString);
		}

		return newXml.toString();
	}

	/**
	 * Registers this configuration as an MBean in a JMX server.
	 */
	private void registerMBean() {
		try {
			log.info("Registering MBean with name:" + mbeanName);
			server.registerMBean(this, mbeanName);
		} catch (InstanceAlreadyExistsException e) {
			throw new ConfigurationException(e, mbeanName);
		} catch (MBeanRegistrationException e) {
			throw new ConfigurationException(e, mbeanName);
		} catch (NotCompliantMBeanException e) {
			throw new ConfigurationException(e, mbeanName);
		}
	}

	/**
	 * Retrieves the fields from the instance registered with the MBean server.
	 */
	private void retrieveConfigFromServer() {
		try {
			beanFactory = (ConfigurableListableBeanFactory) server.getAttribute(mbeanName, "BeanFactory");
			filename = (String) server.getAttribute(mbeanName, "FileName");
			xml = (String) server.getAttribute(mbeanName, "XML");

			server.invoke(mbeanName, "addListener", new Config[] { this }, new String[] {getClass().getName()});
		} catch (AttributeNotFoundException e) {
			throw new ConfigurationException(
                    e,
			                                  "Attribute not found on Config MBean.");
		} catch (InstanceNotFoundException e) {
			throw new ConfigurationException(
                    e,
			                                  "Config instance not found.");
		} catch (MBeanException e) {
			throw new ConfigurationException(
                    e,
			                                  "Error communicating with Config MBean.");
		} catch (ReflectionException e) {
			throw new ConfigurationException(
                    e,
			    "Unable to read attributes from Config MBean.");
		}

	}

	/**
	 * Does the actual loading of the new XML definition.
	 *
	 * @param xmlString the new XML definition.
	 * @see #update(String)
	 */
	private void updateInternal(final String xmlString) {
		InputStream in = new ByteArrayInputStream(xmlString.getBytes());

		try {
			createXMLBeanFactory(in);
		} catch (BeansException e) {
			throw new ConfigurationException(
                    e,
			        "Unable to read XML configuration from file '" + filename + "'");
		}

		setMBeanAttribute("XML", xml);
		xml = xmlString;
		updateConfigListeners();
	}

	/**
	 * Creates a new XML Bean factory and gives it to the MBean.
	 *
	 * @param in the stream to read the bean definitions from
	 * @throws BeansException error ocurred while loading configuration
	 */
	private void createXMLBeanFactory(InputStream in) throws BeansException {
		if (null == in) {
			throw new ConfigurationException(null, null);
		}

		beanFactory = new XmlBeanFactory(new InputStreamResource(in));
		invokeBeanFactoryPostProcessors();

		// if there is no server, there is no MBean yet and this is the only instance
		setMBeanAttribute("BeanFactory", beanFactory);

	}

	/**
	 * Sets the specified property on the MBean representation of this Config.
	 * 
	 * @param fieldName the name of the field to set.
	 * @param value the value to set.
	 */
	private void setMBeanAttribute(String fieldName, Object value) {
		// only set the attribute if this MBean is registered...
		if (server.isRegistered(mbeanName)) {
			try {
				server.setAttribute(mbeanName, new Attribute(fieldName, value));
			} catch (InstanceNotFoundException e) {
				throw new ConfigurationException(e, mbeanName);
			} catch (AttributeNotFoundException e) {
				throw new ConfigurationException(e, fieldName);
			} catch (InvalidAttributeValueException e) {
				throw new ConfigurationException(e, value);
			} catch (MBeanException e) {
				throw new ConfigurationException(e, mbeanName);
			} catch (ReflectionException e) {
				throw new ConfigurationException(e, mbeanName);
			}
		}
	}

	/**
	 * Updates all config listeners with new factories, filename and xml.
	 */
	private void updateConfigListeners() {
		if (null == configChangeListeners) {
			return;
		}

		for (Iterator iterator = configChangeListeners.iterator(); iterator.hasNext();) {
			Config currConfig = (Config) iterator.next();

			currConfig.beanFactory = beanFactory;
			currConfig.xml = xml;
			currConfig.filename = filename;
		}
	}

	/**
	 * Instantiate and invoke all registered BeanFactoryPostProcessor beans, respecting explicit order if given.
	 * Must be called before singleton instantiation.
	 * 
	 * @throws BeansException if post processors fail.
	 */
	private void invokeBeanFactoryPostProcessors() throws BeansException {
		String[] beanNames = beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);
		BeanFactoryPostProcessor[] factoryProcessors = new BeanFactoryPostProcessor[beanNames.length];

		for (int i = 0; i < beanNames.length; i++) {
			factoryProcessors[i] = (BeanFactoryPostProcessor) getBean(beanNames[i]);
		}

		Arrays.sort(factoryProcessors, new OrderComparator());

		for (int i = 0; i < factoryProcessors.length; i++) {
			BeanFactoryPostProcessor factoryProcessor = factoryProcessors[i];
			factoryProcessor.postProcessBeanFactory(beanFactory);
		}
	}

	/**
	 * Adds a new Config object that will listen for changes in the configuration.
	 * @param listener the Config object to notify when the config changes.
	 */
	public void addListener(Config listener) {
		configChangeListeners.add(listener);
	}

	/**
	 * Gets the filename of the configuration file.
	 *
	 * @return the config filename.
	 */
	public String getFileName() {
		return filename;
	}

	/**
	 * Gets the XML currently loaded into memory as a string.
	 *
	 * @return xml in memory.
	 */
	public String getXML() {
		return xml;
	}

	/**
	 * Sets the XML currently loaded into memory.
	 *
	 * @param xml in memory.
	 */
	public void setXML(final String xml) {
		this.xml = xml;
	}

	/**
	 * Gets the BeanFactory associated with this Config instance.
	 * 
	 * @return the BeanFactory instance
	 */
	public ConfigurableListableBeanFactory getBeanFactory() {
		return beanFactory;
	}

	/**
	 * Sets the BeanFactory to be associated with this Config instance.
	 *
	 * @param factory the BeanFactory instance.
	 */
	public void setBeanFactory(final ConfigurableListableBeanFactory factory) {
		this.beanFactory = factory;
	}
}
