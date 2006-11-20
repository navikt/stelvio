package no.stelvio.common.error.strategy.support;

import java.util.HashSet;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;
import no.stelvio.common.event.audit.AuditEvent;
import no.stelvio.common.event.audit.AuditItem;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo maybe this should be in the event package so not to get circular dependencies between the event and error
 * packages
 * @todo implement this fully when we know how CEI works.
 */
public class EventPublisherExceptionHandlerStrategy implements ExceptionHandlerStrategy, ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    public <T extends Throwable> T handleException(T throwable) throws T {
        applicationEventPublisher.publishEvent(createAuditEvent());
        return throwable;
    }

    private <T extends Throwable> AuditEvent createAuditEvent() {
        HashSet<AuditItem> items = new HashSet<AuditItem>();
        items.add(new AuditItem("description", "dummyAuditable"));
        
        return new AuditEvent(this, "dummy message", "userLogin", "userLocation", items);
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
