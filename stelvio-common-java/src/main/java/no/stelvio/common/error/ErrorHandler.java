package no.stelvio.common.error;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public interface ErrorHandler {
    Err getError(Class<? extends Throwable> clazz);
}
