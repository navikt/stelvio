package no.stelvio.service.menu.support;

import java.util.List;

import no.stelvio.domain.menu.MenuItem;
import no.stelvio.repository.menu.MenuRepository;
import no.stelvio.service.menu.MenuItemService;

/**
 * Implements services for accessing data related to menu. 
 * 
 * @author person4f9bc5bd17cc, Accenture
 */
public class DefaultMenuItemService implements MenuItemService {
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