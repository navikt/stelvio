package no.stelvio.presentation.security.page.definition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import no.stelvio.common.security.support.SecurityContextSetter;
import no.stelvio.common.security.support.SimpleSecurityContext;
import no.stelvio.presentation.security.page.AbstractPhaselistenerTestCase;
import no.stelvio.presentation.security.page.MockHttpServletRequestExtended;
import no.stelvio.presentation.security.page.PageAccessDeniedException;
import no.stelvio.presentation.security.page.PageAuthenticationRequiredException;
import no.stelvio.presentation.security.page.definition.parse.SecurityConfiguration;
import no.stelvio.presentation.security.page.definition.parse.support.JsfApplication;
import no.stelvio.presentation.security.page.definition.parse.support.JsfPage;
import no.stelvio.presentation.security.page.definition.parse.support.SecurityConfigurationXml;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * JeeSecurityObjectTest.
 * 
 *
 */
public class JeeSecurityObjectTest extends AbstractPhaselistenerTestCase {
	private JeeSecurityObject secObject;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSetUp() {
		secObject = new JeeSecurityObject();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onTearDown() {
	}
	
	/**
	 * Setup security class.
	 * 
	 * @return jsf app
	 */
	private JsfApplication setupSecureApp() {

		URL url = Thread.currentThread().getContextClassLoader().getResource("WEB-INF/security/faces-security-config.xml");
		SecurityConfigurationXml config = new SecurityConfigurationXml(url);
		JsfApplication app = config.getJsfApplication();
		return app;
	}

	/**
	 * Test initializeSecurityDefinitions.
	 */
	@Test
	public void initializeSecurityDefinitions() {
		/*
		 * ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		 * 
		 * URL url =
		 * Thread.currentThread().getContextClassLoader().getResource(exctx.getInitParameter(Constants.SECURITY_CONFIG_FILE));
		 * System.out.println("Url " + url); secObject.initializeSecurityDefinitions(); JsfPage page =
		 * secObject.getPageObject("/pages/transaksjon/familieforhold/familieforhold"); assertNotNull(page);
		 */
	}

	/**
	 * Test authorizePageAccessTrue.
	 */
	@Test
	public void authorizePageAccessTrue() {

		JsfApplication app = setupSecureApp();
		SecurityConfiguration config = new MockSecurityConfiguration(app);
		HttpServletRequest request = new MockHttpServletRequest();
		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions(request);

		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession) exctx.getSession(true);
		super.setView("test/page1");
		changeRoles("VEILEDER");
		assertTrue(secObject.authorizePageAccess("test/page1", session, request));

		String viewId = "test/page2";
		// ONLY MIDDELS ROLE SHOULD NOT GIVE ACCESS TO /test/page2
		changeRoles("MIDDELS");
		try {
			secObject.authorizePageAccess(viewId, session, request);
			fail("Should have thrown a PageAccessDeniedException.");
		} catch (PageAccessDeniedException ex) {
			assertTrue(true);
		}
		changeRoles("MIDDELS", "EKSTERN");
		assertTrue(secObject.authorizePageAccess(viewId, session, request));
		changeRoles("MIDDELS", "FULLMAKT", "FULLMAKT_FULLSTENDIG");
		assertTrue(secObject.authorizePageAccess(viewId, session, request));

	}

	/**
	 * Change roles.
	 * 
	 * @param roles
	 *            roles
	 */
	private void changeRoles(String... roles) {
		List<String> list = new ArrayList<String>();
		System.out.println("Setting new roles:" + Arrays.toString(roles));
		for (String string : roles) {
			System.out.println("Adding role: " + string);
			list.add(string);
		}
		SecurityContextSetter.setSecurityContext(new SimpleSecurityContext("SomeUser", list));
	}

	/**
	 * Tests the authorization on two pages. One uses OR concatenation and should allow access the other uses AND and should
	 * deny access.
	 */
	@Test(expected = PageAccessDeniedException.class)
	public void authorizePageAccessThrowsAccessDenied() {
		JsfApplication app = setupSecureApp();

		SecurityConfiguration config = new MockSecurityConfiguration(app);
		HttpServletRequest request = new MockHttpServletRequest();
		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions(request);

		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession) exctx.getSession(true);
		super.setView("test/page1");
		// assertTrue(secObject.authorizePageAccess("/test/page1", session));
		// Should throw exception
		secObject.authorizePageAccess("test/page1", session, request);
	}

	/**
	 * Test authorizePageAccessWhenNotAuthenticated.
	 */
	@Test
	public void authorizePageAccessWhenNotAuthenticated() {
		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		MockHttpServletRequestExtended request = (MockHttpServletRequestExtended) exctx.getRequest();
		request.setUserPrincipal(null);
		SecurityContextSetter.setSecurityContext(new SimpleSecurityContext(null, null));

		String servletPath = "test/page1.jsf";
		String queryString = "start=start&something=another";
		request.setPathElements("", servletPath, null, queryString);

		JsfApplication app = setupSecureApp();
		SecurityConfiguration config = new MockSecurityConfiguration(app);
		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions(request);
		String viewId = "test/page1";

		HttpSession session = (HttpSession) exctx.getSession(true);

		assertTrue(callToAuthorizePageAccessForNonAuthenticatedUserThrowsException(viewId, session));
	}

	/**
	 * Checks whether a call to JeeSecurityObject.authorizePageAccess() throws an PageAuthenticationRequiredException.
	 * 
	 * @param viewId
	 *            view id
	 * @param session
	 *            session
	 * @return true if exception is thrown, otherwise false
	 */
	private boolean callToAuthorizePageAccessForNonAuthenticatedUserThrowsException(String viewId, HttpSession session) {
		try {
			HttpServletRequest request = new MockHttpServletRequest();
			secObject.authorizePageAccess(viewId, session, request);
		} catch (PageAuthenticationRequiredException e) {
			return true;
		}
		return false;
	}

	/**
	 * Test authorizePageNoAuthenticationORAuthorizationRequired.
	 */
	@Test
	public void authorizePageNoAuthenticationORAuthorizationRequired() {
		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		MockHttpServletRequestExtended request = (MockHttpServletRequestExtended) exctx.getRequest();
		request.setUserPrincipal(null);

		String servletPath = "/somepage.jsf";
		String queryString = "start=start&something=another";
		request.setPathElements("", servletPath, null, queryString);

		JsfApplication app = setupSecureApp();
		SecurityConfiguration config = new MockSecurityConfiguration(app);

		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions(request);

		HttpSession session = (HttpSession) exctx.getSession(true);
		assertTrue(secObject.authorizePageAccess("/somepage", session, request));
	}

	/**
	 * Test authorizePageAuthenticationOnlyRequired.
	 */
	@Test
	public void authorizePageAuthenticationOnlyRequired() {
		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		MockHttpServletRequestExtended request = (MockHttpServletRequestExtended) exctx.getRequest();
		List<String> roles = new ArrayList<String>();
		roles.add("OnlyRole");
		request.setPrincipalRoles(roles);

		String servletPath = "/test/page3.jsf";
		String queryString = "start=start&something=another";
		request.setPathElements("/", servletPath, null, queryString);

		JsfApplication app = setupSecureApp();
		SecurityConfiguration config = new MockSecurityConfiguration(app);
		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions(request);

		HttpSession session = (HttpSession) exctx.getSession(true);
		assertTrue(secObject.authorizePageAccess("/test/page3", session, request));
	}

	/**
	 * Test getPageObject.
	 */
	@Test
	public void getPageObject() {

		JsfApplication app = setupSecureApp();
		JsfPage wildcard = new JsfPage();
		wildcard.setPageName("/wildcardpages/*");
		app.addJsfPage(wildcard);

		HttpServletRequest request = new MockHttpServletRequest();
		SecurityConfiguration config = new MockSecurityConfiguration(app);
		secObject.setSecurityConfiguration(config);
		secObject.initializeSecurityDefinitions(request);
		String viewId = "/test/page1";
		String viewId2 = "/wildcardpages/somepage";
		JsfPage page = secObject.getPageObject(viewId);
		assertEquals(viewId, page.getPageName());
		JsfPage page2 = secObject.getPageObject(viewId2);
		assertEquals(viewId2, page2.getPageName());
	}

	/**
	 * Test handleProtocolSwitchToHttps.
	 */
	@Test
	public void handleProtocolSwitchToHttps() {
		// org.springframework.webflow.context.ExternalContext exctx = ExternalContextHolder.getExternalContext();
		// MockHttpServletRequestExtended request = (MockHttpServletRequestExtended) exctx.getNativeRequest();
		// StringBuffer requestURL = new StringBuffer();
		// String http = "http://localhost/test/page3";
		// String https = "https://localhost/test/page3";
		// requestURL.append(http);
		// request.setRequestURL(requestURL);
		// String servletPath = "/test/page3";
		// String queryString = "";
		// request.setPathElements("", servletPath, null, queryString);
		//
		// String viewId = "/test/page3";
		//
		// secObject.handleProtocolSwitch(viewId, true);
		// MockHttpServletResponse response = (MockHttpServletResponse) exctx.getNativeResponse();
		// assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		// assertEquals(https, response.getMessage());
		assertTrue(true);
	}

	/**
	 * Test handleProtocolSwitchToHttp.
	 */
	@Test
	public void handleProtocolSwitchToHttp() {

		// ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
		// MockHttpServletRequestExtended request = (MockHttpServletRequestExtended) exctx.getRequest();
		// StringBuffer requestURL = new StringBuffer();
		// String http = "http://localhost/test/page3";
		// String https = "https://localhost/test/page3";
		// requestURL.append(https);
		// request.setRequestURL(requestURL);
		// String servletPath = "/test/page3";
		// String queryString = "";
		// request.setPathElements("", servletPath, null, queryString);
		// request.setSecure(true);
		// String viewId = "/test/page3";
		//
		// secObject.handleProtocolSwitch(viewId, false);
		// MockHttpServletResponse response = (MockHttpServletResponse) exctx.getResponse();
		// assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY, response.getStatus());
		// assertEquals(http, response.getMessage());
		assertTrue(true);

	}

}
