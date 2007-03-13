package no.stelvio.common.codestable;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author personf8e9850ed756
 * @todo add javadoc
 */
public class CodesTableNotFoundExceptionTest {
    @Test
    public void messageIsCorrect() {
        CodesTableNotFoundException ctnf = new CodesTableNotFoundException(Cti.class);

        assertEquals("Not the correct message; ", "Codestable x not found", ctnf.getMessage());
    }

    private static class Cti extends CodesTableItem<TestCtiCode, String> {
        public String toString() {
            return "cti";
        }
    }
}
