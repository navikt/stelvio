package no.stelvio.common.error;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Thrown to indicate that an unrecoverable system exception has occured.
 *
 * @author personf8e9850ed756
 * @todo better javadoc
 */
public abstract class SystemUnrecoverableException extends UnrecoverableException {

    /**
     * {@inheritDoc}
     */
    protected SystemUnrecoverableException(Object... templateArguments) {
        super(templateArguments);
    }

    /**
     * {@inheritDoc}
     */
    protected SystemUnrecoverableException(Throwable cause, Object... templateArguments) {
        super(cause, templateArguments);
    }

    /**
     * {@inheritDoc}
     */
    protected SystemUnrecoverableException(ExceptionToCopyHolder holder) {
        super(holder);
    }
}
