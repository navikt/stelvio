package no.trygdeetaten.web.framework.taglib.decorator;

import java.util.Date;

import org.displaytag.decorator.ColumnDecorator;
import org.displaytag.exception.DecoratorException;

import no.trygdeetaten.common.framework.util.DateUtil;
import no.trygdeetaten.web.framework.taglib.support.DateSupport;

/**
 * DisplayTag decorator which formats a date.
 * 
 * @author person356941106810, Accenture
 * @version $Id: DateDecorator.java 2713 2005-12-15 12:24:55Z skb2930 $
 */
public class DateDecorator implements ColumnDecorator {
	/**
	 * Decorates a column value by turning a <code>java.util.Date</code> into a string on the format dd.MM.yyyy.
	 *
	 * @see ColumnDecorator#decorate(Object)
	 */
	public String decorate(Object date) throws DecoratorException {
		if (null == date) {
			return DateSupport.NON_BREAK_SPACE;
		}

		if (!(date instanceof Date)) {
			throw new DecoratorException(
				getClass(),
			    "Expected object of type java.util.Date, got object of type "
			    + date.getClass().getName()
			    + ": "
			    + date.toString());
		}

		final String formatted = DateUtil.format((Date) date);

		return DateSupport.blankToNonBreakSpace(formatted);
	}
}
