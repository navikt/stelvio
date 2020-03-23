package no.stelvio.repository.menu.support;

import static no.stelvio.common.util.Internal.cast;

import java.util.List;

import org.springframework.orm.hibernate4.HibernateTemplate;

import no.stelvio.domain.menu.MenuItem;
import no.stelvio.repository.menu.MenuRepository;

/**
 * Implementation of MenuRepository interface that uses Hibernate to perform CRUD operations. Class contains a
 * HibernateTemplate reference that must be injected.
 *
 */
public class HibernateMenuRepository implements MenuRepository {
	private HibernateTemplate hibernateTemplate;

	@Override
	public List<MenuItem> getParents() {
		return cast(hibernateTemplate.findByNamedQuery("MenuItem.findParents"));
	}

	/**
	 * Sets a HibernateTemplate.
	 *
	 * @param hibernateTemplate instance
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
