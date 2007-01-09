package no.stelvio.common.error.support;

import no.stelvio.common.error.StelvioException;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
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
