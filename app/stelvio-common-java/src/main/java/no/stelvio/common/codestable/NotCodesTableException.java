package no.stelvio.common.codestable;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class NotCodesTableException extends CodesTableException {
    public NotCodesTableException(Class<? extends AbstractCodesTableItem> codesTable) {
        super(codesTable);
    }

    protected String messageTemplate() {
        return "{0} is not a codes table";
    }
}
