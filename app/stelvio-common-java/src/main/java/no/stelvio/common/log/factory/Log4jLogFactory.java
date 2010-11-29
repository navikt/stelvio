package no.stelvio.common.log.factory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.FileWatchdog;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * A commons-logging LogFactory implementation which adds support for Log4J and file watching.
 * 
 * Change the map (maybe to a JDK 1.5 impl.) used to store class loaders in commons logging by setting the
 * LogFactory.HASHTABLE_IMPLEMENTATION_PROPERTY; look at its javadoc for more information
 * 
 * Should we make use of LogFactory.DIAGNOSTICS_DEST_PROPERTY?
 * 
 * By having our own LogFactory we will probably get memory leaks due to classloader instances not getting
 * released. Look at javadoc for commons logging's WeakHashtable. 
 * 
 * Commons Logging has version already, but without refresh support. If JMX for Log4J is used, use Commons Logging's
 * version instead.
 * 
 * @author person356941106810, Accenture
 * @version $Id: Log4jLogFactory.java 2194 2005-04-06 09:43:22Z psa2920 $
 */
public class Log4jLogFactory extends LogFactory {
	/** The config. refresh interval. Default 60 seconds.*/
	private long refreshInterval = FileWatchdog.DEFAULT_DELAY;

	/**
 	* Configuration attributes. Not thread safe.
 	*/
	private Map<String, Object> attributes = new Hashtable<String, Object>();

	/**
	 * The org.apache.commons.logging.Log instances that have
	 * already been created, keyed by logger name. Not thread safe.
	 */
	private Map<String, Log> instances = new Hashtable<String, Log>();

	/**
	 * The name of the property used to determine the interval between each refresh of 
	 * the log4j configuration (in milliseconds).
	 */
	public static final String REFRESH_INTERVAL =
		"no.stelvio.common.log.factory.Log4jLogFactory.refreshInterval";

	/** Wether or not to use the default init. sequence in LogManager */
	public static final String DEFAULT_INIT_OVERRIDE_KEY = "log4j.defaultInitOverride";

	/**
	 * The property key that will point to the Log4J configuration file.
	 */
	public static final String DEFAULT_CONFIGURATION_KEY = "log4j.configuration";

	/**
	 * Static initializer to make sure the Log4J framework is initialized correctly
	 */
	static {
		configure();
	}

	/**
	 * Return the configuration attribute with the specified name (if any),
	 * or <code>null</code> if there is no such attribute.
	 *
	 * @param name Name of the attribute to return
	 * 
	 * @return the configuration attribute
	 */
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	/**
	 * Return an array containing the names of all currently defined
	 * configuration attributes.  If there are no such attributes, a zero
	 * length array is returned.
	 * 
	 * @return an array with all attribute names.
	 */
	public String[] getAttributeNames() {
		List<String> names = new ArrayList<String>(attributes.keySet());
		String[] results = new String[names.size()];
		names.toArray(results);

		return results;
	}

	/**
	* Convenience method to derive a name from the specified class and
	* call <code>getInstance(String)</code> with it.
	*
	* @param clazz Class for which a suitable Log name will be derived.
	* @return A Log instance for the specified class name.
	* @exception LogConfigurationException if a suitable <code>Log</code> instance cannot be returned.
	*/
	public Log getInstance(Class clazz) throws LogConfigurationException {
		return getInstance(clazz.getName());
	}

	/**
	* <p>Construct (if necessary) and return a <code>Log</code> instance,
	* using the factory's current set of configuration attributes.</p>
	*
	* <p><strong>NOTE</strong> - Depending upon the implementation of
	* the <code>LogFactory</code> you are using, the <code>Log</code>
	* instance you are returned may or may not be local to the current
	* application, and may or may not be returned again on a subsequent
	* call with the same name argument.</p>
	*
	* @param name Logical name of the <code>Log</code> instance to be returned 
	* (the meaning of this name is only known to the underlying logging implementation
	* that is being wrapped)
	*
	* @exception LogConfigurationException if a suitable <code>Log</code> instance 
	* cannot be returned
	* 
	* @return a Log instance
	*/
	public Log getInstance(String name) throws LogConfigurationException {
		Log log = instances.get(name);

		if (log == null) {
			log = new Log4JLogger(Logger.getLogger(name));
			instances.put(name, log);
		}

		return log;
	}

	/**
	* Release any internal references to previously created {@link Log}
	* instances returned by this factory.  This is useful environments
	* like servlet containers, which implement application reloading by
	* throwing away a ClassLoader.  Dangling references to objects in that
	* class loader would prevent garbage collection.
	*/
	public void release() {
		instances.clear();
	}

	/**
	* Remove any configuration attribute associated with the specified name.
	* If there is no such attribute, no action is taken.
	*
	* @param name Name of the attribute to remove
	*/
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	/**
	* Set the configuration attribute with the specified name.  Calling
	* this with a <code>null</code> value is equivalent to calling
	* <code>removeAttribute(name)</code>.
	*
	* @param name Name of the attribute to set
	* @param value Value of the attribute to set, or <code>null</code>
	*  to remove any setting for this attribute
	*/
	public void setAttribute(String name, Object value) {
		if (value == null) {
			attributes.remove(name);
			// Should not check for reconfiguring if refresh interval's value cannot be checked against
		} else {
			attributes.put(name, value);

			if (REFRESH_INTERVAL.equalsIgnoreCase(name)) {
				long refresh = Long.parseLong(value.toString());

				// reconfigure the logging framework if the refresh interval changes
				if (refresh != refreshInterval) {
					refreshInterval = refresh;
					// this must be set so that the static configure method can reach it
					System.setProperty(REFRESH_INTERVAL, Long.toString(refreshInterval));
					LogLog.debug("Reconfiguring Log4J with a refresh value of [" + refreshInterval + "] milliseconds.");
					configure();
				}
			}
		}
	}

	/**
	 * Performs configuration of the Log4J framework. This will ensure that the
	 * framework is initialized with watch functionality.
	 */
	private static void configure() {
		// make sure the Log4J log manager initialization is overridden.
		System.setProperty(DEFAULT_INIT_OVERRIDE_KEY, "true");
		LogLog.debug("Initializing Log4J with automatic refresh of properties.");
		LogManager.resetConfiguration();

		// get the property file to use (default to "log4j.properties")
		String log4jConfig = OptionConverter.getSystemProperty(DEFAULT_CONFIGURATION_KEY, "log4j.properties");
		URL url;

		try {
			url = new URL(log4jConfig);
		} catch (MalformedURLException ex) {
			// so, resource is not a URL:
			// attempt to get the resource from the class path
			url = Loader.getResource(log4jConfig);
		}

		// get the actual filename and configure it
		if (url != null) {
			String fileName = url.getFile();
			String objRefresh = OptionConverter.getSystemProperty(REFRESH_INTERVAL, null);
			long refreshInterval = FileWatchdog.DEFAULT_DELAY;

			if (objRefresh != null) {
				refreshInterval = Long.parseLong(objRefresh);
			}

			LogLog.debug(
				"Using URL ["
					+ url
					+ "] for automatic log4j configuration. Refresh interval is ["
					+ refreshInterval
					+ "] milliseconds.");

			// check wether it is an xml file or a prop. file
			if (fileName.endsWith(".xml")) {
				DOMConfigurator.configure(fileName);
			} else {
				PropertyConfigurator.configure(fileName);
			}

		} else {
			LogLog.debug("Could not find resource: [" + log4jConfig + "].");
		}
	}
}
