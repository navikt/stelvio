package no.stelvio.common.config;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

public class InvalidPropertyException extends ConfigurationException {

	private final String TEMPLATE_LEAD = "Property/properties ";
	private final String TEMPLATE_END = " have invalid values";
	
	/**
	 * {@inheritDoc no.stelvio.common.config.ConfigurationException#ConfigurationException(ExceptionToCopyHolder)}
	 */	
	public InvalidPropertyException(ExceptionToCopyHolder holder) {
		super(holder);
	}

	/**
	 * Constructor that takes a list of properties that have invalid values
	 * @param templateArguments
	 */
	public InvalidPropertyException(Object... templateArguments) {
		super(templateArguments);
	}

	/**
	 * Constructor that takes a cause and a list of properties that have invalid values
	 * @param Throwable cause
	 * @param templateArguments
	 */
	public InvalidPropertyException(Throwable cause, Object... templateArguments) {
		super(cause, templateArguments);
	}

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
