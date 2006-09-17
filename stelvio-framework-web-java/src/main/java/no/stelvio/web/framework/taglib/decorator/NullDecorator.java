package no.stelvio.web.framework.taglib.decorator;

import org.displaytag.decorator.ColumnDecorator;
import org.displaytag.exception.DecoratorException;

/**
 * Formats a null so that a nbsp is returned instead to prevent blank columns.
 * @author person356941106810, Accenture
 */
public class NullDecorator implements ColumnDecorator {
	
	/**
	 * {@inheritDoc}
	 * @see org.displaytag.decorator.ColumnDecorator#decorate(java.lang.Object)
	 */
	public String decorate(Object columnValue) throws DecoratorException {
		if (columnValue == null || "".equals(columnValue.toString().trim())) {
			return "&nbsp;";
		}
		return columnValue.toString();
	}

}
