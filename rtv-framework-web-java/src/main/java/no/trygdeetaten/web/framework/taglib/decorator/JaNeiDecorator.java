package no.trygdeetaten.web.framework.taglib.decorator;

import org.displaytag.decorator.ColumnDecorator;
import org.displaytag.exception.DecoratorException;

/**
 * Formaterer en boolean til "OK" hvis den er true
 * 
 * @author person17040a0d57d6, Accenture
 * @version $Id: JaNeiDecorator.java 1883 2005-01-21 14:45:32Z rra2920 $
 */
public class JaNeiDecorator implements ColumnDecorator {

	private static final String NBSP = "&nbsp;";

	/**
	 * {@inheritDoc}
	 * @see org.displaytag.decorator.ColumnDecorator#decorate(java.lang.Object)
	 */
	public String decorate(Object object) throws DecoratorException {

		String returnValue = NBSP;

		if (null == object) {
			return returnValue;
		}

		try {
			Boolean value = (Boolean) object;
			
			if (value.booleanValue()) {
				returnValue = "Ja";
			} else {
				returnValue = "Nei";
			}
			
			return returnValue;
			
		} catch (ClassCastException cce) {
			throw new DecoratorException(FNRDecorator.class, "Kan bare dekorere objekter av typen java.lang.String", cce);
		}
	}
}