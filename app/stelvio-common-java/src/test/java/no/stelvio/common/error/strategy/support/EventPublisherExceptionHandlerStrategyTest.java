package no.stelvio.common.error.strategy.support;

import org.hamcrest.MatcherAssert;
import static org.hamcrest.core.IsInstanceOf.isA;
import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import no.stelvio.common.event.ApplicationEvent;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class EventPublisherExceptionHandlerStrategyTest {
    private EventPublisherExceptionHandlerStrategy strategy;

    @Test
    public void shouldPublishEventForException() throws Throwable {
        Mockery context = new Mockery();
        final ApplicationEventPublisher eventPublisher = context.mock(ApplicationEventPublisher.class);
        strategy.setApplicationEventPublisher(eventPublisher);

        context.expects(new InAnyOrder() {{
            one (eventPublisher).publishEvent(new ApplicationEvent(this){});
        }});

        strategy.handleException(new Throwable());
    }

    @Test
    public void shouldImplementApplicationEventPublisherAware() {
        MatcherAssert.assertThat(strategy, isA(ApplicationEventPublisherAware.class));
    }

    @Before
    public void setupStrategy() {
        strategy = new EventPublisherExceptionHandlerStrategy();
    }
}
