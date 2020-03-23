package no.stelvio.service.menu.to;

import java.util.ArrayList;
import java.util.List;

import no.stelvio.common.transferobject.ServiceResponse;
import no.stelvio.domain.menu.MenuItem;

/**
 * Serializable Response object returned from the MenuItemService.
 * 
 *
 */
public class MenuItemServiceResponse extends ServiceResponse {

	private static final long serialVersionUID = 2721957614150130964L;

	private List<MenuItem> menuItemList;

	/**
	 * Constructs a new MenuItemSerivceResponse without any menu items. The MenuItemSerivceResponse will be set to hold an empty
	 * list of <code>MenuItem</code>s.
	 * 
	 */
	public MenuItemServiceResponse() {
		super();
		this.menuItemList = new ArrayList<>();
	}

	/**
	 * Constructs a new MenuItemSerivceResponse holding the list of <code>MenuItem</code>s specified by the parameter. If the
	 * parameter is <code>null</code> the MenuItemSerivceResponse will be set to hold an empty list.
	 * 
	 * @param menuItemList
	 *            list of <code>MenuItem</code>s
	 */
	public MenuItemServiceResponse(List<MenuItem> menuItemList) {
		this();
		// Only set a list if it's not null
		if (menuItemList != null) {
			this.menuItemList = menuItemList;
		}
	}

	/**
	 * Gets a list of <code>MenuItem</code>s.
	 * 
	 * @return list of <code>MenuItem</code>s
	 */
	public List<MenuItem> getMenuItemList() {
		return menuItemList;
	}

	/**
	 * Sets a list of <code>MenuItem</code>s.
	 * 
	 * @param menuItemList
	 *            list of <code>MenuItem</code>s
	 */
	public void setMenuItemList(List<MenuItem> menuItemList) {
		this.menuItemList = menuItemList;
	}

}
