package no.stelvio.common.error;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo should it be called facade?
 */
public interface ExceptionHandlerFacade {
    <T extends Throwable> void handle(T e);
    <T extends Throwable> void rethrow(T e) throws Throwable;
	<T extends Throwable> void throwNew(Class<T> exceptionToThrow, Object... params) throws Throwable;
	<T extends Throwable> void throwNew(Class<T> exceptionToThrow, Throwable cause, Object... params) throws Throwable;
}
