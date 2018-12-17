package no.stelvio.common.error.message.support;

import no.stelvio.common.error.message.Extractor;

/**
 * 
 * @author personf8e9850ed756
 */
public class FromExceptionExtractor implements Extractor {

	@Override
	public String messageFor(Throwable throwable) {
		return throwable.getLocalizedMessage();
	}
}
