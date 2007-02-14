/**
 * 
 */
package no.stelvio.presentation.jsf.action;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.LifecycleFactory;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.domain.menu.MenuItem;
import no.stelvio.domain.menu.MenuItemPermission;
import no.stelvio.domain.menu.MenuItemScreen;
import no.stelvio.presentation.security.page.MockExternalContextExtended;
import no.stelvio.presentation.security.page.MockHttpServletRequestExtended;
import no.stelvio.service.menu.MenuService;
import no.stelvio.service.menu.to.MenuItemServiceResponse;

import org.apache.myfaces.custom.navmenu.NavigationMenuItem;
import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.test.mock.MockExternalContext;
import org.apache.shale.test.mock.MockFacesContext;
import org.apache.shale.test.mock.MockFacesContextFactory;
import org.apache.shale.test.mock.MockHttpServletResponse;
import org.apache.shale.test.mock.MockHttpSession;
import org.apache.shale.test.mock.MockLifecycle;
import org.apache.shale.test.mock.MockLifecycleFactory;
import org.apache.shale.test.mock.MockPrincipal;
import org.apache.shale.test.mock.MockServletConfig;
import org.apache.shale.test.mock.MockServletContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * @author utvikler
 *
 */
public class MenuActionTest {

	MenuAction menuAction;
	
	private Principal principal;
	private List<String> roles;
	
    // Mock object instances for our tests
	private MockServletConfig config;
    private MockExternalContext externalContext;
    private MockFacesContext facesContext;
    private MockFacesContextFactory	facesContextFactory;
    private MockLifecycle lifecycle;
    private MockLifecycleFactory lifecycleFactory;
    private MockHttpServletRequestExtended request;
    private MockHttpServletResponse response;
    private MockServletContext servletContext;
    private MockHttpSession session;

	
	@Before
	public void setUp() {
		menuAction = new MenuAction();
		menuAction.setMenuService(new MyMenuService());
	
        this.principal = new MockPrincipal("TestUser");
		this.roles = new ArrayList<String>();
		this.roles.add("admin");
		this.roles.add("web");
		 // Set up Servlet API Objects
        servletContext = new MockServletContext();
        
        
       config = new MockServletConfig(servletContext);
        session = new MockHttpSession();
       session.setServletContext(servletContext);
       request = new MockHttpServletRequestExtended(session, principal, roles);
       request.setServletContext(servletContext);
       response = new MockHttpServletResponse();

        // Set up JSF API Objects
        FactoryFinder.releaseFactories();
        FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
        "org.apache.shale.test.mock.MockApplicationFactory");
        
        FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
        "no.stelvio.presentation.security.page.MockFacesContextFactoryExtended");
        
        FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
        "org.apache.shale.test.mock.MockLifecycleFactory");
        
        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
        "org.apache.shale.test.mock.MockRenderKitFactory");

        externalContext =
            new MockExternalContext(servletContext, request, response);

        
        lifecycleFactory = (MockLifecycleFactory)
        FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        lifecycle = (MockLifecycle)
        lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        facesContextFactory = (MockFacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        facesContext = (MockFacesContext)
        facesContextFactory.getFacesContext(servletContext,
                request,
                response,
                lifecycle);
        externalContext = (MockExternalContextExtended) facesContext.getExternalContext();
	}
	
    
	
	@Before
    public void setUpRequestContext() {
	    RequestContext requestContext = new SimpleRequestContext("brukeroversikt", "module", "process", "transaction");
	    RequestContextHolder.setRequestContext(requestContext);
    }

	
	/**
	 * The menuitems returned from menuAction should have this structure:
	 * |- MI1
	 * |   |- MI1_1
	 * |   |- MI1_2
	 * |- MI2
	 *
	 */
	@Test
	public void getMenuItems() {
		List<NavigationMenuItem> items = menuAction.getNavigationItems();
		assertTrue(items.size() ==  2);
		NavigationMenuItem mI1 = items.get(0);
		assertTrue(mI1.getNavigationMenuItems().length == 2);
		NavigationMenuItem mI2 = items.get(1);
		assertTrue(mI2.getNavigationMenuItems().length == 0);
	}
	
	@Test
	public void getMenuItemsNotInScreen() {
	    // set the current screen 
		RequestContext requestContext = new SimpleRequestContext("no_page", "module", "process", "transaction");
	    RequestContextHolder.setRequestContext(requestContext);
		List<NavigationMenuItem> items = menuAction.getNavigationItems();
		assertTrue(items.size() == 0);
	}
	
	@Test
	public void getMenuItemsNoPermissions() {
		// remove all roles from the external context
		((MockHttpServletRequestExtended)FacesContext.getCurrentInstance().getExternalContext().getRequest()).setPrincipalRoles(new ArrayList());
		
		List<NavigationMenuItem> items = menuAction.getNavigationItems();
		assertTrue(items.size() == 0);
	}
	
	private class MyMenuService implements MenuService {

		/**
		 * Gets a set of mocked menu items
		 * @return MenuItemServiceResponse
		 */
		public MenuItemServiceResponse getMenuItems() {
			MenuItemServiceResponse serviceResponse = getMockMenuWithChildren2();
			return serviceResponse;
		}

		
		private MenuItemServiceResponse getMockMenuWithChildren() {
			// PERMISSIONS
			MenuItemPermission admin1 = new MenuItemPermission("admin", false);
			
			MenuItemPermission web = new MenuItemPermission("web", false);
			
			
			// SCREENS
			MenuItemScreen s1 = new MenuItemScreen(1, "brukeroversikt");
			
			MenuItemScreen s2 = new MenuItemScreen(2, "minprofil");
			
			
			// MENUS
			// Menu item 1
			MenuItem mI1 = new MenuItem(1, null, 0, "Brukeroversikt");
			mI1.setFlowName("brukeroversikt-flow");
			
			// Set permissions for M1
			List<MenuItemPermission> pMI1 = new ArrayList<MenuItemPermission>();
			pMI1.add(admin1);
			pMI1.add(web);
			mI1.setPermissions(pMI1);
			
			// Set sreens for M1
			List<MenuItemScreen> sMI1 = new ArrayList<MenuItemScreen>();
			sMI1.add(s1);
			sMI1.add(s2);
			mI1.setScreens(sMI1);
			
			// Children of M1
			// Menu item MI1_1
			MenuItem mI1_1 = new MenuItem(2, null, 0, "Child 1-1");
			mI1_1.setFlowName("sok");
			mI1_1.setLeadtext("Child 1-1");
			mI1_1.setMenuId(2);
			
			// persmissions Menu item M1_1
			List<MenuItemPermission> pMI1_1 = new ArrayList<MenuItemPermission>();
			pMI1_1.add(admin1);
			pMI1_1.add(web);
			mI1_1.setPermissions(pMI1_1);
			
			// Screens Menu item M1_1
			List<MenuItemScreen> sMI1_1 = new ArrayList<MenuItemScreen>();
			sMI1_1.add(s1);
			sMI1_1.add(s2);
			mI1_1.setScreens(sMI1_1);
			
			// Menu item MI1_2
			MenuItem mI1_2 = new MenuItem(3, null, 0 , "Child 1-2");
			mI1_2.setFlowName("childflow1-2");
			mI1_2.setLeadtext("Child 1-2");
			mI1_2.setMenuId(3);
			
			// Set permissions for MI1_2
			List<MenuItemPermission> pMI1__2 = new ArrayList<MenuItemPermission>();
			pMI1__2.add(admin1);
			pMI1__2.add(web);
			mI1_2.setPermissions(pMI1__2);
			
			// Set sreens for MI1_2
			List<MenuItemScreen> sMI1_2 = new ArrayList<MenuItemScreen>();
			sMI1_2.add(s1);
			sMI1_2.add(s2);
			mI1_2.setScreens(sMI1_2);
			
			
			// Add children to r1
			List<MenuItem> children_MI1 = new ArrayList<MenuItem>();
			children_MI1.add(mI1_1);
			children_MI1.add(mI1_2);
			mI1.setChildren(children_MI1);
			
			// Menu item MI2
			MenuItem mI2 = new MenuItem(6, null, 0, "Søke person");
			mI2.setFlowName("sok");
			mI2.setLeadtext("Søke person");
			mI2.setMenuId(6);
			
			// Set permissions for MI2
			List<MenuItemPermission> pMI2 = new ArrayList<MenuItemPermission>();
			pMI2.add(admin1);
			pMI2.add(web);
			mI2.setPermissions(pMI2);
			
			// screen for MI2
			List<MenuItemScreen> sMI2 = new ArrayList<MenuItemScreen>();
			sMI2.add(s1);
			sMI2.add(s2);
			mI2.setScreens(sMI2);
			
			// Add to list
			List<MenuItem> menus = new ArrayList<MenuItem>();
			menus.add(mI1);
			menus.add(mI2);
			
			MenuItemServiceResponse response = new MenuItemServiceResponse();
			response.setMenuItemList(menus);
			
			return response;
		}
		
		private MenuItemServiceResponse getMockMenuWithChildren2() {
			// PERMISSIONS
			MenuItemPermission admin1 = new MenuItemPermission("admin", false);
			
			MenuItemPermission web = new MenuItemPermission("web", false);
			
			// SCREENS
			MenuItemScreen s1 = new MenuItemScreen(1, "menu-side1");		
			MenuItemScreen s2 = new MenuItemScreen(2, "menu-side1-1");		
			MenuItemScreen s3 = new MenuItemScreen(3, "menu-side1-2");		
			MenuItemScreen s4 = new MenuItemScreen(4, "menu-side2");		
			
			
			// MENUS
			// Menu item 1
			MenuItem mI1 = new MenuItem(1, null, 0, "Side1");
			mI1.setFlowName("til1");
			
			// Set permissions for M1
			List<MenuItemPermission> pMI1 = new ArrayList<MenuItemPermission>();
			pMI1.add(admin1);
			pMI1.add(web);
			mI1.setPermissions(pMI1);
			
			// Set sreens for M1
			List<MenuItemScreen> sMI1 = new ArrayList<MenuItemScreen>();
			sMI1.add(s1);
			sMI1.add(s2);
			sMI1.add(s3);
			sMI1.add(s4);
			mI1.setScreens(sMI1);
			
			// Children of M1
			// Menu item MI1_1
			MenuItem mI1_1 = new MenuItem(2, null, 0, "Side 1-1");
			mI1_1.setFlowName("til1-1");
			mI1_1.setMenuId(2);
			
			// persmissions Menu item M1_1
			List<MenuItemPermission> pMI1_1 = new ArrayList<MenuItemPermission>();
			pMI1_1.add(admin1);
			pMI1_1.add(web);
			mI1_1.setPermissions(pMI1_1);
			
			// Screens Menu item M1_1
			List<MenuItemScreen> sMI1_1 = new ArrayList<MenuItemScreen>();
			sMI1_1.add(s1);
			sMI1_1.add(s2);
			sMI1_1.add(s3);
			sMI1_1.add(s4);
			mI1_1.setScreens(sMI1_1);
			
			// Menu item MI1_2
			MenuItem mI1_2 = new MenuItem(3, null, 0 , "Side 1-2");
			mI1_2.setFlowName("til1-2");
			mI1_2.setMenuId(3);
			
			// Set permissions for MI1_2
			List<MenuItemPermission> pMI1__2 = new ArrayList<MenuItemPermission>();
			pMI1__2.add(admin1);
			pMI1__2.add(web);
			mI1_2.setPermissions(pMI1__2);
			
			// Set sreens for MI1_2
			List<MenuItemScreen> sMI1_2 = new ArrayList<MenuItemScreen>();
			sMI1_2.add(s1);
			sMI1_2.add(s2);
			sMI1_2.add(s3);
			sMI1_2.add(s4);
			mI1_2.setScreens(sMI1_2);
			
			
			// Add children to r1
			List<MenuItem> children_MI1 = new ArrayList<MenuItem>();
			children_MI1.add(mI1_1);
			children_MI1.add(mI1_2);
			mI1.setChildren(children_MI1);
			
			// Menu item MI2
			MenuItem mI2 = new MenuItem(6, null, 0, "Side 2");
			mI2.setFlowName("til2");
			mI2.setMenuId(6);
			
			// Set permissions for MI2
			List<MenuItemPermission> pMI2 = new ArrayList<MenuItemPermission>();
			pMI2.add(admin1);
			pMI2.add(web);
			mI2.setPermissions(pMI2);
			
			// screen for MI2
			List<MenuItemScreen> sMI2 = new ArrayList<MenuItemScreen>();
			sMI2.add(s1);
			sMI2.add(s2);
			sMI2.add(s3);
			sMI2.add(s4);
			mI2.setScreens(sMI2);
			
			// Add to list
			List<MenuItem> menus = new ArrayList<MenuItem>();
			menus.add(mI1);
			menus.add(mI2);
			
			MenuItemServiceResponse response = new MenuItemServiceResponse();
			response.setMenuItemList(menus);
			
			return response;
		}


		
	}
}
