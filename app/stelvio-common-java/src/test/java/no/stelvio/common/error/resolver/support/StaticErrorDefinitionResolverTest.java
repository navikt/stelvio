package no.stelvio.common.error.resolver.support;

import java.util.Collection;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.isNotNull;
import org.junit.Before;
import org.junit.Test;

import no.stelvio.common.error.ErrorHandlingException;
import no.stelvio.common.error.StelvioException;
import no.stelvio.common.error.TestUnrecoverableException;
import no.stelvio.common.error.resolver.ErrorDefinitionNotFoundException;
import no.stelvio.common.error.resolver.ErrorDefinitionResolver;
import no.stelvio.common.error.resolver.ErrorDefinitionResolverTest;
import no.stelvio.common.error.support.ErrorDefinition;

/**
 * Unit test for {@link StaticErrorDefinitionResolver}.
 *
 * @author personf8e9850ed756
 */
public class StaticErrorDefinitionResolverTest extends ErrorDefinitionResolverTest {
    private StaticErrorDefinitionResolver staticErrorResolver;

    @Test
    public void walkTheInheritanceHierarchyWhenFindingError() {
        ErrorDefinition error = staticErrorResolver.resolve(new ErrorDefinitionNotFoundException(ErrorHandlingException.class));
        assertThat(error, isNotNull());
    }

	/**
	 * The definition of an error has a class name that is not available in the classpath when the module gets initalized
	 */
	@Test(expected = IllegalStateException.class)
	public void errorDefinitionHasUnavailableClassNameThrowsException() {
		Collection<ErrorDefinition> errors = new HashSet<ErrorDefinition>();
		errors.add(new ErrorDefinition.Builder("not.a.class.name").message("test: {0}").build());

		new StaticErrorDefinitionResolver(errors);
	}

	@Test(expected = IllegalStateException.class)
	public void errorDefinitionWithWrongNumberOfArgumentsForStelvioExceptionThrowsException() {
		staticErrorResolver.resolve(new TestUnrecoverableException("test"));
	}

	@Test(expected = IllegalStateException.class)
	public void errorDefinitionWithWrongNumberOfArgumentsForOtherExceptionThrowsException() {
		staticErrorResolver.resolve(new Throwable("message"));
	}

	@Before
    public void setupErrorResolver() {
        Collection<ErrorDefinition> errors = new HashSet<ErrorDefinition>();
        errors.add(new ErrorDefinition.Builder(StelvioException.class).message("test: {0}").build());
        errors.add(new ErrorDefinition.Builder(TestUnrecoverableException.class).message("test: {0}{1}").build());
        errors.add(new ErrorDefinition.Builder(Throwable.class).message("test: {0}{1}").build());

        staticErrorResolver = new StaticErrorDefinitionResolver(errors);
    }

    protected ErrorDefinitionResolver errorDefinitionResolver() {
        return staticErrorResolver;
    }
}
