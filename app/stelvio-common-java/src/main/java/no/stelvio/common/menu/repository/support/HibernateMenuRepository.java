package no.stelvio.common.menu.repository.support;

import no.stelvio.common.menu.repository.MenuRepository;

/**
 * Implementation of MenuRepository interface that uses Hibernate to perform CRUD operations.
 * Class contains a HibernateTemplate reference that must be injected.
 * 
 * @author person983601e0e117 (Accenture)
 *
 */
public class HibernateMenuRepository implements MenuRepository {
//
//	public HibernateTemplate hibernateTemplate;
//	
//	/**
//	 * Default no-arg constructor
//	 * Calls to this constructor must be followed by the injection of hibernateTemplate
//	 */
//	public HibernateMenuRepository(){}
//	
//	/**
//	 * {@inheritDoc MenuRepository#getParents()}
//	 */	
//	@SuppressWarnings("unchecked")
//	public List<MenuItem> getParents() {
//		return hibernateTemplate.findByNamedQuery("MenuItem.findParents");
//	}
//
//	/**
//	 * Gets a HibernateTemplate
//	 * @return hibernateTemplate 
//	 */
//	public HibernateTemplate getHibernateTemplate() {
//		return hibernateTemplate;
//	}
//	
//	/**
//	 * Sets a HibernateTemplate
//	 * @param hibernateTemplate instance
//	 */
//	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
//		this.hibernateTemplate = hibernateTemplate;
//	}

}
