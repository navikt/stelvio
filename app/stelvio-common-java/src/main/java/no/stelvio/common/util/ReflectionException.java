package no.stelvio.common.util;

import no.stelvio.common.error.SystemUnrecoverableException;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class ReflectionException extends SystemUnrecoverableException {
    public ReflectionException(Exception cause, String... name) {
        super(cause, (Object)name);
    }

    protected String messageTemplate() {
        return "Problems doing reflection with the following arguments: {0}";
    }
}
