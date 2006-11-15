package no.stelvio.common.error.strategy.support;

import no.stelvio.common.error.SystemException;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Used for unit testing rethrowing of exceptions in exception handling.
 *  
 * @todo better javadoc?
 * @todo don't need this?
*/
public class TestException extends SystemException {
    private String state;

    public TestException(Throwable cause, String state) {
        super(cause, state);
        this.state = state;
    }

    public TestException(String state) {
        super(state);
        this.state = state;
    }

    /**
     * Copy constructor.
     *
     * @param other
     * @todo should this be public? The architecture will use reflection anyway, so the question is: will
     * "user code" have any use of this?
     * @todo might need some other signature to differentiate from new exception(cause)
     *
     */
    protected TestException(ExceptionToCopyHolder other) {
        super(other);
        TestException testException = TestException.class.cast(other.value());
        state = testException.state;
    }

    protected String getMessageTemplate() {
        return "template: {0}";
    }
}
