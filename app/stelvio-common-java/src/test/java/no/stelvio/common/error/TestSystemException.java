package no.stelvio.common.error;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * @author personf8e9850ed756
* @todo write javadoc
*/
public class TestSystemException extends SystemException {
    public TestSystemException(Object... templateArguments) {
        super(templateArguments);
    }

    public TestSystemException(Throwable cause, Object... templateArguments) {
        super(cause, templateArguments);
    }

    public TestSystemException(ExceptionToCopyHolder holder) {
        super(holder);
    }

    protected String getMessageTemplate() {
        return "dummy: {0}";
    }
}
