package no.stelvio.common.codestable;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Abstract class for the CodesTableItem component.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @verion $Id$
 */
public abstract class AbstractCodesTableItem implements Serializable {

	/** A codestableitem's code. */
	protected String code;
	
	/** A codestableitem's decode, i.e. a message. */
	protected String decode;
	
	/** A codestableitem's locale that represents the country and 
	 * language the items decode is defined for.
	 */
	protected Locale locale;
	
	/** Defines the validity of a codestableitem. */
	protected Boolean isValid;
		
	/**
	 * Returns the code represented by this item.
	 * @return The items code.
	 * @see #setCode
	 */
	public String getCode(){
		return code;
	}
	
	/**
	 * Sets the code represented by this item.
	 * @param code the code.
	 * @see #getCode
	 */
	public void setCode(String code){
		this.code = code;
	}

	/**
	 * Returns the decode represented by this item.
	 * @return The items decode.
	 * @see #setDecode
	 */
	public String getDecode(){
		return decode;
	}

	/**
	 * Sets the decode represented by this item.
	 * @param decode the decode.
	 * @see #getDecode
	 */
	public void setDecode(String decode){
		this.decode = decode;
	}

	/**
	 * Returns the locale of this item.
	 * @return The items locale.
	 * @see #setLocale
	 */
	public Locale getLocale(){
		return locale;
	}
	
	/**
	 * Sets the locale of this item.
	 * @param locale the locale of the item.
	 * @see #getLocale
	 */
	public void setLocale(Locale locale){
		this.locale = locale;
	}
	
	/**
	 * Returns the validity of this item.
	 * @return isValid as true if item is valid, false otherwise.
	 * @see #setIsValid
	 */
	public Boolean getIsValid(){
		return isValid;
	}

	/**
	 * Sets the validity of this item.
	 * @param isValid true to mark item as valid, false otherwise.
	 * @see #getIsValid
	 */
	public void setIsValid(Boolean isValid){
		this.isValid = isValid;
	}
	
	/**
	 * Abstract method for toString() that must be implemented by its 
	 * subclasses.
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
			// This aint null
			return false;
		} else if (!this.getClass().equals(other.getClass())) {
			// This is another class than other
			return false;
		} else {
			// This is the same class as other
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