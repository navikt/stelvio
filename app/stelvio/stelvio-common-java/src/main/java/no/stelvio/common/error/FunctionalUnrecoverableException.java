package no.stelvio.common.error;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Thrown to indicate that an unrecoverable functional exception has occured.
 * <p/>
 * Application code will use this for unexpected error conditions in functional logic, for example if input to a method
 * is not correct because of a programmer error.
 *
 * @author personf8e9850ed756
 * @todo better javadoc
 */
public abstract class FunctionalUnrecoverableException extends UnrecoverableException {

    /**
     * {@inheritDoc}
     */
    protected FunctionalUnrecoverableException(Object... templateArguments) {
        super(templateArguments);
    }

    /**
     * {@inheritDoc}
     */
    protected FunctionalUnrecoverableException(Throwable cause, Object... templateArguments) {
        super(cause, templateArguments);
    }

    /**
     * {@inheritDoc}
     */
    protected FunctionalUnrecoverableException(ExceptionToCopyHolder holder) {
        super(holder);
    }
}
