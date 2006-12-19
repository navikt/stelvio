/**
 * 
 */
package no.stelvio.common.menu.service.support;

import java.util.List;

import no.stelvio.common.menu.domain.MenuItem;
import no.stelvio.common.menu.repository.MenuRepository;
import no.stelvio.common.menu.service.MenuItemService;

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
	public List<MenuItem> getMenuItems() {
		return menuRepository.getParents();
	}
	
	/**
	 * @param menuRepository the menuRepository to set
	 */
	public void setMenuRepository(MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}
}