package no.stelvio.service.menu.support;

import no.stelvio.repository.menu.MenuRepository;
import no.stelvio.service.menu.MenuService;
import no.stelvio.service.menu.to.MenuItemServiceResponse;

/**
 * Menu service implementation of the {@link MenuService} business interface.
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $Id$
 */
public class DefaultMenuService implements MenuService {
	private MenuRepository menuRepository;

	/**
	 * {@inheritDoc MenuService#getMenuItems()}.
	 * 
	 * @return MenuItemServiceResponse containg a List of all the menu items.
	 */
	public MenuItemServiceResponse getMenuItems() {
		return new MenuItemServiceResponse(menuRepository.getParents());
	}

	/**
	 * Sets the menu repository (dependency injected).
	 * 
	 * @param menuRepository
	 *            The menu repository to set
	 */
	public void setMenuRepository(MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}
}