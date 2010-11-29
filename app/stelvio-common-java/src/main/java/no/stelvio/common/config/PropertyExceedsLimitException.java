package no.stelvio.common.config;

/**
 * Exception used when a property in a component has been configured with values that are not within the allowable limits. These
 * limits are defined by the components' business rules, and the components validation method throws this exception.
 * 
 * @author person983601e0e117 (Accenture)
 */
public class PropertyExceedsLimitException extends ConfigurationException {

	private static final long serialVersionUID = -7112686645249634415L;

	private Object minValue;

	private Object maxValue;

	private String propertyName;

	/**
	 * Constructs a <code>PropertyExceedsLimitException</code> with message and property name.
	 * 
	 * @param message
	 *            - the exception message.
	 * @param propertyName
	 *            - the name of property that was misconfigured.
	 */
	public PropertyExceedsLimitException(String message, String propertyName) {
		super(message);
		this.propertyName = propertyName;
	}

	/**
	 * Constructs a <code>PropertyExceedsLimitException</code> message, property name, minimum value and/or maximum value..
	 * 
	 * @param message
	 *            - the exception message.
	 * @param propertyName
	 *            - the name of property that was misconfigured.
	 * @param minValue
	 *            - the the minimum allowable value of property.
	 * @param maxValue
	 *            - the maximum allowable value of property.
	 */
	public PropertyExceedsLimitException(String message, String propertyName, Object minValue, Object maxValue) {
		this(message, propertyName, minValue, maxValue, null);
	}

	/**
	 * Constructs a <code>PropertyExceedsLimitException</code> with minimum value, maximum value, property name, message and
	 * cause.
	 * 
	 * @param message
	 *            - the exception message.
	 * @param propertyName
	 *            - the name of property that was misconfigured.
	 * @param minValue
	 *            - the the minimum allowable value of property.
	 * @param maxValue
	 *            - the maximum allowable value of property.
	 * @param cause
	 *            - the throwable that caused the exception to be raised.
	 */
	public PropertyExceedsLimitException(String message, String propertyName, Object minValue, Object maxValue, 
			Throwable cause) {
		super(message, cause);
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.propertyName = propertyName;
	}

	/**
	 * Get max allowed value.
	 * 
	 * @return maxValue
	 */
	public Object getMaxValue() {
		return maxValue;
	}

	/**
	 * Get min allowed value.
	 * 
	 * @return minValue
	 */
	public Object getMinValue() {
		return minValue;
	}

	/**
	 * Get property name of misconfigured property.
	 * 
	 * @return propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

}