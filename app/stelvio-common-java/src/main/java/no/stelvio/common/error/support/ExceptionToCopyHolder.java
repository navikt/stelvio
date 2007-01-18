package no.stelvio.common.error.support;

import no.stelvio.common.error.StelvioException;

/**
 * A helper class for holding an exception when creating a new instance of the same exception.
 * 
 * @author personf8e9850ed756
 * @deprecated is not needed anymore; the cause is now cleared directly through reflection without having to create
 * a new instance of the exception.
 */
public class ExceptionToCopyHolder<T extends StelvioException> {
    private final T stelvioException;

    public ExceptionToCopyHolder(T exception) {
        this.stelvioException = exception;
    }

    public T value() {
        return stelvioException;
    }
}
