package no.stelvio.common.error;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class ErrorNotFoundException extends ErrorHandlingException {
    private Class<? extends Throwable> clazz;

    /**
     * Creates an instance of the exception for the specified class.
     *
     * @param clazz the class for which an error could not be found.
     * @todo is it correct with <? extends Throwable>? If T should be used, it must probably be specified on the class.
     */
    public ErrorNotFoundException(Class<? extends Throwable> clazz) {
        this(null, clazz);
    }

    public ErrorNotFoundException(Throwable cause, Class<? extends Throwable> clazz) {
        super(cause, clazz);
        this.clazz = clazz;
    }

    protected ErrorNotFoundException(ExceptionToCopyHolder<ErrorNotFoundException> holder) {
        super(holder);
        this.clazz = holder.value().clazz;
    }

    protected String messageTemplate() {
        return "Could not find error for {0}";
    }
}
