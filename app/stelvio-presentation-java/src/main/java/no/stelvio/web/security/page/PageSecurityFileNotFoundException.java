package no.stelvio.web.security.page;

import no.stelvio.common.security.SecurityException;

/**
 * Thrown when the configuration file containing the security definitions cannot be found.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class PageSecurityFileNotFoundException extends SecurityException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor to set the name of the configuration file.
	 * @param configFile the configuration filename.
	 */
	public PageSecurityFileNotFoundException(String configFile){
		super(configFile);
	}
	/**
	 * Constructor to set the name of the configuration file and the root cause for the exception.
	 * @param cause the root cause for the exception.
	 * @param configFile the configuration filename.
	 */
	public PageSecurityFileNotFoundException(Throwable cause,String configFile){
		super(cause,configFile);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String messageTemplate() {
		return "Could not find the security configuration file: {0}";
	}
}
