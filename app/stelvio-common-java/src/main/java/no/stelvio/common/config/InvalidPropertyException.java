package no.stelvio.common.config;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

public class InvalidPropertyException extends ConfigurationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1223318644736665109L;
	private static final String TEMPLATE_LEAD = "Property/properties ";
	private static final String TEMPLATE_END = " have invalid values";
	

    /**
	 * Constructs a copy of the specified InvalidPropertyException without the cause.
     * <p>
     * Is used by the framework to make a copy for rethrowing without getting class path problems with the exception
     * classes that is part of the cause stack.
	 *
	 * @param holder ExceptionToCopyHolder
     */
	public InvalidPropertyException(ExceptionToCopyHolder holder) {
		super(holder);
	}

	/**
	 * Constructor that takes a list of properties that have invalid values
	 * @param templateArguments dynamic list of arguments
	 */
	public InvalidPropertyException(Object... templateArguments) {
		super(templateArguments);
	}

	/**
	 * Constructor that takes a cause and a list of properties that have invalid values
	 * @param cause of this exception
	 * @param templateArguments dynamic list of arguments
	 */
	public InvalidPropertyException(Throwable cause, Object... templateArguments) {
		super(cause, templateArguments);
	}

	/**
	 * Produces a message template used signaling which properties had invalid values and caused this exception to be thrown
	 * 
	 * @param numArgs number of template arguments passed through constructor
	 * @return messageTemplate for this exception
	 */
	@Override
	protected String messageTemplate(int numArgs) {
		StringBuffer template = new StringBuffer(TEMPLATE_LEAD);
		template.append("{0}");
		for (int i = 1; i<numArgs; i++) {
			template.append(", {").append(i).append("}");
		}
		template.append(TEMPLATE_END);
		return template.toString();
	}

}
