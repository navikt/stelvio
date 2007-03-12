package no.stelvio.common.codestable;

import java.util.Locale;

import org.apache.commons.lang.builder.ToStringBuilder;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;

/**
 * CodesTableItem represents an item in a <code>CodesTable</code>.
 *
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public abstract class CodesTableItem extends AbstractCodesTableItem {
	private static final long serialVersionUID = 8512658758978068886L;

	/** Constructor for <code>CodesTableItem</code>. Only available to subclasses. */
	protected CodesTableItem() {
	}

	/**
	 * Constructor for an item, initializing its attributes.
	 *
	 * @param code the code.
	 * @param decode the decode.
	 * @param isValid validity of the item.
	 * @deprecated CodesTableItems will no longer expose Locale to client. Replaced by {@link CodesTableItem(String,
	 *             String, boolean)}
	 */
	@Deprecated
	public CodesTableItem(String code, String decode, Locale locale, boolean isValid) {
		super(code, decode, isValid);
	}

	/**
	 * Constructor for an item, initializing its attributes.
	 *
	 * @param code the code.
	 * @param decode the decode.
	 * @param isValid validity of the item.
	 */
	public CodesTableItem(String code, String decode, boolean isValid) {
		super(code, decode, isValid);
	}

	/**
	 * Returns a <Code>String</code> representation of this object.
	 * <p/>
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this)
				.append("code", getCode())
				.append("decode", getDecode())
				.append("isValid", isValid())
				.toString();
	}
}