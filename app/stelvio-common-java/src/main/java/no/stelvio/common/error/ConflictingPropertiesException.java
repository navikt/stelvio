package no.stelvio.common.error;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Thrown if two or more properties set up by confiugration are in conflict with each other 
 * @author person983601e0e117, Accenture
 *
 */
public class ConflictingPropertiesException extends ConfigurationException {

	private final String TEMPLATE_LEAD = "Properties ";
	private final String TEMPLATE_END = " have conflicting values";	
	
	/**
	 * {@inheritDoc no.stelvio.common.error.UnrecoverableException#UnrecoverableException(ExceptionToCopyHolder)}
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
	protected String messageTemplate(int numberOfTemplateArguments) {
		StringBuffer template = new StringBuffer(TEMPLATE_LEAD);
		template.append("{0}");
		
		for (int i = 1; i<numberOfTemplateArguments; i++) {
			if((i+1) == numberOfTemplateArguments){
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
