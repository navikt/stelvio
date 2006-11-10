/**
 * 
 */
package no.stelvio.common.menu;

import java.util.List;

import no.stelvio.common.menu.domain.Menu;

/**
 * Defines an interface for accessing data related to menu. 
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public interface MenuItemService {
	/**
	 * Method to get retriveve all menu items.
	 * @return a list of Menu objects.
	 */
	public List<Menu> getMenuItems();
}