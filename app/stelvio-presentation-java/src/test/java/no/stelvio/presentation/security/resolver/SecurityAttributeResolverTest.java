package no.stelvio.presentation.security.resolver;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import no.stelvio.common.security.SecurityContext;

import no.stelvio.common.security.support.SecurityContextSetter;
import no.stelvio.common.security.support.SimpleSecurityContext;
import no.stelvio.common.security.validation.RoleNotValidException;
import no.stelvio.common.security.validation.RoleValidator;
import no.stelvio.common.security.validation.support.RoleValidatorUtil;
import no.stelvio.common.security.validation.ValidRole;

import no.stelvio.presentation.security.page.AbstractPhaselistenerTestCase;

import com.groundside.jsf.securityresolver.adapter.AttributeResolver;

public class SecurityAttributeResolverTest extends AbstractPhaselistenerTestCase {

	private FacesContext faces;
	private SecurityAttributeResolver resolver;

	/**
	 * ValidRolesEnum.
	 */
	private enum ValidRolesEnum implements ValidRole {
		ROLE1, ROLE2;

		public String getRoleName() {
			return name();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSetUp() {
		resolver = new SecurityAttributeResolver();
	}

	/* Needs to be overridden as long as <code>AbstractPhaselistenerTestCase</code> is implemented */
	@Override
	protected void onTearDown() {
	}
	
	@Test
	public void shouldNotSupportUnknownNumbers() {
		final int UNSUPPORTED_NUMBER_1 = 1337, UNSUPPORTED_NUMBER_2 = 42;
		assertFalse("This uknown number should default to false", resolver.isSupported(UNSUPPORTED_NUMBER_1));
		assertFalse("This uknown number should default to false", resolver.isSupported(UNSUPPORTED_NUMBER_2));
	}

	@Test
	public void shouldSupportUserInRole() {
		assertTrue("Should be true.", resolver.isSupported(AttributeResolver.USER_IN_ROLE));
	}

	@Test
	public void shouldSupportUsersInAllRoles() {
		assertTrue("Should be true.", resolver.isSupported(AttributeResolver.USER_IN_ALL_ROLES));
	}
	
	@Test
	public void shouldSupportAuth_type() {
		assertFalse("Should be false.", resolver.isSupported(AttributeResolver.AUTH_TYPE));
	}

	@Test
	public void shouldSupportPrincipal_name() {
		assertTrue("Should be true.", resolver.isSupported(AttributeResolver.PRINCIPAL_NAME));
	}

	@Test
	public void shouldSupportSecured() {
		assertTrue("Should be true.", resolver.isSupported(AttributeResolver.SECURED));
	}

	/**
	 * Test is security enabled.
	 */
	@Test
	public void isSecurityEnabled() {
		logoutUser();
		assertFalse("Should be false.", resolver.isSecurityEnabled(faces));
		loginUser("Donald");
		assertTrue("Should be true.", resolver.isSecurityEnabled(faces));
	}

	/**
	 * Test get principal name.
	 */
	@Test
	public void getPrincipalName() {
		logoutUser();
		assertNull("User should be null as the user is not logged in yet.", resolver.getPrincipalName(faces));
		loginUser("Donald");
		String principal = resolver.getPrincipalName(faces);
		assertNotNull("User should not be null.", principal);
		assertEquals("Userid should be 'Donald'.", principal, "Donald");
	}

	/**
	 * Get authentication type.
	 */
	@Test
	public void getAuthenticationType() {
		assertEquals("Should return the string: 'Not supported.'", "Not supported.", resolver.getAuthenticationType(faces));
	}

	/**
	 * Test is user in all roles.
	 */
	@Test
	public void isUserInAllRoles() {
		logoutUser();
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE1");
		roles.add("ROLE2");
		assertFalse("Should return false, user is not logged in.", resolver.isUserInAllRoles(faces, roles));
		loginUser("Donald");
		assertTrue("Should return true, user 'Donald' has all the roles.", resolver.isUserInAllRoles(faces, roles));
		loginUser("Dolly");
		assertFalse("Should return false, user 'Dolly' does not have all the roles.", resolver.isUserInAllRoles(faces, roles));
	}

	/**
	 * Test is user in role.
	 */
	@Test
	public void isUserInRole() {
		logoutUser();
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE1");
		roles.add("ROLE2");
		assertFalse("Should return false, user is not logged in.", resolver.isUserInRole(faces, roles));
		loginUser("Donald");
		assertTrue("Should return true, user 'Donald' has all the roles.", resolver.isUserInRole(faces, roles));
		loginUser("Dolly");
		assertTrue("Should return true, user 'Dolly' has one of the roles.", resolver.isUserInRole(faces, roles));
		loginUser("Test");
		assertFalse("Should return false, user 'Test' does not have any of the roles.", resolver.isUserInRole(faces, roles));
	}

	/**
	 * Test roleNameInValidIsUserInRole.
	 */
	@Test(expected = RoleNotValidException.class)
	public void roleNameInValidIsUserInRole() {
		loginUser("Donald");

		List<String> roles = new ArrayList<String>();
		roles.add("something");
		roles.add("none");
		// Should throw exception
		resolver.isUserInRole(faces, roles);
	}

	/**
	 * Test roleNameInValidIsUserInAllRoles.
	 */
	@Test(expected = RoleNotValidException.class)
	public void roleNameInValidIsUserInAllRoles() {
		loginUser("Donald");

		List<String> roles = new ArrayList<String>();
		roles.add("something");
		roles.add("none");
		// Should throw exception
		resolver.isUserInAllRoles(faces, roles);
	}

	/**
	 * Creates a SecurityContext corresponding to the supplied userId and adds it to the current thread.
	 * 
	 * @param userId
	 *            the userId
	 */
	private void loginUser(String userId) {
		SecurityContext ctx;
		RoleValidator validator = RoleValidatorUtil.createValidatorFromEnum(ValidRolesEnum.class);
		if (userId.equalsIgnoreCase("Donald")) {
			List<String> roles1 = new ArrayList<String>();
			roles1.add("ROLE1");
			roles1.add("ROLE2");
			ctx = new SimpleSecurityContext(userId, roles1, validator);
		} else if (userId.equalsIgnoreCase("Dolly")) {
			List<String> roles2 = new ArrayList<String>();
			roles2.add("ROLE1");
			roles2.add("ROLE3");
			ctx = new SimpleSecurityContext(userId, roles2, validator);
		} else {
			List<String> roles3 = new ArrayList<String>();
			ctx = new SimpleSecurityContext(userId, roles3, validator);
		}
		SecurityContextSetter.setSecurityContext(ctx);
	}

	/**
	 * Logout user.
	 */
	private void logoutUser() {
		SecurityContextSetter.setSecurityContext(new SimpleSecurityContext(null, new ArrayList<String>()));
	}

}
