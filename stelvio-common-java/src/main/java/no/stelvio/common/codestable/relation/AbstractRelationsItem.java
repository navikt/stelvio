package no.stelvio.common.codestable.relation;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Base class for mapping relations. Mapping relations are used to map between legal combinations of CodeTableItems.
 * Such relations typically represents a many-to-many relation, thus, it can't be created as a CodesTableItem.
 * 
 * @author personec2cedb8e118 (Bouvet)
 * @version $Id$
 */
@MappedSuperclass
public abstract class AbstractRelationsItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** The key for this item. */
	@Id
	@Column(name = "id")
	private String id;

	/** The date the item is valid from. Not really relevant for mapping relations. */
	@Column(name = "FROM_DATE")
	@Temporal(TemporalType.DATE)
	private Date fromDate;

	/** The date the item is valid to. Not really relevant for mapping relations. */
	@Column(name = "TO_DATE")
	@Temporal(TemporalType.DATE)
	private Date toDate;


	/** If the item is valid or not. */
	@Column(name = "valid")
	private boolean valid;

	/**
	 * Returns true if the other object is of the same class and 
	 * has the id as this object
	 *  
	 * @param other object to compare to
	 * @return true if objects are equal
	 */
	public boolean equals(Object other) {
		if (other == null || !getClass().equals(other.getClass())){
			return false;			
		}
		
		return getId().equals(((AbstractRelationsItem) other).getId());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getId().hashCode();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("fromDate", getFromDate())
			.append("toDate", getToDate())
			.append("isValid", isValid())
			.toString();
	}

	/**
	 * Get the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the id.
	 *
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the fromDate.
	 *
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * Set the fromDate.
	 *
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * Get the toDate.
	 *
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * Set the toDate.
	 *
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * Get the valid.
	 *
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Set the valid.
	 *
	 * @param valid the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

}
