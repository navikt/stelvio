package no.stelvio.common.codestable;

import java.util.Date;
import java.util.Locale;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CodesTableItemPeriodic represents an item in a <code>CodesTable</code> with a valid period.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public abstract class CodesTableItemPeriodic extends AbstractCodesTableItem {
	/** The date the item is valid from. */
	private Date fromDate;
	
	/** The date the item is valid to. */
	private Date toDate;
	
	/**
	 * Constructor for <code>CodesTableItemPeriodic</code>. Only available to subclasses.
	 */
	protected CodesTableItemPeriodic(){
	}
	
	/**
	 * Constructor for an item, initializing all of its attributes.
	 * 
	 * @param code the code.
	 * @param decode the decode.
	 * @param fromDate the date an item is valid from.
	 * @param toDate the date an item is valid to.
	 * @param locale the locale of the item.
	 * @param isValid validity of the item.
	 */
	public CodesTableItemPeriodic(String code, String decode, Date fromDate, Date toDate, Locale locale, boolean isValid){
		super(code, decode, locale, isValid);
		this.fromDate = fromDate;
		this.toDate = toDate;
	}
	
	/**
	 * Returns the date the item is valid from.
	 * @return The date the item is valid from.
	 * @see #setFromDate
	 */
	public Date getFromDate(){
		return fromDate;
	}
	
	/**
	 * Sets the date the item is valid from.
	 * @param date the date the item is valid.
	 * @see #getFromDate
	 */
	public void setFromDate(Date date){
		this.fromDate = date;
	}
	
	/**
	 * Returns the date the item is valid to.
	 * @return The date the item is valid to.
	 * @see #setToDate
	 */
	public Date getToDate(){
		return toDate;
	}

	/**
	 * Sets the date the item is valid to.
	 * @param date the date the item is valid to.
	 * @see #getToDate
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