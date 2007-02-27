package no.stelvio.common.codestable.support;

import no.stelvio.common.error.SystemUnrecoverableException;

/**
 * CodesTableException
 * @author personf8e9850ed756
 * @todo write javadoc
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
