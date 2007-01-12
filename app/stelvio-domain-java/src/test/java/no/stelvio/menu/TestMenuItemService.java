/**
 * 
 */
package no.stelvio.common.menu;

import java.util.ArrayList;
import java.util.List;

import no.stelvio.common.menu.service.MenuItemService;
import no.stelvio.domain.menu.MenuItem;
import no.stelvio.domain.menu.MenuItemPermission;
import no.stelvio.domain.menu.MenuItemScreen;


/**
 * @author utvikler
 *
 */
public class TestMenuItemService implements MenuItemService {
	public List<MenuItem> getMenuItems() {
		return getMockMenuWithChildren();
	}
	
	/**
	 * TODO: Document me
	 * @return
	 */
	private List<MenuItem> getMockMenuWithChildren() {
		// PERMISSIONS
		MenuItemPermission admin1 = new MenuItemPermission();
		admin1.setRole("admin");
		admin1.setDiscretion(false);
		
		MenuItemPermission admin2 = new MenuItemPermission();
		admin2.setRole("admin");
		admin2.setDiscretion(true);
		
		MenuItemPermission web = new MenuItemPermission();
		web.setRole("web");
		web.setDiscretion(false);
		
		MenuItemPermission batch = new MenuItemPermission();
		batch.setRole("batch");
		batch.setDiscretion(false);
		
		// SCREENS
		MenuItemScreen s1 = new MenuItemScreen();
		s1.setName("brukeroversikt");
		
		MenuItemScreen s2 = new MenuItemScreen();
		s2.setName("minprofil");
		
		MenuItemScreen s3 = new MenuItemScreen();
		s3.setName("reghenvendelse");
		
		// MENUS
		// ROOT 1
		MenuItem r1 = new MenuItem();
		r1.setFlowName("brukeroversikt-flow");
		r1.setLeadtext("Brukeroversikt");
		r1.setMenuId(1);
		
		// Set permissions for r2
		List<MenuItemPermission> pr1 = new ArrayList<MenuItemPermission>();
		pr1.add(admin1);
		pr1.add(admin2);
		pr1.add(web);
		pr1.add(batch);
//		r1.setMenuItemPermissions(pr1);
		
		// Set sreens for r1
		List<MenuItemScreen> sr1 = new ArrayList<MenuItemScreen>();
		sr1.add(s1);
		sr1.add(s2);
		sr1.add(s3);
		r1.setScreens(sr1);
		
		MenuItem c1_1 = new MenuItem();
		c1_1.setFlowName("childflow1-1");
		c1_1.setLeadtext("Child 1-1");
		c1_1.setMenuId(2);
		
		// Set permissions for c1_1
		List<MenuItemPermission> pc1_1 = new ArrayList<MenuItemPermission>();
		pc1_1.add(admin1);
		pc1_1.add(admin2);
		pc1_1.add(web);
		pc1_1.add(batch);
//		c1_1.setMenuItemPermissions(pc1_1);
		
		// Set sreens for c1_1
		List<MenuItemScreen> sc1_1 = new ArrayList<MenuItemScreen>();
		sc1_1.add(s1);
		sc1_1.add(s2);
		sc1_1.add(s3);
		c1_1.setScreens(sc1_1);
		
		MenuItem c1_2 = new MenuItem();
		c1_2.setFlowName("childflow1-2");
		c1_2.setLeadtext("Child 1-2");
		c1_2.setMenuId(3);
		
		// Set permissions for c1_2
		List<MenuItemPermission> pc1_2 = new ArrayList<MenuItemPermission>();
		pc1_2.add(admin1);
		pc1_2.add(admin2);
		pc1_2.add(web);
		c1_2.setPermissions(pc1_2);
		
		// Set sreens for c1_1
		List<MenuItemScreen> sc1_2 = new ArrayList<MenuItemScreen>();
		sc1_2.add(s1);
		sc1_2.add(s2);
		sc1_2.add(s3);
		c1_2.setScreens(sc1_2);
		
		MenuItem c1_2_1 = new MenuItem();
		c1_2_1.setFlowName("childflow1-2-1");
		c1_2_1.setLeadtext("Child 1-2-1");
		c1_2_1.setMenuId(4);
		
		// Set permissions for c1_2_1
		List<MenuItemPermission> pc1_2_1 = new ArrayList<MenuItemPermission>();
		pc1_2_1.add(admin1);
		pc1_2_1.add(admin2);
		pc1_2_1.add(web);
		pc1_2_1.add(batch);
		c1_2_1.setPermissions(pc1_2_1);
		
		// Set sreens for c1_1
		List<MenuItemScreen> sc1_2_1 = new ArrayList<MenuItemScreen>();
		sc1_2_1.add(s1);
		sc1_2_1.add(s3);
		c1_2_1.setScreens(sc1_2_1);
		
		// Add children to c1_2
		List<MenuItem> cl1_2 = new ArrayList<MenuItem>();
		cl1_2.add(c1_2_1);
		c1_2.setChildren(cl1_2);
		
		MenuItem c1_3 = new MenuItem();
		c1_3.setFlowName("childflow1-3");
		c1_3.setLeadtext("Child 1-3");
		c1_3.setMenuId(5);
		
		// Set permissions for c1_3
		List<MenuItemPermission> pc1_3 = new ArrayList<MenuItemPermission>();
		pc1_3.add(admin1);
		pc1_3.add(web);
		pc1_3.add(batch);
		c1_3.setPermissions(pc1_3);
		
		// Set sreens for r1
		List<MenuItemScreen> sc1_3 = new ArrayList<MenuItemScreen>();
		sc1_3.add(s1);
		sc1_3.add(s2);
		c1_3.setScreens(sr1);
		
		// Add children to r1
		List<MenuItem> cl1 = new ArrayList<MenuItem>();
		cl1.add(c1_1);
		cl1.add(c1_2);
		cl1.add(c1_3);
		r1.setChildren(cl1);
		
		// ROOT 2
		MenuItem r2 = new MenuItem();
		r2.setFlowName("sok");
		r2.setLeadtext("Søke person");
		r2.setMenuId(6);
		
		// Set permissions for r2
		List<MenuItemPermission> pr2 = new ArrayList<MenuItemPermission>();
		pr2.add(admin1);
		pr2.add(admin2);
		pr2.add(web);
		pr2.add(batch);
//		r2.setMenuItemPermissions(pr2);
		
		// Set sreens for c1_1
		List<MenuItemScreen> sr2 = new ArrayList<MenuItemScreen>();
		sr2.add(s1);
		sr2.add(s2);
		sr2.add(s3);
//		r2.setScreens(sr2);
		
		MenuItem c2_1 = new MenuItem();
		c2_1.setFlowName("start-flow");
		c2_1.setLeadtext("Start");
		c2_1.setMenuId(7);
		
		// Set permissions for c2_1
		List<MenuItemPermission> pc2_1 = new ArrayList<MenuItemPermission>();
		pc2_1.add(admin1);
		pc2_1.add(admin2);
		pc2_1.add(web);
		pc2_1.add(batch);
		c2_1.setPermissions(pc2_1);
		
		// Set sreens for c1_1
		List<MenuItemScreen> sc2_1 = new ArrayList<MenuItemScreen>();
		sc2_1.add(s1);
		sc2_1.add(s3);
		c2_1.setScreens(sc2_1);
		
		// Add children to r2
		List<MenuItem> cl2 = new ArrayList<MenuItem>();
		cl2.add(c2_1);
		r2.setChildren(cl2);
		
		// ROOT 3
		MenuItem r3 = new MenuItem();
		r3.setFlowName("rootflow3");
		r3.setLeadtext("Root 3");
		r3.setMenuId(8);
		
		// Set permissions for r3
		List<MenuItemPermission> pr3 = new ArrayList<MenuItemPermission>();
		pr3.add(admin1);
		pr3.add(admin2);
		pr3.add(web);
		pr3.add(batch);
		r3.setPermissions(pr3);
		
		// Set sreens for c1_1
		List<MenuItemScreen> sr3 = new ArrayList<MenuItemScreen>();
		sr3.add(s1);
		sr3.add(s3);
		r3.setScreens(sr3);
		
		// Add to list
		List<MenuItem> menus = new ArrayList<MenuItem>();
		menus.add(r1);
		menus.add(r2);
		menus.add(r3);
		
		return menus;
	}
}
