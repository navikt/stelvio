package no.stelvio.repository.menu;

import java.util.List;

import no.stelvio.domain.menu.MenuItem;


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
	 * Method to retrieve MenuItem objects that has no parent (parent is <code>null</code>).
	 * Children are retrived with eager fetching.
	 * 
	 * @return List of root MenuItem objects. 
	 */
	List<MenuItem> getParents();
	
}