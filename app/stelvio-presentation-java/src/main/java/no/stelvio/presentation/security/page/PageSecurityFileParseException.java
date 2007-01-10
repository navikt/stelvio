package no.stelvio.presentation.security.page;

import no.stelvio.common.security.SecurityException;

/**
 * Thrown if the parsing of the security definitions from the configuration file
 * fails.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class PageSecurityFileParseException extends SecurityException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor to set the name of the configuration file.
	 * 
	 * @param configFile
	 *            the configuration filename.
	 */
	public PageSecurityFileParseException(String configFile) {
		super(configFile);
	}

	/**
	 * Constructor to set the name of the configuration file and the root cause
	 * for the exception.
	 * 
	 * @param cause
	 *            the root cause for the exception.
	 * @param configFile
	 *            the configuration filename.
	 */
	public PageSecurityFileParseException(Throwable cause, String configFile) {
		super(cause, configFile);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String messageTemplate(final int numArgs) {
		return "An error occured while parsing file: {0}";
	}

}
