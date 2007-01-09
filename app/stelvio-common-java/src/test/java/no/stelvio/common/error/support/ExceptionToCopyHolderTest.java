package no.stelvio.common.error.support;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import no.stelvio.common.error.FunctionalRecoverableException;
import no.stelvio.common.error.SystemUnrecoverableException;
import no.stelvio.common.error.TestRecoverableException;
import no.stelvio.common.error.TestUnrecoverableException;

/**
 * Unit test for {@link ExceptionToCopyHolder}.
 * 
 * @author personf8e9850ed756
 */
public class ExceptionToCopyHolderTest {
    @Test
    public void addingASystemExceptionReturnsTheSame() {
        ExceptionToCopyHolder<SystemUnrecoverableException> holder =
                new ExceptionToCopyHolder<SystemUnrecoverableException>(new TestUnrecoverableException("sys"));
        assertTrue("Should return a UnrecoverableException", holder.value() instanceof SystemUnrecoverableException);
    }

    @Test
    public void addingARecoverableExceptionReturnsTheSame() {
        ExceptionToCopyHolder<FunctionalRecoverableException> holder =
                new ExceptionToCopyHolder<FunctionalRecoverableException>(new TestRecoverableException("rec"));
        assertTrue("Should return a RecoverableException", holder.value() instanceof FunctionalRecoverableException);
    }
}
