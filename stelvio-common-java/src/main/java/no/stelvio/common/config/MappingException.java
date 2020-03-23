package no.stelvio.common.config;

import no.stelvio.common.error.FunctionalUnrecoverableException;

/**
 * Thrown when a mapping fails.
 * 
 */
public class MappingException extends FunctionalUnrecoverableException {

	private static final long serialVersionUID = 3265819057144018850L;

	private Class sourceClass;

	private Class destClass;

	/**
	 * Constructs a <code>MappingException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public MappingException(String message) {
		super(message);
	}

	/**
	 * Constructs a <code>MappingException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public MappingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>MappingException</code> with source class, destination class and message.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param sourceClass -
	 *            the mapping source class.
	 * @param destClass -
	 *            the destination class.
	 */
	public MappingException(String message, Class sourceClass, Class destClass) {
		super(message);
		this.sourceClass = sourceClass;
		this.destClass = destClass;
	}

	/**
	 * Constructs a <code>MappingException</code> with source class, destination class, message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param sourceClass -
	 *            the mapping source class.
	 * @param destClass -
	 *            the destination class.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public MappingException(String message, Class sourceClass, Class destClass, Throwable cause) {
		super(message, cause);
		this.sourceClass = sourceClass;
		this.destClass = destClass;
	}

	/**
	 * Get destination class.
	 * 
	 * @return class
	 */
	public Class getDestClass() {
		return destClass;
	}

	/**
	 * Get source class.
	 * 
	 * @return class
	 */
	public Class getSourceClass() {
		return sourceClass;
	}

}
