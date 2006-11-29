package no.stelvio.common.error.support;

import java.util.Collection;
import java.util.HashSet;

import org.hamcrest.MatcherAssert;
import static org.hamcrest.core.IsNull.isNotNull;
import org.junit.Before;
import org.junit.Test;

import no.stelvio.common.error.ErrorDefinition;
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
        ErrorDefinition error = defaultErrorResolver.resolve(new ErrorNotFoundException(ErrorHandlingException.class));
        MatcherAssert.assertThat(error, isNotNull());
    }

    @Before
    public void setupErrorResolver() {
        Collection<ErrorDefinition> errors = new HashSet<ErrorDefinition>();
        errors.add(new ErrorDefinition.Builder(StelvioException.class).message("test: {0}").build());

        defaultErrorResolver = new DefaultErrorResolver(errors);
    }

    protected ErrorResolver errorResolver() {
        return defaultErrorResolver;
    }
}
