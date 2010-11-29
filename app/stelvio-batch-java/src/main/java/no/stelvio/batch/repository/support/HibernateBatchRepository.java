package no.stelvio.batch.repository.support;

import java.util.List;

import no.stelvio.batch.domain.BatchDO;
import no.stelvio.batch.exception.InvalidBatchEntryException;
import no.stelvio.batch.repository.BatchRepository;

import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Implementation of the BatchRepository that uses Hibernate to update the BatchDO. A HibernateTemplate should be injected
 * before this implementation is exposed to a client.
 * 
 * @author person983601e0e117 (Accenture)
 * @see BatchDO
 * 
 */
public class HibernateBatchRepository implements BatchRepository {

	private HibernateTemplate hibernateTemplate;

	private final String[] nameAndSliceParams = { "batchname", "slice" };
	private static final String NAME_AND_SLICE_QUERY = "BatchDO.findByNameAndSlice";

	/**
	 * {@inheritDoc}
	 */
	public void updateBatch(BatchDO batch) {
		if (batch.getBatchname() != null && !batch.getBatchname().trim().equals("")) {
			getHibernateTemplate().merge(batch);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public BatchDO findByNameAndSlice(String batchName, int slice) throws InvalidBatchEntryException {
		Object[] arguments = { batchName, Integer.valueOf(slice) };
		List batchDos = getHibernateTemplate().findByNamedQueryAndNamedParam(NAME_AND_SLICE_QUERY, nameAndSliceParams,
				arguments);

		// No such batch defined in database
		if (batchDos.size() == 0) {
			throw new InvalidBatchEntryException("No entry found in database for batch with name=" + batchName + ", slice="
					+ slice);

		}
		// Multiple batches matching name+slice defined in DB. Although this shouldn't
		// be possible as the batch meta data table should have a unique constraint for
		// combination batchname+slice
		if (batchDos.size() > 1) {
			throw new InvalidBatchEntryException("Multiple(" + batchDos.size() + ") entries in database for batch with name="
					+ batchName + ", slice=" + slice);
		}
		return (BatchDO) batchDos.get(0);
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

}