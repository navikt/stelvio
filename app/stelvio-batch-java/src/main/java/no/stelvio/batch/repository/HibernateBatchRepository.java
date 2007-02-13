package no.stelvio.batch.repository;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import no.stelvio.batch.domain.BatchDO;
import no.stelvio.batch.exception.InvalidBatchEntryException;

/**
 * Implementation of the BatchRepository that uses Hibernate to update the BatchDO.
 * A HibernateTemplate should be injected before this implementation is exposed to a client.
 * 
 * @author person983601e0e117 (Accenture)
 * @see BatchDO
 *
 */
public class HibernateBatchRepository implements BatchRepository {

	private HibernateTemplate hibernateTemplate;
	
	/**
	 * {@inheritDoc}
	 */
	public void updateBatch(BatchDO batch) {
		if(batch.getBatchname() != null && !batch.getBatchname().trim().equals("")){
			getHibernateTemplate().merge(batch);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public BatchDO findByName(String batchName) throws InvalidBatchEntryException {
		List batchDos = getHibernateTemplate().findByNamedQueryAndNamedParam("BatchDO.findByName", "batchname", batchName);
		if(batchDos.size() != 1){
			throw new InvalidBatchEntryException(batchName);
		}
		return (BatchDO) batchDos.get(1);
	}	
	
	/**
	 * Flushes the changes made through the hibernate template to the database
	 *
	 */
	public void flushChanges(){
		getHibernateTemplate().flush();
	}

	/**
	 * Gets the hibernateTemplate
	 * @see HibernateTemplate
	 * @return the hibernateTemplate
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * Sets the hibernateTemplate
	 * @see HibernateTemplate
	 * @param hibernateTemplate
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
