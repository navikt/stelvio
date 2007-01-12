package no.stelvio.batch.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Representation of a batch.
 * 
 * @author person356941106810, Accenture
 * @version $Id: BatchDO.java 2064 2005-03-04 09:18:15Z psa2920 $ 
 */
public class BatchDO extends no.stelvio.common.core.DomainObject {

	private static final long serialVersionUID = 6318007808581245609L;

	/** identifier field */
	private String batchname;

	/** nullable persistent field */
	private String parameters;

	/** nullable persistent field */
	private String status;

	/** default constructor */
	public BatchDO() {
	}

	/**
	 * Returns the batch name.
	 * 
	 * @return the batch name.
	 */
	public String getBatchname() {
		return this.batchname;
	}

	/**
	 * Sets the batch name.
	 * 
	 * @param batchname the batch name.
	 */
	public void setBatchname(String batchname) {
		this.batchname = batchname;
	}

	/**
	 * Returns the batch parameters.
	 * 
	 * @return the batch parameters.
	 */
	public String getParameters() {
		return this.parameters;
	}

	/**
	 * Sets the batch parameters.
	 * 
	 * @param parameters the batch parameters.
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	/**
	 * Returns the batch status.
	 * 
	 * @return the batch status
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * Sets the batch status.
	 * 
	 * @param status the batch status.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/** 
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("batchname", getBatchname()).toString();
	}

	/** 
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {

		if (this == other) {
			return true;
		}

		if (!(other instanceof BatchDO)) {
			return false;
		}
		BatchDO castOther = (BatchDO) other;
		return new EqualsBuilder().append(this.getBatchname(), castOther.getBatchname()).isEquals();
	}

	/** 
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder().append(getBatchname()).toHashCode();
	}
}
