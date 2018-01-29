package no.stelvio.common.error.strategy.support;

import java.util.HashMap;
import java.util.Map;

import no.stelvio.common.error.message.Extractor;
import no.stelvio.common.error.message.support.FromDatabaseExtractor;
import no.stelvio.common.error.message.support.FromExceptionExtractor;
import no.stelvio.common.error.resolver.ErrorDefinitionResolver;
import no.stelvio.common.error.support.ErrorDefinition;
import no.stelvio.common.error.support.Severity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ExceptionHandlerStrategy that logs expceptions. The name of the Log may be specified by the
 * <code>{@link #setLogName(String)}</code>
 * 
 * should we use internalLogger to specific logs? Like ENTERPRISE_LOG / SYSTEM_LOG as were done in previous version -> use a
 *       internalLogger resolver maybe? Share with event logging
 * 
 * @author personf8e9850ed756
 * @author person983601e0e117
 * 
 */
public class LoggerExceptionHandlerStrategy extends AbstractOrderedExceptionHandlerStrategy {

	/** Internal logger, used by this component. */
	private static Log internalLogger = LogFactory.getLog(LoggerExceptionHandlerStrategy.class);

	/** Maps of logs used to log the exceptions passed to {@link #handleException(Throwable)}. */
	private static Map<String, Log> loggers = new HashMap<String, Log>();

	/**
	 * Holds the default severity, used to set internalLogger level for exceptions that don't have a specified internalLogger
	 * level in the database.
	 */
	private Severity defaultSeverity = Severity.ERROR;

	/**
	 * The internalLogger name to be used. LoggerExceptionStrategy.class will be used if no name is specified
	 */
	private String logName = null;

	/**
	 * <code>Extractor</code> that extracts message from the exception is used if nothing is specified.
	 * 
	 * @see FromDatabaseExtractor
	 */
	private Extractor extractor = new FromExceptionExtractor();

	private ErrorDefinitionResolver errorDefinitionResolver;

	/**
	 * Logs the exception. For Stelvio exceptions, it will only be logged if that is not done already.
	 * 
	 * what about the properties from StelvioException like errorId, userId, etc?
	 * 
	 * {@inheritDoc}
	 */
	public <T extends Throwable> T handleException(T throwable) {
		logMessage(throwable);
		return throwable;
	}

	/**
	 * Does the actual logging by extracting a message for the exception and logging at the correct level defined for the
	 * exception.
	 * 
	 * Uses internalLogger level specified by the {@link #setDefaultSeverity(Severity)} property if internalLogger level cannot
	 * be resolved by the injected {@link ErrorDefinitionResolver}
	 * 
	 * @param <T>
	 *            a Throwable type variable
	 * @param throwable
	 *            the exception to internalLogger.
	 */
	private <T extends Throwable> void logMessage(T throwable) {
		String message = extractor.messageFor(throwable);
		ErrorDefinition error = null;
		try {
			if (errorDefinitionResolver != null) {
				error = errorDefinitionResolver.resolve(throwable);
			} else {
				internalLogger.warn("ErrorDefinitionResolver hasn't been injected into LoggerExceptionHandlerStrategy."
						+ " this is considered as invalid configuration of LoggerExceptionHandlerStrategy.");
			}
		} catch (Exception e) {
			internalLogger.error("Exception occured during resolve of throwable", e);
		}
		Severity severity = defaultSeverity;

		// If Log level not defined for a StelvioException, internalLogger a warning
		if ((error != null && error.getSeverity() != null)) {
			severity = error.getSeverity();
		}

		Log exceptionLogger = getLogger(throwable);

		switch (severity) {
		case FATAL:
			exceptionLogger.fatal(message, throwable);
			break;

		case ERROR:
			exceptionLogger.error(message, throwable);
			break;

		case WARN:
			exceptionLogger.warn(message, throwable);
			break;
		case INFO:
			exceptionLogger.info(message, throwable);
			break;
		case DEBUG:
			exceptionLogger.debug(message, throwable);
			break;
		default:
			break;
		}
	}

	/**
	 * Sets the extractor used to get an Error message used for logging for a specified Throwable.
	 * 
	 * @param extractor
	 *            an extractor
	 * @see Extractor
	 */
	public void setExtractor(Extractor extractor) {
		this.extractor = extractor;
	}

	/**
	 * Sets the ErrorDefinitionResolver, used to resolve a ErrorDefinition from a Throwable.
	 * 
	 * @param errorDefinitionResolver
	 *            an error definition resolver
	 * @see ErrorDefinitionResolver
	 */
	public void setErrorResolver(ErrorDefinitionResolver errorDefinitionResolver) {
		this.errorDefinitionResolver = errorDefinitionResolver;
	}

	/**
	 * Sets the default severity used to idenfity internalLogger level for exceptions that aren't specified in the database or
	 * don't have a internalLogger level specified in the database.
	 * 
	 * <p>
	 * The property is set in the Spring configuration in the following way:
	 * 
	 * <pre class="code">
	 * &lt;property name=&quot;defaulSeverity&quot; value=&quot;WARN&quot;/&gt;
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param defaultSeverity
	 *            the default severity
	 * @see Severity for the available severity values
	 */
	public void setDefaultSeverity(Severity defaultSeverity) {
		this.defaultSeverity = defaultSeverity;
	}

	/**
	 * Sets the internalLogger name that will be used to identify the internalLogger created by this class. If no logName is
	 * specified, internalLogger will be initialized with LoggerExceptionHandlerStrategy.class as input parameter to
	 * LogFactory.getLog().
	 * 
	 * @param logName the logger's name.
	 */
	public void setLogName(String logName) {
		this.logName = logName;
	}

	/**
	 * Get logger.
	 * 
	 * @param t exception
	 * @return logger
	 */
	private Log getLogger(Throwable t) {

		try {
			String loggerName = (logName != null) ? logName : t.getStackTrace()[0].getClassName();

			if (!loggers.containsKey(loggerName)) {
				loggers.put(loggerName, LogFactory.getLog(loggerName));
			}
			return loggers.get(loggerName);
		} catch (Exception e) {
			// This shouldn't occur, but if it does a logger is still needed
			internalLogger.error("Error retrieving log for exception " + t.getClass().getName(), e);
			return internalLogger;
		}
	}

}
