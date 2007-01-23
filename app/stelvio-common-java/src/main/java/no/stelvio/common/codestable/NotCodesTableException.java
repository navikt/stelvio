package no.stelvio.common.codestable;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.common.codestable.support.CodesTableException;

/**
 * NotCodesTableException
 * @author personf8e9850ed756
 * @todo write javadoc
 * @version $Id$
 */
public class NotCodesTableException extends CodesTableException {
  

	private static final long serialVersionUID = -1071348408365492500L;

	/**
     * Constructs a new NotCodesTableException 
     * @param codesTable
     */
	public NotCodesTableException(Class<? extends AbstractCodesTableItem> codesTable) {
        super(codesTable);
    }

    protected String messageTemplate(final int numArgs) {
        return "{0} is not a codes table";
    }
}
