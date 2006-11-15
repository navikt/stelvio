package no.stelvio.common.error;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * @author personf8e9850ed756
 * @todo add javadoc
 * @todo didn't run as JUnit 4.x test case. Don't know why; could be my test runner.
 */
public abstract class AbstractExceptionTest<T extends StelvioException> {
    @Test
    public void copyConstructorCopiesAllFields() {
        T original = createException();
        original.setLogged();
        T copy = createCopy(original);

        assertEquals(copy.isLogged(), original.isLogged(), "isLogged() should match;");
        assertEquals(copy.getErrorId(), original.getErrorId(), "getErrorId() should match;");
        assertEquals(copy.getProcessId(), original.getProcessId(), "getProcessId() should match;");
        assertEquals(copy.getScreenId(), original.getScreenId(), "getScreenId() should match;");
        assertEquals(copy.getTransactionId(), original.getTransactionId(),
                "getTransactionId() should match;");
        assertEquals(copy.getUserId(), original.getUserId(), "getUserId() should match;");

        Exception exCopy = ((Exception) copy);
        Exception exOriginal = ((Exception) original);
        assertEquals(exCopy.getMessage(), exOriginal.getMessage(), "getMessage() should match;");
        assertEquals(exCopy.getLocalizedMessage(), exOriginal.getLocalizedMessage(),
                "getLocalizedMessage() should match;");
        assertEquals(exCopy.toString(), exOriginal.toString(), "toString() should match;");

        assertNull(exCopy.getCause(), "cause should have been removed");
    }

    @Test
    public void causeIsSavedForLaterUsage() {
        RuntimeException cause = new RuntimeException("The original cause");
        Exception ewc = (Exception) createExceptionWithCause(cause);

        assertSame(cause, ewc.getCause(), "Cause is the original cause");
    }

    @Test
    public void copyConstructorShouldBeProtected() throws NoSuchMethodException {
        Constructor constructor =
                createException().getClass().getSuperclass().getDeclaredConstructor(ExceptionToCopyHolder.class);

        assertTrue(Modifier.isProtected(constructor.getModifiers()), "Copy constructor should be protected");
    }

    protected abstract T createExceptionWithCause(Exception cause);

    protected abstract T createException();

    protected abstract T createCopy(T le);

    @BeforeClass
    public void setUpRequestContext() {
        RequestContext.setModuleId("module");
        RequestContext.setProcessId("process");
        RequestContext.setScreenId("screen");
        RequestContext.setTransactionId("transaction");
        RequestContext.setUserId("user");
    }
}
