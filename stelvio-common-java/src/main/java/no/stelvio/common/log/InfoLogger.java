package no.stelvio.common.log;

import org.apache.commons.logging.Log;

/**
 * Information logger.
 * <p>
 * Logger used for information logging. Restricts logging to <code>INFO</code> as the maximum log-level.
 * The logger also provides for a variable number of attributes to be appended to the log message.
 * </p>
 * <p>Usage:</p>
 * <p><ul>
 * <li>Inject a logger with a specified logger name, that the logger should use (otherwise default logger is used). 
 * <li>Always check if the wanted log-level is enabled before calling any of the log-methods.
 * </ul>
 * @since Stelvio 1.0.6.3
 */
public interface InfoLogger {

	/**
	 * Log debug message.
	 * @param message Message to log
	 */
	void debug(Object message);

	/**
	 * Log debug message.
	 * @param message Message to log
	 * @param attributes Additional message attributes
	 */
	void debug(Object message, Object... attributes);

	/**
	 * Log info message.
	 * @param message Message to log
	 */
	void info(Object message);

	/**
	 * Log info message.
	 * @param message Message to log
	 * @param attributes Additional message attributes
	 */
	void info(Object message, Object... attributes);

	/**
	 * Log trace message.
	 * @param message Message to log
	 */
	void trace(Object message);

	/**
	 * Log trace message.
	 * @param message Message to log
	 * @param attributes Additional message attributes
	 */
	void trace(Object message, Object... attributes);
	
	/**
	 * Get logger.
	 * 
	 * @return logger
	 */
	Log getLog();
	
	/**
	 * Set logger to be used when logging.
	 * 
	 * @param log Log
	 */
	void setLog(Log log);

	/**
	 * Checks if debug-loglevel is enabled. Should always be checked before calling the debug-log methods.
	 * @return <code>true</code> if debug-loglevel is enabled in logger configuration
	 */
	boolean isDebugEnabled();

	/**
	 * Checks if info-loglevel is enabled. Should always be checked before calling the info-log methods.
	 * @return <code>true</code> if info-loglevel is enabled in logger configuration
	 */
	boolean isInfoEnabled();

	/**
	 * Checks if trace-loglevel is enabled. Should always be checked before calling the trace-log methods.
	 * @return <code>true</code> if trace-loglevel is enabled in logger configuration
	 */
	boolean isTraceEnabled();

}
