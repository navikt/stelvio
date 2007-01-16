package no.stelvio.common.codestable;

import no.stelvio.common.codestable.support.CodesTableException;

/**
 * Exception thrown when <code>CodesTable</code> or a 
 * <code>CodesTablePeriodic</code> doesn't hold a requested 
 * <code>decode</code>.
 * @author personf8e9850ed756
 * @todo write javadoc
 * @version $Id$
 */
public class DecodeNotFoundException extends CodesTableException {

	/**
	 * 
	 * @param message
	 */
	public DecodeNotFoundException(String message) {
		super(message);
	}

	protected String messageTemplate(final int numArgs) {
		return "No decode found for code {0}";
	}
}