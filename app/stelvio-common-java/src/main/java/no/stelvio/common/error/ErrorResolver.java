package no.stelvio.common.error;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo should the name be ErrorHolder instead?
 */
public interface ErrorResolver {
    Err resolve(Throwable throwable);
}
