package no.stelvio.dto.error.strategy.support;

/**
 * Imitates a dto exception.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public class ImitatorDtoException extends RuntimeException {

	private static final long serialVersionUID = -2572526833326302529L;

	private String throwableToImitateClassName;

	/**
	 * @param throwableToImitate
	 *            exception that should be imitated.
	 */
	public ImitatorDtoException(Throwable throwableToImitate) {
		super(throwableToImitate.getMessage());

		throwableToImitateClassName = throwableToImitate.getClass().getName();
		StackTraceElement[] stackTraceElements = throwableToImitate.getStackTrace().clone();
		setStackTrace(stackTraceElements);
	}

	/**
	 * Returns a string representation of the object.
	 * <p>
	 * This will be a string consisting of the name of the throwable class which this instance is imitating, the string `
	 * <code>(imitated)</code>', and the localized message of the throwable this instance is imitating.
	 * <p>
	 * In other words, this method returns a string equal to the value of: <blockquote>
	 * 
	 * <pre>
	 * imitatedThrowable.getClass().getName() + ' (imitated): ' + imitatedThrowable.getLocalizedMessage()
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @return a string representation of the object.
	 */
	public String toString() {
		String name = throwableToImitateClassName + " (imitated)";
		String message = getLocalizedMessage();

		return (message != null) ? (name + ": " + message) : name;
	}

}
