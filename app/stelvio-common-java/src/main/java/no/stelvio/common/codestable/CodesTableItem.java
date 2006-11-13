package no.stelvio.common.codestable;

import java.util.Locale;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CodesTableItem represents an item in a <code>CodesTable</code>.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodesTableItem extends AbstractCodesTableItem {

	/**
	 * Constructor for <code>CodesTableItem</code>.
	 */
	public CodesTableItem(){
	}
	
	/**
	 * Constructor for an item, initializing its attributes.
	 * @param code the code.
	 * @param decode the decode.
	 * @param locale the locale of the item.
	 * @param isvalid validity of the item.
	 */
	public CodesTableItem(String code, String decode, Locale locale, Boolean isValid){
		this.code = code;
		this.decode = decode;
		this.locale = locale;
		this.isValid = isValid;
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
			.append("locale", getLocale())
			.append("isValid", getIsValid())
			.toString();
	}
}