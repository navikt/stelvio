package no.stelvio.service.menu.support;

import no.stelvio.common.transferobject.EntityListResponse;
import no.stelvio.common.transferobject.support.DefaultEntityListResponse;
import no.stelvio.domain.menu.MenuItem;
import no.stelvio.repository.menu.MenuRepository;
import no.stelvio.service.menu.MenuService;

/**
 * Menu service implementation of the {@link MenuService} business interface.
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $Id$
 */
public class DefaultMenuService implements MenuService {
	private MenuRepository menuRepository;

	/**
	 * {@inheritDoc MenuService#getMenuItems()}
	 * 
	 * @return List of all the menu items.
	 */
	public EntityListResponse<MenuItem> getMenuItems() {
		return new DefaultEntityListResponse<MenuItem>(menuRepository
				.getParents());
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