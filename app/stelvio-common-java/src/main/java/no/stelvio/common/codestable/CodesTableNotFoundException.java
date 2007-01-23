package no.stelvio.common.codestable;

import java.text.MessageFormat;

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
	private static final MessageFormat FORMAT = new MessageFormat("Codestable {0} does not exist");

    /**
     * Constructs a new CodesTableNotFoundException
     * @param cti CodestableItem that was not found
     */
    public <T extends AbstractCodesTableItem> CodesTableNotFoundException(Class<T> cti) {
        super(create(cti));
    }

    private static <T extends AbstractCodesTableItem> String create(Class<T> cti) {
        String s = FORMAT.format(new Object[]{cti});
        return s;
    }

    protected String messageTemplate(final int numArgs) {
        return "Problems with handling codes table: {0}";
    }
}
