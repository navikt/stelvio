package no.stelvio.common.error;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public interface ExceptionHandlerFacade {
	<T extends Throwable> void handle(T e);

	<T extends Throwable> void rethrow(T e) throws T;

	<T extends Throwable> void throwNew(Class<T> exceptionToThrow, Object... params) throws T;

	<T extends Throwable> void throwNew(Class<T> exceptionToThrow, Throwable cause, Object... params) throws T;
}
