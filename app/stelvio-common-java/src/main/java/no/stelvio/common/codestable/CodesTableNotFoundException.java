package no.stelvio.common.codestable;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.common.codestable.support.CodesTableException;

/**
 * Exception thrown when a <code>CodesTable</code> or <code>CodesTablePeriodic</code> cannot be retrieved.
 *
 * @author personf8e9850ed756
 * @version $Id$
 */
public class CodesTableNotFoundException extends CodesTableException {
	private static final long serialVersionUID = -6427780265990487437L;

	/**
	 * Constructs a new CodesTableNotFoundException.
	 *
	 * @param cause the underlying cause of this exception.
	 * @param cti CodestableItem that was not found.
	 */
	public <T extends AbstractCodesTableItem> CodesTableNotFoundException(Throwable cause, Class<T> cti) {
		super(cause, cti);
	}

	/**
	 * Constructs a new CodesTableNotFoundException.
	 *
	 * @param cti CodestableItem that was not found.
	 */
	public <T extends AbstractCodesTableItem> CodesTableNotFoundException(Class<T> cti) {
		super(cti);
	}

	/** {@inheritDoc} */
	protected String messageTemplate(final int numArgs) {
		return "Codestable {0} does not exist";
	}
}
