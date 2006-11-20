package no.stelvio.common.error;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit test for {@link Err}.
 *
 * @author personf8e9850ed756
 */
public class ErrTest {
    @Test
    public void buildCompleteObject() {
        Err err = new Err.Builder(String.class.getName()).
                message("An error messageFrom").
                severity(Severity.WARN).
                shortDescription("A short description").
                longDescription("A longer description").build();

        assertEquals("Wrong className;", "java.lang.String", err.getClassName());
        assertEquals("Wrong messageFrom;", "An error messageFrom", err.getMessage());
        assertEquals("Wrong severity;", Severity.WARN, err.getSeverity());
        assertEquals("Wrong shortDescription;", "A short description", err.getShortDescription());
        assertEquals("Wrong longDescription;", "A longer description", err.getLongDescription());
    }
}
