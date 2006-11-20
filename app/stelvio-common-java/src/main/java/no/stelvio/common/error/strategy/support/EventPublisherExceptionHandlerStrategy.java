package no.stelvio.common.error.strategy.support;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo maybe this should be in the event package so not to get circular dependencies between the event and error
 * packages
 */
public class EventPublisherExceptionHandlerStrategy implements ExceptionHandlerStrategy, ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    public <T extends Throwable> T handleException(T throwable) throws T {

        return throwable;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
