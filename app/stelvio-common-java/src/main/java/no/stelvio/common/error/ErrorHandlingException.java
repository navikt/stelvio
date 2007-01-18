package no.stelvio.common.error;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo should not inherit from our hierarchy; use JDK's IllegalState or something 
 */
public abstract class ErrorHandlingException extends SystemUnrecoverableException {
    protected ErrorHandlingException(Object... templateArguments) {
        super(templateArguments);
    }

    protected ErrorHandlingException(Throwable cause, Object... templateArguments) {
        super(cause, templateArguments);
    }
}
