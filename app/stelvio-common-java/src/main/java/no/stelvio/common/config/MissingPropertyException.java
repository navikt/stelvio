package no.stelvio.common.config;

import no.stelvio.common.error.strategy.support.RethrowExceptionHandlerStrategy;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Exception thrown if one or more required properties are not set by configuration
 * @author person983601e0e117, Accenture
 *
 */
public class MissingPropertyException extends ConfigurationException {


	private static final long serialVersionUID = 2193502321565754897L;
	
	private final static String TEMPLATE_LEAD = "Required property/properties ";
	private final static String TEMPLATE_END = " was not set by configuration";

	 /**
	 * Constructs a copy of the specified UnrecoverableException without the cause.
     * <p>
     * Is used by the framework to make a copy for rethrowing without getting class path problems with the exception
     * classes that is part of the cause stack.
	 *
	 * @param holder
     * @see RethrowExceptionHandlerStrategy
     */
	public MissingPropertyException(ExceptionToCopyHolder holder) {
		super(holder);
	}

	/**
	 * Constructs a new MissingPropertyException
	 * @param templateArguments list of required properties was missing
	 */
	public MissingPropertyException(Object... templateArguments) {
		super(templateArguments);
	}

	/**
	 * Constructs a new MissingPropertyException
	 * @param cause the cause of this exception
	 * @param templateArguments list of required properties was missing
	 */
	public MissingPropertyException(Throwable cause, Object... templateArguments) {
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
			template.append(", {").append(i).append("}");
		}
		template.append(TEMPLATE_END);
		return template.toString();
	}

}
