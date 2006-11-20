package no.stelvio.common.error.message;

/**
 * Builds message from exception's argument(s).
 *
 * @author personf8e9850ed756
 * @todo better javadoc
 */
public interface Builder {
    // TODO should it take the exception
    String build(Object... arguments);
}
