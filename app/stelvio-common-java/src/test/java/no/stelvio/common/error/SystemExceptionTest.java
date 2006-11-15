package no.stelvio.common.error;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Unit test of SystemException.
 * 
 * @author person7553f5959484
 * @version $Revision: 2836 $ $Author: psa2920 $ $Date: 2006-04-25 12:15:25 +0200 (Tue, 25 Apr 2006) $
 */
public class SystemExceptionTest extends AbstractExceptionTest<TestSystemException> {
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

    protected TestSystemException createException() {
        return new TestSystemException("message");
    }

    protected TestSystemException createExceptionWithCause(Exception e) {
        return new TestSystemException(e, "message");
    }

    protected TestSystemException createCopy(TestSystemException ae) {
        return new TestSystemException(new ExceptionToCopyHolder<TestSystemException>(ae));
    }
}
