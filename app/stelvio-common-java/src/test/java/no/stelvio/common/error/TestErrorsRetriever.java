package no.stelvio.common.error;

import no.stelvio.common.error.support.ErrorsRetriever;

import java.util.Collection;
import java.util.HashSet;

/**
 * An integration test implementation of {@link ErrorsRetriever}.
 *
 * @author personf8e9850ed756
 */
public class TestErrorsRetriever implements ErrorsRetriever {
	private static boolean throwException = false;

	public Collection<ErrorDefinition> retrieve() {
        HashSet<ErrorDefinition> errors = new HashSet<ErrorDefinition>();
        errors.add(new ErrorDefinition.Builder(Throwable.class.getName()).message("error: {0}").severity(Severity.FATAL).build());

	    if (throwException) {
		    throw new IllegalStateException("illegal state");
	    }

        return errors;
    }

	public static void setThrowException(final boolean throwException) {
		TestErrorsRetriever.throwException = throwException;
	}
}
