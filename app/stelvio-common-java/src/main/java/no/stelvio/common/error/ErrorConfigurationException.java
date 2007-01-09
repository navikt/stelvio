package no.stelvio.common.error;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class ErrorConfigurationException extends ErrorHandlingException {
    private Class<? extends Throwable> clazz;
    private int argsInError;
    private int argsInException;

    public ErrorConfigurationException(Class<? extends Throwable> clazz, int argsInError, int argsInException) {
        super(clazz, argsInError, argsInException);
        this.clazz = clazz;
        this.argsInError = argsInError;
        this.argsInException = argsInException;
    }

    protected String messageTemplate(final int numArgs) {
        return "Configuration for error {0} is not defined correctly; {1} args in definition, but {2} args in exception";
    }
}
