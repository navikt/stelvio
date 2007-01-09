package no.stelvio.common.error.support;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class ErrorDefinitionRetrieverFailedException extends RuntimeException {
	public ErrorDefinitionRetrieverFailedException(Throwable cause, String message) {
		super(message, cause);
	}
}
