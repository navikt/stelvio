package no.stelvio.dto.time;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Data Transfer Object representation of <code>no.stelvio.domain.time.ChangeStamp</code>.
 * 
 * This object must always be WS-I and Java 1.4 compliant
 * 
 * @author person983601e0e117 (Accenture)
 * @author person6045563b8dec (Accenture)
 * 
 */
public class ChangeStampDto implements Serializable {

	private static final long serialVersionUID = -5331617955168131234L;

	private String createdBy;

	private Date createdDate;

	private String updatedBy;

	private Date updatedDate;

	/**
	 * Default no-arg constructor.
	 */
	public ChangeStampDto() {
	}

	/**
	 * Constructor with arguments.
	 * 
	 * @param createdBy
	 *            Created by user id
	 * @param createdDate
	 *            Creation date
	 * @param updatedBy
	 *            Last updated by user id
	 * @param updatedDate
	 *            Last updated date
	 */
	public ChangeStampDto(String createdBy, Date createdDate, String updatedBy, Date updatedDate) {
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
	}

	/**
	 * Returns a String representation of this ChangeStampDto object.
	 * 
	 * @return a String representation of the object
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("createdBy", createdBy).append("createdDate", createdDate).append("updatedBy",
				updatedBy).append("updatedDate", updatedDate).toString();
	}

	/**
	 * Gets created by userid.
	 * 
	 * @return created by
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets created by user id.
	 * 
	 * @param createdBy
	 *            user id
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Gets created date.
	 * 
	 * @return created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets created date.
	 * 
	 * @param createdDate
	 *            created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets created by userid.
	 * 
	 * @return updated by
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * Sets updated by.
	 * 
	 * @param updatedBy
	 *            user id
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * Gets updated date.
	 * 
	 * @return updated date
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * Sets updated date.
	 * 
	 * @param updatedDate
	 *            updated date
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}
