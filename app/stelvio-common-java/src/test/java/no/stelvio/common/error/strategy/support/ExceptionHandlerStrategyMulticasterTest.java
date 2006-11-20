package no.stelvio.common.error.strategy.support;

import java.util.ArrayList;

import org.hamcrest.MatcherAssert;
import static org.hamcrest.core.IsSame.same;
import org.jmock.InThisOrder;
import org.jmock.Mockery;
import org.junit.Test;

import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;

/**
 * Unit test for {@link ExceptionHandlerStrategyMulticaster}.
 *
 * @author personf8e9850ed756
 */
public class ExceptionHandlerStrategyMulticasterTest {
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

        ArrayList<ExceptionHandlerStrategy> strategies = new ArrayList<ExceptionHandlerStrategy>();
        strategies.add(strategy1);
        strategies.add(strategy2);

        ExceptionHandlerStrategyMulticaster strategyMulticaster = new ExceptionHandlerStrategyMulticaster();
        strategyMulticaster.setStrategies(strategies);
        IllegalStateException result = strategyMulticaster.handleException(exception);

        MatcherAssert.assertThat(result, same(exception));
        context.assertIsSatisfied();
    }
}

