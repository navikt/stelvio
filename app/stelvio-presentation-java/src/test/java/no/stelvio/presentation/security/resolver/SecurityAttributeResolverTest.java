package no.stelvio.presentation.security.resolver;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;

import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.SecurityContextHolder;
import no.stelvio.common.security.support.SimpleSecurityContext;
import no.stelvio.test.context.StelvioContextSetter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.jsf.FacesContextUtils;

import com.groundside.jsf.securityresolver.adapter.AttributeResolver;

public class SecurityAttributeResolverTest {
	
	private FacesContext faces;/* = new FacesContext(){

		@Override
		public void addMessage(String clientId, FacesMessage message) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public Application getApplication() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public Iterator getClientIdsWithMessages() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public ExternalContext getExternalContext() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public Severity getMaximumSeverity() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public Iterator getMessages() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public Iterator getMessages(String clientId) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public RenderKit getRenderKit() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public boolean getRenderResponse() {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public boolean getResponseComplete() {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public ResponseStream getResponseStream() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public ResponseWriter getResponseWriter() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public UIViewRoot getViewRoot() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void release() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void renderResponse() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void responseComplete() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void setResponseStream(ResponseStream responseStream) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void setResponseWriter(ResponseWriter responseWriter) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void setViewRoot(UIViewRoot root) {
			// TODO Auto-generated method stub
			
		}
		
	};*/
	
	private SecurityAttributeResolver resolver = new SecurityAttributeResolver();
	
	@Before
	public void setUp() throws Exception {
		
	}
	
	/**
	 * Creates a SecurityContext corresponding to the supplied userId and adds it to the current thread. 
	 * @param userId the userId
	 * @return a SecurityContext
	 */
	private void loginUser(String userId){
		SecurityContext ctx;
		
		if(userId.equalsIgnoreCase("Donald")){
			List<String> roles1 = new ArrayList<String>();
			roles1.add("ROLE1");
			roles1.add("ROLE2");
			ctx = new SimpleSecurityContext(userId,roles1);
		} else if (userId.equalsIgnoreCase("Dolly")){
			List<String> roles2 = new ArrayList<String>();
			roles2.add("ROLE1");
			roles2.add("ROLE3");
			ctx = new SimpleSecurityContext(userId,roles2);
		} else {
			List<String> roles3 = new ArrayList<String>();
			ctx = new SimpleSecurityContext(userId,roles3);
		}
		StelvioContextSetter.setSecurityContext(ctx);
	}
	private void logoutUser(){
		StelvioContextSetter.setSecurityContext(new SimpleSecurityContext(null,new ArrayList<String>()));
	}

	@Test
	public void testIsSupported() {
		assertTrue("Should be true.",resolver.isSupported(AttributeResolver.SECURED));
		assertTrue("Should be true.",resolver.isSupported(AttributeResolver.PRINCIPAL_NAME));
		assertFalse("Should be false.",resolver.isSupported(AttributeResolver.AUTH_TYPE));
		assertTrue("Should be true.",resolver.isSupported(AttributeResolver.USER_IN_ALL_ROLES));
		assertTrue("Should be true.",resolver.isSupported(AttributeResolver.USER_IN_ROLE));
		
	}

	@Test
	public void testIsSecurityEnabled() {
		logoutUser();
		assertFalse("Should be false.",resolver.isSecurityEnabled(faces));
		loginUser("Donald");
		assertTrue("Should be true.",resolver.isSecurityEnabled(faces));
	}

	@Test
	public void testGetPrincipalName() {
		logoutUser();
		assertNull("User should be null as the user is not logged in yet.", resolver.getPrincipalName(faces));
		loginUser("Donald");
		String principal = resolver.getPrincipalName(faces);
		assertNotNull("User should not be null.",principal);
		assertEquals("Userid should be 'Donald'.",principal,"Donald");
	}

	@Test
	public void testGetAuthenticationType() {
		assertEquals("Should return the string: 'Not supported.'", "Not supported.", resolver.getAuthenticationType(faces));
	}

	@Test
	public void testIsUserInAllRoles() {
		logoutUser();
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE1");
		roles.add("ROLE2");
		assertFalse("Should return false, user is not logged in.",resolver.isUserInAllRoles(faces,roles));
		loginUser("Donald");
		assertTrue("Should return true, user 'Donald' has all the roles.",resolver.isUserInAllRoles(faces,roles));
		loginUser("Dolly");
		assertFalse("Should return false, user 'Dolly' does not have all the roles.",resolver.isUserInAllRoles(faces,roles));	
	}

	@Test
	public void testIsUserInRole() {
		logoutUser();
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE1");
		roles.add("ROLE2");
		assertFalse("Should return false, user is not logged in.",resolver.isUserInRole(faces,roles));
		loginUser("Donald");
		assertTrue("Should return true, user 'Donald' has all the roles.",resolver.isUserInRole(faces,roles));
		loginUser("Dolly");
		assertTrue("Should return true, user 'Dolly' has one of the roles.",resolver.isUserInRole(faces,roles));
		loginUser("Test");
		assertFalse("Should return false, user 'Test' does not have any of the roles.",resolver.isUserInRole(faces,roles));
	}

}
