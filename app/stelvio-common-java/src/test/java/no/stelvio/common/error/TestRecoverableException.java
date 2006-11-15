package no.stelvio.common.error;

import no.stelvio.common.error.support.Diversifier;

/**
 * @author personf8e9850ed756
* @todo write javadoc
*/
public class TestRecoverableException extends RecoverableException {
    private TestRecoverableException(RecoverableException other, Diversifier diversifier) {
        super(other, diversifier);
    }

    public TestRecoverableException(Object... templateArguments) {
        super(templateArguments);
    }

    public TestRecoverableException(Throwable cause, Object... templateArguments) {
        super(cause, templateArguments);
    }

    protected String getMessageTemplate() {
        return "dummy: {0}";
    }
}
