package no.stelvio.batch.domain;

import java.sql.Blob;
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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.domain.time.ChangeStamp;

/*
 * tabell oppdateres ved start og stop av kj�ringer med f�lgende kolonner:

 <<Batchnavn		Lagres ved oppstart av batch
 <<Slicenr			lagres ved oppstart av batch
 Starttid/Sluttid   <-- STARTTID LOGGES MED EN GANG; SLUTTID VED STOPP
 Kj�retid 			<-- REGNES UT VED STOPP
 Parametre p� kj�retidspunktet
 Outputdata fra kj�ring, f.eks. dump av fremdriftstellere
 Returkode - NULL s� lenge batchen kj�rer	<-- OPPDATERES VED STOPP

 */

/**
 * Representation of batch history. *
 * 
 * @author person5dc3535ea7f4 (Accenture)
 */
@Entity
@Table(name = "T_BATCH_HIST")
@NamedQueries( {
		@NamedQuery(name = "BatchHistDO.findByNameAndSlice", query = "SELECT b FROM BatchHistDO b where b.batchname=:batchname AND b.slice=:slice"),
		@NamedQuery(name = "BatchHistDO.findByNameAndTimeInterval", query = "SELECT b FROM BatchHistDO b where b.batchname=:batchname AND b.startTime >= :beginInterval AND b.startTime <= :endInterval"),
		@NamedQuery(name = "BatchHistDO.findByNameAndStartDay", query = "SELECT b from BatchHistDO b where b.batchname = :batchname AND b.startTime >= :startDay"),
		@NamedQuery(name = "BatchHistDO.findById", query = "SELECT b FROM BatchHistDO b where b.batchHistId=:batchHistId") })		
		public class BatchHistDO {

	/** Logger. */
	protected static final Logger logger = LoggerFactory
			.getLogger(BatchHistDO.class);

	/**
	 * identifier field, read only. for a unique identifyer of a batch use the
	 * batchnam/slice combination
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BATCH_HIST_ID", insertable = true, updatable = false)
	private long batchHistId;

	/** batch name, read only. */
	@Column(name = "BATCH_NAME", insertable = true, updatable = false)
	private String batchname;

	/** start criteria (included) for this batch, read only. */
	@Column(name = "SLICE_START", insertable = true, updatable = false)
	private String sliceStart;

	/** end criteria (included) for this batch, read only. */
	@Column(name = "SLICE_END", insertable = true, updatable = false)
	private String sliceEnd;

	@Column(name = "SLICE", insertable = true, updatable = false, nullable = true)
	private int slice;

	/** Batch start time. Automatically inserted by onCreate() **/
	// @Temporal(TemporalType.TIMESTAMP)
	// @Column(name = "startTime", nullable = false)
	// private Date startTime;
	// TODO Should not be nullable!
	// @Column(name = "startTime", nullable = false)
	@Column(name = "STARTTIME", nullable = true)
	private Date startTime;

	// TODO Should not be nullable!
	// @Column(name = "ENDTIME", nullable = false)
	@Column(name = "ENDTIME", nullable = true)
	private Date endTime;

	/** nullable persistent field */
	@Column(name = "PARAMETERS", insertable = true, updatable = false)
	private String parameters;

	/** nullable persistent field */
	@Column(name = "OUTPUT", insertable = true, updatable = true)
	private Clob output;

	/** nullable persistent field, may be updated. */
	@Column(name = "STATUS", insertable = true)
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
	}

	public long getBatchHistId() {
		return batchHistId;
	}

	public void setStartTime() {
		this.startTime = new Date();
	}

	public void setEndTime() {
		this.endTime = new Date();
	}

	public void setOutput(Clob output) {
		this.output = output;
	}

	public Date getEndtime() {
		return endTime;
	}

	public Date getStartTime() {
		return startTime;
	}

}
