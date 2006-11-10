/**
 * 
 */
package no.stelvio.common.menu;

import java.util.List;

import no.stelvio.common.menu.domain.Menu;
import no.stelvio.common.menu.repository.MenuRepository;

/**
 * Implements services for accessing data related to menu. 
 * 
 * @author person4f9bc5bd17cc, Accenture
 */
public class MenuItemServiceImpl implements MenuItemService {
	private MenuRepository menuRepository;

	/**
	 * {@inheritDoc MenuItemService#getMenuItems()}
	 */
	public List<Menu> getMenuItems() {
		return menuRepository.getParents();
	}
	
	/**
	 * @param menuRepository the menuRepository to set
	 */
	public void setMenuRepository(MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}
}