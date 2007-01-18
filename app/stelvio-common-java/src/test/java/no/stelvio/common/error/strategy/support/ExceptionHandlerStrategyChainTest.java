package no.stelvio.common.error.strategy.support;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsSame.same;
import org.jmock.InThisOrder;
import org.jmock.Mockery;
import org.junit.Test;

import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;

/**
 * Unit test for {@link ExceptionHandlerStrategyChain}.
 *
 * @author personf8e9850ed756
 */
public class ExceptionHandlerStrategyChainTest {
    @Test
    public void shouldCallEachMemberInTheList() {
        Mockery context = new Mockery();
        final ExceptionHandlerStrategy strategy1 = context.mock(ExceptionHandlerStrategy.class, "strategy1");
        final ExceptionHandlerStrategy strategy2 = context.mock(ExceptionHandlerStrategy.class, "strategy2");
        final IllegalStateException exception = new IllegalStateException();

        context.expects(new InThisOrder() {{
            one (strategy1).handleException(exception); will(returnValue(exception));
            one (strategy2).handleException(exception); will(returnValue(exception));
        }});

        Set<ExceptionHandlerStrategy> strategies = new HashSet<ExceptionHandlerStrategy>(2);
        strategies.add(strategy1);
        strategies.add(strategy2);

        ExceptionHandlerStrategyChain strategyChain = new ExceptionHandlerStrategyChain();
        strategyChain.setStrategies(strategies);
        IllegalStateException result = strategyChain.handleException(exception);

        assertThat(result, same(exception));
        context.assertIsSatisfied();
    }
}

