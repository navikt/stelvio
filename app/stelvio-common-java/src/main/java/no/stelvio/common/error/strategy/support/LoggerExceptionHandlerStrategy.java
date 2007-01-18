package no.stelvio.common.error.strategy.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.error.message.Extractor;
import no.stelvio.common.error.message.support.FromDatabaseExtractor;
import no.stelvio.common.error.message.support.FromExceptionExtractor;
import no.stelvio.common.error.resolver.ErrorDefinitionResolver;
import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;
import no.stelvio.common.error.support.ErrorDefinition;

/**
 * @author personf8e9850ed756
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
     * @see FromDatabaseExtractor
     */
    private Extractor extractor = new FromExceptionExtractor();
    private ErrorDefinitionResolver errorDefinitionResolver;

    /**
     * Logs the exception.
     * 
     * {@inheritDoc}
     * @todo what about the properties from StelvioException like errorId, userId, etc?
     */
    public <T extends Throwable> T handleException(T throwable) {
        String message = extractor.messageFor(throwable);
        ErrorDefinition error = errorDefinitionResolver.resolve(throwable);

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

    public void setErrorResolver(ErrorDefinitionResolver errorDefinitionResolver) {
        this.errorDefinitionResolver = errorDefinitionResolver;
    }
}
