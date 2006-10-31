package no.stelvio.common.codestable;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
//import javax.persistence.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

//TODO: importer javax.persistence og un-comment annotationsene og skriv spørringene

/**
 * CodesTableItem represents an item in a codes table.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @author person7553f5959484, Accenture
 * @version $Id$
 */
//@Entity(name="CodesTableItem")
//@NamedQueries({
//@NamedQuery(name="CodesTableItem.findAll" query="select c from CodesTableItem c")}})
public class CodesTableItem implements Serializable {
	
	private String code;
	private String decode;
	private Date fromDate;
	private Date toDate;
	private Locale locale;
	private Boolean isValid;
	
	//@Version 
	private Long version;
	
	/**
	 * Empty constructor for an item.
	 */
	public CodesTableItem() {		
	}

	/**
	 * Constructor for an item, initializing all of its attributes.
	 * @param code the code.
	 * @param decode the decode.
	 * @param fromDate the date an item is valid from.
	 * @param toDate the date an item is valid to.
	 * @param locale the locale of the item.
	 * @param isvalid validity of the item.
	 * @param version the version of the item.
	 */
	public CodesTableItem(String code, String decode, Date fromDate, Date toDate, Locale locale, Boolean isValid){
		this.code = code;
		this.decode = decode;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.locale = locale;
		this.isValid = isValid;
	}
		
	/**
	 * Returns the version of this item.
	 * @return the code.
	 */
	public Long getVersion(){
		return this.version;
	}
	
	/**
	 * Returns the code represented by this item.
	 * @return the code.
	 */
	//@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
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
	 * Returns the first date the item is valid from.
	 * @return the first date the item is valid
	 */
	public Date getFromDate(){
		return fromDate;
	}
	
	/**
	 * Sets the first date the item is valid from.
	 * @param date the first date the item is valid
	 */
	public void setFromDate(Date date){
		this.fromDate = date;
	}
	
	/**
	 * Returns the last date the item is valid from.
	 * @return the last date the item is valid
	 */
	public Date getToDate(){
		return toDate;
	}

	/**
	 * Sets the last date the item is valid from.
	 * @param date the last date the item is valid
	 */
	public void setToDate(Date date){
		this.toDate = date;
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
	 * Returns a String representation of this object.
	 * 
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("code", getCode())
			.append("decode", getDecode())
			.append("locale", getLocale())
			.append("validFrom", getFromDate())
			.append("validTo", getToDate())
			.append("isValid", getIsValid())
			.toString();
	}

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
			CodesTableItem castOther = (CodesTableItem) other;
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