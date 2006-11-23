package no.stelvio.web.security.page.exceptions;

import no.stelvio.common.security.SecurityException;

public class PageSecurityFileParseException extends SecurityException {

	private static final long serialVersionUID = 1L;

	public PageSecurityFileParseException(String configFile){
		super(configFile);
	}
	public PageSecurityFileParseException(Throwable cause,String configFile){
		super(cause,configFile);
	}
	
	@Override
	protected String messageTemplate() {
		return "An error occured while parsing file: {0}";
	}

}
