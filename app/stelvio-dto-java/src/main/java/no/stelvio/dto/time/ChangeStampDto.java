package no.stelvio.dto.time;

import java.io.Serializable;
import java.util.Date;

/**
 * Data Transfer Object representation of <code>no.stelvio.domain.time.ChangeStamp</code>
 * 
 * This object must always be WS-I and Java 1.4 compliant
 * @author person983601e0e117 (Accenture)
 *
 */
public class ChangeStampDto implements Serializable {

	private String createdBy;

	private Date createdDate;
	
	private String updatedBy;
	
	private Date updatedDate;

	/**
	 * Default no-arg constructor
	 */
	public ChangeStampDto(){}
	
	/**
	 * Gets created by userid
	 * @return created by
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets created by user id
	 * @param createdBy user id
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Gets created date
	 * @return created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets created date
	 * @param createdDate
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets created by userid
	 * @return updated by
	 */	
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * Sets updated by
	 * @param updatedBy user id
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * Gets updated date
	 * @return updated date
	 */	
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * Sets updated date
	 * @param updatedDate updated date
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}	
	
	
}
