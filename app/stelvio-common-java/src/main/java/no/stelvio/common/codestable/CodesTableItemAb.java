package no.stelvio.common.codestable;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public abstract class CodesTableItemAb implements Serializable {

	private String code;
	private String decode;
	private Locale locale;
	private Boolean isValid;
	
	/**
	 * Constructor for CodesTableItem.
	 */
	public CodesTableItemAb(){
	}
	
	/**
	 * Constructor for an item, initializing its attributes.
	 * @param code the code.
	 * @param decode the decode.
	 * @param locale the locale of the item.
	 * @param isvalid validity of the item.
	 * @param version the version of the item.
	 */
	public CodesTableItemAb(String code, String decode, Locale locale, Boolean isValid){
		this.code = code;
		this.decode = decode;
		this.locale = locale;
		this.isValid = isValid;
	}
	
	/**
	 * Returns the code represented by this item.
	 * @return the code.
	 */
	public String getCode(){
		return code;
	}
	
	/**
	 * Sets the code represented by this item.
	 * @param code the code
	 */
	public void setCode(String code){
		this.code = code;
	}

	/**
	 * Returns the decode represented by this item.
	 * @return the decode.
	 */
	public String getDecode(){
		return decode;
	}

	/**
	 * Sets the decode represented by this item
	 * @param decode the decode
	 */
	public void setDecode(String decode){
		this.decode = decode;
	}

	/**
	 * Returns the locale of this item.
	 * @return the locale of the item
	 */
	public Locale getLocale(){
		return locale;
	}
	
	/**
	 * Sets the locale of this item
	 * @param locale the locale of the item
	 */
	public void setLocale(Locale locale){
		this.locale = locale;
	}
	
	/**
	 * Returns the validity of this item.
	 * @return true if item is valid, false otherwise
	 */
	public Boolean getIsValid(){
		return isValid;
	}

	/**
	 * Sets the validity of this item.
	 * @param isValid true to mark item as valid, false otherwise
	 */
	public void setIsValid(Boolean isValid){
		this.isValid = isValid;
	}
	
	/**
	 * Abstract method for toString().
	 */
	public abstract String toString();
	
	/**
	 * Compares this instance with another object. 
	 * The method returns true if the other object is not null,
	 * and is of same class as this and other.getCode() 
	 * equals this.getCode().
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
			CodesTableItemAb castOther = (CodesTableItemAb) other;
			return new EqualsBuilder().append(this.getCode(), castOther.getCode()).isEquals();
		}
	}

	/**
	 * Hash code is computed based on the class name of this instance and the code.
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode() {
		// Compare by class name to support multiple classloaders
		return new HashCodeBuilder().append(this.getClass().getName()).append(getCode()).toHashCode();
	}
}