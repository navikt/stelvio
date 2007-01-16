package no.stelvio.common.codestable;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.support.AbstractCodesTableItem;

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

    private static class Cti extends AbstractCodesTableItem {
        public String toString() {
            return "cti";
        }
    }
}
