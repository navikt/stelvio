package no.stelvio.presentation.security.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.support.SecurityContextSetter;
import no.stelvio.common.security.support.SimpleSecurityContext;

import org.junit.Test;

import com.groundside.jsf.securityresolver.adapter.AttributeResolver;

public class SecurityAttributeResolverTest {
	private FacesContext faces;	
	private SecurityAttributeResolver resolver = new SecurityAttributeResolver();
	
	@Test
	public void isSupported() {
		assertTrue("Should be true.",resolver.isSupported(AttributeResolver.SECURED));
		assertTrue("Should be true.",resolver.isSupported(AttributeResolver.PRINCIPAL_NAME));
		assertFalse("Should be false.",resolver.isSupported(AttributeResolver.AUTH_TYPE));
		assertTrue("Should be true.",resolver.isSupported(AttributeResolver.USER_IN_ALL_ROLES));
		assertTrue("Should be true.",resolver.isSupported(AttributeResolver.USER_IN_ROLE));
	}

	@Test
	public void isSecurityEnabled() {
		logoutUser();
		assertFalse("Should be false.",resolver.isSecurityEnabled(faces));
		loginUser("Donald");
		assertTrue("Should be true.",resolver.isSecurityEnabled(faces));
	}

	@Test
	public void getPrincipalName() {
		logoutUser();
		assertNull("User should be null as the user is not logged in yet.", resolver.getPrincipalName(faces));
		loginUser("Donald");
		String principal = resolver.getPrincipalName(faces);
		assertNotNull("User should not be null.",principal);
		assertEquals("Userid should be 'Donald'.",principal,"Donald");
	}

	@Test
	public void getAuthenticationType() {
		assertEquals("Should return the string: 'Not supported.'", "Not supported.", resolver.getAuthenticationType(faces));
	}

	@Test
	public void isUserInAllRoles() {
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
	public void isUserInRole() {
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
	
	/**
	 * Creates a SecurityContext corresponding to the supplied userId and adds it to the current thread.
	 *  
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
		
		SecurityContextSetter.setSecurityContext(ctx);
	}
	
	private void logoutUser(){
		SecurityContextSetter.setSecurityContext(new SimpleSecurityContext(null,new ArrayList<String>()));
	}
}
