package no.stelvio.common.event.audit;

import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Unit test for {@link AuditItem}.
 *
 * @author personf8e9850ed756
 */
public class AuditItemTest {
    @Test
    public void constructorArgumentsAreMandatory() {
        createAuditItemAndCheckForMissingInputFailure(null, new Object());
        createAuditItemAndCheckForMissingInputFailure("description", null);
    }

    private void createAuditItemAndCheckForMissingInputFailure(String description, Object auditable) {
        try {
            new AuditItem(description, auditable);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // should happen
        }
    }
}
