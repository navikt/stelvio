package no.stelvio.common.error;

import com.agical.rmock.extension.junit.RMockTestCase;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class ErrTest extends RMockTestCase {
    public void testIdShouldBeSetCorrectory() {
        Err err = new Err.Builder("id").build();

        assertThat(err.getId(), is.eq("id"));
    }
}
