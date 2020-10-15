package no.stelvio.batch.repository.support;

import java.util.List;

import org.springframework.orm.hibernate5.HibernateTemplate;

import no.stelvio.batch.domain.BatchDO;
import no.stelvio.batch.exception.InvalidBatchEntryException;
import no.stelvio.batch.repository.BatchRepository;

/**
 * Implementation of the BatchRepository that uses Hibernate to update the BatchDO. A HibernateTemplate should be injected
 * before this implementation is exposed to a client.
 * 
 * @see BatchDO
 * 
 */
public class HibernateBatchRepository implements BatchRepository {

	private HibernateTemplate hibernateTemplate;

	private final String[] nameAndSliceParams = { "batchname", "slice" };
	private static final String NAME_AND_SLICE_QUERY = "BatchDO.findByNameAndSlice";

	@Override
	public void updateBatch(BatchDO batch) {
		if (batch.getBatchname() != null && !batch.getBatchname().trim().equals("")) {
			getHibernateTemplate().merge(batch);
		}
	}

	/**
	 * This version should return first (latest) entrance of batchname and slice
	 */
	@Override
	public BatchDO findByNameAndSlice(String batchName, int slice) throws InvalidBatchEntryException {
        Object[] arguments = {batchName, slice};
        List batchDos = getHibernateTemplate().findByNamedQueryAndNamedParam(NAME_AND_SLICE_QUERY, nameAndSliceParams,
				arguments);

		// No such batch defined in database
		if (batchDos.size() == 0) {
			throw new InvalidBatchEntryException("No entry found in database for batch with name=" + batchName + ", slice="
					+ slice);
		}

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