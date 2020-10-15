package no.stelvio.batch.domain;

import java.sql.Clob;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.domain.time.ChangeStamp;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Representation of batch history. *
 * 
 */
@Entity
@Table(name = "T_BATCH_HIST")
@NamedQueries( {
		@NamedQuery(name = "BatchHistDO.findByNameAndSlice", query = "SELECT b FROM BatchHistDO b where b.batchname=:batchname AND b.slice=:slice"),
		@NamedQuery(name = "BatchHistDO.findByNameAndTimeInterval", query = "SELECT b FROM BatchHistDO b where b.batchname=:batchname AND b.startTime >= :beginInterval AND b.startTime <= :endInterval"),
		@NamedQuery(name = "BatchHistDO.findByNameAndStartDay", query = "SELECT b from BatchHistDO b where b.batchname = :batchname AND b.startTime >= :startDay"),
		@NamedQuery(name = "BatchHistDO.findById", query = "SELECT b FROM BatchHistDO b where b.batchHistId=:batchHistId") })		
public class BatchHistDO {

	protected static final Logger logger = LoggerFactory.getLogger(BatchHistDO.class);

	/**
	 * identifier field, read only. for a unique identifier of a batch use the
	 * batchname/slice combination
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN_BATCH_HIST")
	@SequenceGenerator(name = "SEQ_GEN_BATCH_HIST", sequenceName = "S_BATCH_HIST", allocationSize = 1)
	@Column(name = "BATCH_HIST_ID", insertable = false, updatable = false)
	private long batchHistId;

	/** batch name, read only. */
	@Column(name = "BATCH_NAME", updatable = false)
	private String batchname;

	/** start criteria (included) for this batch, read only. */
	@Column(name = "SLICE_START", updatable = false)
	private String sliceStart;

	/** end criteria (included) for this batch, read only. */
	@Column(name = "SLICE_END", updatable = false)
	private String sliceEnd;

	@Column(name = "SLICE", updatable = false)
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

	/** Batch start time. Automatically inserted by onCreate() **/
	@Column(name = "STARTTIME")
	private Date startTime;

	// TODO Should not be nullable!
	// @Column(name = "ENDTIME", nullable = false)
	@Column(name = "ENDTIME")
	private Date endTime;

	/** nullable persistent field */
	@Column(name = "PARAMETERS", updatable = false)
	private String parameters;

	/** nullable persistent field */
	@Column(name = "OUTPUT")
	private Clob output;

	/** nullable persistent field, may be updated. */
	@Column(name = "STATUS")
	private String status;

	/** Entity listener for persisting start time **/
	// @PrePersist
	// protected void onCreate(){
	// startTime = new Date();
	// }
	/**
	 * Version column used by Hibernate for optimistic locking.
	 */
	@Version
	@Column(name = "versjon", nullable = false)
	private long version;

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

	/** default constructor. */
	public BatchHistDO() {
	}

	/**
	 * @param slice
	 *            the slice to set
	 */
	public void setSlice(int slice) {
		this.slice = slice;
		updateChangeStamp();
	}

	/**
	 * @return the batchname
	 */
	public String getBatchname() {
		return batchname;
	}

	/**
	 * @param batchname
	 *            the batchname to set
	 */
	public void setBatchname(String batchname) {
		this.batchname = batchname;
		updateChangeStamp();
	}

	/**
	 * Gets the value specifying the sliceEnd of this batch slice. The value is
	 * batch specific and might for example be dates or numbers. The sliceEnd
	 * value should also be processed by this batch.
	 * 
	 * @return String representing the sliceEnd
	 */
	public String getSliceEnd() {
		return sliceEnd;
	}

	/**
	 * The the sequence number for the batch specified by the batch name. The
	 * combination of batch name and slice is unique.
	 * 
	 * @return slice
	 */
	public int getSlice() {
		return slice;
	}

	/**
	 * Gets the value specifying the sliceStart of this batch slice. The value
	 * is batch specific and might for example be dates or numbers. The
	 * sliceStart value should also be processed by this batch.
	 * 
	 * @return String representing the sliceStart
	 */
	public String getSliceStart() {
		return sliceStart;
	}

	/**
	 * @return the parameters
	 */
	public String getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
		updateChangeStamp();
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
		updateChangeStamp();
	}

	/**
	 * Returns the batch version.
	 * 
	 * @return the batch version
	 */
	public long getVersion() {
		return version;
	}

	public void setBatchHistId(long batchHistId) {
		this.batchHistId = batchHistId;
		updateChangeStamp();
	}

	public long getBatchHistId() {
		return batchHistId;
	}

	public void setStartTime() {
		this.startTime = new Date();
		updateChangeStamp();
	}

	public void setEndTime() {
		this.endTime = new Date();
		updateChangeStamp();
	}

	public void setOutput(Clob output) {
		this.output = output;
		updateChangeStamp();
	}

	public Date getEndtime() {
		return endTime;
	}

	public Date getStartTime() {
		return startTime;
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
	 * Makes sure that the ChangeStamp object is updated. Called by the setter methods.
	 */
	private void updateChangeStamp() {
		String userId;
		if (RequestContextHolder.isRequestContextSet()) {
			String fullUserId = RequestContextHolder.currentRequestContext().getUserId();
			if (fullUserId != null){
				userId = shortenBatchName(fullUserId);				
			} else {
				userId = fullUserId;				
			}
			
		} else {
			userId = null;
			if (logger.isInfoEnabled()) {
				logger.info("RequestContext is not set for this thread.");
			}
		}

		if (userId == null || userId.trim().equals("")) {
			userId = this.batchname;
		}

		if (changeStamp == null) {
			if (logger.isWarnEnabled()) {
				logger.warn("ChangeStamp entity not set for batch: " + this.batchname);
			}
			return;
		}
		changeStamp.updatedBy(userId);
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

		if (!(other instanceof BatchHistDO)) {
			return false;
		}
		BatchHistDO castOther = (BatchHistDO) other;
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
	
	private String shortenBatchName(String batchName){
		String[] name = batchName.split("\\.");
		return name[name.length-1];			
	}

}
