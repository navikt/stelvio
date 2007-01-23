package no.stelvio.common.codestable;

import no.stelvio.common.codestable.support.CodesTableException;

/**
 * Exception thrown when <code>CodesTable</code> or a <code>CodesTablePeriodic</code> doesn't hold a requested
 * <code>decode</code>.
 *
 * @author personf8e9850ed756
 * @version $Id$
 */
public class DecodeNotFoundException extends CodesTableException {

	private static final long serialVersionUID = 8665509491381533830L;

	/**
	 * Creates an instance of the exception with the given code.
	 * 
	 * @param code the decode is not found for this code. 
	 */
	public DecodeNotFoundException(Object code) {
		super(code);
	}

	/**
	 * {@inheritDoc}
	 */
	protected String messageTemplate(final int numArgs) {
		return "No decode found for code {0}";
	}
}