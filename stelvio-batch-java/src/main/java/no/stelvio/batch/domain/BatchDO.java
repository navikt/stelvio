package no.stelvio.batch.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.domain.time.ChangeStamp;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Representation of a batch.
 * 
 * In code uniquely identified by the combination of batchname and slice.
 * 
 * @version $Id: BatchDO.java 2064 2005-03-04 09:18:15Z psa2920 $
 */
@Entity
@Table(name = "T_BATCH")
@NamedQuery(name = "BatchDO.findByNameAndSlice", 
		query = "SELECT b FROM BatchDO b where b.batchname=:batchname AND b.slice=:slice")
public class BatchDO {

	private static final long serialVersionUID = 6318007808581245609L;

	/** Logger. */
	protected static final Log log = LogFactory.getLog(BatchDO.class);

	/** identifier field, read only. for a unique identifyer of a batch use the batchnam/slice combination */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BATCH_ID", insertable = false, updatable = false)
	private long batchId;

	/** batch name, read only. */
	@Column(name = "BATCH_NAME", insertable = false, updatable = false)
	private String batchname;

	/** nullable persistent field, read only. */
	@Column(name = "PARAMETERS", insertable = false, updatable = false)
	private String parameters;

	/** nullable persistent field, may be updated. */
	@Column(name = "STATUS")
	private String status;

	/** start criteria (included) for this batch, read only. */
	@Column(name = "SLICE_START", insertable = false, updatable = false)
	private String sliceStart;

	/** end criteria (included) for this batch, read only. */
	@Column(name = "SLICE_END", insertable = false, updatable = false)
	private String sliceEnd;

	/** slice number, read only. */
	@Column(name = "SLICE", insertable = false, updatable = false)
	private int slice;

	/**
	 * Audit information.
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "createdBy", column = @Column(name = "opprettet_av")),
			@AttributeOverride(name = "createdDate", column = @Column(name = "dato_opprettet")),
			@AttributeOverride(name = "updatedBy", column = @Column(name = "endret_av")),
			@AttributeOverride(name = "updatedDate", column = @Column(name = "dato_endret")) })
	private ChangeStamp changeStamp;

	/**
	 * Version column used by Hibernate for optimistic locking.
	 */
	@Version
	@Column(name = "versjon", nullable = false)
	private long version;

	/** default constructor. */
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
	 * Sets the batch name. The combination of batch name and slice is unique.
	 * 
	 * @param batchname
	 *            the batch name.
	 */
	public void setBatchname(String batchname) {
		this.batchname = batchname;
		updateChangeStamp();
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
	 * @param parameters
	 *            the batch parameters.
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
		updateChangeStamp();
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
	 * @param status
	 *            the batch status.
	 */
	public void setStatus(String status) {
		this.status = status;
		updateChangeStamp();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this);
		sb.append("batchname", getBatchname()).append("slice", getSlice());
		if (getSliceStart() != null && getSliceEnd() != null) {
			if (!getSliceStart().equals(getSliceEnd())) {
				sb.append("sliceStart", getSliceStart());
				sb.append("sliceEnd").append(getSliceEnd());
			}
		}
		sb.append("parameters", getParameters());
		sb.append("status", getStatus());
		return sb.toString();

	}

	/**
	 * {@inheritDoc}
	 * 
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
		return new EqualsBuilder().append(this.getBatchname(), castOther.getBatchname()).append(this.getSlice(),
				castOther.getSlice()).isEquals();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder().append(getBatchname()).append(getSlice()).toHashCode();
	}

	/**
	 * Gets the value specifying the sliceEnd of this batch slice. The value is batch specific and might for example be dates or
	 * numbers. The sliceEnd value should also be processed by this batch.
	 * 
	 * @return String representing the sliceEnd
	 */
	public String getSliceEnd() {
		return sliceEnd;
	}

	/**
	 * The the sequence number for the batch specified by the batch name. The combination of batch name and slice is unique.
	 * 
	 * @return slice
	 */
	public int getSlice() {
		return slice;
	}

	/**
	 * Gets the value specifying the sliceStart of this batch slice. The value is batch specific and might for example be dates
	 * or numbers. The sliceStart value should also be processed by this batch.
	 * 
	 * @return String representing the sliceStart
	 */
	public String getSliceStart() {
		return sliceStart;
	}

	/**
	 * Database primary key.
	 * 
	 * <b>For a unique identifyer of a batch use the batchnam/slice combination, do not use batchId</b>
	 * 
	 * @return the batchId
	 */
	public long getBatchId() {
		return batchId;
	}

	/**
	 * Returns the batch change stamp.
	 * 
	 * @return the batch change stamp
	 */
	public ChangeStamp getChangeStamp() {
		return changeStamp;
	}

	/**
	 * Sets the batch change stamp.
	 * 
	 * @param changeStamp
	 *            the batch change stamp.
	 */
	public void setChangeStamp(ChangeStamp changeStamp) {
		this.changeStamp = changeStamp;
	}

	/**
	 * Returns the batch version.
	 * 
	 * @return the batch version
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * Sets the batch version.
	 * 
	 * @param version
	 *            the batch version.
	 */
	public void setVersion(long version) {
		this.version = version;
		updateChangeStamp();
	}

	/**
	 * Makes sure that the ChangeStamp object is updated. Called by the setter methods.
	 */
	private void updateChangeStamp() {
		String userId;
		if (RequestContextHolder.isRequestContextSet()) {
			userId = RequestContextHolder.currentRequestContext().getUserId();
		} else {
			userId = null;
			if (log.isInfoEnabled()) {
				log.info("RequestContext is not sat for this thread.");
			}
		}

		if (userId == null || userId.trim().equals("")) {
			userId = this.batchname;
		}

		if (changeStamp == null) {
			if (log.isWarnEnabled()) {
				log.warn("ChangeStamp entity not set for batch: " + this.batchname);
			}
			return;
		}
		changeStamp.updatedBy(userId);
	}

}