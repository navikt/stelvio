package no.stelvio.common.codestable;

import no.stelvio.common.error.SystemUnrecoverableException;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public abstract class CodesTableException extends SystemUnrecoverableException {
    protected CodesTableException(Object... templateArguments) {
        super(templateArguments);
    }

    protected CodesTableException(Throwable cause, Object... templateArguments) {
        super(cause, templateArguments);
    }

    protected CodesTableException(ExceptionToCopyHolder holder) {
        super(holder);
    }
}
