package no.stelvio.common.error.support;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import no.stelvio.common.error.RecoverableException;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.error.TestRecoverableException;
import no.stelvio.common.error.TestSystemException;

/**
 * Unit test for {@link ExceptionToCopyHolder}.
 * 
 * @author personf8e9850ed756
 */
public class ExceptionToCopyHolderTest {
    @Test
    public void addingASystemExceptionReturnsTheSame() {
        ExceptionToCopyHolder<SystemException> holder =
                new ExceptionToCopyHolder<SystemException>(new TestSystemException("sys"));
        assertTrue("Should return a SystemException", holder.value() instanceof SystemException);
    }

    @Test
    public void addingARecoverableExceptionReturnsTheSame() {
        ExceptionToCopyHolder<RecoverableException> holder =
                new ExceptionToCopyHolder<RecoverableException>(new TestRecoverableException("rec"));
        assertTrue("Should return a RecoverableException", holder.value() instanceof RecoverableException);
    }
}
