/**
 * 
 */
package no.stelvio.domain.menu;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import no.stelvio.common.menu.service.MenuItemService;
import no.stelvio.domain.menu.MenuItem;
import no.stelvio.domain.menu.MenuItemPermission;
import no.stelvio.domain.menu.MenuItemScreen;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/*
 * Test data to insert to a database of choice.
 *   

CREATE TABLE PERMISSION (PERMISSION_ID INTEGER NOT NULL, ROLE VARCHAR(255), DISCRETION TINYINT, VERSION NUMERIC(19), PRIMARY KEY (PERMISSION_ID))
CREATE TABLE SCREEN (SCREEN_ID INTEGER NOT NULL, VERSION NUMERIC(19), NAME VARCHAR(255), PRIMARY KEY (SCREEN_ID))
CREATE TABLE MENU (MENU_ID INTEGER NOT NULL, LEAD_TEXT VARCHAR(255), SORTING INTEGER, FLOW_NAME VARCHAR(255), VERSION NUMERIC(19), PARENT_ID INTEGER, PRIMARY KEY (MENU_ID))
CREATE TABLE PERMISSION_MENU (PERMISSION_ID INTEGER NOT NULL, MENU_ID INTEGER NOT NULL, PRIMARY KEY (PERMISSION_ID, MENU_ID))
CREATE TABLE SCREEN_MENU (SCREEN_ID INTEGER NOT NULL, MENU_ID INTEGER NOT NULL, PRIMARY KEY (SCREEN_ID, MENU_ID))

INSERT INTO PERMISSION(permission_id, version, role, discretion) VALUES(1,1,'admin',0)
INSERT INTO PERMISSION(permission_id, version, role, discretion) VALUES(2,1,'web',1)
INSERT INTO PERMISSION(permission_id, version, role, discretion) VALUES(3,1,'batch',0)

INSERT INTO SCREEN(screen_id, version, name) VALUES(1,1,'brukeroversikt')
INSERT INTO SCREEN(screen_id, version, name) VALUES(2,1,'minprofil')
INSERT INTO SCREEN(screen_id, version, name) VALUES(3,1,'reghenvendelse')
INSERT INTO SCREEN(screen_id, version, name) VALUES(4,1,'sokperson')

INSERT INTO MENU(menu_id, lead_text, sorting, flow_name, version, parent_id) VALUES(1,'MenuItem 01',1,'Flow 01',1,NULL)
INSERT INTO MENU(menu_id, lead_text, sorting, flow_name, version, parent_id) VALUES(2,'MenuItem 02',1,'Flow 02',1,NULL)
INSERT INTO MENU(menu_id, lead_text, sorting, flow_name, version, parent_id) VALUES(3,'MenuItem 03',1,'Flow 03',1,NULL)
INSERT INTO MENU(menu_id, lead_text, sorting, flow_name, version, parent_id) VALUES(4,'MenuItem 04 - child',1,'Flow 04',1,1)
INSERT INTO MENU(menu_id, lead_text, sorting, flow_name, version, parent_id) VALUES(5,'MenuItem 05 - child',1,'Flow 05',1,2)
INSERT INTO MENU(menu_id, lead_text, sorting, flow_name, version, parent_id) VALUES(6,'MenuItem 06 - child',1,'Flow 06',1,1)
INSERT INTO MENU(menu_id, lead_text, sorting, flow_name, version, parent_id) VALUES(7,'Menu07 - child', 1, 'Flow 07', 1, 4)
INSERT INTO MENU(menu_id, lead_text, sorting, flow_name, version, parent_id) VALUES(8,'Menu08 - child', 1, 'Flow 08', 1, 7)

INSERT INTO PERMISSION_MENU(menu_id, permission_id) VALUES (1, 1)
INSERT INTO PERMISSION_MENU(menu_id, permission_id) VALUES (1, 2)
INSERT INTO PERMISSION_MENU(menu_id, permission_id) VALUES (1, 3)
INSERT INTO PERMISSION_MENU(menu_id, permission_id) VALUES (2, 1)
INSERT INTO PERMISSION_MENU(menu_id, permission_id) VALUES (2, 2)
INSERT INTO PERMISSION_MENU(menu_id, permission_id) VALUES (3, 1)
INSERT INTO PERMISSION_MENU(menu_id, permission_id) VALUES (3, 3)
INSERT INTO PERMISSION_MENU(menu_id, permission_id) VALUES (4, 1)
INSERT INTO PERMISSION_MENU(menu_id, permission_id) VALUES (4, 2)
INSERT INTO PERMISSION_MENU(menu_id, permission_id) VALUES (4, 3)
INSERT INTO PERMISSION_MENU(menu_id, permission_id) VALUES (5, 1)
INSERT INTO PERMISSION_MENU(menu_id, permission_id) VALUES (6, 1)
INSERT INTO PERMISSION_MENU(menu_id, permission_id) VALUES (6, 2)

INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (1, 1)
INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (2, 1)
INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (3, 1)
INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (4, 1)
INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (1, 2)
INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (2, 2)
INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (4, 2)
INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (3, 3)
INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (1, 4)
INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (2, 4)
INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (3, 4)
INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (4, 4)
INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (2, 5)
INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (4, 5)
INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (1, 6)
INSERT INTO SCREEN_MENU(screen_id, menu_id) VALUES (4, 6)
 */

/**
 * Test class to test the menu component.
 * 
 * @author person4f9bc5bd17cc, Accenture
 */
public class MenuTest extends TestCase {
	private MenuItemService menuItemService;
	private String screen;
	private List<String> roles = new ArrayList<String>();
	
	/**
	 * Initialize components prior to running tests.
	 */
	public void setUp() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("common-java_test_menu_beans.xml"); 
		menuItemService = (MenuItemService) context.getBean("menu.menuItemService");
		
		screen = "4";
		
		roles.add("admin");
		roles.add("web");
		roles.add("batch");
	}

	/**
	 * Check if the menu has the correct permissions.
	 * @param menuItemPermissions to check
	 * @return true if the menu has correct permissions, false otherwise
	 */
	private boolean checkPermission(List<MenuItemPermission> menuItemPermissions) {
		boolean isValid = false;
		
		for (MenuItemPermission menuItemPermission : menuItemPermissions) {
			// Check roles
			for (String role : roles) {
				if (menuItemPermission.getRole().equalsIgnoreCase(role)) {
					isValid = true;
					break;
				}
			}
			
			// Check desrection
			if (isValid && !menuItemPermission.isDiscretion()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Check if menu is in specified screen
	 * @param menuItemScreens to check
	 * @return true if menu is in screen, false otherwise 
	 */
	private boolean inScreen(List<MenuItemScreen> menuItemScreens) {
		for (MenuItemScreen menuItemScreen : menuItemScreens) {
			if (menuItemScreen.getScreenId() == new Integer(this.screen)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Recursive method to print the menutree
	 * @param menuItem the menu to print
	 * @param indent number of spaces to print before the text
	 */
	public void printMenu(MenuItem menuItem, int indent) {
		List<MenuItemPermission> permissions = menuItem.getPermissions() == null || menuItem.getPermissions().size() <= 0 ? null : menuItem.getPermissions();
		List<MenuItemScreen> screens = menuItem.getScreens() == null || menuItem.getScreens().size() <= 0 ? null : menuItem.getScreens();
		
		boolean createMenuItem = false;
		if (permissions == null && screens == null) {
			createMenuItem = true;
		}
		else if (permissions == null && (screens != null && inScreen(screens))) {
			createMenuItem = true;
		}
		else if ((permissions != null && checkPermission(permissions)) && screens == null) {
			createMenuItem = true;
		}
		else if ((permissions != null && checkPermission(permissions)) && (screens != null && inScreen(screens))) {
			createMenuItem = true;
		}
		
		if (createMenuItem) {
			for (int i = 0; i < indent; i++) {
				System.out.print(" ");
			}
			
			System.out.println(menuItem.getFlowName()+" - "+menuItem.getLeadtext());
			if (menuItem.getChildren() != null && menuItem.getChildren().size() > 0) {
				for (MenuItem aMenu : menuItem.getChildren()) {
					printMenu(aMenu, indent+1);
				}
			}
		}
	}
	
	/**
	 * Method to run the test
	 */
	public void test() {
		List<MenuItem> menuItems = menuItemService.getMenuItems();
		for (MenuItem menuItem : menuItems) {
			printMenu(menuItem, 0);
		}
		System.out.println("menus: "+menuItems.size());
		
		assertTrue("No menu items returned", menuItems.size() > 0);
	}
	
	/**
	 * Cleans up after tests are complete.
	 */
	@Override
	public void tearDown() {
		menuItemService = null;
	}
}