package no.stelvio.batch.repository.support;

import no.stelvio.batch.domain.FileIdentity;
import no.stelvio.batch.repository.FileIdentityRepository;

import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Hibernate implementation of {@link FileIdentityRepository}. Before this implementation is used, a HibernateTemplate must be
 * set, typically through Spring dependency injection.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public class HibernateFileIdentityRepository implements FileIdentityRepository {

	/** HibernateTemplate. */
	private HibernateTemplate hibernateTemplate;

	/**
	 * {@inheritDoc}
	 */
	public FileIdentity findById(Long fileIdentityId) {
		return (FileIdentity) getHibernateTemplate().get(FileIdentity.class, fileIdentityId);
	}

	/**
	 * {@inheritDoc}
	 */
	public Long save(FileIdentity fileIdentity) {
		if (fileIdentity.getFileIdentityId() != 0) {
			getHibernateTemplate().persist(fileIdentity);
		} else {
			getHibernateTemplate().merge(fileIdentity);
		}
		// Flush to force database update, and consequent primary key generation
		getHibernateTemplate().flush();
		return fileIdentity.getFileIdentityId();
	}

	/**
	 * Gets the HibernateTemplate.
	 * 
	 * @return hibernateTemplate
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * Sets the hibernateTemplate.
	 * 
	 * @param hibernateTemplate
	 *            instance of a <code>HibernateTemplate</code>
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
