package no.stelvio.common.event.audit;

import junit.framework.TestCase;

/**
 * Unit test for {@link AuditItem}.
 *
 * @author personf8e9850ed756
 */
public class AuditItemTest extends TestCase {
    public void testConstructorArgumentsAreMandatory() {
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
