package no.stelvio.web.framework.taglib.decorator;

import org.displaytag.decorator.ColumnDecorator;
import org.displaytag.exception.DecoratorException;

/**
 * Formaterer et 11-tegns fødselsnummer på format XXXXXX XXXXX
 * 
 * @author person17040a0d57d6, Accenture
 * @version $Id: FNRDecorator.java 1707 2004-12-16 11:15:20Z tsb2920 $
 */
public class FNRDecorator implements ColumnDecorator {

	private static final String NBSP = "&nbsp;";

	/**
	 * {@inheritDoc}
	 * @see org.displaytag.decorator.ColumnDecorator#decorate(java.lang.Object)
	 */
	public String decorate(Object object) throws DecoratorException {

		if (null == object) {
			return NBSP;
		}

		try {
			String value = ((String) object).trim();
			if (value.length() == 0) {
				return NBSP;
			} else if (value.length() == 11) {
				return new StringBuffer(17).append(value.substring(0, 6)).append(NBSP).append(value.substring(6)).toString();
			} else {
				return value;
			}
		} catch (ClassCastException cce) {
			throw new DecoratorException(FNRDecorator.class, "Kan bare dekorere objekter av typen java.lang.String", cce);
		}
	}
}