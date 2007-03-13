package no.stelvio.common.codestable;

import no.stelvio.common.codestable.support.CodesTableException;

/**
 * Exception thrown when a codes table class does not extend the correct class.
 *
 * @author personf8e9850ed756
 * @version $Id$
 */
public class NotCodesTableException extends CodesTableException {
	private static final long serialVersionUID = -1071348408365492500L;

	/**
	 * Constructs a new NotCodesTableException.
	 *
	 * @param codesTable the class that is not a codes table class.
	 */
	public NotCodesTableException(Class codesTable) {
		super(codesTable);
	}

	protected String messageTemplate(final int numArgs) {
		return "{0} is not a codes table";
	}
}
