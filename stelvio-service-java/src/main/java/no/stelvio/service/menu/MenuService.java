package no.stelvio.service.menu;

import no.stelvio.service.menu.to.MenuItemServiceResponse;

/**
 * Business interface for menu operations. This service provides an interface
 * for accessing the menu and menuitems.
 * <p>
 * A menu is specific to an application. The menu items are configured in a
 * database.To use the menu service in an application , this service should be
 * made available as an EJB. Best practice for using the menu service in a web
 * application would be to create a custom menu taglib that is responsible for
 * rendering the menu.
 * 
 * @version $Id$
 */
public interface MenuService {
	/**
	 * Retrieves a list of all menu items.
	 * <p>
	 * The menu items have a specific configuration that specifies: 
	 * <li>Which permissions the user must have to view the item.
	 * <li>Which screens the item should be available from.
	 * <li>If the item should be available for users with discretion. 
	 * <p>
	 * When a client asks to retrieve the menu items, all menu items are
	 * returned regardless of the permissions the client has. It is up to the
	 * client to filter the menu items based on permission, screens, discretion
	 * etc. before displaying them in an application.
	 * 
	 * @return a <code>MenuItemSerivceResponse</code> holding a list of all available <code>MenuItem</code>s
	 */
	MenuItemServiceResponse getMenuItems();
}