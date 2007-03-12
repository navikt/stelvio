package no.stelvio.common.codestable;

import java.util.Date;
import java.util.Locale;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;

/**
 * CodesTableItemPeriodic represents an item in a <code>CodesTable</code> with a valid period.
 * <p/>
 * This class is a MappedSuperclass, meaning that Entities that inherits from this class must map to a table that
 * defines columns set up by this class
 *
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
@MappedSuperclass
public abstract class CodesTableItemPeriodic extends AbstractCodesTableItem {
	private static final long serialVersionUID = 5966326178149304963L;

	/** The date the item is valid from. */
	@Column(name = "FROM_DATE")
	@Temporal(TemporalType.DATE)
	private Date fromDate;

	/** The date the item is valid to. */
	@Column(name = "TO_DATE")
	@Temporal(TemporalType.DATE)
	private Date toDate;

	/** Constructor for <code>CodesTableItemPeriodic</code>. Only available to subclasses. */
	protected CodesTableItemPeriodic() {
	}

	/**
	 * Constructor for an item, initializing all of its attributes.
	 *
	 * @param code the code.
	 * @param decode the decode.
	 * @param fromDate the date an item is valid from.
	 * @param toDate the date an item is valid to.
	 * @param isValid validity of the item.
	 * @deprecated CodesTableItems will no longer expose Locale to client. Replaced by {@link
	 *             CodesTableItemPeriodic(String, String, Date, Date, boolean)}
	 */
	@Deprecated
	public CodesTableItemPeriodic(String code, String decode, Date fromDate, Date toDate, Locale locale, boolean isValid) {
		super(code, decode, isValid);
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	/**
	 * Constructor for an item, initializing all of its attributes.
	 *
	 * @param code the code.
	 * @param decode the decode.
	 * @param fromDate the date an item is valid from.
	 * @param toDate the date an item is valid to.
	 * @param isValid validity of the item.
	 */
	public CodesTableItemPeriodic(String code, String decode, Date fromDate, Date toDate, boolean isValid) {
		super(code, decode, isValid);
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	/**
	 * Returns the date the item is valid from.
	 *
	 * @return The date the item is valid from.
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * Returns the date the item is valid to.
	 *
	 * @return The date the item is valid to.
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * Returns a String representation of this object.
	 * <p/>
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this)
				.append("code", getCode())
				.append("decode", getDecode())
				.append("validFrom", getFromDate())
				.append("validTo", getToDate())
				.append("isValid", isValid())
				.toString();
	}
}