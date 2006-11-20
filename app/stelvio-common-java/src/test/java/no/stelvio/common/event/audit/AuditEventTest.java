package no.stelvio.common.event.audit;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.eq;
import static org.junit.Assert.fail;
import org.junit.Test;

import no.stelvio.common.event.ApplicationEvent;
import no.stelvio.common.event.ApplicationEventTest;

/**
 * Unit test for {@link AuditEvent}.
 *
 * @author personf8e9850ed756
 */
public class AuditEventTest extends ApplicationEventTest {
    @Test
    public void constructorArgumentsAreMandatory() {
        HashSet<AuditItem> auditItems = createAuditItems();

        createAuditEventAndCheckForMissingInputFailure(null, "userLogin", "userLocation", auditItems);
        createAuditEventAndCheckForMissingInputFailure("message", null, "userLocation", auditItems);
        createAuditEventAndCheckForMissingInputFailure("message", "userLogin", null, auditItems);
        createAuditEventAndCheckForMissingInputFailure("message", "userLogin", "userLocation", null);
    }

    @Test
    public void auditInfoIsSaved() {
        AuditEvent event = (AuditEvent) getEvent();

        assertThat(event.getMessage(), eq("message"));
        assertThat(event.getUserLogin(), eq("userLogin"));
        assertThat(event.getUserLocation(), eq("userLocation"));
        assertThat(event.getAuditItems().size(), eq(1));
    }

    protected ApplicationEvent createApplicationEvent() {
        return new AuditEvent(this, "message", "userLogin", "userLocation", createAuditItems());
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

    private HashSet<AuditItem> createAuditItems() {
        HashSet<AuditItem> auditItems = new HashSet<AuditItem>();
        auditItems.add(new AuditItem("description", "auditable"));
        
        return auditItems;
    }
}
