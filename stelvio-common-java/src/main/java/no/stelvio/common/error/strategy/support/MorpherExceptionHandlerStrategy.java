package no.stelvio.common.error.strategy.support;

import java.lang.reflect.Field;

/**
 * Removes all causes, putting an instance of <code>ImitatorException</code> in each cause's place. This will prevent
 * classpath problems when an exception travels from one layer to another with different classpath. For example when an
 * Hibernate exception makes it way to the web application, which probably won't have Hibernate on its classpath.
 * 
 *
 * @see ImitatorException
 */
public class MorpherExceptionHandlerStrategy extends AbstractOrderedExceptionHandlerStrategy {
	@Override
	public <T extends Throwable> T handleException(T exception) {
		T copy;
		Throwable cause = exception.getCause();

		if (exception instanceof Throwable) {
			try {
				copy = resetCause(exception);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Problems with access", e);
			} catch (NoSuchFieldException e) {
				// Only happens if the cause field in the Throwable class is renamed
				throw new IllegalStateException("Field is not present", e);
			}
		} else {
			copy = (T) new ImitatorException(exception);
		}

		Throwable origCopy = copy;

		for (; cause != null; cause = cause.getCause()) {
			ImitatorException imitator = new ImitatorException(cause);
			origCopy.initCause(imitator);
			origCopy = imitator;
		}

		return copy;
	}

	/**
	 * Sets the cause for the exception to be the exception and returns it.
	 * 
	 * @param <T>
	 *            Throwable class
	 * @param exception
	 *            an exception
	 * @return the exception
	 * @throws NoSuchFieldException
	 *             if the field does not exist
	 * @throws IllegalAccessException
	 *             if the method cannot get or set the cause field and does not have access to the definition of the field.
	 */
	private <T extends Throwable> T resetCause(T exception) throws NoSuchFieldException, IllegalAccessException {
		Field cause = Throwable.class.getDeclaredField("cause");
		cause.setAccessible(true);
		// must be set to "this" so initCause won't fail
		cause.set(exception, exception);

		return exception;
	}
}
