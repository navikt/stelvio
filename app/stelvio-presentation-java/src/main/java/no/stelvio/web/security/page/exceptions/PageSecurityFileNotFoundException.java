package no.stelvio.web.security.page.exceptions;

import no.stelvio.common.security.SecurityException;
import no.stelvio.web.security.page.parse.JSFPage;

public class PageSecurityFileNotFoundException extends SecurityException {

	public PageSecurityFileNotFoundException(String configFile){
		super(configFile);
	}
	public PageSecurityFileNotFoundException(Throwable cause,String configFile){
		super(cause,configFile);
	}
	
	@Override
	protected String messageTemplate() {
		return "Could not find the security configuration file: {0}";
	}
}
