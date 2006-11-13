package no.stelvio.common.codestable;

import no.stelvio.common.error.SystemException;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo make more specific exceptions
 */
public class CodesTableException extends SystemException {
    public CodesTableException(String message) {
        super(message);
    }

    protected String getMessageTemplate() {
        return "Problems with handling codes table: {0}";
    }
}
