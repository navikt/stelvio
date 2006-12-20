package no.stelvio.common.error.strategy.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.error.ErrorDefinition;
import no.stelvio.common.error.ErrorResolver;
import no.stelvio.common.error.message.Extractor;
import no.stelvio.common.error.message.support.FromDatabaseExtractor;
import no.stelvio.common.error.message.support.FromExceptionExtractor;
import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo should we log to specific logs? Like ENTERPRISE_LOG / SYSTEM_LOG as were done in previous version ->
 * use a log resolver maybe? Share with event logging
 */
public class LoggerExceptionHandlerStrategy implements ExceptionHandlerStrategy {
    // TODO probably this need to be better
    // Could make a LoggerFactoryBean (check if not exist in Spring) that could be put into constructor)
    private static final Log log = LogFactory.getLog(LoggerExceptionHandlerStrategy.class);

    /**
     * <code>Extractor</code> that extracts message from the exception is used if nothing is specified.
     *
     * @todo better javadoc
     * @see FromDatabaseExtractor
     */
    private Extractor extractor = new FromExceptionExtractor();
    private ErrorResolver errorResolver;

    /**
     * Logs the exception.
     * 
     * {@inheritDoc}
     * @todo javadoc
     * @todo what about the properties from StelvioException like errorId, userId, etc?
     */
    public <T extends Throwable> T handleException(T throwable) {
        String message = extractor.messageFrom(throwable);
        ErrorDefinition error = errorResolver.resolve(throwable);

        switch (error.getSeverity()) {
            case FATAL:
                log.fatal(message);
                break;

            case ERROR:
                log.error(message);
                break;

            case WARN:
                log.warn(message);
                break;
        }

        return throwable;
    }

    public void setExtractor(Extractor extractor) {
        this.extractor = extractor;
    }

    public void setErrorResolver(ErrorResolver errorResolver) {
        this.errorResolver = errorResolver;
    }
}
