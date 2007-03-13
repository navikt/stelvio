package no.stelvio.common.codestable;

import no.stelvio.common.error.SystemUnrecoverableException;

/**
 * Exception thrown to indicate that a codes table has no items.
 *
 * @author personf8e9850ed756, Accenture
 */
public class CodesTableEmptyException extends SystemUnrecoverableException {
	public CodesTableEmptyException(String className) {
		super(className);
	}

	protected String messageTemplate(final int numArgs) {
		return "No entries in codes table {0}";
	}
}
