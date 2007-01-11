package no.stelvio.common.config;

import no.stelvio.common.error.strategy.support.RethrowExceptionHandlerStrategy;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Thrown if two or more properties set up by confiugration are in conflict with each other 
 * @author person983601e0e117, Accenture
 *
 */
public class ConflictingPropertiesException extends ConfigurationException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6905655613431964263L;
	
	private static final String TEMPLATE_LEAD = "Properties ";
	private static final String TEMPLATE_END = " have conflicting values";	
	
    /**
	 * Constructs a copy of the specified ConflictingPropertiesException without the cause.
     * <p>
     * Is used by the framework to make a copy for rethrowing without getting class path problems with the exception
     * classes that is part of the cause stack.
	 *
	 * @param holder
     * @see RethrowExceptionHandlerStrategy
     */

	public ConflictingPropertiesException(ExceptionToCopyHolder holder) {
		super(holder);
	}

	/**
	 * Constructs a new ConflictingPropertiesException
	 * 
	 * @param templateArguments list of properties that caused the conflict
	 */
	public ConflictingPropertiesException(Object... templateArguments) {
		super(templateArguments);
	}

	/**
	 * Constructs a new ConflictingPropertiesException
	 * @param cause the cause of this exception
	 * @param templateArguments list of properties that caused the conflict
	 */	
	public ConflictingPropertiesException(Throwable cause, Object... templateArguments) {
		super(cause, templateArguments);
	}

	/**
	 * Method that returns a message template that lists the required properties that was not set
	 * and subsequently caused this exception to be thrown
	 * 
     * @param numArgs the number of arguments to the exception's constructor.
     * @return the template to use for constructing the exception's messageFrom.
	 */
	@Override
	protected String messageTemplate(int numArgs) {
		StringBuffer template = new StringBuffer(TEMPLATE_LEAD);
		template.append("{0}");
		
		for (int i = 1; i < numArgs; i++) {
			if((i+1) == numArgs){
				template.append(" and");
			}else{
				template.append(",");
			}
			template.append(" {").append(i).append("}");
		}
		template.append(TEMPLATE_END);
		return template.toString();
		
	}

}
