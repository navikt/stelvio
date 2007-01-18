package no.stelvio.common.error.resolver;

import no.stelvio.common.error.ErrorHandlingException;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class ErrorDefinitionNotFoundException extends ErrorHandlingException {
    private Class<? extends Throwable> clazz;

    /**
     * Creates an instance of the exception for the specified class.
     *
     * @param clazz the class for which an error could not be found.
     * @todo is it correct with <? extends Throwable>? If T should be used, it must probably be specified on the class.
     */
    public ErrorDefinitionNotFoundException(Class<? extends Throwable> clazz) {
        this(null, clazz);
    }

    public ErrorDefinitionNotFoundException(Throwable cause, Class<? extends Throwable> clazz) {
        super(cause, clazz);
        this.clazz = clazz;
    }

    protected ErrorDefinitionNotFoundException(ExceptionToCopyHolder<ErrorDefinitionNotFoundException> holder) {
        super(holder);
        this.clazz = holder.value().clazz;
    }

    protected String messageTemplate(final int numArgs) {
        return "Could not find error for {0}";
    }
}
