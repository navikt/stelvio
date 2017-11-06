package no.stelvio.batch.repository.support;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import no.stelvio.batch.domain.BatchHistDO;
import no.stelvio.batch.exception.InvalidBatchEntryException;
import no.stelvio.batch.repository.BatchHistRepository;

import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Implementation of the BatchRepository that uses Hibernate to update the
 * BatchHistDO. A HibernateTemplate should be injected before this
 * implementation is exposed to a client.
 * 
 * @author person5dc3535ea7f4(Accenture)
 * @see BatchHistDO
 * 
 */
public class HibernateBatchHistRepository implements BatchHistRepository {

	private final String[] nameAndSliceParams = { "batchname", "slice" };
	private final String[] nameAndTimeIntervalParams = { "batchname", "beginInterval", "endInterval" };
	private final String[] nameAndStartDayParams = {"batchname", "startDay"/*, "endDay" */};

	private static final String ID_PARAMETER = "batchHistId";

	private static final String NAME_AND_STARTDAY_QUERY = "BatchHistDO.findByNameAndStartDay";
	private static final String NAME_AND_SLICE_QUERY = "BatchHistDO.findByNameAndSlice";
	private static final String ID_QUERY = "BatchHistDO.findById";
	private static final String NAME_AND_TIME_INTERVAL_QUERY = "BatchHistDO.findByNameAndTimeInterval";

	private HibernateTemplate hibernateTemplate;

	/**
	 * {@inheritDoc}
	 */
	public void updateBatchHist(BatchHistDO batch) {
		if (batch.getBatchname() != null
				&& !batch.getBatchname().trim().equals("")) {
			getHibernateTemplate().merge(batch);
		}
	}

	//TODO Test and document
	public Collection<BatchHistDO> findByNameAndTimeInterval(String batchName,
			Date from, Date to) {
		Object[] arguments = { batchName, from, to };
		List batchDos = getHibernateTemplate().findByNamedQueryAndNamedParam(
				NAME_AND_TIME_INTERVAL_QUERY, nameAndTimeIntervalParams,
				arguments);

		return (Collection<BatchHistDO>) batchDos;

	}

	/**
	 * {@inheritDoc} This version should return all matching history rows
	 */
	public Collection<BatchHistDO> findByNameAndSlice(String batchName,
			int slice) throws InvalidBatchEntryException {
		Object[] arguments = { batchName, Integer.valueOf(slice) };
		List batchDos = getHibernateTemplate().findByNamedQueryAndNamedParam(
				NAME_AND_SLICE_QUERY, nameAndSliceParams, arguments);

		// No such batch defined in database
		if (batchDos.size() == 0) {
			throw new InvalidBatchEntryException(
					"No entry found in database for batch with name="
							+ batchName + ", slice=" + slice);
		}

		return (Collection<BatchHistDO>) batchDos;
	}

	/**
	 * Flushes the changes made through the hibernate template to the database.
	 * 
	 */
	public void flushChanges() {
		getHibernateTemplate().flush();
	}

	/**
	 * Gets the hibernateTemplate.
	 * 
	 * @see HibernateTemplate
	 * @return the hibernateTemplate
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * Sets the hibernateTemplate.
	 * 
	 * @see HibernateTemplate
	 * @param hibernateTemplate
	 *            the HibernateTemplate
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public BatchHistDO findLastRunByNameAndSlice(String string, int i) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Persists the initial history and returns the generated ID
	 * 
	 * @param batchHistDO
	 *            the BatchHistDO
	 */
	public long setHist(BatchHistDO batchHistDO) {
		getHibernateTemplate().persist(batchHistDO);
		return batchHistDO.getBatchHistId();
	}

	public BatchHistDO findById(Long batchHistoryID) {
		List batchHistDOs = getHibernateTemplate()
				.findByNamedQueryAndNamedParam(ID_QUERY, ID_PARAMETER,
						batchHistoryID);

		return (BatchHistDO) batchHistDOs.get(0);
	}
	
	//TODO This method won't work yet. 
	//We'll need either a start time for the beginning of the day and end time for the end of the day
	//or to make hibernate understand that we only want to query on the date - not the time
	public Collection<BatchHistDO> findByNameAndDay(String batchName,
			Date startDay) {
		
		Object[] arguments = { batchName, startDay /*, startDay*/};
		List batchDos = getHibernateTemplate().findByNamedQueryAndNamedParam(
				NAME_AND_STARTDAY_QUERY , nameAndStartDayParams,
				arguments);

		return (Collection<BatchHistDO>) batchDos;
	}

}