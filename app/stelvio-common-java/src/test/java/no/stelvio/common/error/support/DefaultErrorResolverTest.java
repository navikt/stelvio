package no.stelvio.common.error.support;

import no.stelvio.common.error.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.isNotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;

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
        assertThat(error, isNotNull());
    }

	/**
	 * The definition of an error has a class name that is not available in the classpath when the module gets initalized
	 */
	@Test(expected = IllegalStateException.class)
	public void errorDefinitionHasUnavailableClassNameThrowsException() {
		Collection<ErrorDefinition> errors = new HashSet<ErrorDefinition>();
		errors.add(new ErrorDefinition.Builder("not.a.class.name").message("test: {0}").build());

		new DefaultErrorResolver(errors);
	}

	@Test(expected = IllegalStateException.class) // TODO other exception
	public void errorDefinitionWithWrongNumberOfArgumentsForStelvioExceptionThrowsException() {
		defaultErrorResolver.resolve(new TestUnrecoverableException("test"));
	}

	@Test(expected = IllegalStateException.class) // TODO other exception
	public void errorDefinitionWithWrongNumberOfArgumentsForOtherExceptionThrowsException() {
		defaultErrorResolver.resolve(new Throwable("message"));
	}

	@Before
    public void setupErrorResolver() {
        Collection<ErrorDefinition> errors = new HashSet<ErrorDefinition>();
        errors.add(new ErrorDefinition.Builder(StelvioException.class).message("test: {0}").build());
        errors.add(new ErrorDefinition.Builder(TestUnrecoverableException.class).message("test: {0}{1}").build());
        errors.add(new ErrorDefinition.Builder(Throwable.class).message("test: {0}{1}").build());

        defaultErrorResolver = new DefaultErrorResolver(errors);
    }

    protected ErrorResolver errorResolver() {
        return defaultErrorResolver;
    }
}
