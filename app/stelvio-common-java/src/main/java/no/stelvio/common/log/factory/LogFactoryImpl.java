package no.stelvio.common.log.factory;

import org.apache.log4j.LogManager;

/**
 * A commons-logging LogFactory implementation which makes sure Log4J log manager initialization is NOT overridden.
 * 
 * @author persone38597605f58 (Capgemini)
 */
public class LogFactoryImpl extends org.apache.commons.logging.impl.LogFactoryImpl {

	/**
	 * Static initializer to make sure the Log4J log manager initialization is NOT overridden.
	 */
	static {
		System.setProperty(LogManager.DEFAULT_INIT_OVERRIDE_KEY, "false");
	}

	/**
	 * Creates a new instance of LogFactoryImpl.
	 */
	public LogFactoryImpl() {
		super();
	}
}
