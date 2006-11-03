package no.stelvio.common.codestable;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.builder.ToStringBuilder;

public class CodesTableItemPeriodic extends CodesTableItemAb {
	
	private Date fromDate;
	private Date toDate;
	
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
	public CodesTableItemPeriodic(String code, String decode, Date fromDate, Date toDate, Locale locale, Boolean isValid){
		super(code, decode, locale, isValid);
		this.fromDate = fromDate;
		this.toDate = toDate;
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
}
