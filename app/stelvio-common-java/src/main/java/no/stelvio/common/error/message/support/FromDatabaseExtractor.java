package no.stelvio.common.error.message.support;

import java.text.MessageFormat;

import no.stelvio.common.error.StelvioException;
import no.stelvio.common.error.message.Extractor;
import no.stelvio.common.error.resolver.ErrorDefinitionResolver;
import no.stelvio.common.error.support.ErrorDefinition;

/**
 * Finds the message associated with the given exception by using the database provided list of <code>ErrorDefinition</code>s.
 * If the exception is not found in the list, use a fallback <code>Extractor</code>.
 *
 * @author personf8e9850ed756
 * @see ErrorDefinition
 * @see Extractor
 * @todo better javadoc
 */
public class FromDatabaseExtractor implements Extractor {
    private ErrorDefinitionResolver errorDefinitionResolver;
    /**
     * <code>Extractor</code> that extracts message directly from the exception is used if nothing is specified.
     *
     * @todo better javadoc
     * @see FromExceptionExtractor
     */
    private Extractor fallback = new FromExceptionExtractor();

    /**
     *
     * @param throwable
     * @return
     * @todo now this supports having non-stelvio exceptions in the database; is this likely?
     * Otherwise we could just jump directly onto fallback if a non-stelvio exception. 
     */
    public String messageFor(Throwable throwable) {
        ErrorDefinition error = errorDefinitionResolver.resolve(throwable);

        if (null == error) {
            return fallback.messageFor(throwable);
        } else {
            if (throwable instanceof StelvioException) {
                Object[] arguments = ((StelvioException) throwable).getTemplateArguments();

                return MessageFormat.format(error.getMessage(), arguments);
            } else {
                // TODO put message from exception into template 
                return error.getMessage();
            }
        }
    }

    public void setErrorResolver(ErrorDefinitionResolver errorDefinitionResolver) {
        this.errorDefinitionResolver = errorDefinitionResolver;
    }

    public void setFallback(Extractor fallback) {
        this.fallback = fallback;
    }
}
