package no.stelvio.web.framework.taglib.support;

import no.stelvio.web.framework.taglib.support.DateSupport;
import junit.framework.TestCase;

/**
 * Unit test for {@link DateSupport}.
 *
 * @author personf8e9850ed756
 * @version $Revision: $, $Date: $
 */
public class DateSupportTest extends TestCase {

	public void testNullReturnsNonBreakSpace() {
		assertEquals("Wrong value returned", DateSupport.NON_BREAK_SPACE, DateSupport.blankToNonBreakSpace(null));
	}

	public void testBlankReturnsNonBreakSpace() {
		assertEquals("Wrong value returned", DateSupport.NON_BREAK_SPACE, DateSupport.blankToNonBreakSpace(""));
		assertEquals("Wrong value returned", DateSupport.NON_BREAK_SPACE, DateSupport.blankToNonBreakSpace(" "));
	}

	public void testNonEmptyReturnsOriginalValue() {
		assertEquals("Wrong value returned", "test", DateSupport.blankToNonBreakSpace("test"));
	}
}
