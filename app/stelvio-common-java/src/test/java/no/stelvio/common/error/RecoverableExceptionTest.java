package no.stelvio.common.error;

import no.stelvio.common.error.support.ExceptionToCopyHolder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.eq;
import org.junit.Test;

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

        assertThat((Double) exception.getTemplateArguments()[0], eq(2.0));
    }

    @Test
    public void takesACauseAndAListOfTheTemplateArguments() {
        TestRecoverableException exception =
                new TestRecoverableException(new IllegalArgumentException("problems"), String.class);

        assertThat(exception.getCause().getMessage(), eq("problems"));
        assertThat((Class<String>) exception.getTemplateArguments()[0], eq(String.class));
    }

    protected TestRecoverableException createException() {
        return new TestRecoverableException("messageFrom");
    }

	protected Class<TestRecoverableException> exceptionClass() {
		return TestRecoverableException.class;
	}

	protected TestRecoverableException createExceptionWithCause(Exception e) {
        return new TestRecoverableException(e, "messageFrom");
    }

    protected TestRecoverableException createCopy(TestRecoverableException re) {
        return new TestRecoverableException(new ExceptionToCopyHolder<TestRecoverableException>(re));
    }

}
