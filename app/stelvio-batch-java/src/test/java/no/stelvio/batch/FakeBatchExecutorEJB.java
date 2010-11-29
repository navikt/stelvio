package no.stelvio.batch;

import java.util.Date;

import no.stelvio.common.util.DateUtil;
import no.stelvio.common.util.DateUtil.DateCreator;

/**
 * This class simulates the role of an EJB in a batch execution environment.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public class FakeBatchExecutorEJB {

	private BatchBi batchPojo;

	private DateCreator dateCreator = new DateUtil.DefaultDateCreator();

	/**
	 * Print the date on stdout.
	 */
	public void printDate() {
		Date now = dateCreator.createDate();
		System.out.println(now);
	}

	/**
	 * Execute batch.
	 * 
	 * @return result
	 */
	public int execute() {
		return batchPojo.executeBatch(0);
	}

	/**
	 * Get batch POJO.
	 * 
	 * @return pojo
	 */
	public BatchBi getBatchPojo() {
		return batchPojo;
	}

	/**
	 * Set batch POJO.
	 * 
	 * @param batchPojo
	 *            pojo
	 */
	public void setBatchPojo(BatchBi batchPojo) {
		this.batchPojo = batchPojo;
	}

	/**
	 * Set date creator.
	 * 
	 * @param dateCreator
	 *            date creator
	 */
	public void setDateCreator(DateCreator dateCreator) {
		this.dateCreator = dateCreator;
	}

}
