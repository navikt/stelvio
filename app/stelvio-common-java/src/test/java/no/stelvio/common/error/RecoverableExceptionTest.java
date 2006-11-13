package no.stelvio.common.error;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

import no.stelvio.common.error.support.Diversifier;

/**
 * Unit test of {@link RecoverableException}.
 * 
 * @author person7553f5959484
 * @version $Revision: 2838 $ $Author: psa2920 $ $Date: 2006-04-25 12:22:58 +0200 (Tue, 25 Apr 2006) $
 */
public class RecoverableExceptionTest extends AbstractExceptionTest<RecoverableException> {
    @Test
    public void takesAListForTheTemplateArguments() {
        TestRecoverableException exception = new TestRecoverableException(2.0);

        assertEquals(exception.getTemplateArguments()[0], 2.0, "Not the correct value;");
    }

    @Test
    public void takesACauseAndAListOfTheTemplateArguments() {
        TestRecoverableException exception =
                new TestRecoverableException(new IllegalArgumentException("problems"), String.class);

        assertEquals(exception.getCause().getMessage(), "problems");
        assertEquals(exception.getTemplateArguments()[0], String.class, "Not the correct value;");
    }

    protected RecoverableException createException() {
        return new TestRecoverableException("message");
    }

    protected RecoverableException createExceptionWithCause(Exception e) {
        return new TestRecoverableException(e, "message");
    }

    protected RecoverableException createCopy(RecoverableException ae) {
        return new TestRecoverableException(ae, Diversifier.INSTANCE);
    }

    private static class TestRecoverableException extends RecoverableException {
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
}
