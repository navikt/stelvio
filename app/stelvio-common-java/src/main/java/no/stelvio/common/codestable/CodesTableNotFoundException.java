package no.stelvio.common.codestable;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.common.codestable.support.CodesTableException;

/**
 * Exception thrown when a <code>CodesTable</code> or <code>CodesTablePeriodic</code>
 * cannot be retrieved.
 * @author personf8e9850ed756
 * @todo write javadoc
 * @version $Id$
 */
public class CodesTableNotFoundException extends CodesTableException {

	private static final long serialVersionUID = -6427780265990487437L;

    
    /**
     * Constructs a new CodesTableNotFoundException
     * @param cause the underlying cause of this exception
     * @param cti CodestableItem that was not found
     */
    public <T extends AbstractCodesTableItem> CodesTableNotFoundException(Throwable cause, Class<T> cti) {
        super(cause, cti);
    }	
	
	/**
     * Constructs a new CodesTableNotFoundException
     * @param cti CodestableItem that was not found
     */
    public <T extends AbstractCodesTableItem> CodesTableNotFoundException(Class<T> cti) {
        super(cti);
    }


    protected String messageTemplate(final int numArgs) {
        return "Codestable {0} does not exist";
    }
}
