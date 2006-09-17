package no.stelvio.web.framework.taglib.support;

import junit.framework.TestCase;
import no.stelvio.web.framework.taglib.support.AmountSupport;

import org.displaytag.exception.DecoratorException;

/**
 * Unit test for {@link AmountSupport}.
 *
 * @author personf8e9850ed756
 * @version $Revision: $, $Date: $
 */
public class AmountSupportTest extends TestCase {

	public void testFormatsCorrectly() throws DecoratorException {
		assertEquals("Fractions not removed correctly; ",
		             "12&nbsp;341&nbsp;234",
		             AmountSupport.formatAmount("12341234.23"));
		assertEquals("Fractions not removed correctly; ", "2&nbsp;345", AmountSupport.formatAmount("2345,34"));
	}

	public void testBlankStringReturnsNbsp() throws DecoratorException {
		assertEquals("Blank string not handled correctly; ", "&nbsp;", AmountSupport.formatAmount(""));
		assertEquals("Blank string not handled correctly; ", "&nbsp;", AmountSupport.formatAmount(" "));
	}
}
