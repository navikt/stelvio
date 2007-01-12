/**
 * 
 */
package no.stelvio.common.menu.service;

import java.util.List;

import no.stelvio.domain.menu.MenuItem;

/**
 * Defines an interface for accessing data related to menu. 
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public interface MenuItemService {
	/**
	 * Method to get retriveve all menu items.
	 * @return a list of MenuItem objects.
	 */
	public List<MenuItem> getMenuItems();
}