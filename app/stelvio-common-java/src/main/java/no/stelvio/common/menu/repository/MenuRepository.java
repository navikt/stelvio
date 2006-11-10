package no.stelvio.common.menu.repository;

import java.util.List;

import no.stelvio.common.menu.domain.Menu;

/**
 * Interface used to interact with persistence store.
 * Implementations might used different strategies to implement the CRUD operations. 
 * The strategy used is given by the implementations prefix (ie: Jpa, Jdbc, Hibernate).
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public interface MenuRepository {
	/**
	 * Method to retrieve Menu objects that has no parent (parent is <code>null</code>).
	 * Children are retrived with eager fetching.
	 * 
	 * @return a list of root Menu objects. 
	 */
	public List<Menu> getParents();
}