package no.stelvio.common.codestable;

import no.stelvio.common.error.SystemUnrecoverableException;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo should be abstract
 */
public class CodesTableException extends SystemUnrecoverableException {
    protected CodesTableException(Object... templateArguments) {
        super(templateArguments);
    }

    protected CodesTableException(Throwable cause, Object... templateArguments) {
        super(cause, templateArguments);
    }

    protected CodesTableException(ExceptionToCopyHolder holder) {
        super(holder);
    }

    protected String messageTemplate() {
        return "Problems with handling codes table: {0}";
    }
}
