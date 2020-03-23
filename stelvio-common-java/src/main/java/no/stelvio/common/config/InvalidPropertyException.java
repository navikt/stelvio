package no.stelvio.common.config;

/**
 * 
 * Exception thrown if one or more properties have invalid values.
 * 
 */
public class InvalidPropertyException extends ConfigurationException {

	private static final long serialVersionUID = -1223318644736665109L;

	private Object[] invalidProperties;

	/**
	 * Constructs a <code>InvalidPropertyException</code> with invalid properties, message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param invalidProperties -
	 *            the invalid properties.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public InvalidPropertyException(String message, Object[] invalidProperties, Throwable cause) {
		super(message, cause);
		this.invalidProperties = invalidProperties.clone();
	}

	/**
	 * Constructs a <code>InvalidPropertyException</code> with invalid properties and message.
	 * 
	 * @param invalidProperties -
	 *            the invalid properties.
	 * @param message -
	 *            the exception message.
	 */
	public InvalidPropertyException(String message, Object[] invalidProperties) {
		super(message);
		this.invalidProperties = invalidProperties.clone();
	}

	/**
	 * Get invalid properties.
	 * 
	 * @return invalid properties
	 */
	public Object[] getInvalidProperties() {
		return invalidProperties;
	}

}