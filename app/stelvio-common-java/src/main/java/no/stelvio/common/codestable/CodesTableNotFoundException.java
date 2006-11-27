package no.stelvio.common.codestable;

import java.text.MessageFormat;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class CodesTableNotFoundException extends CodesTableException {
    private static final MessageFormat FORMAT = new MessageFormat("Codestable {0} does not exist");

    public <T extends AbstractCodesTableItem> CodesTableNotFoundException(Class<T> cti) {
        super(create(cti));
    }

    private static <T extends AbstractCodesTableItem> String create(Class<T> cti) {
        String s = FORMAT.format(new Object[]{cti});
        return s;
    }

    protected String messageTemplate() {
        return "Problems with handling codes table: {0}";
    }
}
