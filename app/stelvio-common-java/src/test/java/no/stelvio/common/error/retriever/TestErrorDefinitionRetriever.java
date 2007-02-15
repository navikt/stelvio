package no.stelvio.common.error.retriever;

import java.util.HashSet;
import java.util.Set;

import no.stelvio.common.error.support.ErrorDefinition;
import no.stelvio.common.error.support.Severity;

/**
 * An integration test implementation of {@link ErrorDefinitionRetriever}.
 *
 * @author personf8e9850ed756
 */
public class TestErrorDefinitionRetriever implements ErrorDefinitionRetriever {
	private static boolean throwException = false;

	public Set<ErrorDefinition> retrieve() {
        HashSet<ErrorDefinition> errors = new HashSet<ErrorDefinition>();
        errors.add(new ErrorDefinition.Builder(Throwable.class.getName()).message("error: {0}").severity(Severity.FATAL).build());

	    if (throwException) {
		    throw new IllegalStateException("illegal state");
	    }

        return errors;
    }

	public static void setThrowException(final boolean throwException) {
		TestErrorDefinitionRetriever.throwException = throwException;
	}
}
