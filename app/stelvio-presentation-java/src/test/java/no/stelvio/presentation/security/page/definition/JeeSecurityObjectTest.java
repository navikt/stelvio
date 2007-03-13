package no.stelvio.presentation.security.page.definition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.stelvio.common.security.support.SecurityContextSetter;
import no.stelvio.common.security.support.SimpleSecurityContext;
import no.stelvio.presentation.security.page.AbstractPhaselistenerTestCase;
import no.stelvio.presentation.security.page.MockHttpServletRequestExtended;
import no.stelvio.presentation.security.page.PageAccessDeniedException;
import no.stelvio.presentation.security.page.constants.Constants;
import no.stelvio.presentation.security.page.definition.parse.SecurityConfiguration;
import no.stelvio.presentation.security.page.definition.parse.support.JeeRole;
import no.stelvio.presentation.security.page.definition.parse.support.JeeRoles;
import no.stelvio.presentation.security.page.definition.parse.support.JsfApplication;
import no.stelvio.presentation.security.page.definition.parse.support.JsfPage;

import org.apache.shale.test.mock.MockHttpServletResponse;
import org.junit.Test;

public class JeeSecurityObjectTest extends AbstractPhaselistenerTestCase{
	private JeeSecurityObject secObject;
	
	@Override
	public void onSetUp()throws Exception{
		secObject = new JeeSecurityObject();
	}
	
	private JsfApplication setupSecureApp(){
		JeeRole role1 = new JeeRole();
		JeeRole role2 = new JeeRole();
		JeeRole role3 = new JeeRole();
		JeeRole role4 = new JeeRole();
		role1.setRole("role1");
		role2.setRole("role2");
		role3.setRole("role3");
		role4.setRole("role4");
		
		JeeRoles roles1 = new JeeRoles();
		JeeRoles roles2 = new JeeRoles();
		roles1.addRole(role1);
		roles1.addRole(role2);
		roles2.addRole(role1);
		roles2.addRole(role4);
		roles1.setRoleConcatenationType("OR");
		roles2.setRoleConcatenationType("AND");
		
		JsfPage page1 = new JsfPage();
		JsfPage page2 = new JsfPage();
		JsfPage page3 = new JsfPage();
		page1.setPageName("/test/page1.xhtml");
		page2.setPageName("/test/page2.xhtml");
		page3.setPageName("/test/page3.xhtml");
		page1.setRequiresAuthorization(true);
		page2.setRequiresAuthorization(true);
		page3.setRequiresAuthentication(true);
		page3.setRequiresAuthorization(false);
		page1.addRoles(roles1);
		page2.addRoles(roles2);
		
		//SslConfig sslConfig = new SslConfig();
		
		JsfApplication secureApp = new JsfApplication();
		secureApp.addJsfPage(page1);
		secureApp.addJsfPage(page2);
		secureApp.addJsfPage(page3);
		return secureApp;
	}
	
	/**
	 * Tests the authorization on two pages. One uses OR concatenation and should allow access
	 * the other uses AND and should deny access.
	 */
	@Test(expected = PageAccessDeniedException.class)
	public void authorizePageAccess() {
		JsfApplication app = setupSecureApp();
		
		SecurityConfiguration config = new MockSecurityConfiguration(app);
		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions();
		
		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)exctx.getSession(true);
		super.setView("/test/page1.xhtml");
		
		assertTrue(secObject.authorizePageAccess("/test/page1.xhtml", session));

		// Should throw exception
		secObject.authorizePageAccess("/test/page2.xhtml", session);
	}
	
	@Test
	public void authorizePageAccessWhenNotAuthenticated() {
		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		MockHttpServletRequestExtended request = (MockHttpServletRequestExtended)exctx.getRequest();
		request.setUserPrincipal(null);
		SecurityContextSetter.setSecurityContext(new SimpleSecurityContext(null,null));
		
		String servletPath = "/test/page1.jsf";
		String queryString = "start=start&something=another";
		request.setPathElements("",servletPath,null,queryString);
		
		JsfApplication app = setupSecureApp();
		SecurityConfiguration config = new MockSecurityConfiguration(app);
		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions();
		String viewId = "/test/page1.xhtml";
		
		HttpSession session = (HttpSession)exctx.getSession(true);
		secObject.authorizePageAccess(viewId, session);
		MockHttpServletResponse response = (MockHttpServletResponse)exctx.getResponse();
		//MockHttpServletRequestExtended request = (MockHttpServletRequestExtended)exctx.getRequest(); 
		assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY,response.getStatus());
		assertEquals("/jsfauthentication",response.getMessage());
		//check session for the viewId requested
		HttpSession s = (HttpSession)exctx.getSession(false);
		String jsfPageToGo = (String)s.getAttribute(Constants.JSFPAGE_TOGO_AFTER_AUTHENTICATION);
		assertEquals(viewId + "?" + queryString,jsfPageToGo);
	}
	
	@Test
	public void authorizePageNoAuthenticationORAuthorizationRequired() {
		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		MockHttpServletRequestExtended request = (MockHttpServletRequestExtended)exctx.getRequest();
		request.setUserPrincipal(null);
		
		String servletPath = "/somepage.jsf";
		String queryString = "start=start&something=another";
		request.setPathElements("",servletPath,null,queryString);
		
		JsfApplication app = setupSecureApp();
		SecurityConfiguration config = new MockSecurityConfiguration(app);
		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions();
		
		HttpSession session = (HttpSession)exctx.getSession(true);
		assertTrue(secObject.authorizePageAccess("/somepage.xhtml", session));
	}
	
	@Test
	public void authorizePageAuthenticationOnlyRequired() {
		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		MockHttpServletRequestExtended request = (MockHttpServletRequestExtended)exctx.getRequest();
		List<String> roles = new ArrayList<String>();
		roles.add("OnlyRole");
		request.setPrincipalRoles(roles);
		
		String servletPath = "/test/page3.jsf";
		String queryString = "start=start&something=another";
		request.setPathElements("/",servletPath,null,queryString);
		
		JsfApplication app = setupSecureApp();
		SecurityConfiguration config = new MockSecurityConfiguration(app);
		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions();
	
		HttpSession session = (HttpSession)exctx.getSession(true);
		assertTrue(secObject.authorizePageAccess("/test/page3.xhtml", session));
	}
	
	@Test
	public void getPageObject() {
		
		JsfApplication app = setupSecureApp();
		JsfPage wildcard = new JsfPage();
		wildcard.setPageName("/wildcardpages/*");
		app.addJsfPage(wildcard);
		
		SecurityConfiguration config = new MockSecurityConfiguration(app);
		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions();
		String viewId ="/test/page1.xhtml";
		String viewId2 ="/wildcardpages/somepage.xhtml";
		JsfPage page = secObject.getPageObject(viewId);
		assertEquals(viewId,page.getPageName());
		JsfPage page2 = secObject.getPageObject(viewId2);
		assertEquals(viewId2,page2.getPageName());
	}
	
	@Test
	public void handleProtocolSwitchToHttps() {
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
	
	@Test
	public void handleProtocolSwitchToHttp() {
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
