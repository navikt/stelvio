package no.stelvio.common.error.strategy.support;

import java.text.MessageFormat;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.error.Err;
import no.stelvio.common.error.RecoverableException;
import no.stelvio.common.error.Severity;
import no.stelvio.common.error.StelvioException;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;
import no.stelvio.common.util.MessageFormatter;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class LoggerExceptionHandlerStrategy implements ExceptionHandlerStrategy {
    // The name of the logs to send messages to
    private static final String ENTERPRISE_LOG = "ENTERPRISE_LOG";
    private static final String SYSTEM_LOG = "SYSTEM_LOG";

    // The logs
    private Log enterpriseLog;
    private Log systemLog;

    // The cache of error configurations
    private Map errorMap;

    // The message formatter
    private MessageFormatter messageFormatter;

    // Necessary internal state to avoid exceptions to occur inside the error handler
    private boolean isInitialized = false;

    /**
     * Constructs a default LogHandlerImpl using the following log names:
     * <ul>
     * 	<li> ENTERPRISE_LOG for logging to the global enterprise production log</li>
     * 	<li> SYSTEM_LOG for logging to the local system production log</li>
     * </ul>
     */
    public LoggerExceptionHandlerStrategy() {
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
//            ServiceResponse response = delegate.execute(new ServiceRequest("InitErrorHandling"));
//            errorMap = (Map) response.getData("ErrorMap");
            isInitialized = true;
        } catch (RuntimeException re) {
//            throw new SystemException(re);
        }
    }

    /**
     * Handles instances of ApplicationExceptions and SystemExceptions, other instances are wrapped within a SystemException
     * and then handled.
     *
     * {@inheritDoc}
     */
    public Throwable handleError(Throwable t) {
/*
        if (!isInitialized) {
            return getDefaultHandler().handleError(t);
        }
*/

        if (t instanceof RecoverableException || t instanceof SystemException) {
            return (Throwable) handleInternal((StelvioException) t);
        } else {
            return new RuntimeException("TODO: fix me"); //TODO use new way of doing this
        }
    }

    /**
     * If the throwable is of type LoggableException the error code and getTemplateArguments will be used to lookup and format the message,
     * otherwise the result of <code>Throwable.getLocalizedMessage()</code> is returned.
     *
     * @param t the <code>Throwable</code> to extract message from.
     * @return the throwable's message.
     * @see Throwable#getLocalizedMessage()
     */
    public String getMessage(Throwable t) {
/*
        if (!isInitialized) {
            return getDefaultHandler().getMessage(t);
        }
*/

        if (t instanceof StelvioException) {
            StelvioException le = (StelvioException) t;
            return getMessage(1/*le.getErrorCode()*/, le.getTemplateArguments());
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
    private StelvioException handleInternal(StelvioException le) {
        // Log the error only once
        if (!le.isLogged()) {
            logInternal(le);
            le.setLogged();
        }

//        return (LoggableException) le.copy();
        // TODO look into this
        return le;
    }

    /**
     * Logs the exception.
     *
     * @param le the exception.
     */
    private void logInternal(StelvioException le) {
        Severity severity = getSeverity(1/*le.getErrorCode()*/);
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
    private String getEnterpriseLogMessage(StelvioException le) {
        Object[] params =
            new Object[] {
                le.getUserId(),
                getSeverity(1/*le.getErrorCode()*/),
                le.getScreenId(),
                le.getProcessId(),
                getMessage(1/*le.getErrorCode()*/, le.getTemplateArguments())};
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
    String getSystemLogMessage(StelvioException le) {
        return "ErrCode=" + 1/*le.getErrorCode()*/ + ",ErrId=" + le.getErrorId() +
                ",Message=" + getMessage(1/*le.getErrorCode()*/, le.getTemplateArguments());
    }

    /**
     * Returns the error message formatted for displaying to user.
     *
     * @param code	the code representing the error type.
     * @param arguments list of details to be included in the error message.
     * @return the error message.
     */
    private String getMessage(int code, Object[] arguments) {
        Err error = (Err) errorMap.get(code); // TODO: not the correct approach in new version

        if (null == error) {
            // The error is not configured
            error = (Err) errorMap.get(0); // TODO: not the correct approach in new version

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
                arguments = new Object[]{code};
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
    private Severity getSeverity(int code) {
        Err error = (Err) errorMap.get(code); // TODO: not the correct approach in new version

        return null == error ? Severity.ERROR : error.getSeverity();
    }

    /**
     * Get the default error handler implementation.
     *
     * @return the default Handler
     * @todo should not have a default handler here?? In facade instead
     */
/*
    private Handler getDefaultHandler() {
        Handler defaultHandler = new DefaultHandlerImpl();
        defaultHandler.init();

        return defaultHandler;
    }
*/

    /**
     * Assigns a message formatter to formatt messages.
     *
     * @param formatter the message formatter.
     */
    public void setMessageFormatter(MessageFormatter formatter) {
        messageFormatter = formatter;
    }
}
