package no.stelvio.common.error.support;

import java.util.Collection;
import java.util.HashSet;

import org.hamcrest.MatcherAssert;
import static org.hamcrest.core.IsNull.isNotNull;
import org.junit.Before;
import org.junit.Test;

import no.stelvio.common.error.Err;
import no.stelvio.common.error.ErrorHandlingException;
import no.stelvio.common.error.ErrorNotFoundException;
import no.stelvio.common.error.ErrorResolver;
import no.stelvio.common.error.ErrorResolverTest;
import no.stelvio.common.error.StelvioException;

/**
 * Unit test for {@link DefaultErrorResolver}.
 *
 * @author personf8e9850ed756
 */
public class DefaultErrorResolverTest extends ErrorResolverTest {
    private DefaultErrorResolver defaultErrorResolver;

    @Test
    public void walkTheInheritanceHierarchyWhenFindingError() {
        Err error = defaultErrorResolver.resolve(new ErrorNotFoundException(ErrorHandlingException.class));
        MatcherAssert.assertThat(error, isNotNull());
    }

    @Before
    public void setupErrorResolver() {
        Collection<Err> errors = new HashSet<Err>();
        errors.add(new Err.Builder(StelvioException.class).message("test: {0}").build());

        defaultErrorResolver = new DefaultErrorResolver(errors);
    }

    protected ErrorResolver errorResolver() {
        return defaultErrorResolver;
    }
}
