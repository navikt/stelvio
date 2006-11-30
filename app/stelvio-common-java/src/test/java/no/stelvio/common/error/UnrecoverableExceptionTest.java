package no.stelvio.common.error;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.eq;
import org.junit.Test;

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

        assertThat((Double) exception.getTemplateArguments()[0], eq(2.0));
    }

    @Test
    public void takesACauseAndAListOfTheTemplateArguments() {
        TestUnrecoverableException exception =
                new TestUnrecoverableException(new IllegalArgumentException("problems"), String.class);

        assertThat(exception.getCause().getMessage(), eq("problems"));
        assertThat((Class<String>) exception.getTemplateArguments()[0], eq(String.class));
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
