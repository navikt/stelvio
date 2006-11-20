package no.stelvio.common.codestable;

import no.stelvio.common.error.UnrecoverableException;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo make more specific exceptions
 */
public class CodesTableException extends UnrecoverableException {
    public CodesTableException(String message) {
        super();
    }

    protected String messageTemplate() {
        return "Problems with handling codes table: {0}";
    }
}
