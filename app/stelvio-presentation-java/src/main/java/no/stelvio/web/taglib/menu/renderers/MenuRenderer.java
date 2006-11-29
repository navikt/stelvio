/**
 * 
 */
package no.stelvio.web.taglib.menu.renderers;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.servlet.ServletContext;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.menu.MenuItemService;
import no.stelvio.common.menu.MenuItemServiceImpl;
import no.stelvio.common.menu.domain.Menu;
import no.stelvio.common.menu.domain.Permission;
import no.stelvio.common.menu.domain.Screen;
import no.stelvio.web.taglib.menu.components.MenuComponent;
import no.stelvio.web.taglib.menu.components.MenuItemComponent;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * TODO: Document me
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class MenuRenderer extends Renderer {
	private MenuComponent root;
	private FacesContext context;
	
	/**
	 * TODO: Document me
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		this.context = context;
		this.root = (MenuComponent) component;
		if (StringUtils.isEmpty(root.getId())) {
			root.setId(context.getViewRoot().createUniqueId());
		}
		
		// Clear children to avoid dupliaction
		root.getChildren().clear();

		List<Menu> menus = getMenuItems();
		
		for (Menu menu : menus) {
			createComponents(menu, 0);
		}
		
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("ul", component);
	}
	
	/**
	 * TODO: Document me
	 */
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("ul");
		writer.close();
	}
	
	/**
	 * TODO: Document me
	 * @return
	 */
	private List<Menu> getMenuItems() {
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		MenuItemService menuItemService = (MenuItemServiceImpl) applicationContext.getBean("menu.menuItemService");
		
		return menuItemService.getMenuItems();
	}
	
	/**
	 * TODO: Document me
	 * @param menu
	 * @param indent
	 */
	@SuppressWarnings("unchecked")
	private void createComponents(Menu menu, int indent) {
		if ((menu.getPermissions() == null || menu.getPermissions().size() <= 0) ||
				(checkPermission(menu.getPermissions()) && inScreen(menu.getScreens()))) {
			MenuItemComponent menuItem = new MenuItemComponent();
			menuItem.setId(context.getViewRoot().createUniqueId());
			menuItem.setLeadText(menu.getLeadtext());
			menuItem.setAction(menu.getFlowName());
			menuItem.setStyleClass(indent <= 0 ? root.getRootStyleClass() : root.getChildStyleClass());
			menuItem.setInlineStyle(indent <= 0 ? root.getRootInlineStyle() : root.getChildInlineStyle());
						
			root.getChildren().add(menuItem);
			
			if (menu.getChildren() != null && menu.getChildren().size() > 0) {
				for (Menu aMenu : menu.getChildren()) {
					createComponents(aMenu, indent+1);
				}
			}
		}
	}
	
	/**
	 * TODO: Add check for discretion
	 * 
	 * Check if the menu has the correct permissions.
	 * @param permissions to check
	 * @return true if the menu has correct permissions, false otherwise
	 */
	private boolean checkPermission(List<Permission> permissions) {
		for (Permission permission : permissions) {
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
	private boolean inScreen(List<Screen> screens) {
		for (Screen screen : screens) {
			if (screen.getName().equalsIgnoreCase(RequestContext.getScreenId())) {
				return true;
			}
		}
		
		return false;
	}
	
//	/**
//	 * TODO: Document me
//	 * @return
//	 */
//	private List<Menu> getMockMenuWithChildren() {
//		// PERMISSIONS
//		Permission admin1 = new Permission();
//		admin1.setRole("admin");
//		admin1.setDiscretion(false);
//		
//		Permission admin2 = new Permission();
//		admin2.setRole("admin");
//		admin2.setDiscretion(true);
//		
//		Permission web = new Permission();
//		web.setRole("web");
//		web.setDiscretion(false);
//		
//		Permission batch = new Permission();
//		batch.setRole("batch");
//		batch.setDiscretion(false);
//		
//		// SCREENS
//		Screen s1 = new Screen();
//		s1.setName("brukeroversikt");
//		
//		Screen s2 = new Screen();
//		s2.setName("minprofil");
//		
//		Screen s3 = new Screen();
//		s3.setName("reghenvendelse");
//		
//		// MENUS
//		// ROOT 1
//		Menu r1 = new Menu();
//		r1.setFlowName("brukeroversikt-flow");
//		r1.setLeadtext("Brukeroversikt");
//		r1.setMenuId(1);
//		
//		// Set permissions for r2
//		List<Permission> pr1 = new ArrayList<Permission>();
//		pr1.add(admin1);
//		pr1.add(admin2);
//		pr1.add(web);
//		pr1.add(batch);
//		r1.setPermissions(pr1);
//		
//		// Set sreens for r1
//		List<Screen> sr1 = new ArrayList<Screen>();
//		sr1.add(s1);
//		sr1.add(s2);
//		sr1.add(s3);
//		r1.setScreens(sr1);
//		
//		Menu c1_1 = new Menu();
//		c1_1.setFlowName("childflow1-1");
//		c1_1.setLeadtext("Child 1-1");
//		c1_1.setMenuId(2);
//		
//		// Set permissions for c1_1
//		List<Permission> pc1_1 = new ArrayList<Permission>();
//		pc1_1.add(admin1);
//		pc1_1.add(admin2);
//		pc1_1.add(web);
//		pc1_1.add(batch);
//		c1_1.setPermissions(pc1_1);
//		
//		// Set sreens for c1_1
//		List<Screen> sc1_1 = new ArrayList<Screen>();
//		sc1_1.add(s1);
//		sc1_1.add(s2);
//		sc1_1.add(s3);
//		c1_1.setScreens(sc1_1);
//		
//		Menu c1_2 = new Menu();
//		c1_2.setFlowName("childflow1-2");
//		c1_2.setLeadtext("Child 1-2");
//		c1_2.setMenuId(3);
//		
//		// Set permissions for c1_2
//		List<Permission> pc1_2 = new ArrayList<Permission>();
//		pc1_2.add(admin1);
//		pc1_2.add(admin2);
//		pc1_2.add(web);
//		c1_2.setPermissions(pc1_2);
//		
//		// Set sreens for c1_1
//		List<Screen> sc1_2 = new ArrayList<Screen>();
//		sc1_2.add(s1);
//		sc1_2.add(s2);
//		sc1_2.add(s3);
//		c1_2.setScreens(sc1_2);
//		
//		Menu c1_2_1 = new Menu();
//		c1_2_1.setFlowName("childflow1-2-1");
//		c1_2_1.setLeadtext("Child 1-2-1");
//		c1_2_1.setMenuId(4);
//		
//		// Set permissions for c1_2_1
//		List<Permission> pc1_2_1 = new ArrayList<Permission>();
//		pc1_2_1.add(admin1);
//		pc1_2_1.add(admin2);
//		pc1_2_1.add(web);
//		pc1_2_1.add(batch);
//		c1_2_1.setPermissions(pc1_2_1);
//		
//		// Set sreens for c1_1
//		List<Screen> sc1_2_1 = new ArrayList<Screen>();
//		sc1_2_1.add(s1);
//		sc1_2_1.add(s3);
//		c1_2_1.setScreens(sc1_2_1);
//		
//		// Add children to c1_2
//		List<Menu> cl1_2 = new ArrayList<Menu>();
//		cl1_2.add(c1_2_1);
//		c1_2.setChildren(cl1_2);
//		
//		Menu c1_3 = new Menu();
//		c1_3.setFlowName("childflow1-3");
//		c1_3.setLeadtext("Child 1-3");
//		c1_3.setMenuId(5);
//		
//		// Set permissions for c1_3
//		List<Permission> pc1_3 = new ArrayList<Permission>();
//		pc1_3.add(admin1);
//		pc1_3.add(web);
//		pc1_3.add(batch);
//		c1_3.setPermissions(pc1_3);
//		
//		// Set sreens for r1
//		List<Screen> sc1_3 = new ArrayList<Screen>();
//		sc1_3.add(s1);
//		sc1_3.add(s2);
//		c1_3.setScreens(sr1);
//		
//		// Add children to r1
//		List<Menu> cl1 = new ArrayList<Menu>();
//		cl1.add(c1_1);
//		cl1.add(c1_2);
//		cl1.add(c1_3);
//		r1.setChildren(cl1);
//		
//		// ROOT 2
//		Menu r2 = new Menu();
//		r2.setFlowName("sok");
//		r2.setLeadtext("Søke person");
//		r2.setMenuId(6);
//		
//		// Set permissions for r2
//		List<Permission> pr2 = new ArrayList<Permission>();
//		pr2.add(admin1);
//		pr2.add(admin2);
//		pr2.add(web);
//		pr2.add(batch);
//		r2.setPermissions(pr2);
//		
//		// Set sreens for c1_1
//		List<Screen> sr2 = new ArrayList<Screen>();
//		sr2.add(s1);
//		sr2.add(s2);
//		sr2.add(s3);
//		r2.setScreens(sr2);
//		
//		Menu c2_1 = new Menu();
//		c2_1.setFlowName("start-flow");
//		c2_1.setLeadtext("Start");
//		c2_1.setMenuId(7);
//		
//		// Set permissions for c2_1
//		List<Permission> pc2_1 = new ArrayList<Permission>();
//		pc2_1.add(admin1);
//		pc2_1.add(admin2);
//		pc2_1.add(web);
//		pc2_1.add(batch);
//		c2_1.setPermissions(pc2_1);
//		
//		// Set sreens for c1_1
//		List<Screen> sc2_1 = new ArrayList<Screen>();
//		sc2_1.add(s1);
//		sc2_1.add(s3);
//		c2_1.setScreens(sc2_1);
//		
//		// Add children to r2
//		List<Menu> cl2 = new ArrayList<Menu>();
//		cl2.add(c2_1);
//		r2.setChildren(cl2);
//		
//		// ROOT 3
//		Menu r3 = new Menu();
//		r3.setFlowName("rootflow3");
//		r3.setLeadtext("Root 3");
//		r3.setMenuId(8);
//		
//		// Set permissions for r3
//		List<Permission> pr3 = new ArrayList<Permission>();
//		pr3.add(admin1);
//		pr3.add(admin2);
//		pr3.add(web);
//		pr3.add(batch);
//		r3.setPermissions(pr3);
//		
//		// Set sreens for c1_1
//		List<Screen> sr3 = new ArrayList<Screen>();
//		sr3.add(s1);
//		sr3.add(s3);
//		r3.setScreens(sr3);
//		
//		// Add to list
//		List<Menu> menus = new ArrayList<Menu>();
//		menus.add(r1);
//		menus.add(r2);
//		menus.add(r3);
//		
//		return menus;
//	}
}