package no.stelvio.common.event.audit;

import java.util.HashSet;

import no.stelvio.common.event.ApplicationEvent;
import no.stelvio.common.event.ApplicationEventTest;

/**
 * Unit test for {@link AuditEvent}.
 *
 * @author personf8e9850ed756
 */
public class AuditEventTest extends ApplicationEventTest {
    public void testConstructorArgumentsAreMandatory() {
        HashSet<AuditItem> auditItems = createAuditItems();

        createAuditEventAndCheckForMissingInputFailure(null, "userLogin", "userLocation", auditItems);
        createAuditEventAndCheckForMissingInputFailure("message", null, "userLocation", auditItems);
        createAuditEventAndCheckForMissingInputFailure("message", "userLogin", null, auditItems);
        createAuditEventAndCheckForMissingInputFailure("message", "userLogin", "userLocation", null);
    }

    public void testAuditInfoIsSaved() {
        AuditEvent event = (AuditEvent) getEvent();

        assertEquals("Message is not correct", event.getMessage(), "message");
        assertEquals("User login is not correct", event.getUserLogin(), "userLogin");
        assertEquals("User location is not correct", event.getUserLocation(), "userLocation");
        assertEquals("1 audit item should be present", event.getAuditItems().size(), 1);
    }

    private void createAuditEventAndCheckForMissingInputFailure(
            String message, String userLogin, String location, HashSet<AuditItem> auditItems) {
        try {
            new AuditEvent(this, message, userLogin, location, auditItems);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // should happen
        }
    }

    protected ApplicationEvent createApplicationEvent() {
        return new AuditEvent(this, "message", "userLogin", "userLocation", createAuditItems());
    }

    private HashSet<AuditItem> createAuditItems() {
        HashSet<AuditItem> auditItems = new HashSet<AuditItem>();
        auditItems.add(new AuditItem("description", "auditable"));
        
        return auditItems;
    }
}
