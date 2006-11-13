package no.stelvio.common.util;

import java.util.Arrays;

import no.stelvio.common.error.SystemException;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class ReflectionException extends SystemException {
    public ReflectionException(Exception cause, String... name) {
        super(cause, Arrays.asList(name));
    }

    protected String getMessageTemplate() {
        return "Problems doing reflection with the following arguments: {0}";
    }
}
