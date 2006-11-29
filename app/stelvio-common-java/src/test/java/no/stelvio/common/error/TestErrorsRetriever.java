package no.stelvio.common.error;

import java.util.Collection;
import java.util.HashSet;

import no.stelvio.common.error.support.ErrorsRetriever;

/**
 * An integration test implementation of {@link ErrorsRetriever}.
 *
 * @author personf8e9850ed756
 */
public class TestErrorsRetriever implements ErrorsRetriever {
    public Collection<ErrorDefinition> retrieve() {
        HashSet<ErrorDefinition> errors = new HashSet<ErrorDefinition>();
        errors.add(new ErrorDefinition.Builder(Throwable.class.getName()).message("error: {0}").severity(Severity.FATAL).build());

        return errors;
    }
}
