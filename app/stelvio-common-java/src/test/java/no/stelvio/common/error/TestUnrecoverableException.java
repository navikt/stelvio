package no.stelvio.common.error;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * @author personf8e9850ed756
* @todo write javadoc
*/
public class TestUnrecoverableException extends UnrecoverableException {
    public TestUnrecoverableException(Object... templateArguments) {
        super(templateArguments);
    }

    public TestUnrecoverableException(Throwable cause, Object... templateArguments) {
        super(cause, templateArguments);
    }

    protected TestUnrecoverableException(ExceptionToCopyHolder holder) {
        super(holder);
    }

    protected String messageTemplate() {
        return "dummy: {0}";
    }
}
