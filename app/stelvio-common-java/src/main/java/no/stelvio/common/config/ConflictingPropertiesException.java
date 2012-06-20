package no.stelvio.common.config;

/**
 * Thrown if two or more properties set up by configuration are in conflict with each other.
 * 
 * @author person983601e0e117, Accenture
 */
public class ConflictingPropertiesException extends ConfigurationException {

	private static final long serialVersionUID = 6905655613431964263L;

	private Object[] conflictingProperties;

	/**
	 * Constructs a <code>ConflictingPropertiesException</code> with conflicting properties, message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param conflictingProperties -
	 *            the conflicting properties.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public ConflictingPropertiesException(String message, Object[] conflictingProperties, Throwable cause) {
		super(message, cause);
		this.conflictingProperties = conflictingProperties.clone();
	}

	/**
	 * Constructs a <code>ConflictingPropertiesException</code> with conflicting properties and message.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param conflictingProperties -
	 *            the conflicting properties.
	 */
	public ConflictingPropertiesException(String message, Object[] conflictingProperties) {
		super(message);
		this.conflictingProperties = conflictingProperties.clone();
	}

	/**
	 * Get conflicting properties.
	 * 
	 * @return conflicting properties
	 */
	public Object[] getConflictingProperties() {
		return conflictingProperties;
	}

}