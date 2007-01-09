package no.stelvio.common.error;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * @author personf8e9850ed756
* @todo write javadoc
*/
public class TestRecoverableException extends FunctionalRecoverableException {
    public TestRecoverableException(Object... templateArguments) {
        super(templateArguments);
    }

    public TestRecoverableException(Throwable cause, Object... templateArguments) {
        super(cause, templateArguments);
    }

    protected TestRecoverableException(ExceptionToCopyHolder holder) {
        super(holder);
    }

    protected String messageTemplate(final int numArgs) {
        return "dummy: {0}";
    }
}
