package no.stelvio.presentation.jsf.security;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletResponse;

import org.apache.shale.test.mock.MockHttpServletResponse;

import no.stelvio.presentation.security.page.AbstractPhaselistenerTestCase;
import no.stelvio.presentation.security.page.definition.JeeSecurityObject;
import no.stelvio.presentation.security.page.definition.parse.SecurityConfiguration;
import no.stelvio.presentation.security.page.definition.parse.support.JeeRole;
import no.stelvio.presentation.security.page.definition.parse.support.JeeRoles;
import no.stelvio.presentation.security.page.definition.parse.support.JsfApplication;
import no.stelvio.presentation.security.page.definition.parse.support.JsfPage;

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
	
	private JeeSecurityObject setupSecureApp(){
		
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
		
		SecurityConfiguration config = new MockSecurityConfiguration(secureApp);
		JeeSecurityObject secObject = new JeeSecurityObject();
		secObject.setSecurityConfiguration(config);
		return secObject;
	}
	
	public void testBeforeAndAfterPhaseUserIsAuthorized() throws Exception{
		JeeSecurityObject secObject = setupSecureApp();
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
		
		JeeSecurityObject secObject = setupSecureApp();
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
