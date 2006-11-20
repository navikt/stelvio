package no.stelvio.common.event;

import java.util.HashSet;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import no.stelvio.common.event.audit.AuditEvent;
import no.stelvio.common.event.audit.AuditItem;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo should check out how to publish an AuditEvent by having a bean that is ApplicationEventPublisherAware
 * that publishees an AuditEvent and our own test ApplicationEventListener
 * @todo should be an integration test.
 */
public class EventIntegrationTest extends AbstractDependencyInjectionSpringContextTests {
    private TestBean bean;
    private TestApplicationEventListener event;

    protected String[] getConfigLocations() {
        return new String[] { "test-event.xml" };
    }

    public void testPublishOk() throws Exception {
        bean.publishEvent();

        assertSame("Should be the test bean instance", bean, event.event.getSource());
        assertEquals("Not correct message", "message", event.event.getMessage());
        assertEquals("Not correct userLogin", "userLogin", event.event.getUserLogin());
        assertEquals("Not correct userLocation", "userLocation", event.event.getUserLocation());
        assertEquals("Not correct size of audit items", 1, event.event.getAuditItems().size());
    }

    /**
     * Will be populated by Spring from the context.
     *
     * @param bean the TestBean to use in this test.
     */
    public void setBean(TestBean bean) {
        this.bean = bean;
    }

    /**
     * Will be populated by Spring from the context.
     *
     * @param event the TestApplicationEventListener to use in this test.
     */
    public void setEvent(TestApplicationEventListener event) {
        this.event = event;
    }

    private static class TestBean implements ApplicationEventPublisherAware {
        private ApplicationEventPublisher applicationEventPublisher;

        public void publishEvent() {
            HashSet<AuditItem> auditItems = new HashSet<AuditItem>();
            auditItems.add(new AuditItem("description", "auditable"));
            applicationEventPublisher.publishEvent(
                    new AuditEvent(this, "message", "userLogin", "userLocation", auditItems));
        }

        public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
            this.applicationEventPublisher = applicationEventPublisher;
        }
    }

    // Just having this in Spring's context file will register it with the application context.
    private static class TestApplicationEventListener implements ApplicationEventListener {
        private AuditEvent event;

        public void onApplicationEvent(ApplicationEvent event) {
            if (event instanceof AuditEvent) {
                this.event = (AuditEvent) event;
            }
        }
    }
}
