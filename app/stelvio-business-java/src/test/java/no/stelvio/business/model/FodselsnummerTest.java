package no.stelvio.business.model;

import org.junit.Test;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class FodselsnummerTest {

    @Test(expected = IllegalArgumentException.class)
    public void emptyNrShouldThrowException() {
        new Fodselsnummer("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullShouldThrowException() {
        new Fodselsnummer(null);
    }

    @Test
    public void okNrNoProblem() {
        new Fodselsnummer("12345678901");
    }
}
