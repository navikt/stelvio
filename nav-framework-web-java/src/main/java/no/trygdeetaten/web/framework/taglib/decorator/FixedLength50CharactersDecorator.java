package no.trygdeetaten.web.framework.taglib.decorator;

/**
 * Truncates a String at 50 characters.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: FixedLength50CharactersDecorator.java 1681 2004-12-10 15:03:55Z psa2920 $
 */
public class FixedLength50CharactersDecorator extends FixedLengthCharactersDecorator {

	/**
	 * {@inheritDoc}
	 * @see no.trygdeetaten.web.framework.taglib.decorator.FixedLengthCharactersDecorator#getMaxLength()
	 */
	protected int getMaxLength() {
		return 50;
	}
}
