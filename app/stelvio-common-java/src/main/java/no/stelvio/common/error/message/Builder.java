package no.stelvio.common.error.message;

/**
 * Builds message from exception's argument(s).
 *
 * @author personf8e9850ed756
 * @todo do we need this?
 */
public interface Builder {
    String build(Object... arguments);
}
