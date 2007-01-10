package no.stelvio.presentation.security.page.util;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.stelvio.presentation.security.page.AbstractPhaselistenerTestCase;
import no.stelvio.presentation.security.page.MockHttpServletRequestExtended;
import no.stelvio.presentation.security.page.PageAccessDeniedException;
import no.stelvio.presentation.security.page.constants.Constants;
import no.stelvio.presentation.security.page.parse.J2EERole;
import no.stelvio.presentation.security.page.parse.J2EERoles;
import no.stelvio.presentation.security.page.parse.JSFApplication;
import no.stelvio.presentation.security.page.parse.JSFPage;
import no.stelvio.presentation.security.page.parse.SecurityConfiguration;

import org.apache.shale.test.mock.MockHttpServletResponse;

public class J2eeSecurityObjectTest extends AbstractPhaselistenerTestCase{
	
	private J2eeSecurityObject secObject;
	
	//private MockExternalContextExtended externalContext = null;
	
	public J2eeSecurityObjectTest(String name){
		super(name);
		secObject = new J2eeSecurityObject();
	}
	public void setUp()throws Exception{
		super.setUp();
		 
	}
	public void tearDown()throws Exception{
		super.tearDown();
	}
	
	private JSFApplication setupSecureApp(){
		
		J2EERole role1 = new J2EERole();
		J2EERole role2 = new J2EERole();
		J2EERole role3 = new J2EERole();
		J2EERole role4 = new J2EERole();
		role1.setRole("role1");
		role2.setRole("role2");
		role3.setRole("role3");
		role4.setRole("role4");
		
		J2EERoles roles1 = new J2EERoles();
		J2EERoles roles2 = new J2EERoles();
		roles1.addRole(role1);
		roles1.addRole(role2);
		roles2.addRole(role1);
		roles2.addRole(role4);
		roles1.setRoleConcatenationType("OR");
		roles2.setRoleConcatenationType("AND");
		
		JSFPage page1 = new JSFPage();
		JSFPage page2 = new JSFPage();
		JSFPage page3 = new JSFPage();
		page1.setPageName("/test/page1.xhtml");
		page2.setPageName("/test/page2.xhtml");
		page3.setPageName("/test/page3.xhtml");
		page1.setRequiresAuthorization(true);
		page2.setRequiresAuthorization(true);
		page3.setRequiresAuthentication(true);
		page3.setRequiresAuthorization(false);
		page1.addRoles(roles1);
		page2.addRoles(roles2);
		
		//SSLConfig sslConfig = new SSLConfig();
		
		JSFApplication secureApp = new JSFApplication();
		secureApp.addJsfPage(page1);
		secureApp.addJsfPage(page2);
		secureApp.addJsfPage(page3);
		return secureApp;
	}
	
	
	/**
	 * Tests the authorization on two pages. One uses OR concatenation and should allow access
	 * the other uses AND and should deny access
	 * @throws Exception
	 */
	public void testAuthorizePageAccess()throws Exception{
		JSFApplication app = setupSecureApp();		
		
		SecurityConfiguration config = new MockSecurityConfiguration(app);
		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions();
		
		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)exctx.getSession(true);
		super.setView("/test/page1.xhtml");
		
		assertTrue(secObject.authorizePageAccess("/test/page1.xhtml", session));
		
		try {
			secObject.authorizePageAccess("/test/page2.xhtml", session);
			fail("Should have thrown PageAccessDeniedException.");
		} catch (PageAccessDeniedException expected) {
			assertTrue(true);
		}	
	}
	public void testAuthorizePageAccessWhenNotAuthenticated()throws Exception{
		
		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		MockHttpServletRequestExtended request = (MockHttpServletRequestExtended)exctx.getRequest();
		request.setUserPrincipal(null);
		
		String servletPath = "/test/page1.jsf";
		String queryString = "start=start&something=another";
		request.setPathElements("",servletPath,null,queryString);
		
		JSFApplication app = setupSecureApp();		
		SecurityConfiguration config = new MockSecurityConfiguration(app);
		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions();
		String viewId = "/test/page1.xhtml";
		
		HttpSession session = (HttpSession)exctx.getSession(true);
		secObject.authorizePageAccess(viewId, session);
		MockHttpServletResponse response = (MockHttpServletResponse)exctx.getResponse();
		
		assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY,response.getStatus());
		assertEquals("/jsfauthentication",response.getMessage());
		//check session for the viewId requested
		HttpSession s = (HttpSession)exctx.getSession(false);
		String jsfPageToGo = (String)s.getAttribute(Constants.JSFPAGE_TOGO_AFTER_AUTHENTICATION);
		assertEquals(viewId + "?" + queryString,jsfPageToGo);
		
	}
	public void testAuthorizePageNoAuthenticationORAuthorizationRequired() throws Exception{
		
		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		MockHttpServletRequestExtended request = (MockHttpServletRequestExtended)exctx.getRequest();
		request.setUserPrincipal(null);
		
		String servletPath = "/somepage.jsf";
		String queryString = "start=start&something=another";
		request.setPathElements("",servletPath,null,queryString);
		
		JSFApplication app = setupSecureApp();		
		SecurityConfiguration config = new MockSecurityConfiguration(app);
		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions();
		
		HttpSession session = (HttpSession)exctx.getSession(true);
		assertTrue(secObject.authorizePageAccess("/somepage.xhtml", session));
	}
	public void testAuthorizePageAuthenticationOnlyRequired() throws Exception{
		
		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		MockHttpServletRequestExtended request = (MockHttpServletRequestExtended)exctx.getRequest();
		List<String> roles = new ArrayList<String>();
		roles.add("OnlyRole");
		request.setPrincipalRoles(roles);
		
		String servletPath = "/test/page3.jsf";
		String queryString = "start=start&something=another";
		request.setPathElements("/",servletPath,null,queryString);
		
		JSFApplication app = setupSecureApp();		
		SecurityConfiguration config = new MockSecurityConfiguration(app);
		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions();
	
		HttpSession session = (HttpSession)exctx.getSession(true);
		assertTrue(secObject.authorizePageAccess("/test/page3.xhtml", session));
	}

	public void testGetPageObject()throws  Exception{
		
		JSFApplication app = setupSecureApp();
		JSFPage wildcard = new JSFPage();
		wildcard.setPageName("/wildcardpages/*");
		app.addJsfPage(wildcard);
		
		SecurityConfiguration config = new MockSecurityConfiguration(app);
		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions();
		String viewId ="/test/page1.xhtml";
		String viewId2 ="/wildcardpages/somepage.xhtml";
		JSFPage page = secObject.getPageObject(viewId);
		assertEquals(viewId,page.getPageName());
		JSFPage page2 = secObject.getPageObject(viewId2);
		assertEquals(viewId2,page2.getPageName());
	}
	
	public void testHandleProtocolSwitchToHttps()throws  Exception{
		
		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		MockHttpServletRequestExtended request = (MockHttpServletRequestExtended)exctx.getRequest();
		StringBuffer requestURL = new StringBuffer();
		String http = "http://localhost/test/page3.xhtml";
		String https = "https://localhost/test/page3.xhtml";
		requestURL.append(http);
		request.setRequestURL(requestURL);
		String servletPath = "/test/page3.xhtml";
		String queryString = "";
		request.setPathElements("",servletPath,null,queryString);

		String viewId = "/test/page3.xhtml";
		
		secObject.handleProtocolSwitch(viewId, true);
		MockHttpServletResponse response = (MockHttpServletResponse)exctx.getResponse();
		assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY,response.getStatus());
		assertEquals(https,response.getMessage());
		
	}
	public void testHandleProtocolSwitchToHttp()throws  Exception{
		
		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		MockHttpServletRequestExtended request = (MockHttpServletRequestExtended)exctx.getRequest();
		StringBuffer requestURL = new StringBuffer();
		String http = "http://localhost/test/page3.xhtml";
		String https = "https://localhost/test/page3.xhtml";
		requestURL.append(https);
		request.setRequestURL(requestURL);
		String servletPath = "/test/page3.xhtml";
		String queryString = "";
		request.setPathElements("",servletPath,null,queryString);
		request.setSecure(true);
		String viewId = "/test/page3.xhtml";
		
		secObject.handleProtocolSwitch(viewId, false);
		MockHttpServletResponse response = (MockHttpServletResponse)exctx.getResponse();
		assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY,response.getStatus());
		assertEquals(http,response.getMessage());
	}
	
}
