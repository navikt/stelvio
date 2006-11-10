package no.stelvio.common.menu.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import no.stelvio.common.menu.domain.Menu;
import no.stelvio.common.menu.domain.Permission;
import no.stelvio.common.menu.domain.Screen;

/**
 * Implementation of MenuRepository interface that uses Java Persistence API to perform CRUD operations.
 * Class contains a EntityManager reference that must be injected.
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class JpaMenuRepository implements MenuRepository {
	/**
	 * The entity manager will be injected by the Spring IoC container.
	 */
	@PersistenceContext
	private EntityManager entityManager;
	
	public JpaMenuRepository() {}
	
	/**
	 * {@inheritDoc MenuRepository#getParents()}
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getParents() {
		return entityManager.createNamedQuery("Menu.findParents").getResultList();
	}
}