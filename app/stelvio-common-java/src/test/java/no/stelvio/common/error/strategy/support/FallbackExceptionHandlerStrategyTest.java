package no.stelvio.common.error.strategy.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsSame.same;
import org.junit.Test;

/**
 * Unit test for {@link FallbackExceptionHandlerStrategy}.
 *
 * @author personf8e9850ed756
 */
public class FallbackExceptionHandlerStrategyTest {
    @Test
    public void sameExceptionIsRethrown() throws Throwable {
        FallbackExceptionHandlerStrategy handler = new FallbackExceptionHandlerStrategy();
        IllegalStateException original = new IllegalStateException();

        try {
            handler.handleException(original);
        } catch (IllegalStateException e) {
            assertThat(e, same(original));
        }
    }
}
