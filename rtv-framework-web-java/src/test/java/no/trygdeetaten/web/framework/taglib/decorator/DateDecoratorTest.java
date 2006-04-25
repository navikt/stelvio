package no.trygdeetaten.web.framework.taglib.decorator;

import java.util.Date;

import junit.framework.TestCase;
import org.displaytag.exception.DecoratorException;

import no.trygdeetaten.common.framework.util.DateUtil;
import no.trygdeetaten.web.framework.taglib.support.DateSupport;

/**
 * Unit test for {@link DateDecorator}.
 *
 * @author personf8e9850ed756
 * @version $Revision: $, $Date: $
 */
public class DateDecoratorTest extends TestCase {
	private DateDecorator dateDecorator;

	public void testNullDateReturnsNbsp() throws DecoratorException {
		assertEquals("Not the correct value returned;", DateSupport.NON_BREAK_SPACE, dateDecorator.decorate(null));
	}

	public void testEternityReturnsNbsp() throws DecoratorException {
		assertEquals("Not the correct value returned;", DateSupport.NON_BREAK_SPACE, dateDecorator.decorate(DateUtil.ETERNITY));
	}

	public void testFormatsDateCorrectly() throws DecoratorException {
		assertEquals("Not the correct value returned;", "12.08.1995", dateDecorator.decorate(new Date(808178400000L)));
	}

	protected void setUp() throws Exception {
		dateDecorator = new DateDecorator();
	}
}
