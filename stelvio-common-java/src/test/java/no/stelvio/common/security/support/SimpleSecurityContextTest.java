package no.stelvio.common.security.support;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.definition.support.DefaultRole;
import no.stelvio.common.security.validation.RoleNotValidException;
import no.stelvio.common.security.validation.RoleValidator;
import no.stelvio.common.security.validation.ValidRole;
import no.stelvio.common.security.validation.support.RoleValidatorUtil;

import org.junit.Test;

/**
 * Test class for the SimpleSecurityContext class.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class SimpleSecurityContextTest {

	/**
	 * The valid roles for the test.
	 */
	private enum ValidRoles implements ValidRole {
		AROLE, BROLE, OTHERROLE, OTHERROLE2, ROLE, ROLE1, ROLE2, ROLE3;
		
		/**
		 * Get role name.
		 * 
		 * @return role name
		 */
		public String getRoleName() {
			return name();
		}
	};

	/**
	 * Get security context.
	 * 
	 * @param userId user id
	 * @param roles roles
	 * @return context
	 */
	private SecurityContext getSecurityContext(String userId, String... roles) {
		List<String> roleList = new ArrayList<String>();
		for (String string : roles) {
			roleList.add(string);
		}
		RoleValidator validator = RoleValidatorUtil.createValidatorFromEnum(ValidRoles.class);
		return new SimpleSecurityContext("User", roleList, validator);
	}

	/**
	 * Test isUserInRole.
	 */
	@Test
	public void isUserInRoleString() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		assertTrue("User has the role.", ctx.isUserInRole("ROLE1"));
		assertFalse("User does not have the role.", ctx.isUserInRole("ROLE"));
	}

	/**
	 * Test isUserInAllRoles.
	 */
	@Test
	public void isUserInAllRolesListOfString() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE1");
		roles.add("ROLE2");
		assertTrue("User has the role.", ctx.isUserInAllRoles(roles));
		roles.set(0, "OTHERROLE");
		assertFalse("User does not have all the roles.", ctx.isUserInAllRoles(roles));
	}

	/**
	 * Test isUserInAllRoles.
	 */
	@Test
	public void isUserInAllRolesStringArray() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		assertTrue("User has the role.", ctx.isUserInAllRoles("ROLE1", "ROLE2"));
		assertFalse("User does not have all the roles.", ctx.isUserInAllRoles("ROLE1", "ROLE"));
	}

	/**
	 * Test isUserInRoles.
	 */
	@Test
	public void isUserInRolesListOfString() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE1");
		roles.add("ROLE2");
		assertTrue("User has the role.", ctx.isUserInRoles(roles));
		roles.set(0, "OTHERROLE");
		roles.set(1, "OTHERROLE2");
		assertFalse("User does not have all the roles.", ctx.isUserInRoles(roles));
	}

	/**
	 * Test isUserInRoles.
	 */
	@Test
	public void isUserInRolesStringArray() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		assertTrue("User has the role.", ctx.isUserInRoles("ROLE1", "ROLE3"));
		assertFalse("User does not have all the roles.", ctx.isUserInRoles("AROLE", "BROLE"));
	}

	/**
	 * Test role validation.
	 */
	@Test
	public void roleValidationWithValidRoles() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");

		// All rolenames valid
		List<String> validRoles = new ArrayList<String>();
		validRoles.add("ROLE1");
		validRoles.add("ROLE2");

		// Should not throw exception
		ctx.isUserInAllRoles(validRoles);
		ctx.isUserInAllRoles(ValidRoles.ROLE1, ValidRoles.ROLE2);
		ctx.isUserInAllRoles(validRoles.get(0), validRoles.get(1));
		ctx.isUserInRole(ValidRoles.ROLE1);
		ctx.isUserInRole(validRoles.get(0));
		ctx.isUserInRoles(validRoles);
		ctx.isUserInRoles(ValidRoles.ROLE1, ValidRoles.ROLE2);
		ctx.isUserInRoles(validRoles.get(0), validRoles.get(1));

	}

	/**
	 * Test role validation.
	 */
	@Test(expected = RoleNotValidException.class)
	public void roleValidationIsUserInAllRolesList() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		// All rolenames invalid
		List<String> invalidRoles = new ArrayList<String>();
		invalidRoles.add("invalid1");
		invalidRoles.add("invalid2");
		// Should throw exception
		ctx.isUserInAllRoles(invalidRoles);
	}

	/**
	 * Test role validation.
	 */
	@Test(expected = RoleNotValidException.class)
	public void roleValidationIsUserInAllRolesRoleArray() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		// All rolenames invalid
		List<String> invalidRoles = new ArrayList<String>();
		invalidRoles.add("invalid1");
		invalidRoles.add("invalid2");

		// Should throw exception
		ctx.isUserInAllRoles(new DefaultRole(invalidRoles.get(0)), new DefaultRole(invalidRoles.get(1)));
	}

	/**
	 * Test role validation.
	 */
	@Test(expected = RoleNotValidException.class)
	public void roleValidationIsUserInAllRolesStringArray() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		// All rolenames invalid
		List<String> invalidRoles = new ArrayList<String>();
		invalidRoles.add("invalid1");
		invalidRoles.add("invalid2");
		// Should throw exception
		ctx.isUserInAllRoles(invalidRoles.get(0), invalidRoles.get(1));
	}

	/**
	 * Test role validation.
	 */
	@Test(expected = RoleNotValidException.class)
	public void roleValidationIsUserInRoleRole() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		// All rolenames invalid
		List<String> invalidRoles = new ArrayList<String>();
		invalidRoles.add("invalid1");
		invalidRoles.add("invalid2");

		// Should throw exception
		ctx.isUserInRole(new DefaultRole(invalidRoles.get(0)));
	}

	/**
	 * Test role validation.
	 */
	@Test(expected = RoleNotValidException.class)
	public void roleValidationIsUserInRoleString() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		// All rolenames invalid
		List<String> invalidRoles = new ArrayList<String>();
		invalidRoles.add("invalid1");
		invalidRoles.add("invalid2");

		// Should throw exception
		ctx.isUserInRole(invalidRoles.get(0));
	}

	/**
	 * Test role validation.
	 */
	@Test(expected = RoleNotValidException.class)
	public void roleValidationIsUserInRolesList() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		// All rolenames invalid
		List<String> invalidRoles = new ArrayList<String>();
		invalidRoles.add("invalid1");
		invalidRoles.add("invalid2");
		// Should throw exception
		ctx.isUserInRoles(invalidRoles);
	}

	/**
	 * Test role validation.
	 */
	@Test(expected = RoleNotValidException.class)
	public void roleValidationIsUserInRolesRoleArray() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");

		// All rolenames invalid
		List<String> invalidRoles = new ArrayList<String>();
		invalidRoles.add("invalid1");
		invalidRoles.add("invalid2");
		// Should throw exception
		ctx.isUserInRoles(new DefaultRole(invalidRoles.get(0)), new DefaultRole(invalidRoles.get(1)));
	}

	/**
	 * Test role validation.
	 */
	@Test(expected = RoleNotValidException.class)
	public void roleValidationIsUserInRolesStringArray() {
		SecurityContext ctx = getSecurityContext("User", "ROLE1", "ROLE2");
		// All rolenames invalid
		List<String> invalidRoles = new ArrayList<String>();
		invalidRoles.add("invalid1");
		invalidRoles.add("invalid2");
		// Should throw exception
		ctx.isUserInRoles(invalidRoles.get(0), invalidRoles.get(1));
	}

}
