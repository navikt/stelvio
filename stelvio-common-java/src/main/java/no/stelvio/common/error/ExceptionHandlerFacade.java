package no.stelvio.common.error;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo should it be called facade?
 */
public interface ExceptionHandlerFacade {
    Err getError(Class<? extends Throwable> clazz);
    Throwable handleError(Throwable e);
}
