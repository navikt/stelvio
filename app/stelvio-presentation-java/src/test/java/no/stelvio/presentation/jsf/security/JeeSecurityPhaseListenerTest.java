package no.stelvio.presentation.jsf.security;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletResponse;

import org.apache.shale.test.mock.MockHttpServletResponse;

import no.stelvio.presentation.security.page.AbstractPhaselistenerTestCase;
import no.stelvio.presentation.security.page.parse.J2EERole;
import no.stelvio.presentation.security.page.parse.J2EERoles;
import no.stelvio.presentation.security.page.parse.JSFApplication;
import no.stelvio.presentation.security.page.parse.JSFPage;
import no.stelvio.presentation.security.page.parse.SecurityConfiguration;
import no.stelvio.presentation.security.page.util.J2eeSecurityObject;
import no.stelvio.presentation.security.page.util.MockSecurityConfiguration;

public class JeeSecurityPhaseListenerTest extends AbstractPhaselistenerTestCase{
	
	private JeeSecurityPhaseListener listener;
	
	//private MockExternalContextExtended externalContext = null;
	
	public JeeSecurityPhaseListenerTest(String name){
		super(name);
		listener = new JeeSecurityPhaseListener();
	}
	public void setUp()throws Exception{
		super.setUp();
		 
	}
	public void tearDown()throws Exception{
		super.tearDown();
	}
	
	private J2eeSecurityObject setupSecureApp(){
		
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
		
		SecurityConfiguration config = new MockSecurityConfiguration(secureApp);
		J2eeSecurityObject secObject = new J2eeSecurityObject();
		secObject.setSecurityConfiguration(config);
		return secObject;
	}
	
	public void testBeforeAndAfterPhaseUserIsAuthorized() throws Exception{
		J2eeSecurityObject secObject = setupSecureApp();
		listener.setJ2eeSecurityObject(secObject);
		PhaseEvent phase = new PhaseEvent(FacesContext.getCurrentInstance(),PhaseId.INVOKE_APPLICATION,super.lifecycle);
		PhaseEvent phase2 = new PhaseEvent(FacesContext.getCurrentInstance(),PhaseId.RESTORE_VIEW,super.lifecycle);
		PhaseEvent phase3 = new PhaseEvent(FacesContext.getCurrentInstance(),PhaseId.RENDER_RESPONSE,super.lifecycle);
		super.setView("/test/page1.xhtml");
		
		listener.afterPhase(phase);
		MockHttpServletResponse response = 
			(MockHttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
		assertEquals("Request should succeed with status code 200.",HttpServletResponse.SC_OK,response.getStatus());
		
		listener.afterPhase(phase2);
		MockHttpServletResponse response2 = 
			(MockHttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
		assertEquals("Request should succeed with status code 200.",HttpServletResponse.SC_OK,response2.getStatus());
		
		listener.beforePhase(phase3);
		MockHttpServletResponse response3 = 
			(MockHttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
		assertEquals("Request should succeed with status code 200.",HttpServletResponse.SC_OK,response3.getStatus());	
	}
	public void testBeforeAndAfterPhaseUserIsNotAuthorized() throws Exception{
		
		J2eeSecurityObject secObject = setupSecureApp();
		listener.setJ2eeSecurityObject(secObject);
		PhaseEvent phase = new PhaseEvent(FacesContext.getCurrentInstance(),PhaseId.INVOKE_APPLICATION,super.lifecycle);
		PhaseEvent phase2 = new PhaseEvent(FacesContext.getCurrentInstance(),PhaseId.RESTORE_VIEW,super.lifecycle);
		PhaseEvent phase3 = new PhaseEvent(FacesContext.getCurrentInstance(),PhaseId.RENDER_RESPONSE,super.lifecycle);
		
		super.setView("/test/page2.xhtml");
		
		listener.afterPhase(phase);
		MockHttpServletResponse response = 
			(MockHttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
		assertEquals("Request should not succeed. Status code should be 403.",HttpServletResponse.SC_FORBIDDEN,response.getStatus());
		
		listener.afterPhase(phase2);
		MockHttpServletResponse response2 = 
			(MockHttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
		assertEquals("Request should not succeed. Status code should be 403.",HttpServletResponse.SC_FORBIDDEN,response2.getStatus());
		
		listener.beforePhase(phase3);
		MockHttpServletResponse response3 = 
			(MockHttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
		assertEquals("Request should not succeed. Status code should be 403.",HttpServletResponse.SC_FORBIDDEN,response3.getStatus());	
	}
}
