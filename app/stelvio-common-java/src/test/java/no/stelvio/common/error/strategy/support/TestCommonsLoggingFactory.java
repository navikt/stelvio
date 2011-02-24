package no.stelvio.common.error.strategy.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.LogFactoryImpl;

import java.lang.reflect.Field;

/**
 * A Commons logging log factory to be used when testing that a component logs correctly.
 *
 * @author personf8e9850ed756, Accenture
*/
public class TestCommonsLoggingFactory extends LogFactoryImpl {
	private Log log;

	/**
	 * Sets the logger to be used by the Commons logging framework.
     * Remember to call {@link #resetLogger()} when finished.
	 *
	 * @param logger the logger implementation that is used by Commons logging.
	 */
	public static void setLogger(Log logger) {
		System.setProperty("org.apache.commons.logging.LogFactory",
				"no.stelvio.common.error.strategy.support.TestCommonsLoggingFactory");
		LogFactory.releaseAll();
		((TestCommonsLoggingFactory) LogFactory.getFactory()).log = logger;
	}
    
    /**
	 * Resets the logger to be used by the Commons logging framework, that is, set it to nothing.
	 */
	public static void resetLogger() {
		System.setProperty("org.apache.commons.logging.LogFactory", null);
		LogFactory.releaseAll();
	}

	/**
	 * Replaces the log instance on the given instance's static log field called "log" with the specified log
	 * instance. Remember that the field cannot be final.
	 *
	 * @param o the instance to replace the log variable on.
	 * @param log the log instance to use.
	 * @throws NoSuchFieldException no field
	 * @throws IllegalAccessException illegal access
	 */
	public static void changeLogger(Object o, Log log) throws NoSuchFieldException, IllegalAccessException {
        changeLogger(o.getClass(), log);
	}

	/**
	 * Change logger.
	 * 
	 * @param clazz class
	 * @param log log
	 * @throws NoSuchFieldException no field
	 * @throws IllegalAccessException illegal access
	 */
	public static void changeLogger(Class clazz, Log log) throws NoSuchFieldException, IllegalAccessException {
		Field logField = clazz.getDeclaredField("log");
		logField.setAccessible(true);
		logField.set(null, log);
	}

	/**
	 * Get instance.
	 * 
	 * @param clazz unused
	 * @return log 
	 */
	public Log getInstance(Class clazz) {
		return log;
	}
}
