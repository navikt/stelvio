package no.stelvio.common.error;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.error.support.ExceptionToCopyHolder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.eq;
import static org.hamcrest.core.IsEqual.isTrue;
import static org.hamcrest.core.IsNull.isNull;
import static org.hamcrest.core.IsSame.same;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

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

        assertThat(copy.isLogged(), eq(original.isLogged()));
        assertThat(copy.getErrorId(), eq(original.getErrorId()));
        assertThat(copy.getProcessId(), eq(original.getProcessId()));
        assertThat(copy.getScreenId(), eq(original.getScreenId()));
        assertThat(copy.getTransactionId(), eq(original.getTransactionId()));
        assertThat(copy.getUserId(), eq(original.getUserId()));

        Exception exCopy = ((Exception) copy);
        Exception exOriginal = ((Exception) original);
        assertThat(exCopy.getMessage(), eq(exOriginal.getMessage()));
        assertThat(exCopy.getLocalizedMessage(), eq(exOriginal.getLocalizedMessage()));
        assertThat(exCopy.toString(), eq(exOriginal.toString()));

        assertThat(exCopy.getCause(), isNull());
    }

    @Test
    public void causeIsSavedForLaterUsage() {
        RuntimeException cause = new RuntimeException("The original cause");
        Exception ewc = (Exception) createExceptionWithCause(cause);

        assertThat(cause, same(ewc.getCause()));
    }

    @Test
    public void copyConstructorShouldBeProtected() throws NoSuchMethodException {
        Constructor constructor =
                createException().getClass().getSuperclass().getDeclaredConstructor(ExceptionToCopyHolder.class);

        assertThat(Modifier.isProtected(constructor.getModifiers()), isTrue());
    }

	@Test(expected = IllegalArgumentException.class)
	public void createExceptionWithoutParametersThrowsException() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		Class<T> exceptionClass = exceptionClass();
		Constructor<T> constructor = exceptionClass.getConstructor(Object[].class);
		constructor.newInstance();
	}

	protected abstract Class<T> exceptionClass();

	protected abstract T createExceptionWithCause(Exception cause);

    protected abstract T createException();

    protected abstract T createCopy(T le);

    @Before
    public void setUpRequestContext() {
        RequestContext.setModuleId("module");
        RequestContext.setProcessId("process");
        RequestContext.setScreenId("screen");
        RequestContext.setTransactionId("transaction");
        RequestContext.setUserId("user");
    }
}
