package no.stelvio.common.error;

/**
 * Thrown to indicate that a method has been passed an invalid/illegal or inappropriate argument.
 *  
 * @author personf8e9850ed756
 */
public class InvalidArgumentException extends SystemUnrecoverableException {
	private static final long serialVersionUID = 123345612346L;

	protected InvalidArgumentException(String argumentName, Object argument) {
        super(argumentName, argument);
    }

	@Override
	protected String messageTemplate(int numArgs) {
		return "Invalid argument specified ({0} --> {1})";
	}
}
