package no.stelvio.common.config;

import no.stelvio.common.error.strategy.support.RethrowExceptionHandlerStrategy;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

public class PropertyExceedsLimitException extends ConfigurationException {

	

	private static final long serialVersionUID = -7112686645249634415L;

    /**
	 * Constructs a copy of the specified PropertyExceedsLimitException without the cause.
     * <p>
     * Is used by the framework to make a copy for rethrowing without getting class path problems with the exception
     * classes that is part of the cause stack.
	 *
	 * @param holder
     * @see RethrowExceptionHandlerStrategy
     */
	public PropertyExceedsLimitException(ExceptionToCopyHolder holder) {
		super(holder);
	}

	/**
	 * Constructor that takes the property
	 * @param propertyName name of property that was misconfigured
	 */
	public PropertyExceedsLimitException(String propertyName) {
		super(propertyName);
	}	
	
	/**
	 * Constructor that takes the property that exceeded the limit as parameter
	 * @param throwable cause of this exception
	 * @param propertyName name of property that was exceeded limits
	 */
	public PropertyExceedsLimitException(Throwable cause, String propertyName) {
		super(cause, propertyName);
	}		
	
	/**
	 * Constructor that takes the property and the min and max value allowed for that property
	 * @param propertyName name of property that was misconfigured
	 * @param minValue the minimum allowable value of property
	 * @param minValue the maximum allowable value of property
	 */
	public PropertyExceedsLimitException(String propertyName, Object minValue, Object maxValue) {
		super(propertyName, minValue, maxValue);
	}

	/**
	 * Constructor that takes the property and the min and max value allowed for that property
	 * @param cause of this exception
	 * @param propertyName name of property that was misconfigured
	 * @param minValue the minimum allowable value of property
	 * @param minValue the maximum allowable value of property
	 */
	public PropertyExceedsLimitException(Throwable cause, String propertyName, Object minValue, Object maxValue) {
		super(cause, propertyName, minValue, maxValue);
	}

	/**
	 * Method that returns a message template with the property that had a value that exceeded limits
	 * and subsequently caused this exception to be thrown.
	 * 
	 * If the leagal limits were listed in this exception's constructor, they will be listed in the message template
	 */
	@Override
	protected String messageTemplate(int numArgs) {
		if(numArgs == 1){
			return "Property {0} is configured with a value that is not within the allowable limits";
		}else{
			return "Property {0} is configured with a value that is not between {1} and {2}";
		}
	}

}
