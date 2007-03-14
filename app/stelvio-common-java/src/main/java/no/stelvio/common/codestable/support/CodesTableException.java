package no.stelvio.common.codestable.support;

import no.stelvio.common.error.SystemUnrecoverableException;

/**
 * Super class for exceptions thrown when handling codes tables.
 *
 * @author personf8e9850ed756
 * @version $Id$
 */
public abstract class CodesTableException extends SystemUnrecoverableException {
    protected CodesTableException(Object... templateArguments) {
        super(templateArguments);
    }

    protected CodesTableException(Throwable cause, Object... templateArguments) {
        super(cause, templateArguments);
    }
}
