package no.stelvio.common.error;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Unit test of UnrecoverableException.
 * 
 * @author person7553f5959484
 * @version $Revision: 2836 $ $Author: psa2920 $ $Date: 2006-04-25 12:15:25 +0200 (Tue, 25 Apr 2006) $
 */
public class UnrecoverableExceptionTest extends AbstractExceptionTest<TestUnrecoverableException> {
    @Test
    public void takesAListForTheTemplateArguments() {
        TestUnrecoverableException exception = new TestUnrecoverableException(2.0);

        assertEquals(exception.getTemplateArguments()[0], 2.0, "Not the correct value;");
    }

    @Test
    public void takesACauseAndAListOfTheTemplateArguments() {
        TestUnrecoverableException exception =
                new TestUnrecoverableException(new IllegalArgumentException("problems"), String.class);

        assertEquals(exception.getCause().getMessage(), "problems");
        assertEquals(exception.getTemplateArguments()[0], String.class, "Not the correct value;");
    }

    protected TestUnrecoverableException createException() {
        return new TestUnrecoverableException("messageFrom");
    }

    protected TestUnrecoverableException createExceptionWithCause(Exception e) {
        return new TestUnrecoverableException(e, "messageFrom");
    }

    protected TestUnrecoverableException createCopy(TestUnrecoverableException ae) {
        return new TestUnrecoverableException(new ExceptionToCopyHolder<TestUnrecoverableException>(ae));
    }
}
