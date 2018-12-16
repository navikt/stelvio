package no.stelvio.common.error.logging.support;

import java.lang.reflect.Field;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.MDC;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import no.stelvio.common.error.ErrorCode;
import no.stelvio.common.error.FunctionalRecoverableException;
import no.stelvio.common.error.FunctionalUnrecoverableException;
import no.stelvio.common.error.RecoverableException;
import no.stelvio.common.error.SystemUnrecoverableException;
import no.stelvio.common.error.logging.ExceptionLogger;
import no.stelvio.common.error.support.Severity;
import no.stelvio.common.log.MDCLogger;
import no.stelvio.common.log.MdcConstants;
import no.stelvio.common.util.ReflectUtil;
import no.stelvio.common.util.ReflectionException;

/**
 * Default exceptionlogger implementation used to log exceptions based on the log4j log framework.
 * <p>
 * Default error logger, <code>no.stelvio.common.error.logging.ExceptionLogger</code> is used if no logger is set for the logger
 * instance.
 * </p>
 * <p>
 * The following algorithm will be used to resolve the loglevel:
 * </p>
 * <ul>
 * <li>If an ErrorCode exists it will see if a LogLevel is defined for this code in the</li>
 * <li>If above fails, checks if a loglevel is defined for the exception class</li>
 * <li>If above fails, checks if a loglevel is defined for one of the exception super classes</li>
 * <li>If above fails, checks if a default loglevel is defined in LogLevelMessageSource</li>
 * <li>If all fails, uses {@link #getFallbackLogLevel()}</li>
 * </ul>
 * 
 * @author person983601e0e117 (Accenture)
 * @author person6045563b8dec (Accenture)
 * @author person15754a4522e7 (ex-coder)
 */
public class DefaultExceptionLogger extends MDCLogger implements ExceptionLogger {

	// Loggers used to log, default log is used if no other log is injected.
	private static final Log DEFAULT_LOG = LogFactory.getLog(ExceptionLogger.class);

	private Log log;

	private MessageSource logLevelMessageSource;

	private static final String UNRESOLVED = "unresolved";

	/** Default level key. */
	public static final String DEFAULT_LEVEL_KEY = "DEFAULT";

	private Severity fallbackLogLevel = Severity.ERROR;

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

	@Override
	public void log(Throwable t) {
		if (isFunctionalException(t)) {
			logFunctional(t);
		} else {
			logTechnical(t);
		}
	}

	@Override
	public void log(String message, Throwable t) {
		if (isFunctionalException(t)) {
			logFunctional(message, t);
		} else {
			logTechnical(message, t);
		}
	}

	@Override
	public void logFunctional(Throwable t) {
		logFunctional("", t);
	}

	@Override
	public void logFunctional(String message, Throwable t) {
		MDC.put(MdcConstants.MDC_ERROR_TYPE, MdcConstants.MDC_ERROR_TYPE_FUNCTIONAL_VALUE);
		doLog(message, t);
	}

	@Override
	public void logTechnical(Throwable t) {
		logTechnical("", t);
	}

	@Override
	public void logTechnical(String message, Throwable t) {
		MDC.put(MdcConstants.MDC_ERROR_TYPE, MdcConstants.MDC_ERROR_TYPE_TECHNICAL_VALUE);
		doLog(message, t);
	}

	/**
	 * Logs the relevant throwable with the severity specified by the loglevel.properties or class default (see class level
	 * javadoc).
	 * 
	 * @param message
	 *            Appended to the log
	 * @param t
	 *            Throwable to log
	 */
	private void doLog(String message, Throwable t) {
		try {
			final Severity severity = resolveLogLevel(t);
			logMessage(message, t, severity);
		} catch (Throwable e) {
			System.err.println("Unable to log exception. Exception thrown while attempting to log :");
			System.err.println(message + " " + e.getMessage());
			e.printStackTrace(System.err);
			System.err.println("Exception that should have been logged:");
			System.err.println(t.getMessage());
			t.printStackTrace(System.err);
		}
	}

	/**
	 * 
	 * Will resolve the LogLevel for a given exception.
	 * <ul>
	 * <li>If an ErrorCode exists it will see if a LogLevel is defined for this code in the</li>
	 * <li>If above fails, checks if a loglevel is defined for the exception class</li>
	 * <li>If above fails, checks if a loglevel is defined for one of the exception super classes</li>
	 * <li>If above fails, checks if a default loglevel is defined in LogLevelMessageSource</li>
	 * <li>If all fails, uses {@link #getFallbackLogLevel()}</li>
	 * </ul>
	 * 
	 * @param ex
	 *            exception
	 * @return severity
	 */
	protected Severity resolveLogLevel(Throwable ex) {
		Severity logLevel = null;
		if (logLevelMessageSource != null) {

			// Resolve using error code
			if (ex instanceof SystemUnrecoverableException) {
				SystemUnrecoverableException systemUnrecoverableException = (SystemUnrecoverableException) ex;
				String errorCode = systemUnrecoverableException.getErrorCode() != null ? systemUnrecoverableException
						.getErrorCode().getErrorCode() : null;
				String resolvedLevel = findLogLevelInMessageSource(errorCode);
				logLevel = findSeverityForString(resolvedLevel);

				// Try to resolve for exception type
			}
			if (logLevel == null) {
				Class<?> exceptionClass = ex.getClass();
				String resolvedLevel = UNRESOLVED;
				do {
					resolvedLevel = findLogLevelInMessageSource(exceptionClass.getName());
					logLevel = findSeverityForString(resolvedLevel);
					exceptionClass = exceptionClass.getSuperclass();
				} while (logLevel == null && exceptionClass != null);

				// Try to resolve default defined in properties
			}
			if (logLevel == null) {
				String resolvedLevel = findLogLevelInMessageSource(DEFAULT_LEVEL_KEY);
				logLevel = findSeverityForString(resolvedLevel);
			}

			// Use fallback loglevel defined by logging component
		}
		if (logLevel == null) {
			logLevel = fallbackLogLevel;
		}
		return logLevel;
	}

	/**
	 * Sets the MessageSource that will be used to retrieve loglevels.
	 * 
	 * @param logLevelMessageSource
	 *            log level message source
	 * @see ReloadableResourceBundleMessageSource
	 */
	public void setLogLevelMessageSource(MessageSource logLevelMessageSource) {
		this.logLevelMessageSource = logLevelMessageSource;
	}

	/**
	 * Gets Loglevel used if everything else fails (ie: unable to resolve loglevel in resourcebundle).
	 * 
	 * @return severity
	 */
	public Severity getFallbackLogLevel() {
		return fallbackLogLevel;
	}

	/**
	 * Sets the Loglevel used if everything else fails (ie: unable to resolve loglevel in resourcebundle).
	 * 
	 * @param fallbackLogLevel
	 *            log level
	 */
	public void setFallbackLogLevel(Severity fallbackLogLevel) {
		this.fallbackLogLevel = fallbackLogLevel;
	}

	/**
	 * This method assembles the Log message that will be used in addition to the exception instance when the exception is
	 * logged.
	 * 
	 * @param message
	 *            message
	 * @param t
	 *            The exception to log
	 * @return log message
	 */
	protected String assembleLogMessage(String message, Throwable t) {
		StringBuffer sb = new StringBuffer();

		// Add message
		sb.append("Message: '");

		if (thereIsAnAdditionalMessage(message)) {
			sb.append(message).append(". ");
		}

		sb.append(t.getMessage()).append("'.");

		// Add error code
		if (isExceptionWithErrorCode(t)) {
			String errorCode = getErrorCodeFromException(t);
			sb.append(" ErrorCode=").append(errorCode).append(". ");
		}

		// Add field values
		appendFieldValues(t, sb);

		// Add templateArguments
		// Check if throwable is instance of exception
		// with the deprecated template arguments
		if (mayHaveTemplateArguments(t)) {
			Object[] templateArguments = getTemplateArgument(t);
			// Have the deprecated templateArguments been used by exception class?
			if (templateArguments != null && templateArguments.length != 0) {
				// Append the template arguments
				sb.append(" TemplateArguments: ");
				for (int i = 0; i < templateArguments.length; i++) {
					Object object = templateArguments[i];
					sb.append(object);
					sb.append(templateArguments.length == i + 1 ? "." : ", ");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Sets MDC properties from RequestContext (if it is set on RequestContextHolder).
	 * 
	 * @param t
	 *            Exception to be logged.
	 */
	protected void setMdcProperties(Throwable t) {
		setMdcProperties();
	}

	/**
	 * Resets the MDC properties set by {@link #setMdcProperties(Throwable)}.
	 * 
	 */
	@Override
	protected void resetMdcProperties() {
		super.resetMdcProperties();
		// Error and eror type are specific to error logger
		MDC.remove(MdcConstants.MDC_ERROR);
		MDC.remove(MdcConstants.MDC_ERROR_TYPE);
	}

	/**
	 * Logs an exception with the specified message, using the log level specified by the severity.
	 * 
	 * @param message
	 *            message
	 * @param t
	 *            exception
	 * @param severity
	 *            severity
	 */
	protected void logMessage(String message, Throwable t, final Severity severity) {
		switch (severity) {

		case FATAL:
			if (getLog().isFatalEnabled()) {
				setMdcProperties(t);
				getLog().fatal(assembleLogMessage(message, t), t);
				resetMdcProperties();
			}
			break;
		case ERROR:
			if (getLog().isErrorEnabled()) {
				setMdcProperties(t);
				getLog().error(assembleLogMessage(message, t), t);
				resetMdcProperties();
			}
			break;
		case WARN:
			if (getLog().isWarnEnabled()) {
				setMdcProperties(t);
				getLog().warn(assembleLogMessage(message, t), t);
				resetMdcProperties();
			}
			break;
		case INFO:
			if (getLog().isInfoEnabled()) {
				setMdcProperties(t);
				getLog().info(assembleLogMessage(message, t), t);
				resetMdcProperties();
			}
			break;
		case DEBUG:
			if (getLog().isDebugEnabled()) {
				setMdcProperties(t);
				getLog().debug(assembleLogMessage(message, t), t);
				resetMdcProperties();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Appends all fields defined by subclasses of one of the three StelvioExceptions: {@link FunctionalUnrecoverableException},
	 * {@link SystemUnrecoverableException} and {@link RecoverableException}.
	 * 
	 * @param t
	 *            exception
	 * @param sb
	 *            string
	 */
	protected void appendFieldValues(Throwable t, StringBuffer sb) {
		try {
			Class<?> declaringClass = t.getClass();
			boolean fieldAppended = false;
			while (shouldExtractProperties(declaringClass)) {

				Field[] fields = declaringClass.getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);

					// The exception is a subclass of a StelvioException -> Exception defined by App

					if (shouldFieldBeAppended(field)) {
						// Append "Properties" prefix for the first properties, comma for before the rest
						String prefix = (!fieldAppended ? (sb.toString().endsWith(" ") ? "Properties: " : " Properties: ")
								: ", ");
						sb.append(prefix);
						sb.append(field.getName()).append("=").append(getFormattedFieldValue(field.get(t)));
						fieldAppended = true;
					}
				}
				declaringClass = declaringClass.getSuperclass();
			}
			if (fieldAppended) {
				sb.append(".");
			}
		} catch (IllegalAccessException e) {
			// Do nothing, logging can be done without properties being logged
		}
	}

	/**
	 * Method that checks if this is a Stelvio exception type that defines an error code Also works as a hook method for other
	 * implementations.
	 * 
	 * @param t
	 *            exception
	 * @return true if it is a Stelvio exception that defines an error code
	 */
	protected boolean isExceptionWithErrorCode(Throwable t) {
		return (t instanceof SystemUnrecoverableException);
	}

	/**
	 * Gets the errorCode from this exception, if it passes the {@link #isExceptionWithErrorCode(Throwable)} test Also works as
	 * a hook method for other implementations.
	 * 
	 * @param t
	 *            exception
	 * @return error code
	 */
	protected String getErrorCodeFromException(Throwable t) {
		String errorCodeString = null;
		if (isExceptionWithErrorCode(t)) {
			try {
				ErrorCode errorCode = ReflectUtil.getPropertyFromClass(t, "errorCode");
				errorCodeString = errorCode == null ? "null" : errorCode.getErrorCode();
			} catch (ReflectionException e) {
				errorCodeString = null;
			}
		}
		return errorCodeString;
	}

	/**
	 * Only extract properties if subclass of Stelvio Exceptions Also works as a hook method for other implementations.
	 * 
	 * @param declaringClass
	 *            class
	 * @return true if properties should be extract
	 */
	protected boolean shouldExtractProperties(Class<?> declaringClass) {

		boolean isAStelvioExceptionSubclass = (SystemUnrecoverableException.class.isAssignableFrom(declaringClass)
				|| FunctionalUnrecoverableException.class.isAssignableFrom(declaringClass) || FunctionalRecoverableException.class
				.isAssignableFrom(declaringClass));

		return isAStelvioExceptionSubclass;
	}

	/**
	 * Method that decides whether a field should be appended to the log message or not.
	 * 
	 * @param field
	 *            field
	 * @return true if field is to be appended, otherwise false
	 */
	protected boolean shouldFieldBeAppended(Field field) {
		boolean shouldBeAppended = true;
		Class<?> declaringClass = field.getDeclaringClass();

		// Do not append properties that are declared by classes other than Stelvio sub classes
		if (!shouldExtractProperties(declaringClass)) {
			shouldBeAppended = false;

			// Do not append ErrorCode field
		} else if (field.getName().equalsIgnoreCase("errorcode") || field.getName().equalsIgnoreCase("serialVersionUID")) {
			shouldBeAppended = false;
		}
		return shouldBeAppended;
	}

	/**
	 * Checks whether the exception class may specify template arguments used in the deprecated exception framework. Also works
	 * as a hook method for other implementations.
	 * 
	 * @param t
	 *            exception
	 * @return true if mayHaveTemplateArguments
	 */
	protected boolean mayHaveTemplateArguments(Throwable t) {
		return t instanceof SystemUnrecoverableException || t instanceof FunctionalUnrecoverableException
				|| t instanceof RecoverableException;
	}

	/**
	 * Retrieves the field value to log. If the field to log is an array, the field must be treated differently in order to get
	 * the content of the error in the log, and not only the objects identificator.
	 * 
	 * @param field
	 *            the field value to log
	 * @return the field value to log
	 */
	private Object getFormattedFieldValue(Object field) {
		if (field != null && field.getClass().isArray()) {
			return ArrayUtils.toString(field, null);
		} else {
			return field;
		}
	}

	/**
	 * Converts a String into a Severity enum instance.
	 * 
	 * @param severityString
	 *            severity
	 * @return severity
	 */
	private Severity findSeverityForString(String severityString) {
		Severity severity = null;
		try {
			if (severityString != null && !severityString.equals(UNRESOLVED)) {
				severity = Severity.valueOf(severityString.toUpperCase());
			}
		} catch (IllegalArgumentException e) {
		}
		return severity;
	}

	/**
	 * Looks up the log level in a message source based on the key provided.
	 * 
	 * @param key
	 *            key
	 * @return loglevel as string
	 */
	private String findLogLevelInMessageSource(String key) {
		return logLevelMessageSource.getMessage(key, null, UNRESOLVED, null);
	}

	/**
	 * Checks if a throwable is functional.
	 * 
	 * @param t
	 *            Throwable
	 * @return true if the throwable is functional
	 */
	protected boolean isFunctionalException(Throwable t) {
		return (t instanceof FunctionalUnrecoverableException || t instanceof FunctionalRecoverableException);
	}

	/**
	 * Retrieves the template arguments from a Throwable that is a subclass of either a {@link SystemUnrecoverableException},
	 * {@link FunctionalUnrecoverableException} or a {@link RecoverableException}.
	 * 
	 * @param t
	 *            exception
	 * @return arguments
	 */
	private Object[] getTemplateArgument(Throwable t) {
		Class<?> clazz = t.getClass();
		TemplateArgumentFieldCallback callback = new TemplateArgumentFieldCallback();
		callback.throwableInstance = t;
		ReflectionUtils.doWithFields(clazz, callback, new TemplateArgumentFieldFilter());
		return callback.templateArguments;
	}

	/**
	 * Return true if there is an additional message.
	 * 
	 * @param message
	 *            message
	 * @return true if there is an additional message
	 */
	protected boolean thereIsAnAdditionalMessage(String message) {
		return (message != null && message.length() > 0);
	}

	/**
	 * FieldFilter used to filter out the templateArgument field when performing reflection.
	 * 
	 * @author person983601e0e117
	 */
	class TemplateArgumentFieldFilter implements FieldFilter {
		/**
		 * Returns true if field matches.
		 * 
		 * @param field
		 *            field
		 * @return true if match
		 */
		public boolean matches(Field field) {
			field.setAccessible(true);
			return field.getName().equalsIgnoreCase("templateArguments");
		}
	}

	/**
	 * Field callback for the template argument field. Executed on the template argument field
	 * 
	 * @author person983601e0e117 (Accenture)
	 * 
	 */
	class TemplateArgumentFieldCallback implements FieldCallback {

		private Object[] templateArguments = null;

		private Throwable throwableInstance;

		/**
		 * do with.
		 * 
		 * @param arg0
		 *            field
		 * @throws IllegalAccessException
		 *             illegal access
		 * @throws IllegalArgumentException
		 *             illegal argument
		 */
		public void doWith(Field arg0) throws IllegalArgumentException, IllegalAccessException {
			this.templateArguments = (Object[]) arg0.get(throwableInstance);
		}
	}
}
