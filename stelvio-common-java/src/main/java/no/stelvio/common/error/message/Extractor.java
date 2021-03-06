package no.stelvio.common.error.message;

/**
 * Used to extract a message which is used to describe an exception.
 *
 */
public interface Extractor {
	/**
	 * Extract the message that describes the given exception.
	 *
	 * @param throwable the exception to extract a message for.
	 * @return the message that describes the given exception.
	 */
	String messageFor(Throwable throwable);
}
