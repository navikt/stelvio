/**
 * 
 */
package no.stelvio.presentation.action;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.domain.menu.MenuItem;
import no.stelvio.domain.menu.MenuItemPermission;
import no.stelvio.domain.menu.MenuItemScreen;
import no.stelvio.service.menu.MenuService;

import org.apache.myfaces.custom.navmenu.NavigationMenuItem;

/**
 * Action-class for menu-related actions. The MenuService to be used
 * must be injected.
 * 
 * @author person6045563b8dec
 *
 */
public class MenuAction {
	
	private MenuService menuService;

	/**
	 * Menu items are retrieved by the <code>MenuService<code> service. A menu structure
	 * consisting of <code>NavigationMenuItem<code> is created from the <code>MenuItem<code>
	 * objects returned by the service. 
	 * The menu strucutre consists of a list of <code>NavigationMenuItems<code>, each 
	 * <code>NavigationMenuItem<code> may have one or several nested or unnested 
	 * <code>NavigationMenuItem<code> as children. 
	 *  
	 * 
	 * @return List of <code>NavigationMenuItem<code> objects.
	 */
	public List<NavigationMenuItem> getNavigationItems() {

		List<NavigationMenuItem> menu = new ArrayList<NavigationMenuItem>();
		List<MenuItem> menuItems = menuService.getMenuItems().getMenuItemList();
		
		for(MenuItem item : menuItems) {
			NavigationMenuItem navItem = createComponents(item);
			if(navItem != null) {
				menu.add(navItem);
			}
		}
		
		return menu;
	}

	
	/**
	 * Creates a <code>NavigationMenuItem<code> object from a <code>MenuItem<code> object.
	 * If the user has permission to see the menu item, and the menu item should be 
	 * visible on the current page, a <code>NavigationMenuItem<code> is created to
	 * represent the menu item object. If a menu item has children, this method
	 * is called recursively to create all children and the children of the children.
	 * 
	 * @param menu  a <code>MenuItem<code> 
	 * @return a <code>NavigationMenuItem<code> representing the <code>MenuItem<code>
	 */
	private NavigationMenuItem createComponents(MenuItem menu) {
		NavigationMenuItem menuItem = null;
		List<MenuItemPermission> permissions = menu.getPermissions() == null 
										|| menu.getPermissions().size() <= 0 ? null : menu.getPermissions();
		List<MenuItemScreen> screens = menu.getScreens() == null  
										|| menu.getScreens().size() <= 0 ? null : menu.getScreens();
		
		boolean createMenuItem = false;
		if (permissions == null && screens == null) {
			createMenuItem = true;
		} else if (permissions == null && (screens != null && inScreen(screens))) {
			createMenuItem = true;
		} else if ((permissions != null && checkPermission(permissions)) && screens == null) {
			createMenuItem = true;
		} else if ((permissions != null && checkPermission(permissions)) && (screens != null && inScreen(screens))) {
			createMenuItem = true;
		}
		
		if (createMenuItem) {
			menuItem = new NavigationMenuItem(menu.getLeadtext(), menu.getFlowName());
			
			if (menu.getChildren() != null && menu.getChildren().size() > 0) {
				for (MenuItem aMenu : menu.getChildren()) {
					
					NavigationMenuItem childItem = createComponents(aMenu);
					if(childItem != null) {
						menuItem.add(childItem);
					}
				}
			}
		}
		return menuItem;
	}
	
	/**
	 * TODO: Add check for discretion
	 * 
	 * Check if the user has the correct permissions to view the menuitem.
	 * 
	 * @param permissions to check
	 * @return true if the menu has correct permissions, false otherwise
	 */
	private boolean checkPermission(List<MenuItemPermission> permissions) {
		FacesContext context = FacesContext.getCurrentInstance();
		for (MenuItemPermission permission : permissions) {
			ExternalContext externalContext = context.getExternalContext();

			if (externalContext.isUserInRole(permission.getRole())) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Check if menu is in specified screen
	 * @param screens to check
	 * @return true if menu is in screen, false otherwise 
	 */
	private boolean inScreen(List<MenuItemScreen> screens) {
		for (MenuItemScreen screen : screens) {
			if (screen.getName().equalsIgnoreCase(RequestContextHolder.currentRequestContext().getScreenId())) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Sets the menuservice instance to use in this class
	 * 
	 * @param menuService the menuService to set
	 */
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}
}
