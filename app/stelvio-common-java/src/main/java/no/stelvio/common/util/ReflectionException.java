package no.stelvio.common.util;

import no.stelvio.common.error.UnrecoverableException;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class ReflectionException extends UnrecoverableException {
    public ReflectionException(Exception cause, String... name) {
        super(cause);
    }

    protected String messageTemplate() {
        return "Problems doing reflection with the following arguments: {0}";
    }
}
