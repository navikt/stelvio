package no.stelvio.common.codestable;

import java.util.Locale;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CodesTableItem represents an item in a <code>CodesTable</code>.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public abstract class CodesTableItem extends AbstractCodesTableItem {
	/**
	 * Constructor for <code>CodesTableItem</code>. Only available to subclasses.
	 */
	protected CodesTableItem(){
	}
	
	/**
	 * Constructor for an item, initializing its attributes.
	 * @deprecated CodesTableItems will no longer expose Locale to client. Replaced by {@link CodesTableItem(String, String, boolean)}
	 * @param code the code.
	 * @param decode the decode.
	 * @param locale the locale of the item.
	 * @param isValid validity of the item.
	 */
	@Deprecated
	public CodesTableItem(String code, String decode, Locale locale, boolean isValid){
		super(code, decode, isValid);
	}
	
	/**
	 * Constructor for an item, initializing its attributes.
	 * @param code the code.
	 * @param decode the decode.
	 * @param isValid validity of the item.
	 */
	public CodesTableItem(String code, String decode, boolean isValid){
		super(code, decode, isValid);
	}	
	
	/**
	 * Returns a <Code>String</code> representation of this object.
	 * 
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