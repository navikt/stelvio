package no.nav.web.framework.taglib.decorator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.displaytag.decorator.ColumnDecorator;
import org.displaytag.exception.DecoratorException;

import no.nav.common.framework.util.DateUtil;

/**
 * DisplayTag decorator which formats a tps date.
 * @author Øyvin Jakobsen, Accenture
 * @version $Id: TpsDateDecorator.java 2815 2006-03-02 11:26:03Z skb2930 $
 */
public class TpsDateDecorator implements ColumnDecorator {
	private static final String NBSP = "&nbsp;";
	private static final String TPS_DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * Decorates a column value by turing a string date on the format yyy-MM-dd 
	 * into a string on the format dd.MM.yyyy
	 *
	 * @see org.displaytag.decorator.ColumnDecorator#decorate(java.lang.Object)
	 * @todo use DateUtil
	 */
	public String decorate(Object dateString) throws DecoratorException {
		if (dateString == null) {
			return NBSP;
		}
		
		if (dateString instanceof Date) {
			dateString = createDateFormat().format(dateString);
		}
		
		if (!(dateString instanceof String)) {
			throw new DecoratorException(this.getClass(), "Object is not of type java.lang.String");

		} else if ("".equals(((String) dateString).trim())) {
			return NBSP;
		}

		Date date = null;

		try {
			date = createDateFormat().parse((String) dateString);
		} catch (ParseException e) {
			throw new DecoratorException(
				this.getClass(),
				"Failed to parse the TPS date string: " + dateString + ",  errorDetails: " + e.getMessage());
		}

		String stringDate = DateUtil.format(date); // Don't display 31.12.9999

		if ("31.12.9999".equals(stringDate)) {
			return NBSP;
		} else {
			return stringDate;
		}
	}

	/**
	 * Creates a date formatter.
	 *
	 * @return a date formatter.
	 */
	private static DateFormat createDateFormat() {
		final SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat.getInstance();
		dateFormat.setLenient(false);
		dateFormat.applyLocalizedPattern(TPS_DATE_FORMAT);

		return dateFormat;
	}
}
