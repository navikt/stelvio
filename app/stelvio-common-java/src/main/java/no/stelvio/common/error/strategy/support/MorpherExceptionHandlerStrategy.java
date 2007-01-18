package no.stelvio.common.error.strategy.support;

import java.lang.reflect.Field;

import no.stelvio.common.error.StelvioException;
import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class MorpherExceptionHandlerStrategy implements ExceptionHandlerStrategy {
    /**
     * @param exception
     * @return
     */
public <T extends Throwable> T handleException(T exception) {
        T copy;
	    Throwable cause = exception.getCause();

        if (exception instanceof StelvioException) {
            try {
	            copy = resetCause(exception);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Problems with access", e);
            } catch (NoSuchFieldException e) {
	            throw new IllegalStateException("Field is not present", e);
            }
        } else {
            // TODO maybe create an exception to hold exceptions not being our own or expand Imitator to extend SystemUnrecoverableException
            copy = (T) new ImitatorException(exception);
        }

        Throwable origCopy = copy;

	    // TODO have to check that cause.getCause != cause in addition
        for (; cause != null; cause = cause.getCause()) {
            ImitatorException imitator = new ImitatorException(cause);
            origCopy.initCause(imitator);
            origCopy = imitator;
        }

        return copy;
    }

	private <T extends Throwable> T resetCause(T exception) throws NoSuchFieldException, IllegalAccessException {
		Class<?> clazz = exception.getClass();

		while (!clazz.equals(Throwable.class)) {
			clazz = clazz.getSuperclass();
		}

		Field cause = clazz.getDeclaredField("cause");
		cause.setAccessible(true);
		// must be set to "this" so initCause won't fail
		cause.set(exception, exception);

		return exception;
	}
}
