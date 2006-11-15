package no.stelvio.common.error.strategy.support;

import no.stelvio.common.error.SystemException;
import no.stelvio.common.error.support.Diversifier;

/**
 * Used for unit testing rethrowing of exceptions in exception handling.
 *  
 * @todo better javadoc?
 * @todo don't need this?
*/
public class TestException extends SystemException {
    private String state;

    /**
     * Copy constructor.
     *
     * @param other another instance of this exception that state should be copied from.
     * @param diversifier
     * @todo should this be public? The architecture will use reflection anyway, so the question is: will
     * "user code" have any use of this?
     * @todo might need some other signature to differentiate from new exception(cause)
     *
     */
    public TestException(TestException other, Diversifier diversifier) {
        super(other);
        state = other.state;
    }

    public TestException(Throwable cause, String state) {
        super(cause, state);
        this.state = state;
    }

    public TestException(String state) {
        super(state);
        this.state = state;
    }

    protected String getMessageTemplate() {
        return "template: {0}";
    }
}
