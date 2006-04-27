package no.nav.web.framework.taglib.decorator;

import org.displaytag.decorator.ColumnDecorator;

import no.nav.web.framework.taglib.support.AmountSupport;

/**
 * Formats a number using the following rules:
 *
 * <ul>
 *   <li> Fractions are removed, that is: 10,40 becomes 10
 *   <li> Spaces are used as thousands separator, that is: 10 000 000
 *   <li> Negative numbers are shown with '-' in front, that is: -10 000
 * </ul>
 *
 * If the input parameter is a String it will be checked for the decimal signs ',' or '.' which will be removed together with
 * the fraction. The result is converted to an Integer.
 * If the input parameter is neither a String nor a Number, the input parameters <code>toString()</code> is returned.
 * All blank spaces will be converted to <code>&#38;nbsp;</code>.
 *
 * @author person17040a0d57d6, Accenture
 * @author Stig Kleppe-J&oslash;rgensen, Accenture
 */
public class AmountDecorator implements ColumnDecorator {
	/**
	 * {@inheritDoc}
	 */
	public String decorate(Object columnValue) {
		return AmountSupport.formatAmount(columnValue);
	}
}