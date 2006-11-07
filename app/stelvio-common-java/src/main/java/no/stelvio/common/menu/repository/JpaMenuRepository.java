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
	 * {@inheritDoc MenuRepository.getParents()}
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getParents() {
		return entityManager.createNamedQuery("Menu.findParents").getResultList();
	}

	/**
	 * TODO: Remove me
	 */
	public void populateTestData() {
		// Permissions
		Permission p1 = new Permission();
		p1.setRole("admin");
		p1.setDiscretion(false);
		entityManager.persist(p1);
		
		// Screens
		Screen s1 = new Screen();
		s1.setName("brukeroversikt");
		entityManager.persist(s1);
		
		// Menus
		Menu m1 = new Menu();
		m1.setFlowName("Flow 1");
		m1.setLeadtext("Menu 1");
		entityManager.persist(m1);
		
		Menu m2 = new Menu();
		m2.setFlowName("Flow 2");
		m2.setLeadtext("Menu 2");
		m2.setParent(m1);
		entityManager.persist(m2);
	}
}