package no.stelvio.common.error.strategy.support;

import static org.hamcrest.MatcherAssert.assertThat;
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
            one (eventPublisher).publishEvent(with(an(ApplicationEvent.class)));
        }});

        strategy.handleException(new Throwable());
        context.assertIsSatisfied();
    }

    @Test
    public void shouldImplementApplicationEventPublisherAware() {
        assertThat(strategy, isA(ApplicationEventPublisherAware.class));
    }

    @Before
    public void setupStrategy() {
        strategy = new EventPublisherExceptionHandlerStrategy();
    }
}
