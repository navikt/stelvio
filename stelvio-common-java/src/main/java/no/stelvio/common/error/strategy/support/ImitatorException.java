package no.stelvio.common.error.strategy.support;

/**
 * RuntimeException that imitates another exception, that is, it looks like the other when printing it out, but doesn't
 * include any causes. This will be used by {@link MorpherExceptionHandlerStrategy} to enable rethrowing an exception
 * onto other layers without forcing these to include exceptions included in causes in their classpath.
 *
 * This will fill in the stack trace which is quite expensive, but this need to be performance tested.
 * If logging is done too, the stack trace will be filled in anyway (see over)
 * should it extend SystemUnrecoverableException?
 *  
 * @author personf8e9850ed756
 * @see MorpherExceptionHandlerStrategy
 */
class ImitatorException extends RuntimeException {
	private String throwableToImitateClassName;

	/**
	 * @param throwableToImitate exception that should be imitated.
	 */
	public ImitatorException(Throwable throwableToImitate) {
		super(throwableToImitate.getMessage());

		throwableToImitateClassName = throwableToImitate.getClass().getName();
		StackTraceElement[] stackTraceElements = throwableToImitate.getStackTrace().clone();
		setStackTrace(stackTraceElements);
	}

	/**
	 * Returns a string representation of the object.
	 * <p>
	 * This will be a string consisting of the name of the throwable class which this instance is imitating, the string
	 * `<code>(imitated)</code>', and the localized message of the throwable this instance is imitating.
	 * <p>
	 * In other words, this method returns a string equal to the
	 * value of:
	 * <blockquote> <pre>
	 * imitatedThrowable.getClass().getName() + ' (imitated): ' + imitatedThrowable.getLocalizedMessage()
	 * </pre></blockquote>
	 *
	 * @return a string representation of the object.
	 */
	public String toString() {
		String name = throwableToImitateClassName + " (imitated)";
		String message = getLocalizedMessage();

		return (message != null) ? (name + ": " + message) : name;
	}
}
