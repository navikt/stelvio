package no.stelvio.batch.repository;

import org.springframework.orm.hibernate3.HibernateTemplate;

import no.stelvio.batch.domain.FileIdentity;

/**
 * Hibernate implementation of {@link FileIdentityRepository}. Before this implementation is used, 
 * a HibernateTemplate must be set, typically through Spring dependency injection.
 * 
 * @author person983601e0e117 (Accenture)
 *
 */
public class HibernateFileIdentity implements FileIdentityRepository {

	/** HibernateTemplate */
	private HibernateTemplate hibernateTemplate;
	
	/**
	 * {@inheritDoc}
	 */
	public FileIdentity findById(long fileIdentityId) {
		return (FileIdentity) getHibernateTemplate().get(FileIdentity.class, fileIdentityId);
	}

	/**
	 * {@inheritDoc}
	 */	
	public void save(FileIdentity fileIdentity) {
		if(fileIdentity.getFileIdentityId() != 0){
			getHibernateTemplate().persist(fileIdentity);
		}else{
			getHibernateTemplate().merge(fileIdentity);
		}		
	}

	/**
	 * Gets the HibernateTemplate
	 * @return hibernateTemplate
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * Sets the hibernateTemplate
	 * @param hibernateTemplate instance of a <code>HibernateTemplate</code>
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	

}
