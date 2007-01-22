package no.stelvio.common.codestable.support;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Abstract class for the CodesTableItem/CodesTableItemPeriodic components.
 * 
 * This class is a MappedSuperclass, meaning that Entities that inherits from this
 * class must map to a table that defines columns set up by this class
 * 
 * @author personb66fa0b5ff6e (Accenture)
 * @author person983601e0e117 (Accenture)
 * @verion $Id$
 */
@MappedSuperclass
public abstract class AbstractCodesTableItem implements Serializable {
	
	/** A codestableitem's code. */
	@Column(name="code")
	private String code;
	
	/** A codestableitem's decode, i.e. a message. */
	@Column(name="decode")
	private String decode;
	
	/** A codestableitem's locale that represents the country and language the items decode is defined for. */
	@Transient
	private Locale locale;
	
	/** Defines the validity of a codestableitem. */
	@Column(name="valid")
	private boolean valid;
		
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
	 * @param isValid validity of the item.
	 */
	protected AbstractCodesTableItem(String code, String decode, Locale locale, boolean isValid){
		this.code = code;
		this.decode = decode;
		this.valid = isValid;
		this.locale = locale;
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
	 * Returns the validity of this item.
	 * 
	 * @return valid as <code>true</code> if item is valid, false otherwise.
	 */
	public boolean isValid(){
		return valid;
	}
	
	/**
	 * Gets the locale
	 * @return locale the locale of this item
	 */
	public Locale getLocale() {
		return locale;
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
			return new EqualsBuilder().append(this.getCode(), castOther.getCode()).isEquals();
		}
	}

	/**
	 * Hash code is computed based on the class name of this instance, the code and the locale.
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode() {
		// Compare by class name to support multiple classloaders
		return new HashCodeBuilder().append(this.getClass().getName()).append(getCode()).toHashCode();
	}

}