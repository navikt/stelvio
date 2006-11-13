package no.stelvio.common.error;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

import no.stelvio.common.error.support.Diversifier;

/**
 * Unit test of SystemException.
 * 
 * @author person7553f5959484
 * @version $Revision: 2836 $ $Author: psa2920 $ $Date: 2006-04-25 12:15:25 +0200 (Tue, 25 Apr 2006) $
 */
public class SystemExceptionTest extends AbstractExceptionTest<SystemException> {
    @Test
    public void takesAListForTheTemplateArguments() {
        TestSystemException exception = new TestSystemException(2.0);

        assertEquals(exception.getTemplateArguments()[0], 2.0, "Not the correct value;");
    }

    @Test
    public void takesACauseAndAListOfTheTemplateArguments() {
        TestSystemException exception =
                new TestSystemException(new IllegalArgumentException("problems"), String.class);

        assertEquals(exception.getCause().getMessage(), "problems");
        assertEquals(exception.getTemplateArguments()[0], String.class, "Not the correct value;");
    }

    protected SystemException createException() {
        return new TestSystemException("message");
    }

    protected SystemException createExceptionWithCause(Exception e) {
        return new TestSystemException(e, "message");
    }

    protected SystemException createCopy(SystemException ae) {
        return new TestSystemException(ae, Diversifier.INSTANCE);
    }

    private static class TestSystemException extends SystemException {
        private TestSystemException(SystemException other, Diversifier diversifier) {
            super(other, diversifier);
        }

        public TestSystemException(Object... templateArguments) {
            super(templateArguments);
        }

        public TestSystemException(Throwable cause, Object... templateArguments) {
            super(cause, templateArguments);
        }

        protected String getMessageTemplate() {
            return "dummy: {0}";
        }
    }
}
