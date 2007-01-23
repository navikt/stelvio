package no.stelvio.common.codestable;

import no.stelvio.common.codestable.support.CodesTableException;

/**
 * Exception thrown when <code>CodesTable</code> or a <code>CodesTablePeriodic</code> doesn't hold a requested
 * item.
 *
 * @author personf8e9850ed756
 * @version $Id$
 */
public class ItemNotFoundException extends CodesTableException {

	private static final long serialVersionUID = -1206901328468603479L;

	/**
	 * Creates an instance of the exception with the given code.
	 *
	 * @param code the item is not found for this code.
	 */
	public ItemNotFoundException(Object code) {
		super(code);
	}

	/**
	 * {@inheritDoc}
	 */
	protected String messageTemplate(final int numArgs) {
		return "No codes table item found for code {0}";
	}
}
