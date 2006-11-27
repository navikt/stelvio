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
    public Collection<Err> retrieve() {
        HashSet<Err> errors = new HashSet<Err>();
        errors.add(new Err.Builder(Throwable.class.getName()).message("error: {0}").severity(Severity.FATAL).build());

        return errors;
    }
}
