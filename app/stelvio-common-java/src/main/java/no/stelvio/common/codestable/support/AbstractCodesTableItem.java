package no.stelvio.common.codestable.support;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Abstract class for the CodesTableItem/CodesTableItemPeriodic components.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @verion $Id$
 */
public abstract class AbstractCodesTableItem implements Serializable {
	/** A codestableitem's code. */
	private String code;
	
	/** A codestableitem's decode, i.e. a message. */
	private String decode;
	
	/** A codestableitem's locale that represents the country and language the items decode is defined for. */
	private Locale locale;
	
	/** Defines the validity of a codestableitem. */
	private Boolean isValid;
		
	/**
	 * Constructor for <code>AbstractCodesTableItem</code>. Only available to subclasses.
	 */
	protected AbstractCodesTableItem() {
	}
	
	/**
	 * Constructor for an item, initializing its attributes.
	 * 
	 * @param code the code.
	 * @param decode the decode.
	 * @param locale the locale of the item.
	 * @param isValid validity of the item.
	 */
	protected AbstractCodesTableItem(String code, String decode, Locale locale, Boolean isValid){
		this.code = code;
		this.decode = decode;
		this.locale = locale;
		this.isValid = isValid;
	}

	/**
	 * Returns the code represented by this item.
	 * 
	 * @return The items code.
	 */
	public String getCode(){
		return code;
	}

	/**
	 * Returns the decode represented by this item.
	 * 
	 * @return The items decode.
	 */
	public String getDecode(){
		return decode;
	}

	/**
	 * Returns the locale of this item.
	 * 
	 * @return The items locale.
	 */
	public Locale getLocale(){
		return locale;
	}
	
	/**
	 * Returns the validity of this item.
	 * 
	 * @return isValid as true if item is valid, false otherwise.
	 */
	public boolean getIsValid(){
		return isValid;
	}
	
	/**
	 * Abstract method for toString() that must be implemented by its subclasses.
	 * 
	 * @return Returns a <Code>String</code> representation of this object.
	 */
	public abstract String toString();
	
	/**
	 * Compares this instance with another object. 
	 * The method returns true if the other object is not null,
	 * and is of same class as this and other.getCode() equals this.getCode(),
	 * and other.getLocale() equals this.getLocale().
	 * 
	 * {@inheritDoc}
	 */
	public boolean equals(Object other) {
		if (null == other) {
			return false;
		} else if (!this.getClass().equals(other.getClass())) {
			return false;
		} else {
			AbstractCodesTableItem castOther = (AbstractCodesTableItem) other;
			return new EqualsBuilder().append(this.getCode(), castOther.getCode()).append(this.getLocale(), castOther.getLocale()).isEquals();
		}
	}

	/**
	 * Hash code is computed based on the class name of this instance, the code and the locale.
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode() {
		// Compare by class name to support multiple classloaders
		return new HashCodeBuilder().append(this.getClass().getName()).append(getCode()).append(getLocale()).toHashCode();
	}
}