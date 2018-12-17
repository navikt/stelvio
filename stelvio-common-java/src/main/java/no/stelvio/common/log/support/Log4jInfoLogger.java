package no.stelvio.common.log.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.log.InfoLogger;
import no.stelvio.common.log.MDCLogger;

/**
 * Information logger based on Log4J.
 * <p>
 * Restricts logging to <code>INFO</code> as the maximum log-level. In addition adds MDC properties to the log (See
 * {@link MDCLogger}). Default logger, <code>no.stelvio.common.log.InfoLogger</code> is used if no logger is set for the
 * logger instance.
 * </p>
 * 
 * @see no.stelvio.common.log.InfoLogger
 * @author person15754a4522e7 (ex-coder)
 * @since Stelvio 1.0.6.3
 */
public class Log4jInfoLogger extends MDCLogger implements InfoLogger {

	// Loggers used to log, default log is used if no other log is injected.
	private static final Log DEFAULT_LOG = LogFactory.getLog(InfoLogger.class);
	private Log log;

	@Override
	public void debug(Object message) {
		this.debug(message, new Object[] {});
	}

	@Override
	public void debug(Object message, Object... attributes) {
		setMdcProperties();
		getLog().debug(formatMessage(message, attributes));
		resetMdcProperties();
	}

	@Override
	public void info(Object message) {
		this.info(message, new Object[] {});
	}

	@Override
	public void info(Object message, Object... attributes) {
		setMdcProperties();
		getLog().info(formatMessage(message, attributes));
		resetMdcProperties();

	}

	@Override
	public void trace(Object message) {
		this.trace(message, new Object[] {});
	}

	@Override
	public void trace(Object message, Object... attributes) {
		setMdcProperties();
		getLog().trace(formatMessage(message, attributes));
		resetMdcProperties();
	}

	@Override
	public boolean isDebugEnabled() {
		return getLog().isDebugEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return getLog().isInfoEnabled();
	}

	@Override
	public boolean isTraceEnabled() {
		return getLog().isTraceEnabled();
	}

	@Override
	public Log getLog() {
		return (log == null) ? DEFAULT_LOG : this.log;
	}

	@Override
	public void setLog(Log log) {
		this.log = log;
	}

	/**
	 * The logger name used by the logger. Loggers with same name will log to the same destination.
	 * 
	 * @param name
	 *            Logger name
	 */
	public void setLogname(String name) {
		this.log = LogFactory.getLog(name);
	}

	/**
	 * Format message for log.
	 * 
	 * @param message
	 *            message
	 * @param attributes
	 *            attribute
	 * @return formatted message
	 */
	private String formatMessage(Object message, Object... attributes) {
		StringBuffer builder = new StringBuffer();
		builder.append(message);
		for (Object attribute : attributes) {
			builder.append(", ").append(attribute);
		}

		return builder.toString();
	}

}
