package no.stelvio.common.error.retriever;

/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
public class RetrieverFailedException extends RuntimeException {
	public RetrieverFailedException(String message) {
		super(message);
	}

	public RetrieverFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}
