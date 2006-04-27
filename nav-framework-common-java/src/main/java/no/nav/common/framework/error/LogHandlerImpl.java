package no.nav.common.framework.error;

import java.text.MessageFormat;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.nav.common.framework.FrameworkError;
import no.nav.common.framework.service.LocalService;
import no.nav.common.framework.service.ServiceFailedException;
import no.nav.common.framework.service.ServiceRequest;
import no.nav.common.framework.service.ServiceResponse;
import no.nav.common.framework.util.MessageFormatter;

/**
 * LogHandlerImpl is the preferred error handler when errors should be logged.
 * 
 * @author person7553f5959484
 * @version $Revision: 2767 $ $Author: skb2930 $ $Date: 2006-02-15 13:00:06 +0100 (Wed, 15 Feb 2006) $
 */
public final class LogHandlerImpl implements Handler {

	// The name of the logs to send messages to
	private static final String ENTERPRISE_LOG = "ENTERPRISE_LOG";
	private static final String SYSTEM_LOG = "SYSTEM_LOG";

	// The logs
	private Log enterpriseLog = null;
	private Log systemLog = null;

	// The cache of error configurations
	private Map errorMap = null;

	// The business delegate
	private LocalService delegate = null;

	// The message formatter
	private MessageFormatter messageFormatter = null;

	// Necessary internal state to avoid exceptions to occur inside the error handler
	private boolean isInitialized = false;

	/**
	 * Constructs a default LogHandlerImpl using the following log names:
	 * <ul>
	 * 	<li> ENTERPRISE_LOG for logging to the global enterprise production log</li>
	 * 	<li> SYSTEM_LOG for logging to the local system production log</li>
	 * </ul>
	 */
	public LogHandlerImpl() {
		enterpriseLog = LogFactory.getLog(ENTERPRISE_LOG);
		systemLog = LogFactory.getLog(SYSTEM_LOG);
	}

	/**
	 * Initializes the error handler by retrieving error configurations and caching them internally.
	 * 
	 * {@inheritDoc}
	 */
	public void init() {
		try {
			ServiceResponse response = delegate.execute(new ServiceRequest("InitErrorHandling"));
			errorMap = (Map) response.getData("ErrorMap");
			isInitialized = true;
		} catch (ServiceFailedException sfe) {
			// Let ErorHandler handle initialization failure
			throw new SystemException(FrameworkError.ERROR_HANDLING_INITIALIZATION_ERROR, sfe);
		} catch (RuntimeException re) {
			throw new SystemException(FrameworkError.ERROR_HANDLING_INITIALIZATION_ERROR, re);
		}
	}

	/**
	 * Handles instances of ApplicationExceptions and SystemExceptions, other instances are wrapped within a SystemException
	 * and then handled.
	 * 
	 * {@inheritDoc}
	 */
	public Throwable handleError(Throwable t) {
		if (!isInitialized) {
			return getDefaultHandler().handleError(t);
		}

		if (t instanceof ApplicationException || t instanceof SystemException) {
			return (Throwable) handleInternal((LoggableException) t);
		} else {
			return (Throwable) handleInternal(
					new SystemException(
							ErrorCode.UNSPECIFIED_ERROR, t, new String[]{t.getLocalizedMessage(), t.getClass().getName()}));
		}
	}

	/**
	 * If the throwable is of type LoggableException the error code and arguments will be used to lookup and format the message,
	 * otherwise the result of <code>Throwable.getLocalizedMessage()</code> is returned.
	 *
	 * @param t the <code>Throwable</code> to extract message from.
	 * @return the throwable's message.
	 * @see Throwable#getLocalizedMessage()
	 */
	public String getMessage(Throwable t) {
		if (!isInitialized) {
			return getDefaultHandler().getMessage(t);
		}

		if (t instanceof LoggableException) {
			LoggableException le = (LoggableException) t;
			return getMessage(le.getErrorCode(), le.getArguments());
		} else {
			if (null == t.getLocalizedMessage()) {
				return t.toString();
			} else {
				return t.getLocalizedMessage();
			}
		}
	}

	/**
	 * Handles instances of LoggableException, logs them once, then removes the cause.
	 * 
	 * @param le the exception to handle.
	 * @return the handled exception.
	 */
	private LoggableException handleInternal(LoggableException le) {
		// Log the error only once
		if (!le.isLogged()) {
			logInternal(le);
			le.setLogged();
		}

		return (LoggableException) le.copy();
	}

	/**
	 * Logs the exception.
	 * 
	 * @param le the exception.
	 */
	private void logInternal(LoggableException le) {
		Integer severity = getSeverity(le.getErrorCode());
		final Throwable throwable = (Throwable) le;

		if (throwable instanceof SystemException) {
			// Log SystemExceptions to both the global enterprise production log
			// and the local system production log
			if (Severity.FATAL.equals(severity)) {
				enterpriseLog.fatal(getEnterpriseLogMessage(le));
				systemLog.fatal(getSystemLogMessage(le), throwable);
			} else if (Severity.ERROR.equals(severity)) {
				enterpriseLog.error(getEnterpriseLogMessage(le));
				systemLog.error(getSystemLogMessage(le), throwable);
			} else {
				systemLog.warn(getSystemLogMessage(le), throwable);
			}
		} else {
			// Log ApplicationExceptions to the local system production log only
			if (Severity.FATAL.equals(severity)) {
				systemLog.fatal(getSystemLogMessage(le), throwable);
			} else if (Severity.ERROR.equals(severity)) {
				systemLog.error(getSystemLogMessage(le), throwable);
			} else {
				systemLog.warn(getSystemLogMessage(le), throwable);
			}
		}
	}

	/**
	 * Returns the error message formatted for logging to the enterprise log.
	 * 
	 * @param le the exception to handle.
	 * @return the formated message.
	 */
	private String getEnterpriseLogMessage(LoggableException le) {
		Object[] params =
			new Object[] {
				le.getUserId(),
				getSeverity(le.getErrorCode()),
				le.getScreenId(),
				le.getProcessId(),
				getMessage(le.getErrorCode(), le.getArguments())};
		return messageFormatter.formatMessage(params);
	}

	/**
	 * Returns the error message formatted for logging to the system log.
	 * </p>
	 * Is package private to enable unit testing.
	 *
	 * @param le the exception to handle.
	 * @return the formated message.
	 */
	String getSystemLogMessage(LoggableException le) {
		return "ErrCode=" + le.getErrorCode() + ",ErrId=" + le.getErrorId() +
		        ",Message=" + getMessage(le.getErrorCode(), le.getArguments());
	}

	/**
	 * Returns the error message formatted for displaying to user.
	 * 
	 * @param code	the code representing the error type.
	 * @param arguments list of details to be included in the error message.
	 * @return the error message.
	 */
	private String getMessage(int code, Object[] arguments) {
		ErrorConfig error = (ErrorConfig) errorMap.get(new Integer(code));

		if (null == error) {
			// The error is not configured
			error = (ErrorConfig) errorMap.get(new Integer(ErrorCode.UNCONFIGURED_ERROR.getCode()));

			if (null == error) {
				// The UNCONFIGURED_ERROR is not configured
				StringBuffer sb = new StringBuffer("En teknisk feil har oppstått. Feilkoden er ");
				sb.append(code).append(". Detaljer: ");

				// Use the original list of details that should have been included in the error message
				for (int arg = 0; arg < arguments.length; arg++) {
					sb.append(arguments[arg]);

					if (arg < arguments.length - 1) {
						sb.append(",");
					}
				}

				sb.append(".");

				return sb.toString();
			} else {
				// The UNCONFIGURED_ERROR has only one parameter: the error code
				arguments = new Object[]{new Integer(code)};
			}
		}

		return new MessageFormat(error.getMessage()).format(arguments);
	}

	/**
	 * Looks up the severity of error with specified code. If severity is not specified,
	 * Severity.WARN is returned.
	 * 
	 * @param code error type
	 * @return the error severity
	 */
	private Integer getSeverity(int code) {
		ErrorConfig error = (ErrorConfig) errorMap.get(new Integer(code));

		return null == error ? Severity.ERROR : error.getSeverity();
	}

	/**
	 * Get the default error handler implementation.
	 * 
	 * @return the default Handler
	 */
	private Handler getDefaultHandler() {
		Handler defaultHandler = new DefaultHandlerImpl();
		defaultHandler.init();

		return defaultHandler;
	}

	/**
	 * Assigns an implementation of a LocalService as the delegate.
	 * 
	 * @param service the local service
	 */
	public void setDelegate(LocalService service) {
		delegate = service;
	}

	/**
	 * Assigns a message formatter to formatt messages.
	 * 
	 * @param formatter the message formatter.
	 */
	public void setMessageFormatter(MessageFormatter formatter) {
		messageFormatter = formatter;
	}
}
