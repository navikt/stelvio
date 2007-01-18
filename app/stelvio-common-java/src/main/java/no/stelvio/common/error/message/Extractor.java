package no.stelvio.common.error.message;

/**
 * Used to extract a messageFrom which is used to describe an exception.
 *
 * @author personf8e9850ed756
 * @todo better javadoc
 * @todo is extractor a good name?
 */
public interface Extractor {
    /**
     * Retrieves the message that describes the given exception.
     *
     * @param throwable the exception to retrieve a message for.
     * @return the message that describes the given exception.
     */
    String messageFor(Throwable throwable);
}
