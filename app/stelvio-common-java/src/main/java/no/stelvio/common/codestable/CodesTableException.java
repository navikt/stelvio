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

    public CodesTableException() {
    }

    public CodesTableException(Exception ex) {
        // TODO: implement body
    }

    public Object copy() {
        return null;  // TODO: implement body
    }
}
