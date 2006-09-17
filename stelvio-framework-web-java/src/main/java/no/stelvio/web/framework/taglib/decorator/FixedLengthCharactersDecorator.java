package no.stelvio.web.framework.taglib.decorator;

import org.displaytag.decorator.ColumnDecorator;
import org.displaytag.exception.DecoratorException;

/**
 * Truncates a String at a certain number of characters. The last 3 characters will be '...'.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: FixedLengthCharactersDecorator.java 2040 2005-03-03 13:00:29Z psa2920 $
 */
public abstract class FixedLengthCharactersDecorator implements ColumnDecorator {

	/** HTML representation of a space. */
	private static final String NBSP = "&nbsp;";

	/**
	 * Returns the max length of the String to be returned from the <code>decorate()</code> method.
	 * 
	 * @return the maximum length.
	 */
	protected abstract int getMaxLength();

	/**
	 * Decorates the column value. Null and 0 length Strings will be decorated as &amp;nbsp;.
	 * Only Strings where length is greater than <code>getMaxLength()</code> characters will be decorated.
	 * 
	 * {@inheritDoc}
	 * @see org.displaytag.decorator.ColumnDecorator#decorate(java.lang.Object)
	 */
	public final String decorate(Object columnValue) throws DecoratorException {

		if (null == columnValue) {
			return NBSP;
		}

		try {
			String value = ((String) columnValue).trim();

			if (0 == value.length()) {
				return NBSP;
			} else if (getMaxLength() < value.length()) {
				return new StringBuffer(getMaxLength()).append(value.substring(0, getMaxLength() - 3)).append("...").toString();
			} else {
				return value;
			}
		} catch (ClassCastException cce) {
			throw new DecoratorException(
				this.getClass(),
				"Expected object of type java.lang.String, got an object of type "
					+ columnValue.getClass().getName()
					+ ", value was "
					+ columnValue.toString(),
				cce);
		}
	}
}
