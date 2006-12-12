package no.stelvio.common.error;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo should we delete one of this or {@link ErrorConfigurationException}?
 */
public class ConfigurationExceptionOld extends UnrecoverableException {
    private final String source;

    public ConfigurationExceptionOld(String source) {
        this(null, source);
    }

    public ConfigurationExceptionOld(Throwable cause, String source) {
        super(cause);
        this.source = source;
    }

    protected ConfigurationExceptionOld(ExceptionToCopyHolder<ConfigurationExceptionOld> holder) {
        super(holder);
        source = holder.value().source;
    }

    protected String messageTemplate(final int numArgs) {
        return "Problems setting up {0}";
    }
}
