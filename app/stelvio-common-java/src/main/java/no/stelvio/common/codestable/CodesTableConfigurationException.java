package no.stelvio.common.codestable;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class CodesTableConfigurationException extends CodesTableException {
    public CodesTableConfigurationException(String message) {
        super(message);
    }

    protected String messageTemplate() {
        return "Problems configuration of codes table module: {0}";
    }
}
