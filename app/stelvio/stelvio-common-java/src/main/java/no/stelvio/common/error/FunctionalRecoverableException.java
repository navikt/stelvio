package no.stelvio.common.error;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Thrown to indicate that a recoverable functional exception has occured.
 * <p/>
 * Application code will use this for error conditions that ALWAYS should be handled.
 *
 * @author personf8e9850ed756
 * @todo better javadoc
 */
public abstract class FunctionalRecoverableException extends RecoverableException {

    /**
     * {@inheritDoc}
     */
    protected FunctionalRecoverableException(Object... templateArguments) {
        super(templateArguments);
    }

    /**
     * {@inheritDoc}
     */
    protected FunctionalRecoverableException(Throwable cause, Object... templateArguments) {
        super(cause, templateArguments);
    }

    /**
     * {@inheritDoc}
     */
    protected FunctionalRecoverableException(ExceptionToCopyHolder holder) {
        super(holder);
    }
}
