package no.stelvio.common.error;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo should we delete one of this or {@link ErrorConfigurationException}?
 */
public class ConfigurationException extends UnrecoverableException {
    private final String source;

    public ConfigurationException(String source) {
        this(null, source);
    }

    public ConfigurationException(Throwable cause, String source) {
        super(cause);
        this.source = source;
    }

    protected ConfigurationException(ExceptionToCopyHolder<ConfigurationException> holder) {
        super(holder);
        source = holder.value().source;
    }

    protected String messageTemplate() {
        return "Problems setting up {0}";
    }
}
