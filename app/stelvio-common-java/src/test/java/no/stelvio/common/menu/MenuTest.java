/**
 * 
 */
package no.stelvio.common.menu;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import no.stelvio.common.menu.domain.Menu;
import no.stelvio.common.menu.domain.Permission;
import no.stelvio.common.menu.domain.Screen;
import no.stelvio.common.menu.repository.MenuRepository;

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

INSERT INTO MENU(menu_id, lead_text, sorting, flow_name, version, parent_id) VALUES(1,'Menu 01',1,'Flow 01',1,NULL)
INSERT INTO MENU(menu_id, lead_text, sorting, flow_name, version, parent_id) VALUES(2,'Menu 02',1,'Flow 02',1,NULL)
INSERT INTO MENU(menu_id, lead_text, sorting, flow_name, version, parent_id) VALUES(3,'Menu 03',1,'Flow 03',1,NULL)
INSERT INTO MENU(menu_id, lead_text, sorting, flow_name, version, parent_id) VALUES(4,'Menu 04 - child',1,'Flow 04',1,1)
INSERT INTO MENU(menu_id, lead_text, sorting, flow_name, version, parent_id) VALUES(5,'Menu 05 - child',1,'Flow 05',1,2)
INSERT INTO MENU(menu_id, lead_text, sorting, flow_name, version, parent_id) VALUES(6,'Menu 06 - child',1,'Flow 06',1,1)
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
	private MenuRepository menuRepo;
	private String screen;
	private List<String> roles = new ArrayList<String>();
	
	/**
	 * Initialize components prior to running tests.
	 */
	public void setUp() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("common-java_test_menu_beans.xml"); 
		menuRepo = (MenuRepository) context.getBean("menu.menuRepositoryImpl");
		
		screen = "4";
		
		roles.add("admin");
		roles.add("web");
		roles.add("batch");
	}

	/**
	 * Check if the menu has the correct permissions.
	 * @param permissions to check
	 * @return true if the menu has correct permissions, false otherwise
	 */
	private boolean checkPermission(List<Permission> permissions) {
		boolean isValid = false;
		
		for (Permission permission : permissions) {
			// Check roles
			for (String role : roles) {
				if (permission.getRole().equalsIgnoreCase(role)) {
					isValid = true;
					break;
				}
			}
			
			// Check desrection
			if (isValid && !permission.isDiscretion()) {
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
	private boolean inScreen(List<Screen> screens) {
		for (Screen screen : screens) {
			if (screen.getScreenId() == new Integer(this.screen)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Recursive method to print the menutree
	 * @param menu the menu to print
	 * @param indent number of spaces to print before the text
	 */
	public void printMenu(Menu menu, int indent) {
		if (checkPermission(menu.getPermissions()) && inScreen(menu.getScreens())) {
			for (int i = 0; i < indent; i++) {
				System.out.print(" ");
			}
			
			System.out.println(menu.getFlowName()+" - "+menu.getLeadtext());
			if (menu.getChildren() != null && menu.getChildren().size() > 0) {
				for (Menu aMenu : menu.getChildren()) {
					printMenu(aMenu, indent+1);
				}
			}
		}
	}
	
	/**
	 * Method to run the test
	 */
	public void test() {
		menuRepo.populateTestData();
		List<Menu> menus = menuRepo.getParents();
		for (Menu menu : menus) {
			printMenu(menu, 0);
		}
		System.out.println("menus: "+menus.size());
	}
	
	/**
	 * Cleans up after tests are complete.
	 */
	@Override
	public void tearDown() {
		menuRepo = null;
	}
}