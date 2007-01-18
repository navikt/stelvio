package no.stelvio.common.error.support;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit test for {@link no.stelvio.common.error.support.ErrorDefinition}.
 *
 * @author personf8e9850ed756
 */
public class ErrorDefinitionTest {
    @Test
    public void buildCompleteObject() {
        ErrorDefinition errorDefinition = new ErrorDefinition.Builder(String.class.getName()).
                message("An error messageFrom").
                severity(Severity.WARN).
                shortDescription("A short description").
                longDescription("A longer description").build();

        assertEquals("Wrong className;", "java.lang.String", errorDefinition.getClassName());
        assertEquals("Wrong messageFrom;", "An error messageFrom", errorDefinition.getMessage());
        assertEquals("Wrong severity;", Severity.WARN, errorDefinition.getSeverity());
        assertEquals("Wrong shortDescription;", "A short description", errorDefinition.getShortDescription());
        assertEquals("Wrong longDescription;", "A longer description", errorDefinition.getLongDescription());
    }
}
