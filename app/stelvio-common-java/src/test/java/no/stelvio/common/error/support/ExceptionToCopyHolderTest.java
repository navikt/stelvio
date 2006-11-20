package no.stelvio.common.error.support;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import no.stelvio.common.error.RecoverableException;
import no.stelvio.common.error.TestRecoverableException;
import no.stelvio.common.error.TestUnrecoverableException;
import no.stelvio.common.error.UnrecoverableException;

/**
 * Unit test for {@link ExceptionToCopyHolder}.
 * 
 * @author personf8e9850ed756
 */
public class ExceptionToCopyHolderTest {
    @Test
    public void addingASystemExceptionReturnsTheSame() {
        ExceptionToCopyHolder<UnrecoverableException> holder =
                new ExceptionToCopyHolder<UnrecoverableException>(new TestUnrecoverableException("sys"));
        assertTrue("Should return a UnrecoverableException", holder.value() instanceof UnrecoverableException);
    }

    @Test
    public void addingARecoverableExceptionReturnsTheSame() {
        ExceptionToCopyHolder<RecoverableException> holder =
                new ExceptionToCopyHolder<RecoverableException>(new TestRecoverableException("rec"));
        assertTrue("Should return a RecoverableException", holder.value() instanceof RecoverableException);
    }
}
