package no.stelvio.common.error;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Unit test of {@link RecoverableException}.
 * 
 * @author person7553f5959484
 * @version $Revision: 2838 $ $Author: psa2920 $ $Date: 2006-04-25 12:22:58 +0200 (Tue, 25 Apr 2006) $
 */
public class RecoverableExceptionTest extends AbstractExceptionTest<TestRecoverableException> {
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

    protected TestRecoverableException createException() {
        return new TestRecoverableException("messageFrom");
    }

    protected TestRecoverableException createExceptionWithCause(Exception e) {
        return new TestRecoverableException(e, "messageFrom");
    }

    protected TestRecoverableException createCopy(TestRecoverableException re) {
        return new TestRecoverableException(new ExceptionToCopyHolder<TestRecoverableException>(re));
    }

}
